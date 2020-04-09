package ProjectBlok7;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class OrfFinder extends JFrame implements ActionListener, MouseListener, WindowListener {
    private static JButton browseBt, databaseBt, startOrfBt, saveDatabaseBt, blastNtBt, blastProtBt, goBackBt, dbGoBackBt, selectAllBlastBt, selectAllOrfBt;
    private static JTextField fileTf, orfLengthTf;
    private static JLabel orfNameLb, dbOrfNameLb, nrSelectedLb,  blastNrSelectedLb;
    private static JPanel tabelPnl, blastResultsPnl, dbSeqPnl, orfExtInfPnl, orfInfoPnl;
    private static JComboBox strandBox, readFrmBox, databaseNtBox, databaseProtBox, databaseEntryBox;
    private static JRadioButton blastpRb, blastnRb;
    private static JTable orfInfoTb, databaseOrfTb, databaseBlastTb, blastTable;
    private static JFrame databaseFrame;
    private static JScrollPane dbBlastSp, dbOrfSp;
    private static int orfSelected = 0, blastResSelected = 0, panelstate;
    private static File path;
    private static JTextArea nuclSeqTxtArea, aminoSeqTxtArea, dbAminoSeqTxtArea, dbNuclSeqTxtArea;
    private static HashMap<String, OrfResultaat> orfResult, dbOrfResultaat;
    private static HashMap<String, BlastResultaat> blastResultaat, dbBlastResultaat;
    private static String seq;

    public static void main(String[] args) {
        OrfFinder frame = new OrfFinder();
        frame.setTitle("ORF Finder");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.createGui();
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Makes a table containing ORF data.
     * @return a 2 dimensional Object array containing ORF data
     */
    private static Object[][] makeDbOrfTableData() {
        try {
            Object[][] tableData = new Object[dbOrfResultaat.size()][8];
            int counter = 0;
            for (OrfResultaat orf : dbOrfResultaat.values()) {
                tableData[counter] = new Object[]{orf.getId(), orf.getStrand(), orf.getFrame(), orf.getLength(), orf.getStart(), orf.getStop(), "Click for sequence", "Click total sequence"};
                counter++;
            }
            return tableData;
        } catch (NullPointerException e) {
            return new Object[0][0];
        }
    }

    /**
     * Calls a function in the database_orf.py file.
     * @param function the function to be called.
     * @param arg2 argument 1.
     * @param arg3 argument 2.
     * @return returns a String containing the output from the function called.
     * @throws IOException
     * @throws InterruptedException
     */
    private static String useDbPython(String function, String arg2, String arg3) throws IOException, InterruptedException {
        StringBuilder pythonReturn = new StringBuilder();
        ProcessBuilder processbuild = new ProcessBuilder().command("python", System.getProperty("user.dir") + "/src/ProjectBlok7/database_orf.py", function, arg2, arg3);
        Process p = processbuild.start();
        p.waitFor();
        System.out.println("Exitcode" + p.exitValue());
        String line;
        BufferedReader inputreader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = inputreader.readLine()) != null)
            pythonReturn.append(line).append("\n");
        BufferedReader errorreader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        System.out.println("\nPython errors:");
        while ((line = errorreader.readLine()) != null)
            System.out.println(line);
        return pythonReturn.toString();
    }

    /**
     * Calls the BLAST.py file.
     * @param blast The blast type to be executed.
     * @param db the database to be called.
     * @param toBlast the sequence to be BLASTed.
     * @param id the ORF id.
     * @return String containing blast results seperated by tab.
     * @throws IOException
     * @throws InterruptedException
     */
    private static String useBlastPython(String blast, String db, String toBlast, int id) throws IOException, InterruptedException {
        StringBuilder pythonReturn = new StringBuilder();
        ProcessBuilder processbuild = new ProcessBuilder().command("python", System.getProperty("user.dir") + "/src/ProjectBlok7/BLAST.py", blast, db, toBlast, Integer.toString(id));
        Process p = processbuild.start();
        p.waitFor();

        System.out.println("Exitcode" + p.exitValue());
        String line;
        BufferedReader inputreader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = inputreader.readLine()) != null)
            pythonReturn.append(line);
        BufferedReader errorreader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        System.out.println("\nPython errors:");
        while ((line = errorreader.readLine()) != null)
            System.out.println(line);
        return pythonReturn.toString();
    }

    /**
     * Makes data used by the table in de database.
     * @return returns data used by the table in de database.
     */
    private static Object[][] makeDbBlastTableData() {
        try {
            Object[][] tableData = new Object[dbBlastResultaat.size()][6];
            int counter = 0;
            for (BlastResultaat blastRes : dbBlastResultaat.values()) {
                tableData[counter] = new Object[]{blastRes.getId(), blastRes.getAccesion(), blastRes.geteValue(), blastRes.getOrganism(), blastRes.getPercIdentity(), blastRes.getDescription()};
                counter++;
            }
            return tableData;
        } catch (NullPointerException e) {
            return new Object[0][0];
        }
    }

    /**
     * Collects data from the database.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void getDatabaseInfo() throws IOException, InterruptedException {
        String[] info = useDbPython("collect", "ORFs", "").split("\n");
        dbOrfResultaat = new HashMap<>(info.length);
        for (String i : info) {
            System.out.println(i);
            String[] f = i.split("\t");
            System.out.println(Arrays.toString(f));
            if (f.length != 1)
                dbOrfResultaat.put(f[1], makeDbOrftResult(f));
        }
        info = useDbPython("collect", "BlastResultsORF", "").split("\n");
        dbBlastResultaat = new HashMap<>(info.length);

        for (String i : info) {
            String[] f = i.split("\t");
            if (f.length != 1)
                dbBlastResultaat.put(f[5], makeDbBlastResult(f));
        }
    }

    /**
     * Makes a BLAST result with data from the database.
     * @param blastInfo A string array containing info about the blast result.
     * @return returns a BlastResultaat class object.
     * @throws IOException
     * @throws InterruptedException
     */
    private static BlastResultaat makeDbBlastResult(String[] blastInfo) throws IOException, InterruptedException {
        BlastResultaat blastRes = new BlastResultaat();
        blastRes.setAccesion(blastInfo[0]);
        blastRes.setDescription(blastInfo[1]);
        blastRes.seteValue(blastInfo[2]);
        blastRes.setPercIdentity(Integer.parseInt(blastInfo[3]));
        blastRes.setId(Integer.parseInt(blastInfo[4]));
        blastRes.setOrganism(useDbPython("getOrgName", blastInfo[6], ""));
        return blastRes;
    }

    /**
     * Makes an ORF result with data from the database.
     * @param orfInfo  string array containing info about an ORF.
     * @return returns a OrfResultaat class object.
     * @throws IOException
     * @throws InterruptedException
     */
    private static OrfResultaat makeDbOrftResult(String[] orfInfo) throws IOException, InterruptedException {
        OrfResultaat orf = new OrfResultaat();
        orf.setDnaSeq(orfInfo[0]);
        orf.setId(Integer.parseInt(orfInfo[1]));
        orf.setStrand(orfInfo[2]);
        orf.setFrame(Integer.parseInt(orfInfo[3]));
        orf.setStart(Integer.parseInt(orfInfo[5]));
        orf.setStop(Integer.parseInt(orfInfo[6]));
        orf.setTotalDnaSeq(useDbPython("collect_dna", orfInfo[7], ""));
        return orf;
    }

    /**
     * Makes a blast result using data from a BLAST result.
     * @param f A string array containing data from a BLAST result.
     * @param id an ORF id.
     * @return returns a BlastResultaat class object.
     */
    private static BlastResultaat makeBlastResult(String[] f, int id) {
        BlastResultaat blastRes = new BlastResultaat();
        blastRes.setId(id);
        blastRes.setDescription(f[1]);
        blastRes.setOrganism(f[5]);
        blastRes.setPercIdentity(Integer.parseInt(f[3]));
        blastRes.seteValue(f[2]);
        blastRes.setAccesion(f[0]);
        return blastRes;
    }

    /**
     * Turns selected path in a sequence.
     * @return a DNA sequence.
     * @throws IOException
     */
    private static String readPath() throws IOException {
        StringBuilder seq = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        if (!(line = br.readLine()).startsWith(">"))
            seq.append(line);
        while ((line = br.readLine()) != null) {
            seq.append(line);
        }
        return seq.toString();
    }

    /**
     * checks if a given string is dna.
     * @param seqGiven a string containing a sequence.
     * @return a boolean. true means the given sequence is dna.
     */
    private static boolean checkDNA(String seqGiven) {
        int nrACTG = 0;
        Pattern actg = Pattern.compile("[ACTGNatgcn]");
        Matcher match = actg.matcher(seqGiven);
        while (match.find())
            nrACTG++;
        return seqGiven.length() == nrACTG;
    }

    /**
     * detemines orfs.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void determineOrfs() throws IOException, InterruptedException {
        orfResult = new HashMap<>();
        int orfsFound = Integer.parseInt(useDbPython("latestID", "", "").strip());
        try {
            if (path.getName().equals(fileTf.getText()))
                seq = readPath();
            else
                seq = fileTf.getText();
        } catch (NullPointerException e) {
            seq = fileTf.getText();
        }

        if (!checkDNA(seq)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Wrong sequence! Please select a DNA sequence in FASTA format",
                    "Wrong sequence",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String minOrfLength = orfLengthTf.getText();
        try {
            if (Integer.parseInt(minOrfLength) < 30)
                minOrfLength = "30";
        } catch (NumberFormatException e) {
            minOrfLength = "30";
        }

        switch (strandBox.getSelectedIndex()) {
            case 0:
                orfsFound = findORFs(seq, minOrfLength, orfsFound, "+");
                findORFs(reverseString(seq), minOrfLength, orfsFound, "-");
                break;
            case 1:
                findORFs(seq, minOrfLength, orfsFound, "+");
                break;
            case 2:
                findORFs(reverseString(seq), minOrfLength, orfsFound, "-");
                break;
        }
    }

    /**
     * reverses a string.
     * @param seqGiven a string to be reversed.
     * @return a reversed string.
     */
    private static String reverseString(String seqGiven) {
        StringBuilder reverse = new StringBuilder();
        for (int i = seqGiven.length() - 1; i >= 0; i--) {
            reverse.append(seqGiven.charAt(i));
        }
        return reverse.toString();
    }

    /**
     * looks for orfs in a given sequence.
     * @param sequence string containing a sequence.
     * @param minOrfLength minimal orf length.
     * @param orfsFound amount of orfs found.
     * @param strand the strand(s) to be searched.
     * @return number of orfs found.
     */
    public static int findORFs(String sequence, String minOrfLength, int orfsFound, String strand) {
        String string = "ATG(.{" + minOrfLength + ",}?)(TAA|TAG|TGA)";
        Matcher m = Pattern.compile(string).matcher(sequence);

        while (m.find()) {
            orfsFound++;
            int frame = m.start() % 3 + 1;
            if (readFrmBox.getSelectedIndex() == 0 || readFrmBox.getSelectedIndex() == frame)
                orfResult.put(Integer.toString(orfsFound), makeOrfResult(m, orfsFound, strand, frame, sequence));
        }
        return orfsFound;
    }

    /**
     * turns data into an orf result.
     * @param m contains info about the found orf.
     * @param orfsFound number of orfs found.
     * @param strand the strand of the found orf.
     * @param frame the frame of the found orf.
     * @param sequence the sequence of the found orf.
     * @return an OrfResultaat class object.
     */
    private static OrfResultaat makeOrfResult(Matcher m, int orfsFound, String strand, int frame, String sequence) {
        OrfResultaat orf = new OrfResultaat();
        orf.setId(orfsFound);
        orf.setStrand(strand);
        orf.setFrame(frame);
        if (!strand.equals("+")) {
            orf.setStart(sequence.length() - m.start());
            orf.setStop(sequence.length() - m.end());
        } else {
            orf.setStart(m.start());
            orf.setStop(m.end());
        }
        orf.setDnaSeq(sequence.substring(m.start(), m.end()));
        return orf;
    }

    /**
     * makes data for the table containing info about the found orfs.
     * @return a 2 dimensional object array containing orf data.
     */
    private static Object[][] getOrfTableData() {
        try {
            Object[][] tableData = new Object[orfResult.size()][8];
            int counter = 0;
            for (OrfResultaat orf : orfResult.values()) {
                tableData[counter] = new Object[]{"", orf.getId(), orf.getStrand(), orf.getFrame(), orf.getLength(), orf.getStart(), orf.getStop(), "Click for sequence"};
                counter++;
            }
            return tableData;
        } catch (NullPointerException e) {
            return new Object[0][0];
        }
    }

    /**
     * Creates the GUI.
     */
    private void createGui() {
        Container window = getContentPane();
        window.setLayout(new GridBagLayout());

        Border blackline = BorderFactory.createLineBorder(Color.BLACK);

        JLabel seqLb = new JLabel("Sequence:");
        window.add(seqLb, getConstraints(GridBagConstraints.NONE, 1, 0, 0,
                0, 0, 0, 0, new Insets(25, 25, 10, 20), GridBagConstraints.LINE_END));

        fileTf = new JTextField("Paste a DNA sequence here or select a FASTA file.");
        window.add(fileTf, getConstraints(GridBagConstraints.HORIZONTAL, 3, 0, 1,
                5, 0, 0, 0, new Insets(25, 0, 10, 0), GridBagConstraints.LINE_START));

        browseBt = new JButton("Browse");
        browseBt.addActionListener(this);
        window.add(browseBt, getConstraints(GridBagConstraints.NONE, 2, 0, 4,
                3, 0, 0, 0, new Insets(25, 10, 10, 0), GridBagConstraints.LINE_START));

        JLabel paramOrfLb = new JLabel("Parameters:");
        window.add(paramOrfLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0,
                5, 0, 0, 0, new Insets(0, 25, 0, 0), GridBagConstraints.LINE_START));

        JLabel orfLengthLB = new JLabel("• Minimal ORF length");
        window.add(orfLengthLB, getConstraints(GridBagConstraints.NONE, 1, 2, 0,
                0, 0, 0, 0, new Insets(0, 25, 0, 0), GridBagConstraints.LINE_START));

        TitledBorder border = BorderFactory.createTitledBorder(blackline, "Miminal = 30");
        border.setTitleJustification(TitledBorder.CENTER);

        orfLengthTf = new JTextField("75");
        orfLengthTf.setBorder(border);
        window.add(orfLengthTf, getConstraints(GridBagConstraints.HORIZONTAL, 1, 2, 1,
                0, 80, 0, 0, new Insets(0, 20, 0, 0), GridBagConstraints.LINE_START));

        JLabel readFrmLb = new JLabel("• Reading frame:");
        window.add(readFrmLb, getConstraints(GridBagConstraints.NONE, 1, 3, 0,
                0, 0, 0, 0, new Insets(3, 25, 3, 0), GridBagConstraints.LINE_START));

        readFrmBox = new JComboBox<>(new String[]{"1, 2, 3", "1", "2", "3"});
        window.add(readFrmBox, getConstraints(GridBagConstraints.HORIZONTAL, 1, 3, 1,
                0, 0, 0, 0, new Insets(3, 20, 3, 0), GridBagConstraints.LINE_START));

        JLabel strandLb = new JLabel("• Strand(s) to read:");
        window.add(strandLb, getConstraints(GridBagConstraints.NONE, 1, 4, 0,
                0, 0, 0, 0, new Insets(3, 25, 0, 0), GridBagConstraints.LINE_START));

        strandBox = new JComboBox<>(new String[]{"Both", "Forward", "Reverse"});
        window.add(strandBox, getConstraints(GridBagConstraints.HORIZONTAL, 1, 4, 1,
                0, 0, 0, 0, new Insets(3, 20, 0, 0), GridBagConstraints.LINE_START));

        startOrfBt = new JButton("Determine ORF's");
        startOrfBt.addActionListener(this);
        window.add(startOrfBt, getConstraints(GridBagConstraints.NONE, 2, 4, 2,
                0, 0, 0, 0, new Insets(3, 80, 0, 0), GridBagConstraints.LINE_START));

        databaseBt = new JButton("Open database");
        databaseBt.addActionListener(this);
        window.add(databaseBt, getConstraints(GridBagConstraints.NONE, 1, 4, 4,
                0, 0, 0, 0, new Insets(3, 10, 7, 25), GridBagConstraints.LINE_END));

        JPanel blastPnl = new JPanel();
        blastPnl.setLayout(new GridLayout(1, 0));
        blastPnl.setBackground(new Color(200, 200, 200));
        blastPnl.setMinimumSize(new Dimension(275, 250));
        blastPnl.setPreferredSize(new Dimension(275, 250));
        blastPnl.setBorder(blackline);
        window.add(blastPnl, getConstraints(GridBagConstraints.NONE, 2, 6, 0,
                0, 0, 0, 0, new Insets(7, 25, 0, 0), GridBagConstraints.CENTER));

        JPanel protBlastPnl = new JPanel();
        protBlastPnl.setLayout(new GridBagLayout());
        protBlastPnl.setBackground(new Color(200, 200, 200));

        ButtonGroup blastTypeProt = new ButtonGroup();

        JLabel blastTypeProtLb = new JLabel("Blast Type:");
        protBlastPnl.add(blastTypeProtLb, getConstraints(GridBagConstraints.NONE, 2, 0, 0,
                0, 0, 1, 0, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        blastpRb = new JRadioButton("BLASTp");
        blastTypeProt.add(blastpRb);
        protBlastPnl.add(blastpRb, getConstraints(GridBagConstraints.NONE, 1, 1, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        JRadioButton blastxRb = new JRadioButton("BLASTx");
        blastTypeProt.add(blastxRb);
        protBlastPnl.add(blastxRb, getConstraints(GridBagConstraints.NONE, 1, 2, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        JLabel databaseProtLb = new JLabel("Database:");
        protBlastPnl.add(databaseProtLb, getConstraints(GridBagConstraints.NONE, 2, 3, 0,
                0, 0, 1, 0, new Insets(0, 0, 5, 0), GridBagConstraints.PAGE_END));

        blastpRb.setSelected(true);

        databaseProtBox = new JComboBox<>(new String[]{"nr", "swissprot", "refseq"});
        protBlastPnl.add(databaseProtBox, getConstraints(GridBagConstraints.NONE, 2, 4, 0,
                0, 0, 0, 0, new Insets(5, 0, 5, 0), GridBagConstraints.CENTER));

        blastProtBt = new JButton("BLAST selected ORF(s)");
        blastProtBt.addActionListener(this);
        blastProtBt.setEnabled(false);
        protBlastPnl.add(blastProtBt, getConstraints(GridBagConstraints.NONE, 2, 5, 0,
                0, 0, 1, 1, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        JPanel ntBlastPnl = new JPanel();
        ntBlastPnl.setLayout(new GridBagLayout());
        ntBlastPnl.setBackground(new Color(200, 200, 200));

        ButtonGroup blastTypeNt = new ButtonGroup();

        JLabel blastTypeNtLb = new JLabel("Blast Type:");
        ntBlastPnl.add(blastTypeNtLb, getConstraints(GridBagConstraints.NONE, 2, 0, 0,
                0, 0, 1, 0, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        blastnRb = new JRadioButton("BLASTn");
        blastTypeNt.add(blastnRb);
        ntBlastPnl.add(blastnRb, getConstraints(GridBagConstraints.NONE, 1, 1, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        JRadioButton tblastnRb = new JRadioButton("tBLASTn");
        blastTypeNt.add(tblastnRb);
        ntBlastPnl.add(tblastnRb, getConstraints(GridBagConstraints.NONE, 1, 2, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        blastnRb.setSelected(true);

        JLabel databaseNtLb = new JLabel("Database:");
        ntBlastPnl.add(databaseNtLb, getConstraints(GridBagConstraints.NONE, 2, 3, 0,
                0, 0, 1, 0, new Insets(0, 0, 5, 0), GridBagConstraints.PAGE_END));

        databaseNtBox = new JComboBox<>(new String[]{"nt", "refseq_rna", "pdb"});
        ntBlastPnl.add(databaseNtBox, getConstraints(GridBagConstraints.NONE, 2, 4, 0,
                0, 0, 0, 0, new Insets(5, 0, 5, 0), GridBagConstraints.CENTER));

        blastNtBt = new JButton("BLAST selected ORF(s)");
        blastNtBt.addActionListener(this);
        blastNtBt.setEnabled(false);
        ntBlastPnl.add(blastNtBt, getConstraints(GridBagConstraints.NONE, 2, 5, 0,
                0, 0, 1, 1, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Protein BLAST", null, protBlastPnl, null);
        tabbedPane.addTab("Nucleotide BLAST", null, ntBlastPnl, null);
        tabbedPane.setSelectedIndex(0);
        blastPnl.add(tabbedPane);

        blastResultsPnl = new JPanel();
        blastResultsPnl.setBorder(blackline);
        blastResultsPnl.setPreferredSize(new Dimension(670, 300));
        window.add(blastResultsPnl, getConstraints(GridBagConstraints.BOTH, 3, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));
        blastResultsPnl.setVisible(false);

        orfInfoPnl = new JPanel();
        orfInfoPnl.setLayout(new GridBagLayout());
        orfInfoPnl.setBackground(new Color(200, 200, 200));
        orfInfoPnl.setBorder(blackline);
        orfInfoPnl.setPreferredSize(new Dimension(670, 300));
        window.add(orfInfoPnl, getConstraints(GridBagConstraints.BOTH, 3, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));

        selectAllOrfBt = new JButton("Select all");
        selectAllOrfBt.addActionListener(this);
        orfInfoPnl.add(selectAllOrfBt, getConstraints(GridBagConstraints.NONE, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 5, 0, 0), GridBagConstraints.LINE_START));

        JLabel orfInfoLb = new JLabel("Info about found ORF(s)");
        orfInfoPnl.add(orfInfoLb, getConstraints(GridBagConstraints.NONE, 3, 0, 0,
                0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

        nrSelectedLb = new JLabel("0 ORF(s) selected");
        orfInfoPnl.add(nrSelectedLb, getConstraints(GridBagConstraints.NONE, 1, 0, 2,
                0, 0, 0, 0, new Insets(5, 0, 0, 5), GridBagConstraints.LINE_END));

        orfInfoTb = new JTable(new DefaultTableModel(null, new String[]{"Select", "ORF ID", "Strand", "Frame", "Length", "Start position", "Stop position", "Sequence info"}));
        orfInfoTb.setAutoCreateRowSorter(true);
        orfInfoTb.setDefaultEditor(Object.class, null);
        orfInfoTb.getTableHeader().setReorderingAllowed(false);

        orfInfoTb.getColumn("Select").setMaxWidth(50);
        orfInfoTb.getColumn("ORF ID").setMaxWidth(60);
        orfInfoTb.getColumn("Strand").setMaxWidth(60);
        orfInfoTb.getColumn("Frame").setMaxWidth(60);
        orfInfoTb.getColumn("Length").setMaxWidth(70);

        orfInfoTb.addMouseListener(this);
        JScrollPane scrollPane = new JScrollPane(orfInfoTb);
        orfInfoPnl.add(scrollPane, getConstraints(GridBagConstraints.BOTH, 3, 1, 0,
                0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.FIRST_LINE_START));

        orfExtInfPnl = new JPanel();
        orfExtInfPnl.setLayout(new GridBagLayout());
        orfExtInfPnl.setBackground(new Color(200, 200, 200));
        orfExtInfPnl.setBorder(blackline);
        orfExtInfPnl.setPreferredSize(new Dimension(670, 300));
        window.add(orfExtInfPnl, getConstraints(GridBagConstraints.BOTH, 3, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));
        orfExtInfPnl.setVisible(false);

        orfNameLb = new JLabel("ORF nr test");
        orfExtInfPnl.add(orfNameLb, getConstraints(GridBagConstraints.NONE, 2, 0, 0,
                0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

        goBackBt = new JButton("Back");
        goBackBt.addActionListener(this);
        orfExtInfPnl.add(goBackBt, getConstraints(GridBagConstraints.NONE, 1, 0, 1,
                0, 0, 0, 0, new Insets(5, 0, 0, 5), GridBagConstraints.LINE_END));

        JLabel aminoSeqLb = new JLabel("Amino acid sequence:");
        orfExtInfPnl.add(aminoSeqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0,
                0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

        aminoSeqTxtArea = new JTextArea();
        aminoSeqTxtArea.setLineWrap(true);
        JScrollPane scrollPane2 = new JScrollPane(aminoSeqTxtArea);
        orfExtInfPnl.add(scrollPane2, getConstraints(GridBagConstraints.BOTH, 2, 2, 0,
                0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

        JLabel nuclSeqLb = new JLabel("Nucleotide sequence:");
        orfExtInfPnl.add(nuclSeqLb, getConstraints(GridBagConstraints.NONE, 1, 3, 0,
                0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

        nuclSeqTxtArea = new JTextArea();
        nuclSeqTxtArea.setLineWrap(true);
        JScrollPane scrollPane3 = new JScrollPane(nuclSeqTxtArea);
        orfExtInfPnl.add(scrollPane3, getConstraints(GridBagConstraints.BOTH, 2, 4, 0,
                0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

        blastResultsPnl.setLayout(new GridBagLayout());
        blastResultsPnl.setBackground(new Color(200, 200, 200));
        blastResultsPnl.setVisible(false);

        blastTable = new JTable(new DefaultTableModel(null, new String[]{"Select", "ORF ID", "E-value", "Organism", "Description", "Percent identity", "Accesion"}));
        blastTable.setAutoCreateRowSorter(true);
        blastTable.setDefaultEditor(Object.class, null);
        blastTable.getTableHeader().setReorderingAllowed(false);
        blastTable.addMouseListener(this);
        blastTable.getColumn("Select").setMaxWidth(50);
        blastTable.getColumn("ORF ID").setMaxWidth(60);
        blastTable.getColumn("E-value").setMaxWidth(60);

        JScrollPane scrollPane6 = new JScrollPane(blastTable);
        blastResultsPnl.add(scrollPane6, getConstraints(GridBagConstraints.BOTH, 4, 1, 0,
                0, 0, 1, 1, new Insets(10, 15, 10, 15), GridBagConstraints.LINE_START));

        selectAllBlastBt = new JButton("Select all");
        selectAllBlastBt.addActionListener(this);
        blastResultsPnl.add(selectAllBlastBt, getConstraints(GridBagConstraints.NONE, 1, 2, 0,
                0, 0, 0, 0, new Insets(0, 15, 15, 0), GridBagConstraints.LINE_START));

        blastNrSelectedLb = new JLabel();
        blastResultsPnl.add(blastNrSelectedLb, getConstraints(GridBagConstraints.NONE, 4, 2, 0,
                0, 0, 0, 0, new Insets(0, 0, 15, 0), GridBagConstraints.CENTER));

        saveDatabaseBt = new JButton("Save to Database");
        saveDatabaseBt.addActionListener(this);
        window.add(saveDatabaseBt, getConstraints(GridBagConstraints.NONE, 1, 7, 4,
                10, 0, 0, 0, new Insets(10, 0, 25, 25), GridBagConstraints.LINE_END));

        panelstate = 0;
    }

    /**
     * Creates the database frame.
     */
    private void makeDatabaseFrame() {
        Thread makeDatabase = new Thread(() -> SwingUtilities.invokeLater(() -> {
            databaseBt.setEnabled(false);
            databaseFrame = new JFrame("Database");
            databaseFrame.setPreferredSize(new Dimension(825, 575));
            databaseFrame.setMinimumSize(new Dimension(650, 400));
            databaseFrame.addWindowListener(this);
            Container window = new Container();
            window.setLayout(new GridBagLayout());

            databaseEntryBox = new JComboBox<>(new String[]{"Found ORFs", "BLAST results"});
            databaseEntryBox.addActionListener(this);
            window.add(databaseEntryBox, getConstraints(GridBagConstraints.NONE, 2, 0, 2,
                    0, 0, 0, 0, new Insets(15, 0, 0, 15), GridBagConstraints.CENTER));

            tabelPnl = new JPanel();
            tabelPnl.setLayout(new GridBagLayout());
            tabelPnl.setBackground(Color.gray);
            window.add(tabelPnl, getConstraints(GridBagConstraints.BOTH, 4, 1, 0,
                    0, 0, 1, 1, new Insets(10, 15, 10, 15), GridBagConstraints.LINE_START));
            try {
                getDatabaseInfo();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            databaseOrfTb = new JTable(new DefaultTableModel(makeDbOrfTableData(), new String[]{"ORF ID", "Strand", "Frame", "Length", "Start position", "Stop position", "ORF sequence", "Total sequence"}));
            databaseOrfTb.setAutoCreateRowSorter(true);
            databaseOrfTb.setDefaultEditor(Object.class, null);
            databaseOrfTb.getTableHeader().setReorderingAllowed(false);
            databaseOrfTb.getColumn("ORF ID").setMaxWidth(60);
            databaseOrfTb.getColumn("Strand").setMaxWidth(60);
            databaseOrfTb.getColumn("Frame").setMaxWidth(60);
            databaseOrfTb.getColumn("Length").setMaxWidth(70);
            databaseOrfTb.addMouseListener(this);

            dbOrfSp = new JScrollPane(databaseOrfTb);
            tabelPnl.add(dbOrfSp, getConstraints(GridBagConstraints.BOTH, 1, 1, 0,
                    0, 0, 1, 1, new Insets(5, 5, 5, 5), GridBagConstraints.FIRST_LINE_START));

            databaseBlastTb = new JTable(new DefaultTableModel(makeDbBlastTableData(), new String[]{"ORF ID", "Accesion", "E-value", "Organism", "Percentage id", "Description"}));
            databaseBlastTb.setAutoCreateRowSorter(true);
            databaseBlastTb.setDefaultEditor(Object.class, null);
            databaseBlastTb.addMouseListener(this);
            databaseBlastTb.getTableHeader().setReorderingAllowed(false);
            databaseBlastTb.getColumn("ORF ID").setMaxWidth(60);
            databaseBlastTb.getColumn("E-value").setMaxWidth(60);

            dbBlastSp = new JScrollPane(databaseBlastTb);
            tabelPnl.add(dbBlastSp, getConstraints(GridBagConstraints.BOTH, 1, 1, 0,
                    0, 0, 1, 1, new Insets(5, 5, 5, 5), GridBagConstraints.FIRST_LINE_START));
            dbSeqPnl = new JPanel();
            dbSeqPnl.setLayout(new GridBagLayout());
            dbSeqPnl.setBackground(Color.gray);
            dbSeqPnl.setPreferredSize(new Dimension(670, 300));
            dbSeqPnl.setVisible(false);
            window.add(dbSeqPnl, getConstraints(GridBagConstraints.BOTH, 4, 1, 0,
                    0, 0, 1, 1, new Insets(10, 15, 10, 15), GridBagConstraints.LINE_START));

            dbOrfNameLb = new JLabel("ORF nr test");
            dbSeqPnl.add(dbOrfNameLb, getConstraints(GridBagConstraints.NONE, 2, 0, 0,
                    0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

            dbGoBackBt = new JButton("Back");
            dbGoBackBt.addActionListener(this);
            dbSeqPnl.add(dbGoBackBt, getConstraints(GridBagConstraints.NONE, 1, 0, 1,
                    0, 0, 0, 0, new Insets(5, 0, 0, 5), GridBagConstraints.LINE_END));

            JLabel aminoSeqLb = new JLabel("Amino acid sequence:");
            dbSeqPnl.add(aminoSeqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0,
                    0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

            dbAminoSeqTxtArea = new JTextArea();
            dbAminoSeqTxtArea.setLineWrap(true);
            JScrollPane scrollPane2 = new JScrollPane(dbAminoSeqTxtArea);
            dbSeqPnl.add(scrollPane2, getConstraints(GridBagConstraints.BOTH, 2, 2, 0,
                    0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

            JLabel nuclSeqLb = new JLabel("Nucleotide sequence:");
            dbSeqPnl.add(nuclSeqLb, getConstraints(GridBagConstraints.NONE, 1, 3, 0,
                    0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

            dbNuclSeqTxtArea = new JTextArea();
            dbNuclSeqTxtArea.setLineWrap(true);
            JScrollPane scrollPane3 = new JScrollPane(dbNuclSeqTxtArea);
            dbSeqPnl.add(scrollPane3, getConstraints(GridBagConstraints.BOTH, 2, 4, 0,
                    0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

            databaseFrame.setContentPane(window);
            databaseFrame.pack();
            databaseFrame.setVisible(true);
        }));
        makeDatabase.start();
    }

    /**
     * returns given GridBagConstraints.
     * @param fill fill
     * @param gridwidth gridwith
     * @param gridy gridy
     * @param gridx gridx
     * @param ipady ipday
     * @param ipadx ipadx
     * @param weighty weigthy
     * @param weightx weightx
     * @param insets insets
     * @param anchor anchor
     * @return GridBagConstraints.
     */
    private GridBagConstraints getConstraints(int fill, int gridwidth, int gridy, int gridx, int ipady,
                                              int ipadx, int weighty, int weightx, Insets insets, int anchor) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridwidth = gridwidth;
        c.gridheight = 1;
        c.gridy = gridy;
        c.gridx = gridx;
        c.ipady = ipady;
        c.ipadx = ipadx;
        c.weighty = weighty;
        c.weightx = weightx;
        c.insets = insets;
        c.anchor = anchor;
        return c;
    }

    /**
     * Determines what happens when the "Blast" button is clicked.
     * @param blastType the type of BLAST to be used (0 = Protein BLAST; 1 = Nucleotide BLAST)
     */
    private void blastClicked(int blastType) {
        if (orfSelected == 0) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please select an ORF to perform a BLAST search",
                    "No ORF selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        blastNrSelectedLb.setText("Now performing a BLAST search");
        blastProtBt.setEnabled(false);
        blastNtBt.setEnabled(false);
        panelstate = 1;

        selectAllBlastBt.setVisible(false);
        blastResultsPnl.setVisible(true);
        orfInfoPnl.setVisible(false);
        orfExtInfPnl.setVisible(false);
        DefaultTableModel dm = (DefaultTableModel) blastTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        Thread blast = new Thread(() -> {
            try {
                blastSearch(blastType);

                Object[][] tableData = new Object[blastResultaat.size()][8];
                System.out.println("blast REsults:::");
                for (BlastResultaat bl : blastResultaat.values()) {
                    System.out.println("id: ");
                    System.out.print(bl.getId());
                    System.out.println("evalue: ");
                    System.out.print(bl.geteValue());
                    System.out.println("organism: ");
                    System.out.print(bl.getOrganism());
                    System.out.println("descripton: ");
                    System.out.print(bl.getDescription());
                    System.out.println("perc iden: ");
                    System.out.print(bl.getPercIdentity());
                    System.out.println("accesion: ");
                    System.out.print(bl.getAccesion());
                }
                try {
                    int counter = 0;
                    for (BlastResultaat blastRes : blastResultaat.values()) {
                        String org = useDbPython("getOrgName", blastRes.getOrganism(), "");
                        tableData[counter] = new Object[]{"", blastRes.getId(), blastRes.geteValue(), org, blastRes.getDescription(), blastRes.getPercIdentity(), blastRes.getAccesion()};
                        counter++;
                    }
                } catch (NullPointerException e) {
                    tableData = new Object[0][0];
                }

                Object[][] finalTableData = tableData;
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel dm1 = (DefaultTableModel) blastTable.getModel();
                    for (Object[] rowData : finalTableData) {
                        dm1.addRow(rowData);
                    }
                    dm1.fireTableDataChanged();
                    selectAllBlastBt.setVisible(true);
                    blastResSelected = 0;
                    blastNrSelectedLb.setText("0 BLAST result(s) selected");
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        blast.start();
    }

    /**
     * Determines what blast to be used.
     * @param blastType the type of BLAST to be used (0 = Protein BLAST; 1 = Nucleotide BLAST)
     * @throws IOException
     * @throws InterruptedException
     */
    private void blastSearch(int blastType) throws IOException, InterruptedException {
        String blast = "";
        String database = "";
        if (blastType == 1) {
            database = (String) databaseProtBox.getSelectedItem();
            if (blastpRb.isSelected())
                blast = "blastp";
            else
                blast = "blastx";
        } else if (blastType == 0) {
            database = (String) databaseNtBox.getSelectedItem();
            if (blastnRb.isSelected())
                blast = "blastn";
            else
                blast = "tblastn";
        }

        blastpython(blast, database, blastType);
    }

    /**
     * BLASTS the selected ORFs
     * @param blast The blast to be used
     * @param db the database to be used
     * @param blastType the blasttype to be used
     * @throws InterruptedException
     * @throws IOException
     */
    private void blastpython(String blast, String db, int blastType) throws InterruptedException, IOException {
        blastResultaat = new HashMap<>();
        String toBlast;
        for (int i = 0; i < orfInfoTb.getRowCount(); i++) {
            toBlast = "";
            if (orfInfoTb.getValueAt(i, 0).equals("✔")) {
                toBlast += ">" + orfInfoTb.getValueAt(i, 1) + "\n";
                if (blastType == 0) {
                    toBlast += orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(i, 1))).getDnaSeq();
                } else {
                    toBlast += orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(i, 1))).getAminoSeq(orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(i, 1))).getDnaSeq());
                }
                int id = orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(i, 1))).getId();
                saveSeqToDatabase();
                saveToDatabase(orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(i, 1))), id);
                String[] pythonReturn = useBlastPython(blast, db, toBlast, id).split("\n");

                try {
                    for (String q : pythonReturn) {
                        String[] f = q.split("\t");
                        System.out.println(Arrays.toString(f));
                        blastResultaat.put(f[0], makeBlastResult(f, (Integer) orfInfoTb.getValueAt(i, 1)));
                    }
                } catch (ArrayIndexOutOfBoundsException ignore) {
                }
            }
        }
    }

    /**
     * sets the database window
     * @param selected the selected window
     */
    private void setDatabaseWindow(int selected) {
        if (selected == 0) {
            dbOrfSp.setVisible(true);
            dbBlastSp.setVisible(false);
        } else {
            dbBlastSp.setVisible(true);
            dbOrfSp.setVisible(false);
        }
    }

    /**
     * Used to choose a file.
     */
    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.showSaveDialog(null);
        path = chooser.getSelectedFile();
        try {
            fileTf.setText(path.getName());
        } catch (NullPointerException ignore) {
        }
    }

    /**
     * A warning pannel pop up
     * @param message the message to be displayed
     * @param title the title of the message box
     * @return the button clicked.
     */
    private int warningPanel(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
    }

    /**
     * saves orf to the databse
     * @param orf OrfResultaat class object
     * @param id orf id
     * @throws IOException
     * @throws InterruptedException
     */
    private void saveToDatabase(OrfResultaat orf, int id) throws IOException, InterruptedException {
        String latestId = useDbPython("latestDna", "", "");
        String info = orf.getDnaSeq() + "\t" + id + "\t" + orf.getStrand() + "\t" + orf.getFrame() + "\t" + orf.getLength() + "\t" + orf.getStart() + "\t" + orf.getStop() + "\t" + latestId;
        useDbPython("save", "ORFs", info);
    }

    /**
     * used to save a sequence to the database.
     * @throws IOException
     * @throws InterruptedException
     */
    private void saveSeqToDatabase() throws IOException, InterruptedException {
        String latestId = Integer.toString((Integer.parseInt(useDbPython("latestDna", "", "").strip()) + 1));
        useDbPython("save", "dna_data", seq + "\t" + latestId);
    }

    /**
     * saves selected orf/blast results to the database.
     * @throws IOException
     * @throws InterruptedException
     */
    private void saveSelectedToDatabase() throws IOException, InterruptedException {
        if (warningPanel("You are about to save the selected entry's to the database.\nThis will close the database window if it's open!", "Save warning") != JOptionPane.YES_OPTION)
            return;
        try {
            databaseFrame.dispose();
        } catch (NullPointerException ignore) {
        }
        databaseBt.setEnabled(true);

        String info;
        if (panelstate == 0) {
            saveSeqToDatabase();
            String latestId = useDbPython("latestDna", "", "");
            for (int i = 0; i < orfInfoTb.getRowCount(); i++) {
                if (!orfInfoTb.getValueAt(i, 0).equals("")) {
                    OrfResultaat orf = orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(i, 1)));
                    info = orf.getDnaSeq() + "\t" + orf.getId() + "\t" + orf.getStrand() + "\t" + orf.getFrame() + "\t" + orf.getLength() + "\t" + orf.getStart() + "\t" + orf.getStop() + "\t" + latestId;
                    useDbPython("save", "ORFs", info);
                }
            }
        } else if (panelstate == 1) {
            for (int i = 0; i < blastTable.getRowCount(); i++) {
                if (!blastTable.getValueAt(i, 0).equals("")) {
                    BlastResultaat blastRes = blastResultaat.get(Integer.toString((Integer) blastTable.getValueAt(i, 1)));
                    String orgId = useDbPython("checkOrg", blastRes.getOrganism(), "");
                    int blastid = Integer.parseInt(useDbPython("latestBlast", "", "")) + 1;
                    info = blastRes.getAccesion() + "\t" + blastRes.getDescription() + "\t" + blastRes.geteValue() + "\t" + blastRes.getPercIdentity() + "\t" + blastRes.getId() + "\t" + blastid + "\t" + orgId;

                    useDbPython("save", "blastresultsorf", info);
                }
            }
        }
    }

    /**
     * selects all blast results.
     */
    private void selectAllBlast() {
        if (blastTable.getRowCount() != 0) {
            String txt = "";
            if (blastTable.getValueAt(0, 0).equals("")) {
                txt = "✔";
                blastResSelected = blastTable.getRowCount();
            } else
                blastResSelected = 0;
            for (int i = 0; i < blastTable.getRowCount(); i++)
                blastTable.setValueAt(txt, i, 0);
            blastNrSelectedLb.setText(blastResSelected + " BLAST result(s) selected");
        }
    }

    /**
     * selects all orf results.
     */
    private void selectAllOrf() {
        if (orfInfoTb.getRowCount() != 0) {
            String txt = "";
            if (orfInfoTb.getValueAt(0, 0).equals("")) {
                txt = "✔";
                orfSelected = orfInfoTb.getRowCount();
            } else
                orfSelected = 0;
            for (int i = 0; i < orfInfoTb.getRowCount(); i++)
                orfInfoTb.setValueAt(txt, i, 0);
            nrSelectedLb.setText(orfSelected + " ORF(s) selected");
        }
    }

    /**
     * starts the determining of orfs.
     */
    private void startOrf() {
        if (blastResultsPnl.isVisible())
            if (warningPanel("Are you sure you want to determine ORF's?\nActive BLAST searches will be canceled and BLAST results will be lost", "BLAST warning!") == JOptionPane.NO_OPTION)
                return;

        orfInfoPnl.setVisible(true);
        orfExtInfPnl.setVisible(false);
        blastResultsPnl.setVisible(false);
        nrSelectedLb.setText("Determining ORFs");
        panelstate = 0;

        DefaultTableModel dm = (DefaultTableModel) orfInfoTb.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        Thread calculateOrfs = new Thread(() -> {
            try {
                blastProtBt.setEnabled(false);
                blastNtBt.setEnabled(false);
                determineOrfs();
                Object[][] tableData = getOrfTableData();
                SwingUtilities.invokeLater(() -> {
                    for (Object[] rowData : tableData) {
                        dm.addRow(rowData);
                    }
                    dm.fireTableDataChanged();
                    orfSelected = 0;
                    nrSelectedLb.setText("0 ORF(s) selected");
                    if (orfInfoTb.getRowCount() != 0) {
                        blastProtBt.setEnabled(true);
                        blastNtBt.setEnabled(true);
                    }
                });
            } catch (IOException | InterruptedException e) {
                nrSelectedLb.setText("Wrong file or sequence selected!");
            }
        });
        calculateOrfs.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBackBt) {
            if (panelstate == 0)
                orfInfoPnl.setVisible(true);
            else if (panelstate == 1)
                blastResultsPnl.setVisible(true);
            orfExtInfPnl.setVisible(false);
        } else if (e.getSource() == databaseBt)
            makeDatabaseFrame();
        else if (e.getSource() == databaseEntryBox)
            setDatabaseWindow(databaseEntryBox.getSelectedIndex());
        else if (e.getSource() == dbGoBackBt) {
            tabelPnl.setVisible(true);
            dbSeqPnl.setVisible(false);
        } else if (e.getSource() == blastNtBt)
            blastClicked(0);
        else if (e.getSource() == blastProtBt)
            blastClicked(1);
        else if (e.getSource() == selectAllOrfBt)
          selectAllOrf();
         else if (e.getSource() == selectAllBlastBt)
           selectAllBlast();
        else if (e.getSource() == browseBt)
            chooseFile();
        else if (e.getSource() == startOrfBt)
            startOrf();
        else if (e.getSource() == saveDatabaseBt) {
            try {
                saveSelectedToDatabase();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == orfInfoTb) {
            int row = orfInfoTb.getSelectedRow();
            int column = orfInfoTb.getSelectedColumn();
            switch (column) {
                case 0:
                    if (orfInfoTb.getValueAt(row, column).equals("")) {
                        orfInfoTb.setValueAt("✔", row, column);
                        orfSelected += 1;
                    } else {
                        orfInfoTb.setValueAt("", row, column);
                        orfSelected -= 1;
                    }
                    nrSelectedLb.setText(orfSelected + " ORF(s) selected");
                    break;
                case 7:
                    nuclSeqTxtArea.setText(orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(row, 1))).getDnaSeq());
                    aminoSeqTxtArea.setText(orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(row, 1))).getAminoSeq(orfResult.get(Integer.toString((Integer) orfInfoTb.getValueAt(row, 1))).getDnaSeq()));
                    orfExtInfPnl.setVisible(true);
                    orfNameLb.setText("ORF ID: " + orfInfoTb.getValueAt(row, 1));
                    orfInfoPnl.setVisible(false);
                    break;
            }
        } else if (e.getSource() == databaseOrfTb) {
            int row = databaseOrfTb.getSelectedRow();
            int column = databaseOrfTb.getSelectedColumn();
            if (column == 0) {
                if (databaseOrfTb.getValueAt(row, column).equals("")) {
                    databaseOrfTb.setValueAt("✔", row, column);
                } else {
                    databaseOrfTb.setValueAt("", row, column);
                }
            } else if (column == 6) {
                dbNuclSeqTxtArea.setText(dbOrfResultaat.get(Integer.toString((Integer) databaseOrfTb.getValueAt(row, 0))).getDnaSeq());
                dbAminoSeqTxtArea.setText(dbOrfResultaat.get(Integer.toString((Integer) databaseOrfTb.getValueAt(row, 0))).getAminoSeq(dbOrfResultaat.get(Integer.toString((Integer) databaseOrfTb.getValueAt(row, 0))).getDnaSeq()));
                dbSeqPnl.setVisible(true);
                dbOrfNameLb.setText("ORF ID: " + databaseOrfTb.getValueAt(row, 0));
                tabelPnl.setVisible(false);
            } else if (column == 7) {
                dbNuclSeqTxtArea.setText(dbOrfResultaat.get(Integer.toString((Integer) databaseOrfTb.getValueAt(row, 0))).getTotalDnaSeq());
                dbAminoSeqTxtArea.setText(dbOrfResultaat.get(Integer.toString((Integer) databaseOrfTb.getValueAt(row, 0))).getAminoSeq(dbOrfResultaat.get(Integer.toString((Integer) databaseOrfTb.getValueAt(row, 0))).getTotalDnaSeq()));
                dbSeqPnl.setVisible(true);
                dbOrfNameLb.setText("ORF ID: " + databaseOrfTb.getValueAt(row, 0));
                tabelPnl.setVisible(false);
            }
        } else if (e.getSource() == blastTable) {
            int row = blastTable.getSelectedRow();
            int column = blastTable.getSelectedColumn();
            if (column == 0) {
                if (blastTable.getValueAt(row, column).equals("")) {
                    blastTable.setValueAt("✔", row, column);
                    blastResSelected += 1;
                } else {
                    blastTable.setValueAt("", row, column);
                    blastResSelected -= 1;
                }
                blastNrSelectedLb.setText(blastResSelected + " BLAST result(s) selected");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getSource() == databaseFrame)
            databaseBt.setEnabled(true);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
