package ProjectBlok7;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class OrfFinder extends JFrame implements ActionListener, MouseListener {
    private static JButton browseBt, databaseBt, startOrfBt, saveDatabaseBt, blastNtBt, blastProtBt;
    private static JTextField fileTf, orfLengthTf;
    private static JLabel seqLb, estTimeLb, elapTimeLb, paramOrfLb, blastTypeNtLb, blastTypeProtLb, databaseProtLb, databaseNtLb, orfLengthLB, readFrmLb, strandLb;
    private static JTextArea orfInfoTxtarea, blastInfoTxtarea;
    private static JPanel blastPnl, orfInfoPnl, blastInfoPnl, ntBlastPnl, protBlastPnl;
    private static JComboBox strandBox, readFrmBox, databaseNtBox, databaseProtBox;
    private static JRadioButton blastpRb, blastxRb, tblastnRb, blastnRb, swissRb, nrRb;
    private static JTable orfInfoTb;

    public static void main(String[] args) {
        OrfFinder frame = new OrfFinder();
        frame.setTitle("ORF Finder");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.createGui();
        frame.pack();
        System.out.println(orfInfoTb.getPreferredSize());
        System.out.println(orfInfoTb.getSize());
        frame.setVisible(true);
    }

    private void createGui() {
        Container window = getContentPane();
        window.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        Border blackline = BorderFactory.createLineBorder(Color.BLACK);

        seqLb = new JLabel("Sequence:");
        window.add(seqLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 0,
                0, 0, 0, 0, new Insets(25, 25, 10, 20), GridBagConstraints.LINE_END));

        fileTf = new JTextField("Paste a DNA sequence here or select a FASTA file.");
        window.add(fileTf, getConstraints(GridBagConstraints.HORIZONTAL, 3, 1, 0, 1,
                5, 0, 0, 0, new Insets(25, 0, 10, 0), GridBagConstraints.LINE_START));

        browseBt = new JButton("Browse");
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
        window.add(startOrfBt, getConstraints(GridBagConstraints.NONE, 2, 1, 4, 2,
                0, 0, 0, 0, new Insets(3, 80, 0, 0), GridBagConstraints.LINE_START));

        estTimeLb = new JLabel("Estimated time:\t");
        window.add(estTimeLb, getConstraints(GridBagConstraints.NONE, 2, 1, 2, 2,
                0, 0, 0, 0, new Insets(10, 80, 2, 0), GridBagConstraints.LINE_START));

        databaseBt = new JButton("Open database");
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
                0, 0, 0, 0, new Insets(0, 25, 0, 0), GridBagConstraints.CENTER));

        protBlastPnl = new JPanel();
        protBlastPnl.setLayout(new GridBagLayout());
        protBlastPnl.setBackground(new Color(200, 200, 200));

        ButtonGroup blastTypeProt = new ButtonGroup();

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

        databaseProtBox = new JComboBox<>(new String[]{"nr", "swissprot", "refseq"});
        protBlastPnl.add(databaseProtBox, getConstraints(GridBagConstraints.NONE, 2, 1, 4, 0,
                0, 0, 0, 0, new Insets(5, 0, 5, 0), GridBagConstraints.CENTER));

        blastProtBt = new JButton("BLAST selected ORF(s)");
        protBlastPnl.add(blastProtBt, getConstraints(GridBagConstraints.NONE, 2, 1, 5, 0,
                0, 0, 1, 1, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        ntBlastPnl = new JPanel();
        ntBlastPnl.setLayout(new GridBagLayout());
        ntBlastPnl.setBackground(new Color(200, 200, 200));

        ButtonGroup blastTypeNt = new ButtonGroup();

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

        databaseNtLb = new JLabel("Database:");
        ntBlastPnl.add(databaseNtLb, getConstraints(GridBagConstraints.NONE, 2, 1, 3, 0,
                0, 0, 1, 0, new Insets(0, 0, 5, 0), GridBagConstraints.PAGE_END));

        databaseNtBox = new JComboBox<>(new String[]{"nt", "refseq_rna", "pdb"});
        ntBlastPnl.add(databaseNtBox, getConstraints(GridBagConstraints.NONE, 2, 1, 4, 0,
                0, 0, 0, 0, new Insets(5, 0, 5, 0), GridBagConstraints.CENTER));

        blastNtBt = new JButton("BLAST selected ORF(s)");
        ntBlastPnl.add(blastNtBt, getConstraints(GridBagConstraints.NONE, 2, 1, 5, 0,
                0, 0, 1, 1, new Insets(0, 0, 10, 0), GridBagConstraints.PAGE_END));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Protein BLAST", null, protBlastPnl, null);
        tabbedPane.addTab("Nucleotide BLAST", null, ntBlastPnl, null);
        tabbedPane.setSelectedIndex(0);
        blastPnl.add(tabbedPane);

        orfInfoPnl = new JPanel();
        orfInfoPnl.setLayout(new GridBagLayout());
        orfInfoPnl.setBackground(new Color(200, 200, 200));
        orfInfoPnl.setBorder(blackline);
        orfInfoPnl.setPreferredSize(new Dimension(670, 300));
        window.add(orfInfoPnl, getConstraints(GridBagConstraints.BOTH, 3, 1, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));

        JButton selectAllBt = new JButton("Select all");
        orfInfoPnl.add(selectAllBt, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 5, 0, 0), GridBagConstraints.LINE_START));

        JLabel orfInfoLb = new JLabel("Info about found ORF(s)");
        orfInfoPnl.add(orfInfoLb, getConstraints(GridBagConstraints.NONE, 3, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

        JLabel nrSelectedLb = new JLabel("0 ORF(s) selected");
        orfInfoPnl.add(nrSelectedLb, getConstraints(GridBagConstraints.NONE, 1, 1, 0, 2,
                0, 0, 0, 0, new Insets(5, 0, 0, 5), GridBagConstraints.LINE_END));

        String[] columnNames = {"Select", "ORF ID", "Strand", "Frame", "Length", "Start position", "Stop position", "Extra Info"};
        Object[][] tableData = {{"", 22385, "-", 3, 1988550, 20, 287000, "Click for sequence"}, {"", 1, "+", 1, 254230, 230, 434280, "Click for extra info"}, {"", 24, "+", 2, 66666366, 33333, 999999, "Click for extra info"}
                , {"", 287523, "-", 3, 1987580, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 4804, "Click for extra info"}, {"", 24, "+", 2, 6666666, 3334333, 999999, "Click for extra info"}
                , {"", 223, "-", 3, 197680, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 254765320, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 62466666, 33333, 999999, "Click for extra info"}
                , {"", 223, "-", 3, 1980, 2580, 200870, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 664266666, 33333, 999432999, "Click for extra info"}
                , {"", 223, "-", 3, 198340, 20, 204200, "Click for extra info"}, {"", 1, "+", 1, 250432, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33333, 999999, "Click for extra info"}
                , {"", 223, "-", 3, 198580, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33423333, 999999, "Click for extra info"}
                , {"", 223, "-", 3, 1234980, 20, 2043200, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33333, 999999, "Click for extra info"}
                , {"", 223, "-", 3, 1975880, 20, 2000, "Click for extra info"}, {"", 1, "+", 1, 250, 230, 480, "Click for extra info"}, {"", 24, "+", 2, 6666666, 33333, 999999, "Click for extra info"}};

//        DefaultTableModel dm = (DefaultTableModel)orfInfoTb.getModel();
//        dm.addRow(Object[]);

//        dm.getDataVector().removeAllElements();
//        dm.fireTableDataChanged();

        orfInfoTb = new JTable(new DefaultTableModel(null, columnNames));
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

        JPanel orfExtInfPnl = new JPanel();
        orfExtInfPnl.setLayout(new GridBagLayout());
        orfExtInfPnl.setBackground(new Color(200, 200, 200));
        orfExtInfPnl.setBorder(blackline);
        orfExtInfPnl.setPreferredSize(new Dimension(670, 300));
        window.add(orfExtInfPnl, getConstraints(GridBagConstraints.BOTH, 3, 1, 6, 2,
                0, 0, 1, 1, new Insets(7, 25, 0, 25), GridBagConstraints.LINE_START));
        orfExtInfPnl.setVisible(false);
//        orfInfoPnl.setVisible(false);

        JLabel orfNameLb = new JLabel("ORF nr test");
        orfExtInfPnl.add(orfNameLb, getConstraints(GridBagConstraints.NONE, 2, 1, 0, 0,
                0, 0, 0, 0, new Insets(5, 0, 0, 0), GridBagConstraints.CENTER));

        JButton goBackBt = new JButton("Back");
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

        saveDatabaseBt = new JButton("Save to Database");
        window.add(saveDatabaseBt, getConstraints(GridBagConstraints.NONE, 1, 1, 7, 4,
                10, 0, 0, 0, new Insets(10, 0, 25, 25), GridBagConstraints.LINE_END));
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (orfInfoTb.isVisible()) {
            int row = orfInfoTb.getSelectedRow();
            int column = orfInfoTb.getSelectedColumn();
            switch (column) {
                case 0:
                    if (orfInfoTb.getValueAt(row, column).equals(""))
                        orfInfoTb.setValueAt("✔", row, column);
                    else
                        orfInfoTb.setValueAt("", row, column);
                    break;
                case 7:
                    System.out.println("laat extra info zien over column: 7 en row: " + row);
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
