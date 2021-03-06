import java.util.HashMap;

public class OrfResultaat extends Resultaat {
    private int start;
    private int stop;
    private String strand;
    private int frame;
    private int orfNr;
    private String dnaSeq;
    private String totalDnaSeq;

    /**
     * returns the whole dna sequence.
     * @return string containing whole dna sequence.
     */
    public String getTotalDnaSeq() {
        return this.totalDnaSeq;
    }

    /**
     * sets the whole dna sequence.
     * @param totalDnaSeq string containing whole dna sequence.
     */
    public void setTotalDnaSeq(String totalDnaSeq) {
        this.totalDnaSeq = totalDnaSeq;
    }

    /**
     * returns frame
     * @return int containing frame.
     */
    public int getFrame() {
        return frame;
    }

    /**
     * sets frame
     * @param frame int containing frame
     */
    public void setFrame(int frame) {
        this.frame = frame;
    }

    /**
     * gets strand.
     * @return strand
     */
    public String getStrand() {
        return strand;
    }

    /**
     * sets strand.
     * @param strand strand.
     */
    public void setStrand(String strand) {
        this.strand = strand;
    }

    /**
     * get start position
     * @return start position
     */
    public int getStart() {
        return start;
    }

    /**
     * sets start position
     * @param start start position
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * gets stop position
     * @return stop position
     */
    public int getStop() {
        return stop;
    }

    /**
     * sets stop position
     * @param stop stop position
     */
    public void setStop(int stop) {
        this.stop = stop;
    }

    static final String[] ONE = {"A", "R", "N", "D", "C", "Q", "E", "G", "H", "I", "L", "K"
            , "M", "F", "P", "S", "T", "W", "Y", "V"};

    static final HashMap<String,String> codonTable = new HashMap<>();


    /**
     * sets orf id
     */
    OrfResultaat() {
        setId(orfNr++);
    }

    /**
     * gets orf length
     * @return orf length
     */
    public int getLength() {
        if (this.getStrand().equals("+"))
            return this.stop - this.start;
        return this.start - this.stop;
    }

    /**
     * gets orf dna sequence
     * @return orf dna sequence
     */
    public String getDnaSeq() {
        return dnaSeq;
    }

    /**
     * sets orf dna sequence
     * @param dnaSeq orf dna sequence
     */
    public void setDnaSeq(String dnaSeq) {
        this.dnaSeq = dnaSeq;
    }

    /**
     * gets animo acid sequence.
     * @param dnaSequence dna sequence
     * @return amino acid sequence
     */
    public String getAminoSeq(String dnaSequence) {
        codonTable.put("TTT","P");codonTable.put("CCT","P");codonTable.put("AAC","N");codonTable.put("GGA","G");
        codonTable.put("TTC","F");codonTable.put("CCC","P");codonTable.put("AAA","K");codonTable.put("GGG","G");
        codonTable.put("TTA","L");codonTable.put("CCA","P");codonTable.put("AAG","K");codonTable.put("ACA","T");
        codonTable.put("TTG","L");codonTable.put("CCG","P");codonTable.put("GAT","D");
        codonTable.put("CTT","L");codonTable.put("ACT","T");codonTable.put("GAA","E");
        codonTable.put("CTC","L");codonTable.put("ACC","T");codonTable.put("GAG","E");
        codonTable.put("CTA","L");codonTable.put("ACG","T");codonTable.put("TGT","C");
        codonTable.put("CTG","L");codonTable.put("GCT","A");codonTable.put("TGC","C");
        codonTable.put("ATT","I");codonTable.put("GCC","A");codonTable.put("TGA","W");
        codonTable.put("ATC","I");codonTable.put("GCA","A");codonTable.put("TGG","W");
        codonTable.put("ATA","I");codonTable.put("GCG","A");codonTable.put("CGT","R");
        codonTable.put("ATG","M");codonTable.put("TAT","Y");codonTable.put("CGC","R");
        codonTable.put("GTT","V");codonTable.put("TAC","Y");codonTable.put("CGA","R");
        codonTable.put("GTC","V");codonTable.put("TAA","-");codonTable.put("CGG","R");
        codonTable.put("GTA","V");codonTable.put("TAG","-");codonTable.put("AGT","S");
        codonTable.put("GTG","V");codonTable.put("CAT","H");codonTable.put("AGC","S");
        codonTable.put("TCT","S");codonTable.put("CAC","H");codonTable.put("AGA","R");
        codonTable.put("TCC","S");codonTable.put("CAA","Q");codonTable.put("AGG","R");
        codonTable.put("TCA","S");codonTable.put("CAG","Q");codonTable.put("GGT","G");
        codonTable.put("TCG","S");codonTable.put("AAT","N");codonTable.put("GGC","G");
        StringBuilder aminoSeq = new StringBuilder();
        int excess = dnaSequence.length() % 3;
        for (int i = 0; i <= (dnaSequence.length()) - (2 + excess); i += 3) {
            String codon = dnaSequence.substring(i, i + 3);
            aminoSeq.append(codonTable.get(codon));
        }
        return aminoSeq.toString().replace("-", "");
    }
}
