import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.HashMap;

public class Resultaat {
    static final String[] ONE = {"A", "R", "N", "D", "C", "Q", "E", "G", "H", "I", "L", "K"
            , "M", "F", "P", "S", "T", "W", "Y", "V"};


    static final HashMap<String,String> codonTable = new HashMap<>();

    private String dnaSeq;
    private int id;

    public String getDnaSeq() {
        return dnaSeq;
    }

    public void setDnaSeq(String dnaSeq) {
        this.dnaSeq = dnaSeq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void calcAminoAcid() {
        System.out.println("hi");
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
        String aminoSeq = "";
        int excess = this.dnaSeq.length() % 3;
        System.out.println(this.dnaSeq.length());
        for (int i = 0; i <= (this.dnaSeq.length()) - (2 + excess); i += 3) {
            String codon = this.dnaSeq.substring(i, i + 3);
            System.out.println("codon:"+codon);
            String aminoAcid =codonTable.get(codon);
            System.out.println(aminoAcid);
            aminoSeq.concat(codonTable.get(codon));
        }


//                    aminoSeq.concat(codon);
                   // aminoSeq+=codon;
                }

            }



