import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.*;
// er is een global int counter nodig om de count bij te houden
// er is een string minORFLength nodig om de minimale orf length aan te geven
// reverseString wordt gebruikt om voor findReverseORFs de string om te draaien
// findReverseORFs gebruikt als input eht resultaat van reverseString
// findReverseORFs en findORFs verwachten eerst de sequentie ,en daarna een strign met de minimale ORF lengte
// HashSet<OrfResultaat> is waar de orfs in worden opgeslagen
// er wordt in de regular expressions geen rekening gehouden met codons omdat het readign frame kan verschuiiven door intronen
// imports:
//import java.util.HashSet;
//import java.util.regex.*;

public class FindORF {
    private static String seqGiven = "ATC";//AATGCATTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTCTGCGATGCTATGGATGATAGATAGTAAGATAGTAGATAGATGATAGUGAACGTACGTCGTACGCGTGCCATGCAATGCTAGCATGCGCCGTAATGTAGCTGACTCGATACTCATGCTAGTAATATACCACTGATCGTACGATCGATCGATGTAGATACTCGTGCATGAGCTAGTCGATCGCTGCTAGCTACGTACGTCGTACCGCTACGTAGTCGTGCTGATCGATGCACGATCGCTGATCGCTGACTGACTGACTGACTGCTGACTGATCACTCGTACTACGACTGACTGACTAGCGTACGTACGACTGACGACACTGACTACGTACGTACGACTGACTGACTGACTGACTACGATCACTGACTACGTACGATCCGTACGACTGACTACGACTGACTACGACTACGTACGACTACGACTGACTACTCATGACTGACTGACTGCAACTGCTAGTCAGACTCTCGTCGACTCAGCTGCATCATCTCACTAACGTACGCATGCTCGTCGTCCTGCTCGACGCTCGATACCTCTACGTCACATGCTGCATCATCCTACACTCATCCGCCGATGCTAGCTACGTCGACTGATCGCTACGTACGTACCGCTGACTGACTCTAAT"; // test string
    public static HashMap<Integer, OrfResultaat> results = new HashMap<Integer, OrfResultaat>(); // Create hashset for ORF objects
    public static int count = 0;                            // counter for ORF id
    public static ArrayList<Integer> oRFToKeep = new ArrayList<Integer>();


    public static void main(String[] args) {
        oRFToKeep.add(1);
        oRFToKeep.add(2);
        checkDNA(seqGiven);
        String reverse = reverseString(seqGiven);       // reverses sequence
        String minOrfLength = "5";                      // min orf length for testing purposes
        findORFs(seqGiven, minOrfLength);              // finds forward ORFs
        findReverseORFs(reverse, minOrfLength);         // finds reverse ORFs
    }


    public static void checkDNA(String seqGiven) {
        int aCTG = 0;
        Pattern actg = Pattern.compile("[ACTG]");
        Matcher match = actg.matcher(seqGiven);
        while (match.find()) {
           aCTG++;
           } ;
        if (seqGiven.length() != aCTG){
            System.out.println("illegal sequence");
        }
    }

    /**
     * this function reverses the DNA sequence
     *
     * @param seqGiven the DNA sequence
     * @return a string containing the reverse of the given DNA sequence
     */
    private static String reverseString(String seqGiven) {
        String str = "read.nextLine()";
        String reverse = "";

        for (int i = seqGiven.length() - 1; i >= 0; i--) {
            reverse = reverse + seqGiven.charAt(i);
        }
        System.out.println("reverse: " + reverse);
        return reverse;

    }

    /**
     * this function is meant to find forward readign frames
     * this function requires a global int named counter
     *
     * @param sequence     the given DNA sequence
     * @param minOrfLength String containing the min length of an orf
     */
    public static void findORFs(String sequence, String minOrfLength) {
        // String to be scanned to find the pattern.
        String string = "ATG(.{" + minOrfLength + ",}?)(TAA|TAG|TGA)";                           // gene signals

        // Create a Pattern object
        Pattern p = Pattern.compile(string);

        // get a matcher object
        Matcher m = p.matcher(sequence);

        while (m.find()) {
            int readignframe = (m.start() % 3) + 1;                 // calculates the readign frame
            if (oRFToKeep.contains(readignframe)) {
                count++;
                String oRFSequence = sequence.substring(m.start(), m.end());
                results.put(count, new OrfResultaat(m.start(), m.end()));                  // stores the start and stop positions
                results.get(count).setId(count);                                                                        // add id to ORFResult
                results.get(count).setDnaSeq(oRFSequence);                                                              // add Sequence to ORFResult
                System.out.println("seq: " + oRFSequence);
            }
        }
    }

    /**
     * this function is meant to find reverse readign frames
     * this function requires a global int named counter
     *
     * @param sequence     the reverse of the given DNA sequence
     * @param minOrfLength String containing the min length of an orf
     */
    public static void findReverseORFs(String sequence, String minOrfLength) {
        int length = sequence.length();
        // String to be scanned to find the pattern.
        String string = "ATG(.{" + minOrfLength + ",}?)(TAA|TAG|TGA)";                           // gene signals

        // Create a Pattern object
        Pattern p = Pattern.compile(string);

        // get a matcher object
        Matcher m = p.matcher(sequence);

        while (m.find()) {
            int readignframe = (m.start() % 3) + 1;                 // calculates the readign frame
            if (oRFToKeep.contains(readignframe)) {
                count++;
                String oRFSequence = sequence.substring(m.start(), m.end());
                results.put(count, new OrfResultaat(length - m.start(), length - m.end()));                  // stores the start and stop positions
                results.get(count).setId(count);                                                                        // add id to ORFResult
                results.get(count).setDnaSeq(oRFSequence);                                                              // add Sequence to ORFResult
                System.out.println("rev seq: " + oRFSequence);
            }
        }
    }
}

