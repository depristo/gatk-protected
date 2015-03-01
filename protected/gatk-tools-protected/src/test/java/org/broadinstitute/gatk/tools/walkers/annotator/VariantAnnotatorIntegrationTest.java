/*
* By downloading the PROGRAM you agree to the following terms of use:
* 
* BROAD INSTITUTE
* SOFTWARE LICENSE AGREEMENT
* FOR ACADEMIC NON-COMMERCIAL RESEARCH PURPOSES ONLY
* 
* This Agreement is made between the Broad Institute, Inc. with a principal address at 415 Main Street, Cambridge, MA 02142 (“BROAD”) and the LICENSEE and is effective at the date the downloading is completed (“EFFECTIVE DATE”).
* 
* WHEREAS, LICENSEE desires to license the PROGRAM, as defined hereinafter, and BROAD wishes to have this PROGRAM utilized in the public interest, subject only to the royalty-free, nonexclusive, nontransferable license rights of the United States Government pursuant to 48 CFR 52.227-14; and
* WHEREAS, LICENSEE desires to license the PROGRAM and BROAD desires to grant a license on the following terms and conditions.
* NOW, THEREFORE, in consideration of the promises and covenants made herein, the parties hereto agree as follows:
* 
* 1. DEFINITIONS
* 1.1 PROGRAM shall mean copyright in the object code and source code known as GATK3 and related documentation, if any, as they exist on the EFFECTIVE DATE and can be downloaded from http://www.broadinstitute.org/gatk on the EFFECTIVE DATE.
* 
* 2. LICENSE
* 2.1 Grant. Subject to the terms of this Agreement, BROAD hereby grants to LICENSEE, solely for academic non-commercial research purposes, a non-exclusive, non-transferable license to: (a) download, execute and display the PROGRAM and (b) create bug fixes and modify the PROGRAM. LICENSEE hereby automatically grants to BROAD a non-exclusive, royalty-free, irrevocable license to any LICENSEE bug fixes or modifications to the PROGRAM with unlimited rights to sublicense and/or distribute.  LICENSEE agrees to provide any such modifications and bug fixes to BROAD promptly upon their creation.
* The LICENSEE may apply the PROGRAM in a pipeline to data owned by users other than the LICENSEE and provide these users the results of the PROGRAM provided LICENSEE does so for academic non-commercial purposes only. For clarification purposes, academic sponsored research is not a commercial use under the terms of this Agreement.
* 2.2 No Sublicensing or Additional Rights. LICENSEE shall not sublicense or distribute the PROGRAM, in whole or in part, without prior written permission from BROAD. LICENSEE shall ensure that all of its users agree to the terms of this Agreement. LICENSEE further agrees that it shall not put the PROGRAM on a network, server, or other similar technology that may be accessed by anyone other than the LICENSEE and its employees and users who have agreed to the terms of this agreement.
* 2.3 License Limitations. Nothing in this Agreement shall be construed to confer any rights upon LICENSEE by implication, estoppel, or otherwise to any computer software, trademark, intellectual property, or patent rights of BROAD, or of any other entity, except as expressly granted herein. LICENSEE agrees that the PROGRAM, in whole or part, shall not be used for any commercial purpose, including without limitation, as the basis of a commercial software or hardware product or to provide services. LICENSEE further agrees that the PROGRAM shall not be copied or otherwise adapted in order to circumvent the need for obtaining a license for use of the PROGRAM.
* 
* 3. PHONE-HOME FEATURE
* LICENSEE expressly acknowledges that the PROGRAM contains an embedded automatic reporting system (“PHONE-HOME”) which is enabled by default upon download. Unless LICENSEE requests disablement of PHONE-HOME, LICENSEE agrees that BROAD may collect limited information transmitted by PHONE-HOME regarding LICENSEE and its use of the PROGRAM.  Such information shall include LICENSEE’S user identification, version number of the PROGRAM and tools being run, mode of analysis employed, and any error reports generated during run-time.  Collection of such information is used by BROAD solely to monitor usage rates, fulfill reporting requirements to BROAD funding agencies, drive improvements to the PROGRAM, and facilitate adjustments to PROGRAM-related documentation.
* 
* 4. OWNERSHIP OF INTELLECTUAL PROPERTY
* LICENSEE acknowledges that title to the PROGRAM shall remain with BROAD. The PROGRAM is marked with the following BROAD copyright notice and notice of attribution to contributors. LICENSEE shall retain such notice on all copies. LICENSEE agrees to include appropriate attribution if any results obtained from use of the PROGRAM are included in any publication.
* Copyright 2012-2014 Broad Institute, Inc.
* Notice of attribution: The GATK3 program was made available through the generosity of Medical and Population Genetics program at the Broad Institute, Inc.
* LICENSEE shall not use any trademark or trade name of BROAD, or any variation, adaptation, or abbreviation, of such marks or trade names, or any names of officers, faculty, students, employees, or agents of BROAD except as states above for attribution purposes.
* 
* 5. INDEMNIFICATION
* LICENSEE shall indemnify, defend, and hold harmless BROAD, and their respective officers, faculty, students, employees, associated investigators and agents, and their respective successors, heirs and assigns, (Indemnitees), against any liability, damage, loss, or expense (including reasonable attorneys fees and expenses) incurred by or imposed upon any of the Indemnitees in connection with any claims, suits, actions, demands or judgments arising out of any theory of liability (including, without limitation, actions in the form of tort, warranty, or strict liability and regardless of whether such action has any factual basis) pursuant to any right or license granted under this Agreement.
* 
* 6. NO REPRESENTATIONS OR WARRANTIES
* THE PROGRAM IS DELIVERED AS IS. BROAD MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND CONCERNING THE PROGRAM OR THE COPYRIGHT, EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER OR NOT DISCOVERABLE. BROAD EXTENDS NO WARRANTIES OF ANY KIND AS TO PROGRAM CONFORMITY WITH WHATEVER USER MANUALS OR OTHER LITERATURE MAY BE ISSUED FROM TIME TO TIME.
* IN NO EVENT SHALL BROAD OR ITS RESPECTIVE DIRECTORS, OFFICERS, EMPLOYEES, AFFILIATED INVESTIGATORS AND AFFILIATES BE LIABLE FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES OF ANY KIND, INCLUDING, WITHOUT LIMITATION, ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER BROAD SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
* 
* 7. ASSIGNMENT
* This Agreement is personal to LICENSEE and any rights or obligations assigned by LICENSEE without the prior written consent of BROAD shall be null and void.
* 
* 8. MISCELLANEOUS
* 8.1 Export Control. LICENSEE gives assurance that it will comply with all United States export control laws and regulations controlling the export of the PROGRAM, including, without limitation, all Export Administration Regulations of the United States Department of Commerce. Among other things, these laws and regulations prohibit, or require a license for, the export of certain types of software to specified countries.
* 8.2 Termination. LICENSEE shall have the right to terminate this Agreement for any reason upon prior written notice to BROAD. If LICENSEE breaches any provision hereunder, and fails to cure such breach within thirty (30) days, BROAD may terminate this Agreement immediately. Upon termination, LICENSEE shall provide BROAD with written assurance that the original and all copies of the PROGRAM have been destroyed, except that, upon prior written authorization from BROAD, LICENSEE may retain a copy for archive purposes.
* 8.3 Survival. The following provisions shall survive the expiration or termination of this Agreement: Articles 1, 3, 4, 5 and Sections 2.2, 2.3, 7.3, and 7.4.
* 8.4 Notice. Any notices under this Agreement shall be in writing, shall specifically refer to this Agreement, and shall be sent by hand, recognized national overnight courier, confirmed facsimile transmission, confirmed electronic mail, or registered or certified mail, postage prepaid, return receipt requested. All notices under this Agreement shall be deemed effective upon receipt.
* 8.5 Amendment and Waiver; Entire Agreement. This Agreement may be amended, supplemented, or otherwise modified only by means of a written instrument signed by all parties. Any waiver of any rights or failure to act in a specific instance shall relate only to such instance and shall not be construed as an agreement to waive any rights or fail to act in any other instance, whether or not similar. This Agreement constitutes the entire agreement among the parties with respect to its subject matter and supersedes prior agreements or understandings between the parties relating to its subject matter.
* 8.6 Binding Effect; Headings. This Agreement shall be binding upon and inure to the benefit of the parties and their respective permitted successors and assigns. All headings are for convenience only and shall not affect the meaning of any provision of this Agreement.
* 8.7 Governing Law. This Agreement shall be construed, governed, interpreted and applied in accordance with the internal laws of the Commonwealth of Massachusetts, U.S.A., without regard to conflict of laws principles.
*/

package org.broadinstitute.gatk.tools.walkers.annotator;

import htsjdk.tribble.readers.LineIterator;
import htsjdk.tribble.readers.PositionalBufferedStream;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;
import org.broadinstitute.gatk.engine.walkers.WalkerTest;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFCodec;
import org.broadinstitute.gatk.utils.variant.GATKVCFConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class VariantAnnotatorIntegrationTest extends WalkerTest {

    final static String REF = b37KGReference;
    final static String CEUTRIO_BAM = validationDataLocation + "CEUTrio.HiSeq.b37.chr20.10_11mb.bam";

    public static String baseTestString() {
        return "-T VariantAnnotator -R " + b36KGReference + " --no_cmdline_in_header -o %s";
    }

    @Test
    public void testHasAnnotsNotAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample2.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("360610e4990860bb5c45249b8ac31e5b"));
        executeTest("test file has annotations, not asking for annotations, #1", spec);
    }

    @Test
    public void testHasAnnotsNotAsking2() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample3.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("d69a3c92a0e8f44e09e7377e3eaed4e8"));
        executeTest("test file has annotations, not asking for annotations, #2", spec);
    }

    @Test
    public void testHasAnnotsAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample2.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("92eb47332dd9d7ee7fbe3120dc39c594"));
        executeTest("test file has annotations, asking for annotations, #1", spec);
    }

    @Test
    public void testHasAnnotsAsking2() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample3.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("c367bf7cebd7b26305f8d4736788aec8"));
        executeTest("test file has annotations, asking for annotations, #2", spec);
    }

    @Test
    public void testNoAnnotsNotAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample2empty.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("540a9be8a8cb85b0f675fea1184bf78c"));
        executeTest("test file doesn't have annotations, not asking for annotations, #1", spec);
    }

    @Test
    public void testNoAnnotsNotAsking2() {
        // the genotype annotations in this file are actually out of order.  If you don't parse the genotypes
        // they don't get reordered.  It's a good test of the genotype ordering system.
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample3empty.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("f900e65b65ff0f9d9eb0891ef9b28c73"));
        executeTest("test file doesn't have annotations, not asking for annotations, #2", spec);
    }

    @Test
    public void testNoAnnotsAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample2empty.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("098dcad8d90d90391755a0191c9db59c"));
        executeTest("test file doesn't have annotations, asking for annotations, #1", spec);
    }

    @Test
    public void testNoAnnotsAsking2() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("f3bbfbc179d2e1bae49890f1e9dfde34"));
        executeTest("test file doesn't have annotations, asking for annotations, #2", spec);
    }

    @Test
    public void testExcludeAnnotations() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard -XA FisherStrand -XA ReadPosRankSumTest --variant " + privateTestDir + "vcfexample2empty.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("7267450fc4d002f75a24ca17278e0950"));
        executeTest("test exclude annotations", spec);
    }

    @Test
    public void testAskingStrandAlleleCountsBySample() throws IOException{
        String logFileName = new String("testAskingStrandAlleleCountsBySample.log");
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample2.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000 -A StrandAlleleCountsBySample -log " + logFileName, 1,
                Arrays.asList("0c0c4a219cb487598fb1fbb77db71eca"));
        executeTest("test file has annotations, adding StrandAlleleCountsBySample annotation", spec);

        File file = new File(logFileName);
        Assert.assertTrue(FileUtils.readFileToString(file).contains("Annotation will not be calculated, must be called from HaplotyepCaller"));
    }

    @Test
    public void testAskingGCContent() throws IOException{
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample2.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000 -A GCContent", 1,
                Arrays.asList("02f634fd978cf2a66738704581508569"));
        final File outputVCF = executeTest("test file has annotations, adding GCContent annotation", spec).getFirst().get(0);
        final VCFCodec codec = new VCFCodec();
        final VCFHeader header = (VCFHeader) codec.readActualHeader(codec.makeSourceFromStream(new FileInputStream(outputVCF)));
        final VCFHeaderLine infoLineGC = header.getInfoHeaderLine(GATKVCFConstants.GC_CONTENT_KEY);
        // GC content must be a Float type
        Assert.assertTrue(infoLineGC.toString().contains("Type=Float"));
    }

    @Test
    public void testOverwritingHeader() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample4.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,001,292", 1,
                Arrays.asList("18592c72d83ee84e1326acb999518c38"));
        executeTest("test overwriting header", spec);
    }

    @Test
    public void testNoReads() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("6de950b381d2d92b21bab6144e8f0714"));
        executeTest("not passing it any reads", spec);
    }

    @Test
    public void testDBTagWithDbsnp() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --dbsnp " + b36dbSNP129 + " -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("e0bd85747c87ea4df6ef67f593cbacbf"));
        executeTest("getting DB tag with dbSNP", spec);
    }

    @Test
    public void testMultipleIdsWithDbsnp() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --alwaysAppendDbsnpId --dbsnp " + b36dbSNP129 + " -G Standard --variant " + privateTestDir + "vcfexample3withIDs.vcf -L " + privateTestDir + "vcfexample3withIDs.vcf", 1,
                Arrays.asList("194a942f17104292192fb564a3c96610"));
        executeTest("adding multiple IDs with dbSNP", spec);
    }

    @Test
    public void testDBTagWithHapMap() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --comp:H3 " + privateTestDir + "fakeHM3.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("9e41ae733a76632b40eda38e3cef909d"));
        executeTest("getting DB tag with HM3", spec);
    }

    @Test
    public void testDBTagWithTwoComps() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --comp:H3 " + privateTestDir + "fakeHM3.vcf --comp:foo " + privateTestDir + "fakeHM3.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("7b718bae0444f1896a6e86da80531218"));
        executeTest("getting DB tag with 2 comps", spec);
    }

    @Test
    public void testNoQuals() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "noQual.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L " + privateTestDir + "noQual.vcf -A QualByDepth", 1,
                Arrays.asList("aea983adc01cd059193538cc30adc17d"));
        executeTest("test file doesn't have QUALs", spec);
    }

    @Test
    public void testUsingExpression() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --resource:foo " + privateTestDir + "targetAnnotations.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -E foo.AF -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("0bed7b4f6ed0556c5e7d398353a9fa91"));
        executeTest("using expression", spec);
    }

    @Test
    public void testUsingExpressionMultiAllele() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --resource:foo " + privateTestDir + "targetAnnotations-multiAllele.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty-multiAllele.vcf -E foo.AF -E foo.AC -L " + privateTestDir + "vcfexample3empty-multiAllele.vcf", 1,
                Arrays.asList("195cf0f5b1aa5c7d00a0595dcca02f4c"));
        executeTest("using expression with multi-alleles", spec);
    }

    @Test
    public void testUsingExpressionWithID() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --resource:foo " + privateTestDir + "targetAnnotations.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -E foo.ID -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("b3fe9d3bdb18ca2629543f849a7d27ed"));
        executeTest("using expression with ID", spec);
    }

    @Test
    public void testTabixAnnotationsAndParallelism() {
        final String MD5 = "99938d1e197b8f10c408cac490a00a62";
        for ( String file : Arrays.asList("CEU.exon.2010_03.sites.vcf", "CEU.exon.2010_03.sites.vcf.gz")) {
            WalkerTestSpec spec = new WalkerTestSpec(
                    baseTestString() + " -A HomopolymerRun --variant:vcf " + validationDataLocation + file + " -L " + validationDataLocation + "CEU.exon.2010_03.sites.vcf --no_cmdline_in_header", 1,
                    Arrays.asList(MD5));
            executeTest("Testing lookup vcf tabix vs. vcf tribble", spec);
        }

        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -A HomopolymerRun -nt 2 --variant:vcf " + validationDataLocation + "CEU.exon.2010_03.sites.vcf -L " + validationDataLocation + "CEU.exon.2010_03.sites.vcf --no_cmdline_in_header", 1,
                Arrays.asList(MD5));

        executeTest("Testing lookup vcf tabix vs. vcf tribble plus parallelism", spec);
    }

    @Test
    public void testSnpEffAnnotations() {
        WalkerTestSpec spec = new WalkerTestSpec(
            "-T VariantAnnotator -R " + hg19Reference + " --no_cmdline_in_header -o %s -A SnpEff --variant " +
            validationDataLocation + "1kg_exomes_unfiltered.AFR.unfiltered.vcf --snpEffFile  " + validationDataLocation +
            "snpEff2.0.5.AFR.unfiltered.vcf -L 1:1-1,500,000 -L 2:232,325,429",
            1,
            Arrays.asList("d9291845ce5a8576898d293a829a05b7")
        );
        executeTest("Testing SnpEff annotations", spec);
    }

    @Test
    public void testSnpEffAnnotationsUnsupportedVersionGATKMode() {
        WalkerTestSpec spec = new WalkerTestSpec(
            "-T VariantAnnotator -R " + b37KGReference + " --no_cmdline_in_header -o %s -A SnpEff " +
            "--variant " + privateTestDir + "vcf4.1.example.vcf " +
            "--snpEffFile  " + privateTestDir + "snpEff_unsupported_version_gatk_mode.vcf " +
            "-L 1:10001292-10012424",
            1,
            Arrays.asList("7352cf23a4d45d3d2bb34ab44a4100ae")
        );
        executeTest("Testing SnpEff annotations (unsupported version, GATK mode)", spec);
    }

    @Test
    public void testSnpEffAnnotationsUnsupportedVersionNoGATKMode() {
        WalkerTestSpec spec = new WalkerTestSpec(
            "-T VariantAnnotator -R " + b37KGReference + " --no_cmdline_in_header -o %s -A SnpEff " +
            "--variant " + privateTestDir + "vcf4.1.example.vcf " +
            "--snpEffFile  " + privateTestDir + "snpEff_unsupported_version_no_gatk_mode.vcf " +
            "-L 1:10001292-10012424",
            1,
            Arrays.asList("87cbf53c65ef4498b721f901f87f0161")
        );
        executeTest("Testing SnpEff annotations (unsupported version, no GATK mode)", spec);
    }

    @Test(enabled = true)
    public void testTDTAnnotation() {
        final String MD5 = "427dfdc665359b67eff210f909ebf8a2";
        WalkerTestSpec spec = new WalkerTestSpec(
                "-T VariantAnnotator -R " + b37KGReference + " -A TransmissionDisequilibriumTest --variant:vcf " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf" +
                        " -L " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf --no_cmdline_in_header -ped " + privateTestDir + "ug.random50000.family.ped -o %s", 1,
                Arrays.asList(MD5));
        executeTest("Testing TDT annotation ", spec);
    }


    @Test(enabled = true)
    public void testChromosomeCountsPed() {
        final String MD5 = "6b5cbedf4a8b3385edf128d81c8a46f2";
        WalkerTestSpec spec = new WalkerTestSpec(
                "-T VariantAnnotator -R " + b37KGReference + " -A ChromosomeCounts --variant:vcf " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf" +
                        " -L " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf --no_cmdline_in_header -ped " + privateTestDir + "ug.random50000.family.ped -o %s", 1,
                Arrays.asList(MD5));
        executeTest("Testing ChromosomeCounts annotation with PED file", spec);
    }

    @Test(enabled = true)
    public void testInbreedingCoeffPed() {
        final String MD5 = "159a771c1deaeffb786097e106943893";
        WalkerTestSpec spec = new WalkerTestSpec(
                "-T VariantAnnotator -R " + b37KGReference + " -A InbreedingCoeff --variant:vcf " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf" +
                        " -L " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf --no_cmdline_in_header -ped " + privateTestDir + "ug.random50000.family.ped -o %s", 1,
                Arrays.asList(MD5));
        executeTest("Testing InbreedingCoeff annotation with PED file", spec);
    }

    @Test
    public void testStrandBiasBySample() throws IOException {
        // pipeline 1: create variant via HalotypeCaller with no default annotations
        final String base = String.format("-T HaplotypeCaller --disableDithering -R %s -I %s", REF, CEUTRIO_BAM) + " --no_cmdline_in_header -o %s -L 20:10130000-10134800";
        final WalkerTestSpec spec = new WalkerTestSpec(base, 1, Arrays.asList(""));
        final File outputVCF = executeTest("testStrandBiasBySample", spec).getFirst().get(0);

        // pipeline 2: create variant via HalotypeCaller; include StrandBiasBySample, exclude FisherStrand annotation
        //             re-Annotate the variant with VariantAnnotator using FisherStrand annotation
        final String baseNoFS = String.format("-T HaplotypeCaller --disableDithering -R %s -I %s", REF, CEUTRIO_BAM) + " --no_cmdline_in_header -o %s -L 20:10130000-10134800 -XA FisherStrand -A StrandBiasBySample";
        final WalkerTestSpec specNoFS = new WalkerTestSpec(baseNoFS, 1, Arrays.asList(""));
        specNoFS.disableShadowBCF();
        final File outputVCFNoFS = executeTest("testStrandBiasBySample component stand bias annotation", specNoFS).getFirst().get(0);

        final String baseAnn = String.format("-T VariantAnnotator -R %s -V %s", REF, outputVCFNoFS.getAbsolutePath()) + " --no_cmdline_in_header -o %s -L 20:10130000-10134800 -A FisherStrand";
        final WalkerTestSpec specAnn = new WalkerTestSpec(baseAnn, 1, Arrays.asList(""));
        specAnn.disableShadowBCF();
        final File outputVCFAnn = executeTest("testStrandBiasBySample re-annotation of FisherStrand", specAnn).getFirst().get(0);

        // confirm that the FisherStrand values are identical for the two pipelines
        final VCFCodec codec = new VCFCodec();
        final FileInputStream s = new FileInputStream(outputVCF);
        final LineIterator lineIterator = codec.makeSourceFromStream(new PositionalBufferedStream(s));
        codec.readHeader(lineIterator);

        final VCFCodec codecAnn = new VCFCodec();
        final FileInputStream sAnn = new FileInputStream(outputVCFAnn);
        final LineIterator lineIteratorAnn = codecAnn.makeSourceFromStream(new PositionalBufferedStream(sAnn));
        codecAnn.readHeader(lineIteratorAnn);

        while( lineIterator.hasNext() && lineIteratorAnn.hasNext() ) {
            final String line = lineIterator.next();
            Assert.assertFalse(line == null);
            final VariantContext vc = codec.decode(line);

            final String lineAnn = lineIteratorAnn.next();
            Assert.assertFalse(lineAnn == null);
            final VariantContext vcAnn = codecAnn.decode(lineAnn);

            Assert.assertTrue(vc.hasAttribute("FS"));
            Assert.assertTrue(vcAnn.hasAttribute("FS"));
            Assert.assertEquals(vc.getAttributeAsDouble("FS", 0.0), vcAnn.getAttributeAsDouble("FS", -1.0));
        }

        Assert.assertFalse(lineIterator.hasNext());
        Assert.assertFalse(lineIteratorAnn.hasNext());
    }

    @Test
    public void testStrandAlleleCountsBySample() throws IOException {
        final WalkerTestSpec spec = new WalkerTestSpec(
                "-T HaplotypeCaller --disableDithering " +
                String.format("-R %s -I %s ", REF, CEUTRIO_BAM) +
                "--no_cmdline_in_header -o %s -L 20:10130000-10134800 " +
                "-A StrandBiasBySample -A StrandAlleleCountsBySample",
                1, Arrays.asList("")
        );
        spec.disableShadowBCF(); //TODO: Remove when BaseTest.assertAttributesEquals() works with SC
        final File outputVCF = executeTest("testStrandAlleleCountsBySample", spec).getFirst().get(0);

        //Confirm that SB and SAC are identical for bi-allelic variants
        final VCFCodec codec = new VCFCodec();
        final FileInputStream s = new FileInputStream(outputVCF);
        final LineIterator lineIterator = codec.makeSourceFromStream(new PositionalBufferedStream(s));
        codec.readHeader(lineIterator);

        while (lineIterator.hasNext()) {
            final String line = lineIterator.next();
            Assert.assertFalse(line == null);
            final VariantContext vc = codec.decode(line);

            if (vc.isBiallelic()) {
                for (final Genotype g : vc.getGenotypes()) {
                    Assert.assertTrue(g.hasExtendedAttribute("SB"));
                    Assert.assertTrue(g.hasExtendedAttribute("SAC"));
                    Assert.assertEquals(g.getExtendedAttribute("SB").toString(), g.getExtendedAttribute("SAC").toString());
                }
            }
        }
    }

    @Test(enabled = false)
    public void testQualByDepth() throws IOException {

        /*
        NOTE: Currently, no HC tests explicitly set their PairHMM implementation, such that outputs may vary depending
        on the underlying system. You may run into this test failing due to one of many PairHMM versions activating.

        Specifically for this test, the site chr 20:10130722 seems to vary in the HC calculated QUAL and QD. This has
        NOTHING to do with a problem in the VariantAnnotator, and is an issue with the way PairHMM is used in tests.

        According to Karthik, things to look for in the logs that may help indicate that the failure is NOT something
        you changed, but the PairHMM switching on you:

        - Using un-vectorized C++ implementation of PairHMM
        - Using AVX accelerated implementation of PairHMM
        - Using SSE4.1 accelerated implementation of PairHMM
        - libVectorLoglessPairHMM unpacked successfully from GATK jar file
        - Using vectorized implementation of PairHMM
        - (Nothing, which I think means another implementation is being used)

        Upon any failures in any HC related tests for now, your best bet is to contact Mauricio to make sure that
        everything still looks ok.

        TODO: Stabilize the PairHMM from arbitrarily running different versions.
         */

        final String base = String.format("-T HaplotypeCaller --disableDithering -R %s -I %s", REF, CEUTRIO_BAM) + " --no_cmdline_in_header -o %s -L 20:10130000-10134800";
        final WalkerTestSpec spec = new WalkerTestSpec(base, 1, Arrays.asList("707be4798b1e14e3d6827a49104be120"));
        final File outputVCF = executeTest("testQualByDepth", spec).getFirst().get(0);

        final String baseNoQD = String.format("-T HaplotypeCaller --disableDithering -R %s -I %s", REF, CEUTRIO_BAM) + " --no_cmdline_in_header -o %s -L 20:10130000-10134800 -XA QualByDepth";
        final WalkerTestSpec specNoQD = new WalkerTestSpec(baseNoQD, 1, Arrays.asList("7e582b422a5de47706daefaae17b8245"));
        specNoQD.disableShadowBCF();
        final File outputVCFNoQD = executeTest("testQualByDepth calling without QD", specNoQD).getFirst().get(0);

        final String baseAnn = String.format("-T VariantAnnotator -R %s -V %s", REF, outputVCFNoQD.getAbsolutePath()) + " --no_cmdline_in_header -o %s -L 20:10130000-10134800 -A QualByDepth";
        final WalkerTestSpec specAnn = new WalkerTestSpec(baseAnn, 1, Arrays.asList("cc4e2b12872b2b25ab1c106516b2ac6a"));
        specAnn.disableShadowBCF();
        final File outputVCFAnn = executeTest("testQualByDepth re-annotation of QD", specAnn).getFirst().get(0);

        // confirm that the QD values are present in the new file for all biallelic variants
        // QD values won't be identical because some filtered reads are missing during re-annotation

        final VCFCodec codec = new VCFCodec();
        final FileInputStream s = new FileInputStream(outputVCF);
        final LineIterator lineIterator = codec.makeSourceFromStream(new PositionalBufferedStream(s));
        codec.readHeader(lineIterator);

        final VCFCodec codecAnn = new VCFCodec();
        final FileInputStream sAnn = new FileInputStream(outputVCFAnn);
        final LineIterator lineIteratorAnn = codecAnn.makeSourceFromStream(new PositionalBufferedStream(sAnn));
        codecAnn.readHeader(lineIteratorAnn);

        while( lineIterator.hasNext() && lineIteratorAnn.hasNext() ) {
            final String line = lineIterator.next();
            Assert.assertFalse(line == null);
            final VariantContext vc = codec.decode(line);

            final String lineAnn = lineIteratorAnn.next();
            Assert.assertFalse(lineAnn == null);
            final VariantContext vcAnn = codecAnn.decode(lineAnn);

	    Assert.assertTrue(vc.hasAttribute("QD"));
	    Assert.assertTrue(vcAnn.hasAttribute("QD"));
        }

        Assert.assertFalse(lineIterator.hasNext());
        Assert.assertFalse(lineIteratorAnn.hasNext());
    }
}
