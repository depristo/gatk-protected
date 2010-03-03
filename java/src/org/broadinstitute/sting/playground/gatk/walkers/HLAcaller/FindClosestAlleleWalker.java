package org.broadinstitute.sting.playground.gatk.walkers.HLAcaller;

import net.sf.samtools.SAMRecord;
import org.broadinstitute.sting.gatk.refdata.ReadMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.*;
import org.broadinstitute.sting.utils.cmdLine.Argument;

import java.util.ArrayList;
import java.util.Hashtable;
/**
 * Finds the most similar HLA allele for each read. Usage: java -jar GenomeAnalysisTK.jar -T FindClosestAllele -I INPUT.bam -R /broad/1KG/reference/human_b36_both.fasta -L INPUT.interval -findFirst | grep -v INFO | sort -k1 > OUTPUT
 * @author shermanjia
 */
@Requires({DataSource.READS, DataSource.REFERENCE})
public class FindClosestAlleleWalker extends ReadWalker<Integer, Integer> {
    @Argument(fullName = "debugRead", shortName = "debugRead", doc = "Print match score for read", required = false)
    public String debugRead = "";

    @Argument(fullName = "findFirst", shortName = "findFirst", doc = "For each read, stop when first HLA allele is found with concordance = 1", required = false)
    public boolean findFirst = false;
    
    @Argument(fullName = "debugAllele", shortName = "debugAllele", doc = "Print match score for allele", required = false)
    public String debugAllele = "";
    
    @Argument(fullName = "ethnicity", shortName = "ethnicity", doc = "Use allele frequencies for this ethnic group", required = false)
    public String ethnicity = "Caucasian";

    @Argument(fullName = "onlyfrequent", shortName = "onlyfrequent", doc = "Only consider alleles with frequency > 0.0001", required = false)
    public boolean ONLYFREQUENT = false;

    String HLAdatabaseFile ="/humgen/gsa-scr1/GSA/sjia/454_HLA/HLA/HLA.nuc.imputed.4digit.sam";
    SAMFileReader HLADictionaryReader = new SAMFileReader();

    String CaucasianAlleleFrequencyFile = "/humgen/gsa-scr1/GSA/sjia/454_HLA/HLA/HLA_CaucasiansUSA.freq";
    String BlackAlleleFrequencyFile = "/humgen/gsa-scr1/GSA/sjia/454_HLA/HLA/HLA_BlackUSA.freq";
    String AlleleFrequencyFile;
    String UniqueAllelesFile               = "/humgen/gsa-scr1/GSA/sjia/454_HLA/HLA/UniqueAlleles";

    String PolymorphicSitesFile = "/humgen/gsa-scr1/GSA/sjia/Sting/HLA.polymorphic.sites";

    boolean DatabaseLoaded = false;
    boolean DEBUG = false;

    String[] HLAnames, HLAreads;
    Integer[] HLAstartpos, HLAstoppos, PolymorphicSites,NonPolymorphicSites;
    double[] SingleAlleleFrequencies;

    double[] nummatched, concordance, numcompared;
    int numHLAlleles = 0;
    int minstartpos = 0;
    int maxstoppos = 0;

    int HLA_A_start = 30018310;
    int HLA_A_end = 30021211;
    
    Hashtable AlleleFrequencies = new Hashtable();
    int iAstart = -1, iAstop = -1, iBstart = -1, iBstop = -1, iCstart = -1, iCstop = -1;
    CigarParser formatter = new CigarParser();

    public Integer reduceInit() { 
        if (!DatabaseLoaded){
            DatabaseLoaded = true;

            //Load HLA dictionary
            out.printf("INFO  Loading HLA dictionary ... ");

            HLADictionaryReader.ReadFile(HLAdatabaseFile);
            HLAreads = HLADictionaryReader.GetReads();
            HLAnames = HLADictionaryReader.GetReadNames();
            HLAstartpos = HLADictionaryReader.GetStartPositions();
            HLAstoppos = HLADictionaryReader.GetStopPositions();
            minstartpos = HLADictionaryReader.GetMinStartPos();
            maxstoppos = HLADictionaryReader.GetMaxStopPos();
            
            out.printf("Done! %s HLA alleles loaded.\n",HLAreads.length);

            nummatched = new double[HLAreads.length];
            concordance = new double[HLAreads.length];
            numcompared = new double[HLAreads.length];

            //Read allele frequencies
            if (ethnicity.equals("Black")){
                AlleleFrequencyFile = BlackAlleleFrequencyFile;
            }else{
                AlleleFrequencyFile = CaucasianAlleleFrequencyFile;
            }
            out.printf("INFO  Reading HLA allele frequencies ... ");
            FrequencyFileReader HLAfreqReader = new FrequencyFileReader();
            HLAfreqReader.ReadFile(AlleleFrequencyFile,UniqueAllelesFile);
            AlleleFrequencies = HLAfreqReader.GetAlleleFrequencies();
            out.printf("Done! Frequencies for %s HLA alleles loaded.\n",AlleleFrequencies.size());

            //FindPolymorphicSites(minstartpos,maxstoppos);
            
            PolymorphicSitesFileReader siteFileReader = new PolymorphicSitesFileReader();
            siteFileReader.ReadFile(PolymorphicSitesFile);
            PolymorphicSites = siteFileReader.GetPolymorphicSites();
            NonPolymorphicSites = siteFileReader.GetNonPolymorphicSites();
            
            
            out.printf("INFO  %s polymorphic and %s non-polymorphic sites found in HLA dictionary\n",PolymorphicSites.length,NonPolymorphicSites.length);
            out.printf("INFO  Comparing reads to database ...\n");

            if (DEBUG){
                //out.printf("Astart[%s]\tAstop[%s]\tBstart[%s]\tBstop[%s]\tCstart[%s]\tCstop[%s]\tnumAlleles[%s]\n",iAstart,iAstop,iBstart,iBstop,iCstart,iCstop,numHLAlleles);
            }
        }
        return 0;
    }

    private void FindPolymorphicSites(int start, int stop){
        boolean initialized, polymorphic, examined;
        char c = ' ';
        ArrayList<Integer> polymorphicsites = new ArrayList<Integer>();
        ArrayList<Integer> nonpolymorphicsites = new ArrayList<Integer>();
        //Find polymorphic sites in dictionary
        for (int pos = start; pos <= stop; pos++){
            initialized = false; polymorphic = false; examined = false;
            //look across all alleles at specific position to see if it is polymorphic
            for (int i = 0; i < HLAreads.length; i++){
                if (pos >= HLAstartpos[i] && pos <= HLAstoppos[i]){
                    if (!initialized){
                        c = HLAreads[i].charAt(pos-HLAstartpos[i]);
                        initialized = true;
                        examined = true;
                    }
                    if (HLAreads[i].charAt(pos-HLAstartpos[i]) != c){
                        polymorphicsites.add(pos);
                        out.printf("POLYMORPHIC\t6\t%s\n", pos);
                        polymorphic = true;
                        break;
                    }
                }

            }
            if (!polymorphic && examined){
                nonpolymorphicsites.add(pos);
                out.printf("CONSERVED\t6\t%s\n", pos);
            }
        }
        PolymorphicSites = polymorphicsites.toArray(new Integer[polymorphicsites.size()]);
        NonPolymorphicSites = nonpolymorphicsites.toArray(new Integer[nonpolymorphicsites.size()]);
    }

    private double CalculateConcordance(SAMRecord read){
        int readstart = read.getAlignmentStart();
        int readstop = read.getAlignmentEnd();
        int numpolymorphicsites, numnonpolymorphicsites, pos;
        char c1, c2;
        double maxConcordance = 0.0, freq = 0.0, minFreq = 0.0;
        String s1 = formatter.FormatRead(read.getCigarString(), read.getReadString());
        String s2;
        int allelestart, allelestop;

        numpolymorphicsites = PolymorphicSites.length;
        numnonpolymorphicsites = NonPolymorphicSites.length;

        if (ONLYFREQUENT){
            minFreq = 0.0001;
        }

        for (int i = 0; i < HLAreads.length; i++){
            nummatched[i] = 0; concordance[i] = 0; numcompared[i] = 0;
            freq = GetAlleleFrequency(HLAnames[i]);
            //Get concordance between read and specific allele
            if (readstart <= HLAstoppos[i] && readstop >= HLAstartpos[i] && freq > minFreq){
                s2 = HLAreads[i];
                
                allelestart = HLAstartpos[i];
                allelestop = HLAstoppos[i];

                //Polymorphic sites: always increment denominator, increment numerator when bases are concordant
                for (int j = 0; j < numpolymorphicsites; j++){
                    pos = PolymorphicSites[j];
                    if (pos >= readstart && pos <= readstop && pos >= allelestart && pos <= allelestop){
                        c1 = s1.charAt(pos-readstart);
                        c2 = s2.charAt(pos-allelestart);
                        if (c1 != 'D'){//allow for deletions (sequencing errors)
                            numcompared[i]++;
                            if (c1 == c2){
                                nummatched[i]++;
                            }else{
                                if (debugRead.equals(read.getReadName()) && debugAllele.equals(HLAnames[i])){
                                    out.printf("%s\t%s\t%s\t%s\t%s\n",read.getReadName(), HLAnames[i], j, c1,c2);
                                }
                            }
                        }
                    }
                }

                //Non-polymorphic sites: increment denominator only when bases are discordant
                
                for (int j = 0; j < numnonpolymorphicsites; j++){
                    pos = NonPolymorphicSites[j];
                    if (pos >= readstart && pos <= readstop && pos >= allelestart && pos <= allelestop){
                        c1 = s1.charAt(pos-readstart);
                        c2 = s2.charAt(pos-allelestart);
                        if (c1 != c2 && c1 != 'D'){//allow for deletions (sequencing errors)
                            numcompared[i]++;
                            if (debugRead.equals(read.getReadName()) && debugAllele.equals(HLAnames[i])){
                                out.printf("%s\t%s\t%s\t%s\t%s\n",read.getReadName(), HLAnames[i], j, c1,c2);
                            }
                        }
                    }
                }
            }

            //Update concordance array
            concordance[i]=nummatched[i]/numcompared[i];
            if (concordance[i] > maxConcordance){maxConcordance = concordance[i];}
            if (debugRead.equals(read.getReadName()) && debugAllele.equals(HLAnames[i])){
                out.printf("%s\t%s\t%s\t%s\t%s\n",read.getReadName(),HLAnames[i],concordance[i],numcompared[i],numcompared[i]-nummatched[i]);
            }
            if (findFirst && (concordance[i] == 1)){
                break;
            }
        
        }

        return maxConcordance;
    }

    private double FindMaxAlleleFrequency(double maxConcordance){
        //finds the max frequency of the alleles that share the maximum concordance with the read of interest
        double freq, maxFreq = 0.0;
        for (int i = 0; i < HLAreads.length; i++){
            if (concordance[i] == maxConcordance && maxConcordance > 0){
                freq = GetAlleleFrequency(HLAnames[i]);
                if (freq > maxFreq){maxFreq = freq;}
            }
        }
        return maxFreq;
    }

    public Integer map(char[] ref, SAMRecord read, ReadMetaDataTracker metaDataTracker) {
        //Calculate concordance for this read and all overlapping reads
        double maxConcordance = CalculateConcordance(read);

        String readname = read.getReadName(), allelename = ""; double freq;
        //For input bam files that contain HLA alleles, find and print allele frequency
        freq = GetAlleleFrequency(readname);
        out.printf("%s\t%s-%s", readname,read.getAlignmentStart(),read.getAlignmentEnd());

        //Find the maximum frequency of the alleles most concordant with the read
        double maxFreq = FindMaxAlleleFrequency(maxConcordance);
        
        //Print concordance statistics between this read and the most similar HLA allele(s)
        for (int i = 0; i < HLAreads.length; i++){
            if (concordance[i] == maxConcordance && maxConcordance > 0){
                freq = GetAlleleFrequency(HLAnames[i]);
                if (freq == maxFreq){
                    out.printf("\t%s\t%.4f\t%.3f\t%.0f\t%.0f",HLAnames[i],freq,concordance[i],numcompared[i],numcompared[i]-nummatched[i]);
                }
            }
        }
        out.print("\n");
        return 1;
    }

    private double GetAlleleFrequency(String allelename){
        double frequency = 0.0;
        //Truncate names to 4-digit "A*0101" format
        if (allelename.length() >= 10){
            allelename=allelename.substring(4,10);
        }else{
            allelename=allelename.substring(4);
        }
        if (AlleleFrequencies.containsKey(allelename)){
            frequency = Double.parseDouble((String) AlleleFrequencies.get(allelename).toString());
        }else{
            frequency=0.0001;
        }
        return frequency;
    }


    public Integer reduce(Integer value, Integer sum) {
        return value + sum;
    }
}

