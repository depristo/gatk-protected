package org.broadinstitute.sting.gatk.walkers;

import net.sf.samtools.*;
import net.sf.picard.reference.ReferenceSequenceFileFactory;
import net.sf.picard.reference.ReferenceSequenceFile;
import net.sf.picard.reference.ReferenceSequence;
import org.broadinstitute.sting.gatk.refdata.ReadMetaDataTracker;
import org.broadinstitute.sting.utils.cmdLine.Argument;
import org.broadinstitute.sting.utils.Utils;
import org.broadinstitute.sting.utils.Pair;
import org.broadinstitute.sting.utils.BaseUtils;
import org.broadinstitute.sting.gatk.io.StingSAMFileWriter;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;

import net.sf.samtools.util.StringUtil;

/**
 * This ReadWalker provides simple, yet powerful read clipping capabilities.  It allows the user to clip bases in reads
 * with poor quality scores, that match particular sequences, or that were generated by particular machine cycles.
 */
@Requires({DataSource.READS})
public class ClipReadsWalker extends ReadWalker<ClipReadsWalker.ReadClipper, ClipReadsWalker.ClippingData> {
    /**
     * an optional argument to dump the reads out to a BAM file
     */
    @Argument(fullName = "outputBam", shortName = "ob", doc = "Write output to this BAM filename instead of STDOUT", required = false)
    StingSAMFileWriter outputBam = null;

    @Argument(fullName = "qTrimmingThreshold", shortName = "QT", doc = "", required = false)
    int qTrimmingThreshold = -1;

    @Argument(fullName = "cyclesToTrim", shortName = "CT", doc = "String of the form 1-10,20-30 indicating machine cycles to clip from the reads", required = false)
    String cyclesToClipArg = null;

    @Argument(fullName = "clipSequencesFile", shortName = "XF", doc = "Remove sequences within reads matching these sequences", required = false)
    String clipSequenceFile = null;

    @Argument(fullName = "clipSequence", shortName = "X", doc = "Remove sequences within reads matching this sequence", required = false)
    String[] clipSequencesArgs = null;

    @Argument(fullName="read", doc="", required=false)
    String onlyDoRead = null;

    //@Argument(fullName = "keepCompletelyClipped", shortName = "KCC", doc = "Unfortunately, sometimes a read is completely clipped away but with SOFTCLIP_BASES this results in an invalid CIGAR string.  ", required = false)
    //boolean keepCompletelyClippedReads = false;

//    @Argument(fullName = "onlyClipFirstSeqMatch", shortName = "ESC", doc="Only clip the first occurrence of a clipping sequence, rather than all subsequences within a read that match", required = false)
//    boolean onlyClipFirstSeqMatch = false;

    @Argument(fullName = "clipRepresentation", shortName = "CR", doc = "How should we actually clip the bases?", required = false)
    ClippingRepresentation clippingRepresentation = ClippingRepresentation.WRITE_NS;

    /**
     * List of sequence that should be clipped from the reads
     */
    List<SeqToClip> sequencesToClip = new ArrayList<SeqToClip>();

    /**
     * List of cycle start / stop pairs (0-based, stop is included in the cycle to remove) to clip from the reads
     */
    List<Pair<Integer, Integer>> cyclesToClip = null;

    /**
     * The initialize function.
     */
    public void initialize() {
        if (qTrimmingThreshold >= 0) {
            logger.info(String.format("Creating Q-score clipper with threshold %d", qTrimmingThreshold));
        }

        //
        // Initialize the sequences to clip
        //
        if (clipSequencesArgs != null) {
            int i = 0;
            for (String toClip : clipSequencesArgs) {
                i++;
                ReferenceSequence rs = new ReferenceSequence("CMDLINE-" + i, -1, StringUtil.stringToBytes(toClip));
                addSeqToClip(rs.getName(), rs.getBases());
            }
        }

        if (clipSequenceFile != null) {
            ReferenceSequenceFile rsf = ReferenceSequenceFileFactory.getReferenceSequenceFile(new File(clipSequenceFile));

            while (true) {
                ReferenceSequence rs = rsf.nextSequence();
                if (rs == null)
                    break;
                else {
                    addSeqToClip(rs.getName(), rs.getBases());
                }
            }
        }


        //
        // Initialize the cycle ranges to clip
        //
        if (cyclesToClipArg != null) {
            cyclesToClip = new ArrayList<Pair<Integer, Integer>>();
            for (String range : cyclesToClipArg.split(",")) {
                try {
                    String[] elts = range.split("-");
                    int start = Integer.parseInt(elts[0]) - 1;
                    int stop = Integer.parseInt(elts[1]) - 1;

                    if (start < 0) throw new Exception();
                    if (stop < start) throw new Exception();

                    logger.info(String.format("Creating cycle clipper %d-%d", start, stop));
                    cyclesToClip.add(new Pair<Integer, Integer>(start, stop));
                } catch (Exception e) {
                    throw new RuntimeException("Badly formatted cyclesToClip argument: " + cyclesToClipArg);
                }
            }
        }

        outputBam.setPresorted(clippingRepresentation != ClippingRepresentation.SOFTCLIP_BASES);
    }

    /**
     * Helper function that adds a seq with name and bases (as bytes) to the list of sequences to be clipped
     *
     * @param name
     * @param bases
     */
    private void addSeqToClip(String name, byte[] bases) {
        SeqToClip clip = new SeqToClip(name, StringUtil.bytesToString(bases));
        sequencesToClip.add(clip);
        logger.info(String.format("Creating sequence clipper %s: %s/%s", clip.name, clip.seq, clip.revSeq));
    }

    /**
     * The reads map function.
     *
     * @param ref  the reference bases that correspond to our read, if a reference was provided
     * @param read the read itself, as a SAMRecord
     * @return the ReadClipper object describing what should be done to clip this read
     */
    public ReadClipper map(char[] ref, SAMRecord read, ReadMetaDataTracker metaDataTracker) {
        if ( onlyDoRead == null || read.getReadName().equals(onlyDoRead) ) {
            ReadClipper clipper = new ReadClipper(read);

            //
            // run all three clipping modules
            //
            clipBadQualityScores(clipper);
            clipCycles(clipper);
            clipSequences(clipper);
            return clipper;
        }

        return null;
    }

    /**
     * clip sequences from the reads that match all of the sequences in the global sequencesToClip variable.
     * Adds ClippingOps for each clip to clipper.
     *
     * @param clipper
     */
    private void clipSequences(ReadClipper clipper) {
        if (sequencesToClip != null) {                // don't bother if we don't have any sequences to clip
            SAMRecord read = clipper.getRead();

            for (SeqToClip stc : sequencesToClip) {
                // we have a pattern for both the forward and the reverse strands
                Pattern pattern = read.getReadNegativeStrandFlag() ? stc.revPat : stc.fwdPat;
                String bases = read.getReadString();
                Matcher match = pattern.matcher(bases);

                // keep clipping until match.find() says it can't find anything else
                boolean found = true;   // go through at least once
                while (found) {
                    found = match.find();
                    //System.out.printf("Matching %s against %s/%s => %b%n", bases, stc.seq, stc.revSeq, found);
                    if (found) {
                        int start = match.start();
                        int stop = match.end() - 1;
                        ClippingOp op = new ClippingOp(ClippingType.MATCHES_CLIP_SEQ, start, stop, stc.seq);
                        clipper.addOp(op);
                    }
                }
            }
        }
    }

    /**
     * Convenence function that takes a read and the start / stop clipping positions based on the forward
     * strand, and returns start/stop values appropriate for the strand of the read.
     *
     * @param read
     * @param start
     * @param stop
     * @return
     */
    private Pair<Integer, Integer> strandAwarePositions(SAMRecord read, int start, int stop) {
        if (read.getReadNegativeStrandFlag())
            return new Pair<Integer, Integer>(read.getReadLength() - stop - 1, read.getReadLength() - start - 1);
        else
            return new Pair<Integer, Integer>(start, stop);
    }

    /**
     * clip bases at cycles between the ranges in cyclesToClip by adding appropriate ClippingOps to clipper.
     *
     * @param clipper
     */
    private void clipCycles(ReadClipper clipper) {
        if (cyclesToClip != null) {
            SAMRecord read = clipper.getRead();

            for (Pair<Integer, Integer> p : cyclesToClip) {   // iterate over each cycle range
                int cycleStart = p.first;
                int cycleStop = p.second;

                if (cycleStart < read.getReadLength()) {
                    // only try to clip if the cycleStart is less than the read's length
                    if (cycleStop >= read.getReadLength())
                        // we do tolerate [for convenience) clipping when the stop is beyond the end of the read
                        cycleStop = read.getReadLength() - 1;

                    Pair<Integer, Integer> startStop = strandAwarePositions(read, cycleStart, cycleStop);
                    int start = startStop.first;
                    int stop = startStop.second;

                    ClippingOp op = new ClippingOp(ClippingType.WITHIN_CLIP_RANGE, start, stop, null);
                    clipper.addOp(op);
                }
            }
        }
    }

    /**
     * Clip bases from the read in clipper from
     * <p/>
     * argmax_x{ \sum{i = x + 1}^l (qTrimmingThreshold - qual)
     * <p/>
     * to the end of the read.  This is blatantly stolen from BWA.
     * <p/>
     * Walk through the read from the end (in machine cycle order) to the beginning, calculating the
     * running sum of qTrimmingThreshold - qual.  While we do this, we track the maximum value of this
     * sum where the delta > 0.  After the loop, clipPoint is either -1 (don't do anything) or the
     * clipping index in the read (from the end).
     *
     * @param clipper
     */
    private void clipBadQualityScores(ReadClipper clipper) {
        SAMRecord read = clipper.getRead();
        int readLen = read.getReadBases().length;
        byte[] quals = read.getBaseQualities();


        int clipSum = 0, lastMax = -1, clipPoint = -1; // -1 means no clip
        for (int i = readLen - 1; i >= 0; i--) {
            int baseIndex = read.getReadNegativeStrandFlag() ? readLen - i - 1 : i;
            byte qual = quals[baseIndex];
            clipSum += (qTrimmingThreshold - qual);
            if (clipSum >= 0 && (clipSum >= lastMax)) {
                lastMax = clipSum;
                clipPoint = baseIndex;
            }
        }

        if (clipPoint != -1) {
            int start = read.getReadNegativeStrandFlag() ? 0 : clipPoint;
            int stop = read.getReadNegativeStrandFlag() ? clipPoint : readLen - 1;
            clipper.addOp(new ClippingOp(ClippingType.LOW_Q_SCORES, start, stop, null));
        }
    }

    /**
     * reduceInit is called once before any calls to the map function.  We use it here to setup the output
     * bam file, if it was specified on the command line
     *
     * @return
     */
    public ClippingData reduceInit() {
        return new ClippingData(sequencesToClip);
    }

    public ClippingData reduce(ReadClipper clipper, ClippingData data) {
        if ( clipper == null )
            return data;

        if (outputBam != null) {
            outputBam.addAlignment(clipper.clipRead(clippingRepresentation));
        } else {
            out.println(clipper.clipRead(clippingRepresentation).format());
        }

        data.nTotalReads++;
        data.nTotalBases += clipper.getRead().getReadLength();
        if (clipper.wasClipped()) {
            data.nClippedReads++;
            for (ClippingOp op : clipper.getOps()) {
                switch (op.type) {
                    case LOW_Q_SCORES:
                        data.incNQClippedBases(op.getLength());
                        break;
                    case WITHIN_CLIP_RANGE:
                        data.incNRangeClippedBases(op.getLength());
                        break;
                    case MATCHES_CLIP_SEQ:
                        data.incSeqClippedBases((String) op.extraInfo, op.getLength());
                        break;
                }
            }
        }

        return data;
    }

    public void onTraversalDone(ClippingData data) {
        out.printf(data.toString());
    }

    // --------------------------------------------------------------------------------------------------------------
    //
    // utility classes
    //
    // --------------------------------------------------------------------------------------------------------------

    private class SeqToClip {
        String name;
        String seq, revSeq;
        Pattern fwdPat, revPat;

        public SeqToClip(String name, String seq) {
            this.name = name;
            this.seq = seq;
            this.fwdPat = Pattern.compile(seq, Pattern.CASE_INSENSITIVE);
            this.revSeq = BaseUtils.simpleReverseComplement(seq);
            this.revPat = Pattern.compile(revSeq, Pattern.CASE_INSENSITIVE);
        }
    }

    /**
     * What is the type of a ClippingOp?
     */
    private enum ClippingType {
        LOW_Q_SCORES,
        WITHIN_CLIP_RANGE,
        MATCHES_CLIP_SEQ
    }

    /**
     * Represents a clip on a read.  It has a type (see the enum) along with a start and stop in the bases
     * of the read, plus an option extraInfo (useful for carrying info where needed).
     * <p/>
     * Also holds the critical apply function that actually execute the clipping operation on a provided read,
     * according to the wishes of the supplid ClippingAlgorithm enum.
     */
    private class ClippingOp {
        public ClippingType type;
        public int start, stop; // inclusive
        public Object extraInfo = null;

        public ClippingOp(ClippingType type, int start, int stop, Object extraInfo) {
            this.type = type;
            this.start = start;
            this.stop = stop;
            this.extraInfo = extraInfo;
        }

        public int getLength() {
            return stop - start + 1;
        }

        /**
         * Clips the bases in clippedRead according to this operation's start and stop.  Uses the clipping
         * representation used is the one provided by algorithm argument.
         *
         * @param algorithm
         * @param clippedRead
         */
        public void apply(ClippingRepresentation algorithm, SAMRecord clippedRead) {
            //clippedRead.setReferenceIndex(1);
            switch (algorithm) {
                case WRITE_NS:
                    for (int i = start; i <= stop; i++)
                        clippedRead.getReadBases()[i] = 'N';
                    break;
                case WRITE_Q0S:
                    for (int i = start; i <= stop; i++)
                        clippedRead.getBaseQualities()[i] = 0;
                    break;
                case WRITE_NS_Q0S:
                    for (int i = start; i <= stop; i++) {
                        clippedRead.getReadBases()[i] = 'N';
                        clippedRead.getBaseQualities()[i] = 0;
                    }
                    break;
                case SOFTCLIP_BASES:
                    if ( ! clippedRead.getReadUnmappedFlag() ) {
                        // we can't process unmapped reads

                        //System.out.printf("%d %d %d%n", stop, start, clippedRead.getReadLength());
                        if ( (stop + 1 - start) == clippedRead.getReadLength() ) {
                            // BAM representation issue -- we can't SOFTCLIP away all bases in a read, just leave it alone
                            logger.info(String.format("Warning, read %s has all bases clip but this can't be represented with SOFTCLIP_BASES, just leaving it alone", clippedRead.getReadName()));
                            break;
                        }

                        if ( start > 0 && stop != clippedRead.getReadLength() - 1 )
                            throw new RuntimeException(String.format("Cannot apply soft clipping operator to the middle of a read: %s to be clipped at %d-%d",
                                    clippedRead.getReadName(), start, stop));

                        Cigar oldCigar = clippedRead.getCigar();

                        int scLeft = 0, scRight = clippedRead.getReadLength();
                        if ( clippedRead.getReadNegativeStrandFlag() ) {
                            if ( start == 0 )
                                scLeft = stop + 1;
                            else
                                scRight = start + 1;
                        } else {                        
                            if ( start == 0 )
                                scLeft = stop;
                            else
                                scRight = start;
                        }

                        Cigar newCigar = _softClip(oldCigar, scLeft, scRight);
                        clippedRead.setCigar(newCigar);

                        int newClippedStart = _getNewAlignmentStartOffset(newCigar, oldCigar);
                        int newStart = clippedRead.getAlignmentStart() + newClippedStart;
                        clippedRead.setAlignmentStart(newStart);

                        //System.out.printf("%s clipping at %d %d / %d %d => %s and %d%n", oldCigar.toString(), start, stop, scLeft, scRight, newCigar.toString(), newStart);
                    }

                    break;
                    //throw new RuntimeException("Softclipping of bases not yet implemented.");
            }
        }
    }

    /**
     * How should we represent a clipped bases in a read?
     */
    private enum ClippingRepresentation {
        WRITE_NS,           // change the bases to Ns
        WRITE_Q0S,          // change the quality scores to Q0
        WRITE_NS_Q0S,       // change the quality scores to Q0 and write Ns
        SOFTCLIP_BASES      // change cigar string to S, but keep bases
    }

    /**
     * A simple collection of the clipping operations to apply to a read along with its read
     */
    public class ReadClipper {
        SAMRecord read;
        List<ClippingOp> ops = null;

        /**
         * We didn't do any clipping work on this read, just leave everything as a default
         *
         * @param read
         */
        public ReadClipper(final SAMRecord read) {
            this.read = read;
        }

        /**
         * Add another clipping operation to apply to this read
         *
         * @param op
         */
        public void addOp(ClippingOp op) {
            if (ops == null) ops = new ArrayList<ClippingOp>();
            ops.add(op);
        }

        public List<ClippingOp> getOps() {
            return ops;
        }

        public boolean wasClipped() {
            return ops != null;
        }

        public SAMRecord getRead() {
            return read;
        }


        /**
         * Return a new read corresponding to this.read that's been clipped according to ops, if any are present.
         *
         * @param algorithm
         * @return
         */
        public SAMRecord clipRead(ClippingRepresentation algorithm) {
            if (ops == null)
                return getRead();
            else {
                try {
                    SAMRecord clippedRead = (SAMRecord) read.clone();
                    for (ClippingOp op : getOps()) {
                        op.apply(algorithm, clippedRead);
                    }
                    return clippedRead;
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e); // this should never happen
                }
            }
        }
    }

    public class ClippingData {
        public long nTotalReads = 0;
        public long nTotalBases = 0;
        public long nClippedReads = 0;
        public long nClippedBases = 0;
        public long nQClippedBases = 0;
        public long nRangeClippedBases = 0;
        public long nSeqClippedBases = 0;

        HashMap<String, Long> seqClipCounts = new HashMap<String, Long>();

        public ClippingData(List<SeqToClip> clipSeqs) {
            for (SeqToClip clipSeq : clipSeqs) {
                seqClipCounts.put(clipSeq.seq, 0L);
            }
        }

        public void incNQClippedBases(int n) {
            nQClippedBases += n;
            nClippedBases += n;
        }

        public void incNRangeClippedBases(int n) {
            nRangeClippedBases += n;
            nClippedBases += n;
        }

        public void incSeqClippedBases(final String seq, int n) {
            nSeqClippedBases += n;
            nClippedBases += n;
            seqClipCounts.put(seq, seqClipCounts.get(seq) + n);
        }

        public String toString() {
            StringBuilder s = new StringBuilder();

            s.append(Utils.dupString('-', 80) + "\n");
            s.append(String.format("Number of examined reads              %d%n", nTotalReads));
            s.append(String.format("Number of clipped reads               %d%n", nClippedReads));
            s.append(String.format("Percent of clipped reads              %.2f%n", (100.0 * nClippedReads) / nTotalReads));
            s.append(String.format("Number of examined bases              %d%n", nTotalBases));
            s.append(String.format("Number of clipped bases               %d%n", nClippedBases));
            s.append(String.format("Percent of clipped bases              %.2f%n", (100.0 * nClippedBases) / nTotalBases));
            s.append(String.format("Number of quality-score clipped bases %d%n", nQClippedBases));
            s.append(String.format("Number of range clipped bases         %d%n", nRangeClippedBases));
            s.append(String.format("Number of sequence clipped bases      %d%n", nSeqClippedBases));

            for (Map.Entry<String, Long> elt : seqClipCounts.entrySet()) {
                s.append(String.format("  %8d clip sites matching %s%n", elt.getValue(), elt.getKey()));
            }

            s.append(Utils.dupString('-', 80) + "\n");
            return s.toString();
        }
    }

    /**
     * Given a cigar string, get the number of bases hard or soft clipped at the start
     */
    private int _getNewAlignmentStartOffset(final Cigar __cigar, final Cigar __oldCigar) {
        int num = 0;
        for (CigarElement e : __cigar.getCigarElements()) {
            if (!e.getOperator().consumesReferenceBases()) {
                if (e.getOperator().consumesReadBases()) {
                    num += e.getLength();
                }
            } else {
                break;
            }
        }

        int oldNum = 0;
        int curReadCounter = 0;

        for (CigarElement e : __oldCigar.getCigarElements()) {
            int curRefLength = e.getLength();
            int curReadLength = e.getLength();
            if (!e.getOperator().consumesReadBases()) {
                curReadLength = 0;
            }

            boolean truncated = false;
            if (curReadCounter + curReadLength > num) {
                curReadLength = num - curReadCounter;
                curRefLength = num - curReadCounter;
                truncated = true;
            }

            if (!e.getOperator().consumesReferenceBases()) {
                curRefLength = 0;
            }

            curReadCounter += curReadLength;
            oldNum += curRefLength;

            if (curReadCounter > num || truncated) {
                break;
            }
        }

        return oldNum;
    }

    /**
     * Given a cigar string, soft clip up to startClipEnd and soft clip starting at endClipBegin
     */
    private Cigar _softClip(final Cigar __cigar, final int __startClipEnd, final int __endClipBegin) {
        if (__endClipBegin <= __startClipEnd) {
            //whole thing should be soft clipped
            int cigarLength = 0;
            for (CigarElement e : __cigar.getCigarElements()) {
                cigarLength += e.getLength();
            }

            Cigar newCigar = new Cigar();
            newCigar.add(new CigarElement(cigarLength, CigarOperator.SOFT_CLIP));
            assert newCigar.isValid(null, -1) == null;
            return newCigar;
        }

        int curLength = 0;
        Vector<CigarElement> newElements = new Vector<CigarElement>();
        for (CigarElement curElem : __cigar.getCigarElements()) {
            if (!curElem.getOperator().consumesReadBases()) {
                if (curLength > __startClipEnd && curLength < __endClipBegin) {
                    newElements.add(new CigarElement(curElem.getLength(), curElem.getOperator()));
                }
                continue;
            }

            int s = curLength;
            int e = curLength + curElem.getLength();
            if (e <= __startClipEnd || s >= __endClipBegin) {
                //must turn this entire thing into a clip
                newElements.add(new CigarElement(curElem.getLength(), CigarOperator.SOFT_CLIP));
            } else if (s >= __startClipEnd && e <= __endClipBegin) {
                //same thing
                newElements.add(new CigarElement(curElem.getLength(), curElem.getOperator()));
            } else {
                //we are clipping in the middle of this guy
                CigarElement newStart = null;
                CigarElement newMid = null;
                CigarElement newEnd = null;

                int midLength = curElem.getLength();
                if (s < __startClipEnd) {
                    newStart = new CigarElement(__startClipEnd - s, CigarOperator.SOFT_CLIP);
                    midLength -= newStart.getLength();
                }

                if (e > __endClipBegin) {
                    newEnd = new CigarElement(e - __endClipBegin, CigarOperator.SOFT_CLIP);
                    midLength -= newEnd.getLength();
                }
                assert midLength >= 0;
                if (midLength > 0) {
                    newMid = new CigarElement(midLength, curElem.getOperator());
                }
                if (newStart != null) {
                    newElements.add(newStart);
                }
                if (newMid != null) {
                    newElements.add(newMid);
                }
                if (newEnd != null) {
                    newElements.add(newEnd);
                }
            }
            curLength += curElem.getLength();
        }

        Vector<CigarElement> finalNewElements = new Vector<CigarElement>();
        CigarElement lastElement = null;
        for (CigarElement elem : newElements) {
            if (lastElement == null || lastElement.getOperator() != elem.getOperator()) {
                if (lastElement != null) {
                    finalNewElements.add(lastElement);
                }
                lastElement = elem;
            } else {
                lastElement = new CigarElement(lastElement.getLength() + elem.getLength(), lastElement.getOperator());
            }
        }
        if (lastElement != null) {
            finalNewElements.add(lastElement);
        }

        Cigar newCigar = new Cigar(finalNewElements);
        assert newCigar.isValid(null, -1) == null;
        return newCigar;
    }
}