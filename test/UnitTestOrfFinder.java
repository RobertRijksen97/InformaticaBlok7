import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitTestOrfFinder {
    public static void main(String[] args) {
        String sequence = "TGACGATCGACTGCATCGAATGGTCGTCCGTGGGCTAGCTAGCTAGCAAGTGATACGACTGACTCAGT";
        String minOrfLength = "20";
        String string = "ATG(.{" + minOrfLength + ",}?)(TAA|TAG|TGA)";                           // gene signals
        Pattern p = Pattern.compile(string);
        Matcher m = p.matcher(sequence);
        while (m.find()) {
            String oRFSequence = sequence.substring(m.start(), m.end());
            if (oRFSequence.equals("ATGGTCGTCCGTGGGCTAGCTAGCTAG")) {
                System.out.println("TRUE");
            } else {
                System.out.println("FALSE");
            }

        }
    }
}