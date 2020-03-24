package ProjectBlok7;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;


class OrfFinder extends JFrame implements ActionListener, MouseListener, WindowListener {
    private static JButton browseBt, databaseBt, startOrfBt, saveDatabaseBt, blastNtBt, blastProtBt, goBackBt, dbGoBackBt, selectAllBlastBt, selectAllOrfBt, searchBt, dbSelectAllBt;
    private static JTextField fileTf, orfLengthTf, searchTf;
    private static JLabel seqLb, estTimeLb, elapTimeLb, paramOrfLb, blastTypeNtLb, blastTypeProtLb, databaseProtLb, databaseNtLb, orfLengthLB, readFrmLb, strandLb, nrSelectedLb, dbOrfNrSelectedLb, dbBlastNrSelectedLb, blastNrSelectedLb;
    private static JPanel blastPnl, orfInfoPnl, orfExtInfPnl, ntBlastPnl, protBlastPnl, dbSeqPnl, tabelPnl, blastResultsPnl;
    private static JComboBox strandBox, readFrmBox, databaseNtBox, databaseProtBox, databaseEntryBox;
    private static JRadioButton blastpRb, blastxRb, tblastnRb, blastnRb;
    private static JTable orfInfoTb, databaseOrfTb, databaseBlastTb, blastTable;
    private static JFrame databaseFrame;
    private static JScrollPane dbBlastSp, dbOrfSp;
    private static ButtonGroup blastTypeProt, blastTypeNt;
    private static int orfSelected = 0, dbOrfSelected = 0, dbBlastResSelected = 0, blastResSelected = 0, panelstate;
    private static File path;
//    private static HashMap<OrfResultaat> orfResultaat, dbOrfResultaat;
//    private static HashMap<BlastResultaat> blastResultaat, dbBlastResultaat;


    public static void main(String[] args) {
        OrfFinder frame = new OrfFinder();
        frame.setTitle("ORF Finder");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.createGui();
        frame.pack();
        frame.setVisible(true);
    }

    private void createGui() {
        Container window = getContentPane();
        window.setLayout(new GridBagLayout());

        Border blackline = BorderFactory.createLineBorder(Color.BLACK);

        seqLb = new JLabel("Sequence:");
        window.add(seqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 0,
                0, 0, 0, 0, new Insets(25, 25, 10, 20), GridBagConstraints.LINE_END));

        fileTf = new JTextField("Paste a DNA sequence here or select a FASTA file.");
        window.add(fileTf, getConstraints(GridBagConstraints.HORIZONTAL, 3, 1, 0, 1,
                5, 0, 0, 0, new Insets(25, 0, 10, 0), GridBagConstraints.LINE_START));

        browseBt = new JButton("Browse");
        browseBt.addActionListener(this);
        window.add(browseBt, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 4,
                3, 0, 0, 0, new Insets(25, 10, 10, 0), GridBagConstraints.LINE_START));

        paramOrfLb = new JLabel("Parameters:");
        window.add(paramOrfLb, getConstraints(GridBagConstraints.NONE, 1, 1, 1, 0,
                5, 0, 0, 0, new Insets(0, 25, 0, 0), GridBagConstraints.LINE_START));

        orfLengthLB = new JLabel("• Minimal ORF length");
        window.add(orfLengthLB, getConstraints(GridBagConstraints.NONE, 1, 1, 2, 0,
                0, 0, 0, 0, new Insets(0, 25, 0, 0), GridBagConstraints.LINE_START));

        TitledBorder border = BorderFactory.createTitledBorder(blackline, "Miminal = 30");
        border.setTitleJustification(TitledBorder.CENTER);

        orfLengthTf = new JTextField("75");
        orfLengthTf.setBorder(border);
        window.add(orfLengthTf, getConstraints(GridBagConstraints.HORIZONTAL, 1, 1, 2, 1,
                0, 80, 0, 0, new Insets(0, 20, 0, 0), GridBagConstraints.LINE_START));

        readFrmLb = new JLabel("• Reading frame:");
        window.add(readFrmLb, getConstraints(GridBagConstraints.NONE, 1, 1, 3, 0,
                0, 0, 0, 0, new Insets(3, 25, 3, 0), GridBagConstraints.LINE_START));

        readFrmBox = new JComboBox<>(new String[]{"1, 2, 3", "1", "2", "3"});
        window.add(readFrmBox, getConstraints(GridBagConstraints.HORIZONTAL, 1, 1, 3, 1,
                0, 0, 0, 0, new Insets(3, 20, 3, 0), GridBagConstraints.LINE_START));

        strandLb = new JLabel("• Strand(s) to read:");
        window.add(strandLb, getConstraints(GridBagConstraints.NONE, 1, 1, 4, 0,
                0, 0, 0, 0, new Insets(3, 25, 0, 0), GridBagConstraints.LINE_START));

        strandBox = new JComboBox<>(new String[]{"Both", "Forward", "Reverse"});
        window.add(strandBox, getConstraints(GridBagConstraints.HORIZONTAL, 1, 1, 4, 1,
                0, 0, 0, 0, new Insets(3, 20, 0, 0), GridBagConstraints.LINE_START));

        startOrfBt = new JButton("Determine ORF's");
        startOrfBt.addActionListener(this);
        window.add(startOrfBt, getConstraints(GridBagConstraints.NONE, 2, 1, 4, 2,
                0, 0, 0, 0, new Insets(3, 80, 0, 0), GridBagConstraints.LINE_START));

        estTimeLb = new JLabel("Estimated time:\t");
        window.add(estTimeLb, getConstraints(GridBagConstraints.NONE, 2, 1, 2, 2,
                0, 0, 0, 0, new Insets(10, 80, 2, 0), GridBagConstraints.LINE_START));

        databaseBt = new JButton("Open database");
        databaseBt.addActionListener(this);
        window.add(databaseBt, getConstraints(GridBagConstraints.NONE, 1, 1, 4, 4,
                0, 0, 0, 0, new Insets(3, 10, 7, 25), GridBagConstraints.LINE_END));

        elapTimeLb = new JLabel("Elapsed time:\t");
        window.add(elapTimeLb, getConstraints(GridBagConstraints.NONE, 2, 1, 3, 2,
                0, 0, 0, 0, new Insets(2, 80, 7, 0), GridBagConstraints.LINE_START));

        blastPnl = new JPanel();
        blastPnl.setLayout(new GridLayout(1, 0));
        blastPnl.setBackground(new Color(200, 200, 200));
        blastPnl.setMinimumSize(new Dimension(275, 250));
        blastPnl.setPreferredSize(new Dimension(275, 250));
        blastPnl.setBorder(blackline);
        window.add(blastPnl, getConstraints(GridBagConstraints.NONE, 2, 1, 6, 0,
                0, 0, 0, 0, new Insets(7, 25, 0, 0), GridBagConstraints.CENTER));

        protBlastPnl = new JPanel();
        protBlastPnl.setLayout(new GridBagLayout());
        protBlastPnl.setBackground(new Color(200, 200, 200));

        blastTypeProt = new ButtonGroup();

        blastTypeProtLb = new JLabel("Blast Type:");
        protBlastPnl.add(blastTypeProtLb, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 0,
                0, 0, 1, 0, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        blastpRb = new JRadioButton("BLASTp");
        blastTypeProt.add(blastpRb);
        protBlastPnl.add(blastpRb, getConstraints(GridBagConstraints.NONE, 1, 1, 1, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        blastxRb = new JRadioButton("BLASTx");
        blastTypeProt.add(blastxRb);
        protBlastPnl.add(blastxRb, getConstraints(GridBagConstraints.NONE, 1, 1, 2, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        databaseProtLb = new JLabel("Database:");
        protBlastPnl.add(databaseProtLb, getConstraints(GridBagConstraints.NONE, 2, 1, 3, 0,
                0, 0, 1, 0, new Insets(0, 0, 5, 0), GridBagConstraints.PAGE_END));

        blastpRb.setSelected(true);

        databaseProtBox = new JComboBox<>(new String[]{"nr", "swissprot", "refseq"});
        protBlastPnl.add(databaseProtBox, getConstraints(GridBagConstraints.NONE, 2, 1, 4, 0,
                0, 0, 0, 0, new Insets(5, 0, 5, 0), GridBagConstraints.CENTER));

        blastProtBt = new JButton("BLAST selected ORF(s)");
        blastProtBt.addActionListener(this);
        blastProtBt.setEnabled(false);
        protBlastPnl.add(blastProtBt, getConstraints(GridBagConstraints.NONE, 2, 1, 5, 0,
                0, 0, 1, 1, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        ntBlastPnl = new JPanel();
        ntBlastPnl.setLayout(new GridBagLayout());
        ntBlastPnl.setBackground(new Color(200, 200, 200));

        blastTypeNt = new ButtonGroup();

        blastTypeNtLb = new JLabel("Blast Type:");
        ntBlastPnl.add(blastTypeNtLb, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 0,
                0, 0, 1, 0, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        blastnRb = new JRadioButton("BLASTn");
        blastTypeNt.add(blastnRb);
        ntBlastPnl.add(blastnRb, getConstraints(GridBagConstraints.NONE, 1, 1, 1, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        tblastnRb = new JRadioButton("tBLASTn");
        blastTypeNt.add(tblastnRb);
        ntBlastPnl.add(tblastnRb, getConstraints(GridBagConstraints.NONE, 1, 1, 2, 0,
                0, 0, 0, 0, new Insets(5, 25, 5, 0), GridBagConstraints.LINE_START));

        blastnRb.setSelected(true);

        databaseNtLb = new JLabel("Database:");
        ntBlastPnl.add(databaseNtLb, getConstraints(GridBagConstraints.NONE, 2, 1, 3, 0,
                0, 0, 1, 0, new Insets(0, 0, 5, 0), GridBagConstraints.PAGE_END));

        databaseNtBox = new JComboBox<>(new String[]{"nt", "refseq_rna", "pdb"});
        ntBlastPnl.add(databaseNtBox, getConstraints(GridBagConstraints.NONE, 2, 1, 4, 0,
                0, 0, 0, 0, new Insets(5, 0, 5, 0), GridBagConstraints.CENTER));

        blastNtBt = new JButton("BLAST selected ORF(s)");
        blastNtBt.addActionListener(this);
        blastNtBt.setEnabled(false);
        ntBlastPnl.add(blastNtBt, getConstraints(GridBagConstraints.NONE, 2, 1, 5, 0,
                0, 0, 1, 1, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Protein BLAST", null, protBlastPnl, null);
        tabbedPane.addTab("Nucleotide BLAST", null, ntBlastPnl, null);
        tabbedPane.setSelectedIndex(0);
        blastPnl.add(tabbedPane);

        blastResultsPnl = new JPanel();
        blastResultsPnl.setBorder(blackline);
        blastResultsPnl.setPreferredSize(new Dimension(670, 300));
        window.add(blastResultsPnl, getConstraints(GridBagConstraints.BOTH, 3, 1, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));
        blastResultsPnl.setVisible(false);

        orfInfoPnl = new JPanel();
        orfInfoPnl.setLayout(new GridBagLayout());
        orfInfoPnl.setBackground(new Color(200, 200, 200));
        orfInfoPnl.setBorder(blackline);
        orfInfoPnl.setPreferredSize(new Dimension(670, 300));
        window.add(orfInfoPnl, getConstraints(GridBagConstraints.BOTH, 3, 1, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));

        selectAllOrfBt = new JButton("Select all");
        selectAllOrfBt.addActionListener(this);
        selectAllOrfBt.setEnabled(false);
        orfInfoPnl.add(selectAllOrfBt, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 5, 0, 0), GridBagConstraints.LINE_START));

        JLabel orfInfoLb = new JLabel("Info about found ORF(s)");
        orfInfoPnl.add(orfInfoLb, getConstraints(GridBagConstraints.NONE, 3, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

        nrSelectedLb = new JLabel("0 ORF(s) selected");
        orfInfoPnl.add(nrSelectedLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 2,
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
        orfInfoPnl.add(scrollPane, getConstraints(GridBagConstraints.BOTH, 3, 1, 1, 0,
                0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.FIRST_LINE_START));

        orfExtInfPnl = new JPanel();
        orfExtInfPnl.setLayout(new GridBagLayout());
        orfExtInfPnl.setBackground(new Color(200, 200, 200));
        orfExtInfPnl.setBorder(blackline);
        orfExtInfPnl.setPreferredSize(new Dimension(670, 300));
        window.add(orfExtInfPnl, getConstraints(GridBagConstraints.BOTH, 3, 1, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));
        orfExtInfPnl.setVisible(false);

        JLabel orfNameLb = new JLabel("ORF nr test");
        orfExtInfPnl.add(orfNameLb, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

        goBackBt = new JButton("Back");
        goBackBt.addActionListener(this);
        orfExtInfPnl.add(goBackBt, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 1,
                0, 0, 0, 0, new Insets(5, 0, 0, 5), GridBagConstraints.LINE_END));

        JLabel aminoSeqLb = new JLabel("Amino acid sequence:");
        orfExtInfPnl.add(aminoSeqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 1, 0,
                0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

        JTextArea aminoSeqTxtArea = new JTextArea("AMINOZUREN EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCG");
        aminoSeqTxtArea.setLineWrap(true);
        JScrollPane scrollPane2 = new JScrollPane(aminoSeqTxtArea);
        orfExtInfPnl.add(scrollPane2, getConstraints(GridBagConstraints.BOTH, 2, 1, 2, 0,
                0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

        JLabel nuclSeqLb = new JLabel("Nucleotide sequence:");
        orfExtInfPnl.add(nuclSeqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 3, 0,
                0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

        JTextArea nuclSeqTxtArea = new JTextArea("NUCLEOTIDEn ACGTCGAAAATTTTTTTTTTTTTTTGCGCCGGGGGGGGCAGGGGGTTTTTTTTTTTTTTTTTTTTTTTTTTGAGAGTGAACGTCGGCGGCCGCCTCCGCGCGCGTCGCGCGGCCGCGGCCGATACGTCAGTCAGTCAGTCAGTCAGTCAG");
        nuclSeqTxtArea.setLineWrap(true);
        JScrollPane scrollPane3 = new JScrollPane(nuclSeqTxtArea);
        orfExtInfPnl.add(scrollPane3, getConstraints(GridBagConstraints.BOTH, 2, 1, 4, 0,
                0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

        blastResultsPnl.setLayout(new GridBagLayout());
        blastResultsPnl.setBackground(new Color(200, 200, 200));
        blastResultsPnl.setVisible(false);

        searchBt = new JButton("Search BLAST description");
        blastResultsPnl.add(searchBt, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 1,
                0, 0, 0, 0, new Insets(15, 10, 0, 0), GridBagConstraints.LINE_START));

        blastTable = new JTable(new DefaultTableModel(null, new String[]{"Select", "ORF ID", "E-value", "Organism", "Description", "Sequence"}));
//JTable blastTable = new JTable(new DefaultTableModel(null, new String[]{"Select", "ORF ID", "E-value", "Organism", "Description", "Sequence"}));
        blastTable.setAutoCreateRowSorter(true);
        blastTable.setDefaultEditor(Object.class, null);
        blastTable.getTableHeader().setReorderingAllowed(false);
        blastTable.addMouseListener(this);
        blastTable.getColumn("Select").setMaxWidth(50);
        blastTable.getColumn("ORF ID").setMaxWidth(60);
        blastTable.getColumn("E-value").setMaxWidth(60);

        JScrollPane scrollPane6 = new JScrollPane(blastTable);
        blastResultsPnl.add(scrollPane6, getConstraints(GridBagConstraints.BOTH, 4, 1, 1, 0,
                0, 0, 1, 1, new Insets(10, 15, 10, 15), GridBagConstraints.LINE_START));

        selectAllBlastBt = new JButton("Select all");
        selectAllBlastBt.addActionListener(this);
        blastResultsPnl.add(selectAllBlastBt, getConstraints(GridBagConstraints.NONE, 1, 1, 2, 0,
                0, 0, 0, 0, new Insets(0, 15, 15, 0), GridBagConstraints.LINE_START));

        blastNrSelectedLb = new JLabel();
        blastResultsPnl.add(blastNrSelectedLb, getConstraints(GridBagConstraints.NONE, 4, 1, 2, 0,
                0, 0, 0, 0, new Insets(0, 0, 15, 0), GridBagConstraints.CENTER));

        searchTf = new JTextField(20);
        blastResultsPnl.add(searchTf, getConstraints(GridBagConstraints.HORIZONTAL, 1, 1, 0, 0,
                0, 0, 0, 0, new Insets(15, 15, 0, 0), GridBagConstraints.LINE_END));

        saveDatabaseBt = new JButton("Save to Database");
        saveDatabaseBt.addActionListener(this);
        window.add(saveDatabaseBt, getConstraints(GridBagConstraints.NONE, 1, 1, 7, 4,
                10, 0, 0, 0, new Insets(10, 0, 25, 25), GridBagConstraints.LINE_END));

        panelstate = 0;
    }

    private void makeDatabaseFrame() {
        Thread makeDatabase = new Thread(() -> {
            System.out.println("ophalen database");
//ophalen database
            SwingUtilities.invokeLater(() -> {
                databaseBt.setEnabled(false);
                databaseFrame = new JFrame("Database");
                databaseFrame.setPreferredSize(new Dimension(825, 575));
                databaseFrame.setMinimumSize(new Dimension(650, 575));
                databaseFrame.addWindowListener(this);
                Container window = new Container();
                window.setLayout(new GridBagLayout());

                JButton searchBt = new JButton("Search description/ORF ID");
                window.add(searchBt, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 1,
                        0, 0, 0, 0, new Insets(15, 10, 0, 0), GridBagConstraints.LINE_START));

                JTextField searchTf = new JTextField(20);
                window.add(searchTf, getConstraints(GridBagConstraints.HORIZONTAL, 1, 1, 0, 0,
                        0, 0, 0, 0, new Insets(15, 15, 0, 0), GridBagConstraints.LINE_END));

                databaseEntryBox = new JComboBox<>(new String[]{"Found ORFs", "BLAST results"});
                databaseEntryBox.addActionListener(this);
                window.add(databaseEntryBox, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 2,
                        0, 0, 0, 0, new Insets(15, 0, 0, 15), GridBagConstraints.CENTER));

                tabelPnl = new JPanel();
                tabelPnl.setLayout(new GridBagLayout());
                tabelPnl.setBackground(Color.gray);
                window.add(tabelPnl, getConstraints(GridBagConstraints.BOTH, 4, 1, 1, 0,
                        0, 0, 1, 1, new Insets(10, 15, 10, 15), GridBagConstraints.LINE_START));

                databaseOrfTb = new JTable(new DefaultTableModel(new Object[][]{{"", "orf1", "+", "2", "1020", "12344", "1432432", "click orf sequence", "click total sequence"}, {"", "orf2", "-", "1", "3202", "34298", "473289", "click orf sequence", "click total sequence"}}, new String[]{"Select", "ORF ID", "Strand", "Frame", "Length", "Start position", "Stop position", "ORF sequence", "Total sequence"}));
                databaseOrfTb.setAutoCreateRowSorter(true);
                databaseOrfTb.setDefaultEditor(Object.class, null);
                databaseOrfTb.getTableHeader().setReorderingAllowed(false);
                databaseOrfTb.getColumn("Select").setMaxWidth(50);
                databaseOrfTb.getColumn("ORF ID").setMaxWidth(60);
                databaseOrfTb.getColumn("Strand").setMaxWidth(60);
                databaseOrfTb.getColumn("Frame").setMaxWidth(60);
                databaseOrfTb.getColumn("Length").setMaxWidth(70);
                databaseOrfTb.addMouseListener(this);

                dbOrfSp = new JScrollPane(databaseOrfTb);
                tabelPnl.add(dbOrfSp, getConstraints(GridBagConstraints.BOTH, 1, 1, 1, 0,
                        0, 0, 1, 1, new Insets(5, 5, 5, 5), GridBagConstraints.FIRST_LINE_START));

                databaseBlastTb = new JTable(new DefaultTableModel(new Object[][]{{"", "orf1", "4*e", "kat", "gaaf eiwit dit hoor", "click for sequence"}, {"", "orf2", "7.54*e", "coole hond", "Deze doet ook dingen", "click for sequence"}}, new String[]{"Select", "ORF ID", "E-value", "Organism", "Description", "Sequence"}));
                databaseBlastTb.setAutoCreateRowSorter(true);
                databaseBlastTb.setDefaultEditor(Object.class, null);
                databaseBlastTb.addMouseListener(this);
                databaseBlastTb.getTableHeader().setReorderingAllowed(false);
                databaseBlastTb.getColumn("Select").setMaxWidth(50);
                databaseBlastTb.getColumn("ORF ID").setMaxWidth(60);
                databaseBlastTb.getColumn("E-value").setMaxWidth(60);

                dbSeqPnl = new JPanel();
                dbSeqPnl.setLayout(new GridBagLayout());
                dbSeqPnl.setBackground(Color.gray);
                dbSeqPnl.setPreferredSize(new Dimension(670, 300));
                dbSeqPnl.setVisible(false);
                window.add(dbSeqPnl, getConstraints(GridBagConstraints.BOTH, 4, 1, 1, 0,
                        0, 0, 1, 1, new Insets(10, 15, 10, 15), GridBagConstraints.LINE_START));

                JLabel orfNameLb = new JLabel("ORF nr test");
                dbSeqPnl.add(orfNameLb, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 0,
                        0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

                dbGoBackBt = new JButton("Back");
                dbGoBackBt.addActionListener(this);
                dbSeqPnl.add(dbGoBackBt, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 1,
                        0, 0, 0, 0, new Insets(5, 0, 0, 5), GridBagConstraints.LINE_END));

                JLabel aminoSeqLb = new JLabel("Amino acid sequence:");
                dbSeqPnl.add(aminoSeqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 1, 0,
                        0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

                JTextArea aminoSeqTxtArea = new JTextArea("AMINOZUREN EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEFDASFDASFDSAGTGRQGRQGGGCCWGCWGWCGCWGWCGWGWGCWCGCGGWCGWCWCGWGGCGCWCG");
                aminoSeqTxtArea.setLineWrap(true);
                JScrollPane scrollPane2 = new JScrollPane(aminoSeqTxtArea);
                dbSeqPnl.add(scrollPane2, getConstraints(GridBagConstraints.BOTH, 2, 1, 2, 0,
                        0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));

                JLabel nuclSeqLb = new JLabel("Nucleotide sequence:");
                dbSeqPnl.add(nuclSeqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 3, 0,
                        0, 0, 0, 0, new Insets(10, 5, 2, 0), GridBagConstraints.LINE_START));

                JTextArea nuclSeqTxtArea = new JTextArea("NUCLEOTIDEn ACGTCGAAAATTTTTTTTTTTTTTTGCGCCGGGGGGGGCAGGGGGTTTTTTTTTTTTTTTTTTTTTTTTTTGAGAGTGAACGTCGGCGGCCGCCTCCGCGCGCGTCGCGCGGCCGCGGCCGATACGTCAGTCAGTCAGTCAGTCAGTCAG");
                nuclSeqTxtArea.setLineWrap(true);
                JScrollPane scrollPane3 = new JScrollPane(nuclSeqTxtArea);
                dbSeqPnl.add(scrollPane3, getConstraints(GridBagConstraints.BOTH, 2, 1, 4, 0,
                        0, 0, 1, 1, new Insets(5, 0, 0, 0), GridBagConstraints.LINE_START));


                dbBlastSp = new JScrollPane(databaseBlastTb);
                tabelPnl.add(dbBlastSp, getConstraints(GridBagConstraints.BOTH, 1, 1, 1, 0,
                        0, 0, 1, 1, new Insets(5, 5, 5, 5), GridBagConstraints.FIRST_LINE_START));

                dbOrfNrSelectedLb = new JLabel("0 ORF(s) selected");
                window.add(dbOrfNrSelectedLb, getConstraints(GridBagConstraints.NONE, 4, 1, 2, 0,
                        0, 0, 0, 0, new Insets(0, 0, 15, 0), GridBagConstraints.CENTER));

                dbBlastNrSelectedLb = new JLabel("0 BLAST result(s) selected");
                dbBlastNrSelectedLb.setVisible(false);
                window.add(dbBlastNrSelectedLb, getConstraints(GridBagConstraints.NONE, 4, 1, 2, 0,
                        0, 0, 0, 0, new Insets(0, 0, 15, 0), GridBagConstraints.CENTER));

                dbSelectAllBt = new JButton("Select all");
                dbSelectAllBt.addActionListener(this);
                window.add(dbSelectAllBt, getConstraints(GridBagConstraints.NONE, 1, 1, 2, 0,
                        0, 0, 0, 0, new Insets(0, 15, 15, 0), GridBagConstraints.LINE_START));

                JButton deleteBt = new JButton("Delete selected");
                window.add(deleteBt, getConstraints(GridBagConstraints.NONE, 1, 1, 2, 3,
                        0, 0, 0, 0, new Insets(0, 0, 15, 15), GridBagConstraints.LINE_END));

                databaseFrame.setContentPane(window);
                databaseFrame.pack();
                databaseFrame.setVisible(true);
            });
        });
        makeDatabase.start();
    }

    private GridBagConstraints getConstraints(int fill, int gridwidth, int gridheight, int gridy, int gridx, int ipady,
                                              int ipadx, int weighty, int weightx, Insets insets, int anchor) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
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

    private void blastClicked(int blastType) {
        blastNrSelectedLb.setText("Now performing a BLAST search");
        blastProtBt.setEnabled(false);
        blastNtBt.setEnabled(false);
        panelstate = 1;

        searchBt.setVisible(false);
        searchTf.setVisible(false);
        selectAllBlastBt.setVisible(false);
        blastResultsPnl.setVisible(true);
        orfInfoPnl.setVisible(false);
        orfExtInfPnl.setVisible(false);
        DefaultTableModel dm = (DefaultTableModel) blastTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();

        Thread blast = new Thread(() -> {
            System.out.println("hij doet iets lit");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Object[][] blastResults = blastSearch(blastType);
            Object[][] blastResults = {{"", "orf1", "4*e", "kat", "gaaf eiwit dit hoor", "click for sequence"}, {"", "orf2", "7.54*e", "coole hond", "Deze doet ook dingen", "click for sequence"}, {"", "orf1", "4*e", "kat", "gaaf eiwit dit hoor", "click for sequence"}, {"", "orf2", "7.54*e", "coole hond", "Deze doet ook dingen", "click for sequence"}};
//                //update hashmap
            SwingUtilities.invokeLater(() -> {
                DefaultTableModel dm1 = (DefaultTableModel) blastTable.getModel();
                for (Object[] rowData : blastResults) {
                    dm1.addRow(rowData);
                }
                dm1.fireTableDataChanged();
                searchBt.setVisible(true);
                searchTf.setVisible(true);
                selectAllBlastBt.setVisible(true);
                blastResSelected = 0;
                blastNrSelectedLb.setText("0 BLAST result(s) selected");
            });
        });
        blast.start();
    }

    private Object[][] blastSearch(int blastType) {
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
//String pythonScript (blast, database, orfInfo??)
        // getBlastInfo
        return new Object[][]{{}};
    }

    private void setDatabaseWindow(int selected) {
        if (selected == 0) {
            dbOrfNrSelectedLb.setVisible(true);
            dbBlastNrSelectedLb.setVisible(false);
            dbOrfSp.setVisible(true);
            dbBlastSp.setVisible(false);
        } else {
            dbBlastNrSelectedLb.setVisible(true);
            dbOrfNrSelectedLb.setVisible(false);
            dbBlastSp.setVisible(true);
            dbOrfSp.setVisible(false);
        }
    }

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

    private Object[][] determineOrfs() {
        if (path.getName().equals(fileTf.getText())) {
            //use path
        } else {
            //use fileTf.getText()
        }
        return new Object[1][1];
    }

    private int warningPanel(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
    }

    private void saveToDatabase() {
        if (warningPanel("You are about to save the selected entry's to the database.\nThis will close the database window if it's open!", "Save warning") == JOptionPane.NO_OPTION)
            return;
        try {
            databaseFrame.dispose();
        } catch (NullPointerException ignore) {
        }
        databaseBt.setEnabled(true);
    }

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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Object[][] tableData = determineOrfs();
            //update hashmap
            Object[][] tableData = {{"", 22385, "-", 3, 1988550, 20, 287000, "Click for sequence"}, {"", 1, "+", 1, 254230, 230, 434280, "Click for extra info"}, {"", 24, "+", 2, 66666366, 33333, 999999, "Click for extra info"}
                    , {"", 287523, "-", 3, 432, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 4804, "Click for extra info"}, {"", 24, "+", 2, 6666666, 3334333, 999999, "Click for extra info"}
                    , {"", 223, "-", 3, 197680, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 254765320, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 62466666, 33333, 999999, "Click for extra info"}
                    , {"", 223, "-", 3, 1980, 2580, 200870, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 664266666, 33333, 999432999, "Click for extra info"}
                    , {"", 223, "-", 3, 198340, 20, 204200, "Click for extra info"}, {"", 1, "+", 1, 250432, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33333, 999999, "Click for extra info"}
                    , {"", 223, "-", 3, 198580, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33423333, 999999, "Click for extra info"}
                    , {"", 223, "-", 3, 1234980, 20, 2043200, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33333, 999999, "Click for extra info"}
                    , {"", 223, "-", 3, 1975880, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33333, 999999, "Click for extra info"}};

            SwingUtilities.invokeLater(() -> {
                for (Object[] rowData : tableData) {
                    dm.addRow(rowData);
                }
                dm.fireTableDataChanged();
                orfSelected = 0;
                nrSelectedLb.setText("0 ORF(s) selected");
                selectAllOrfBt.setEnabled(true);
                blastProtBt.setEnabled(true);
                blastNtBt.setEnabled(true);
            });
        });
        calculateOrfs.start();
    }

//    private void tabelAction(JTable table, int selected, JLabel label, JPanel panelOn, JPanel panelOff, int[] back) {
//        int row = table.getSelectedRow();
//        int column = table.getSelectedColumn();
//        if (column == 0) {
//            if (table.getValueAt(row, column).equals("")) {
//                table.setValueAt("✔", row, column);
//                selected += 1;
//            } else {
//                table.setValueAt("", row, column);
//                selected -= 1;
//            }
//            label.setText(selected + " ORF(s) selected");
//        } else if (Arrays.asList(back).contains(row)) {
//            panelOn.setVisible(true);
//            panelOff.setVisible(false);
//        }
//    }

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
        else if (e.getSource() == selectAllOrfBt) {
            String txt = "";
            if (orfInfoTb.getValueAt(0, 0).equals("")) {
                txt = "✔";
                orfSelected = orfInfoTb.getRowCount();
            } else
                orfSelected = 0;
            for (int i = 0; i < orfInfoTb.getRowCount(); i++)
                orfInfoTb.setValueAt(txt, i, 0);
            nrSelectedLb.setText(orfSelected + " ORF(s) selected");
        } else if (e.getSource() == selectAllBlastBt) {
            String txt = "";
            if (blastTable.getValueAt(0, 0).equals("")) {
                txt = "✔";
                blastResSelected = blastTable.getRowCount();
            } else
                blastResSelected = 0;
            for (int i = 0; i < blastTable.getRowCount(); i++)
                blastTable.setValueAt(txt, i, 0);
            blastNrSelectedLb.setText(blastResSelected + " BLAST result(s) selected");
        } else if (e.getSource() == dbSelectAllBt) {
            int dbEntry = databaseEntryBox.getSelectedIndex();
            String txt = "";
            if (dbEntry == 0) {
                if (databaseOrfTb.getValueAt(0, 0).equals("")) {
                    txt = "✔";
                    dbOrfSelected = databaseOrfTb.getRowCount();
                } else
                    dbOrfSelected = 0;
                for (int i = 0; i < databaseOrfTb.getRowCount(); i++)
                    databaseOrfTb.setValueAt(txt, i, 0);
                dbOrfNrSelectedLb.setText(dbOrfSelected + " ORF(s) selected");
            } else {
                if (databaseBlastTb.getValueAt(0, 0).equals("")) {
                    txt = "✔";
                    dbBlastResSelected = databaseBlastTb.getRowCount();
                } else
                    dbBlastResSelected = 0;
                for (int i = 0; i < databaseBlastTb.getRowCount(); i++)
                    databaseBlastTb.setValueAt(txt, i, 0);
                dbBlastNrSelectedLb.setText(dbBlastResSelected + " BLAST result(s) selected");
            }
        } else if (e.getSource() == browseBt) {
            chooseFile();
        } else if (e.getSource() == startOrfBt)
            startOrf();
        else if (e.getSource() == saveDatabaseBt) {
            saveToDatabase();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        if (e.getSource() == orfInfoTb) {
//            tabelAction(orfInfoTb, orfSelected, nrSelectedLb, orfExtInfPnl, orfInfoPnl, new int[]{7, -1});
//        } else if (e.getSource() == databaseOrfTb) {
//            tabelAction(databaseOrfTb, dbOrfSelected, orfNrSelectedLb, dbSeqPnl, tabelPnl, new int[]{7, 8});
//        } else {
//            tabelAction(databaseBlastTb, dbBlastResSelected, blastNrSelectedLb, dbSeqPnl, tabelPnl, new int[]{5, -1});
//        }
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
                    orfExtInfPnl.setVisible(true);
                    orfInfoPnl.setVisible(false);
                    break;
            }
        } else if (e.getSource() == databaseOrfTb) {
            int row = databaseOrfTb.getSelectedRow();
            int column = databaseOrfTb.getSelectedColumn();
            if (column == 0) {
                if (databaseOrfTb.getValueAt(row, column).equals("")) {
                    databaseOrfTb.setValueAt("✔", row, column);
                    dbOrfSelected += 1;
                } else {
                    databaseOrfTb.setValueAt("", row, column);
                    dbOrfSelected -= 1;
                }
                dbOrfNrSelectedLb.setText(dbOrfSelected + " ORF(s) selected");
            } else if (column == 7 || column == 8) {
                dbSeqPnl.setVisible(true);
                tabelPnl.setVisible(false);
            }
        } else if (e.getSource() == databaseBlastTb) {
            int row = databaseBlastTb.getSelectedRow();
            int column = databaseBlastTb.getSelectedColumn();
            switch (column) {
                case 0:
                    if (databaseBlastTb.getValueAt(row, column).equals("")) {
                        databaseBlastTb.setValueAt("✔", row, column);
                        dbBlastResSelected += 1;
                    } else {
                        databaseBlastTb.setValueAt("", row, column);
                        dbBlastResSelected -= 1;
                    }
                    dbBlastNrSelectedLb.setText(dbBlastResSelected + " BLAST result(s) selected");
                    break;
                case 5:
                    dbSeqPnl.setVisible(true);
                    tabelPnl.setVisible(false);
            }
        } else if (e.getSource() == blastTable) {
            int row = blastTable.getSelectedRow();
            int column = blastTable.getSelectedColumn();
            switch (column) {
                case 0:
                    if (blastTable.getValueAt(row, column).equals("")) {
                        blastTable.setValueAt("✔", row, column);
                        blastResSelected += 1;
                    } else {
                        blastTable.setValueAt("", row, column);
                        blastResSelected -= 1;
                    }
                    blastNrSelectedLb.setText(blastResSelected + " BLAST result(s) selected");
                    break;
                case 5:
                    orfExtInfPnl.setVisible(true);
                    blastResultsPnl.setVisible(false);
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
