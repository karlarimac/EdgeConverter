package rit.g3.screen;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import rit.g3.edgeconverter.EdgeConvertGUI;
import rit.g3.edgeconverter.ExampleFileFilter;
import rit.g3.listener.CreateDDLButtonListener;
import rit.g3.listener.EdgeMenuListener;
import rit.g3.listener.EdgeRadioButtonListener;
import rit.g3.listener.EdgeWindowListener;
import rit.g3.model.Field;

import static rit.g3.screen.DefineRelationScreen.*;
import static rit.g3.utils.Constants.*;

public class DefineTableScreen {

    private static final Logger logger = LogManager.getLogger(DefineTableScreen.class);

    public static final JFrame jfDT = new JFrame(DEFINE_TABLES);
    public static final JPanel jpDTBottom = new JPanel(new GridLayout(1, 2));
    public static final JPanel jpDTCenter = new JPanel(new GridLayout(1, 3));
    public static final JPanel jpDTCenter1 = new JPanel(new BorderLayout());
    public static final JPanel jpDTCenter2 = new JPanel(new BorderLayout());
    public static final JPanel jpDTCenterRight = new JPanel(new GridLayout(1, 2));
    JPanel jpDTCenterRight1;
    public static final JPanel jpDTCenterRight2 = new JPanel(new GridLayout(6, 1));
    public static final JPanel jpDTMove = new JPanel(new GridLayout(2, 1));

    public static final JButton jbDTCreateDDL = new JButton("Create DDL");
    public static final JButton jbDTDefineRelations = new JButton(DEFINE_RELATIONS);
    public static final JButton jbDTVarchar = new JButton("Set Varchar Length");
    public static final JButton jbDTDefaultValue = new JButton("Set Default Value");
    public static final JButton jbDTMoveUp = new JButton("^");
    public static final JButton jbDTMoveDown = new JButton("v");

    public static final ButtonGroup bgDTDataType = new ButtonGroup();
    protected static final String[] strDataType = Field.getStrDataType();
    private static JRadioButton[] jrbDataType = new JRadioButton[strDataType.length];
    public static final JCheckBox jcheckDTDisallowNull = new JCheckBox("Disallow Null");
    public static final JCheckBox jcheckDTPrimaryKey = new JCheckBox("Primary Key");
    public static final JTextField jtfDTVarchar = new JTextField();
    public static final JTextField jtfDTDefaultValue = new JTextField();
    public static final JLabel jlabDTTables = new JLabel("All Tables", SwingConstants.CENTER);
    public static final JLabel jlabDTFields = new JLabel("Fields List", SwingConstants.CENTER);

    JList<String> jlDTTablesAll;
    JList<String> jlDTFieldsTablesAll;

    public static final DefaultListModel<String> dlmDTTablesAll = new DefaultListModel<>();
    public static final DefaultListModel<String> dlmDTFieldsTablesAll = new DefaultListModel<>();
    public static final JMenuBar jmbDTMenuBar = new JMenuBar();
    public static final JMenu jmDTFile = new JMenu("File");
    public static final JMenu jmDTOptions = new JMenu("Options");
    public static final JMenu jmDTHelp = new JMenu("Help");
    public static final JMenuItem jmiDTOpenEdge = new JMenuItem("Open...");
    public static final JMenuItem jmiDTSave = new JMenuItem("Save");
    public static final JMenuItem jmiDTSaveAs = new JMenuItem("Save As...");
    public static final JMenuItem jmiDTExit = new JMenuItem("Exit");
    public static final JMenuItem jmiDTOptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
    public static final JMenuItem jmiDTOptionsShowProducts = new JMenuItem("Show Database Products Available");
    public static final JMenuItem jmiDTHelpAbout = new JMenuItem("About");
    public static final ExampleFileFilter effEdge = new ExampleFileFilter("edg", "Edge Diagrammer Files");
    public static final ExampleFileFilter effSave = new ExampleFileFilter("sav", "Edge Convert Save Files");

    private EdgeMenuListener menuListener;
    private EdgeRadioButtonListener radioListener;
    private EdgeWindowListener edgeWindowListener;
    private CreateDDLButtonListener createDDLListener;

    public JRadioButton[] getJrbDataType() {
        return jrbDataType;
    }

    public DefineTableScreen() {
        this.radioListener = new EdgeRadioButtonListener(this);
        this.createDDLListener = new CreateDDLButtonListener();
        this.menuListener = new EdgeMenuListener();
        this.edgeWindowListener = new EdgeWindowListener();
        this.createDTScreen();
    }

    public void createDTScreen() {// create Define Tables screen
        JScrollPane jspDTTablesAll;
        JScrollPane jspDTFieldsTablesAll;

        jfDT.setLocation(HORIZ_LOC, VERT_LOC);
        jfDT.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jfDT.addWindowListener(edgeWindowListener);
        jfDT.getContentPane().setLayout(new BorderLayout());
        jfDT.setVisible(true);
        jfDT.setSize(HORIZ_SIZE + 150, VERT_SIZE);

        // setup menubars and menus
        jfDT.setJMenuBar(jmbDTMenuBar);
        jmDTFile.setMnemonic(KeyEvent.VK_F);
        jmbDTMenuBar.add(jmDTFile);
        jmiDTOpenEdge.setMnemonic(KeyEvent.VK_E);
        jmiDTOpenEdge.addActionListener(menuListener);

        jmiDTOpenEdge.setMnemonic(KeyEvent.VK_V);

        jmiDTSave.setMnemonic(KeyEvent.VK_S);
        jmiDTSave.setEnabled(false);
        jmiDTSave.addActionListener(menuListener);
        jmiDTSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDTSaveAs.setEnabled(false);
        jmiDTSaveAs.addActionListener(menuListener);
        jmiDTExit.setMnemonic(KeyEvent.VK_X);
        jmiDTExit.addActionListener(menuListener);
        jmDTFile.add(jmiDTOpenEdge);

        jmDTFile.add(jmiDTSave);
        jmDTFile.add(jmiDTSaveAs);
        jmDTFile.add(jmiDTExit);

        jmDTOptions.setMnemonic(KeyEvent.VK_O);
        jmbDTMenuBar.add(jmDTOptions);
        jmiDTOptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDTOptionsOutputLocation.addActionListener(menuListener);
        jmiDTOptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDTOptionsShowProducts.setEnabled(false);
        jmiDTOptionsShowProducts.addActionListener(menuListener);
        jmDTOptions.add(jmiDTOptionsOutputLocation);
        jmDTOptions.add(jmiDTOptionsShowProducts);

        jmDTHelp.setMnemonic(KeyEvent.VK_H);
        jmbDTMenuBar.add(jmDTHelp);
        jmiDTHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmiDTHelpAbout.addActionListener(menuListener);
        jmDTHelp.add(jmiDTHelpAbout);

        jfcEdge.setCurrentDirectory(new File("././resources"));
        jfcOutputDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        jbDTCreateDDL.setEnabled(false);
        jbDTCreateDDL.addActionListener(createDDLListener);

        jbDTDefineRelations.setEnabled(false);
        jbDTDefineRelations.addActionListener(ae -> {
            jfDT.setVisible(false);
            jfDR.setVisible(true); // show the Define Relations screen
            clearDTControls();
            dlmDTFieldsTablesAll.removeAllElements();
        });

        jpDTBottom.add(jbDTDefineRelations);
        jpDTBottom.add(jbDTCreateDDL);
        jfDT.getContentPane().add(jpDTBottom, BorderLayout.SOUTH);

        jlDTTablesAll = new JList<>(dlmDTTablesAll);
        jlDTTablesAll.addListSelectionListener(lse -> handleElement());

        jlDTFieldsTablesAll = new JList<>(dlmDTFieldsTablesAll);
        jlDTFieldsTablesAll.addListSelectionListener(lse -> handleField());

        jbDTMoveUp.setEnabled(false);
        jbDTMoveUp.addActionListener(ae -> handleSelection());
        jbDTMoveDown.setEnabled(false);
        jbDTMoveDown.addActionListener(ae -> repopulateFields());
        jpDTMove.add(jbDTMoveUp);
        jpDTMove.add(jbDTMoveDown);

        jspDTTablesAll = new JScrollPane(jlDTTablesAll);
        jspDTFieldsTablesAll = new JScrollPane(jlDTFieldsTablesAll);
        jpDTCenter1.add(jlabDTTables, BorderLayout.NORTH);
        jpDTCenter2.add(jlabDTFields, BorderLayout.NORTH);
        jpDTCenter1.add(jspDTTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jspDTFieldsTablesAll, BorderLayout.CENTER);
        jpDTCenter2.add(jpDTMove, BorderLayout.EAST);
        jpDTCenter.add(jpDTCenter1);
        jpDTCenter.add(jpDTCenter2);
        jpDTCenter.add(jpDTCenterRight);

        // get the list of currently supported data types
        getSupportedDT();
        jpDTCenterRight.add(jpDTCenterRight1);

        jcheckDTDisallowNull.setEnabled(false);
        jcheckDTDisallowNull.addItemListener(ie -> {
            DefineRelationScreen.getCurrentDTField().setDisallowNull(jcheckDTDisallowNull.isSelected());
            EdgeConvertGUI.setDataSaved(false);
        });

        jcheckDTPrimaryKey.setEnabled(false);
        jcheckDTPrimaryKey.addItemListener(ie -> {
            DefineRelationScreen.getCurrentDTField().setIsPrimaryKey(jcheckDTPrimaryKey.isSelected());
            EdgeConvertGUI.setDataSaved(false);
        });

        jbDTDefaultValue.setEnabled(false);
        jbDTDefaultValue.addActionListener(ae -> {
            String prev = jtfDTDefaultValue.getText();
            boolean goodData = false;
            int i = DefineRelationScreen.getCurrentDTField().getDataType();
            do {
                String result = (String) JOptionPane.showInputDialog(
                        null,
                        "Enter the default value:",
                        "Default Value",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        prev);

                if ((result == null)) {
                    jtfDTDefaultValue.setText(prev);
                    return;
                }
                switch (i) {
                    case 0: // varchar
                        goodData = addCase0(goodData, result);
                        break;
                    case 1: // boolean
                        goodData = addCase1(goodData, result);
                        break;
                    case 2: // Integer
                        goodData = addCase2(goodData, result);
                        break;
                    case 3: // Double
                        goodData = addCase3(goodData, result);
                        break;
                    case 4: // Timestamp
                        goodData = addCase4(goodData, result);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null,
                                "Please enter a value.");
                }
            } while (!goodData);
            int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
            if (selIndex >= 0) {
                String selText = dlmDTFieldsTablesAll.getElementAt(selIndex);
                setCurrentDTField(selText);
                DefineRelationScreen.getCurrentDTField().setDefaultValue(jtfDTDefaultValue.getText());
            }
            EdgeConvertGUI.setDataSaved(false);
        });
        jtfDTDefaultValue.setEditable(false);

        jbDTVarchar.setEnabled(false);
        jbDTVarchar.addActionListener(ae -> {
            String prev = jtfDTVarchar.getText();
            String result = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the varchar length:",
                    "Varchar Length",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    prev);
            if ((result == null)) {
                jtfDTVarchar.setText(prev);
                return;
            }
            checkLength(result);
        });
        jtfDTVarchar.setEditable(false);

        jpDTCenterRight2.add(jbDTVarchar);
        jpDTCenterRight2.add(jtfDTVarchar);
        jpDTCenterRight2.add(jcheckDTPrimaryKey);
        jpDTCenterRight2.add(jcheckDTDisallowNull);
        jpDTCenterRight2.add(jbDTDefaultValue);
        jpDTCenterRight2.add(jtfDTDefaultValue);
        jpDTCenterRight.add(jpDTCenterRight1);
        jpDTCenterRight.add(jpDTCenterRight2);
        jpDTCenter.add(jpDTCenterRight);
        jfDT.getContentPane().add(jpDTCenter, BorderLayout.CENTER);
        jfDT.validate();
    } // createDTScreen

    private void checkLength(String result) {
        showWarning(result);
    }

    private void showWarning(String result) {
        int varchar;
        try {
            if (result.length() > 5) {
                JOptionPane.showMessageDialog(null,
                        "Varchar length must be greater than 0 and less than or equal to 65535.");
                jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
                return;
            }
            varchar = Integer.parseInt(result);
            if (varchar > 0 && varchar <= 65535) { // max length of varchar is 255 before v5.0.3
                jtfDTVarchar.setText(Integer.toString(varchar));
                DefineRelationScreen.getCurrentDTField().setVarcharValue(varchar);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Varchar length must be greater than 0 and less than or equal to 65535.");
                jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
                return;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "\"" + result + "\" is not a number");
            jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
            return;
        }
        EdgeConvertGUI.setDataSaved(false);
    }

    private boolean addCase4(boolean goodData, String result) {
        try {
            jtfDTDefaultValue.setText(result);
            goodData = true;
        } catch (Exception e) {
            logger.fatal("Error ", e);
        }
        return goodData;
    }

    private boolean addCase3(boolean goodData, String result) {
        try {
            jtfDTDefaultValue.setText(result);
            goodData = true;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "\"" + result
                    + "\" is not a double or is outside the bounds of valid double values.");
        }
        return goodData;
    }

    private boolean addCase2(boolean goodData, String result) {
        try {
            jtfDTDefaultValue.setText(result);
            goodData = true;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "\"" + result
                    + "\" is not an integer or is outside the bounds of valid integer values.");
        }
        return goodData;
    }

    private boolean addCase1(boolean goodData, String result) {
        String newResult = result.toLowerCase();
        if (newResult.equals("true") || newResult.equals("false")) {
            jtfDTDefaultValue.setText(newResult);
            goodData = true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "You must input a valid boolean value (\"true\" or \"false\").");
        }
        return goodData;
    }

    private boolean addCase0(boolean goodData, String result) {
        if (result.length() <= Integer.parseInt(jtfDTVarchar.getText())) {
            jtfDTDefaultValue.setText(result);
            goodData = true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "The length of this value must be less than or equal to the Varchar length specified.");
        }
        return goodData;
    }

    private void getSupportedDT() {
        jpDTCenterRight1 = new JPanel(new GridLayout(strDataType.length, 1));
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i] = new JRadioButton(strDataType[i]); // assign label for radio button from String array
            jrbDataType[i].setEnabled(false);
            jrbDataType[i].addActionListener(radioListener);
            bgDTDataType.add(jrbDataType[i]);
            jpDTCenterRight1.add(jrbDataType[i]);
        }
    }

    private void repopulateFields() {
        int selection = jlDTFieldsTablesAll.getSelectedIndex(); // the original selected index
        DefineRelationScreen.getCurrentDTTable().moveFieldDown(selection);
        // repopulate Fields List
        int[] currentNativeFields = DefineRelationScreen.getCurrentDTTable().getNativeFieldsArray();
        jlDTFieldsTablesAll.clearSelection();
        dlmDTFieldsTablesAll.removeAllElements();
        for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
            dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
        }
        jlDTFieldsTablesAll.setSelectedIndex(selection + 1);
        EdgeConvertGUI.setDataSaved(false);
    }

    private void handleSelection() {
        int selection = jlDTFieldsTablesAll.getSelectedIndex();
        DefineRelationScreen.getCurrentDTTable().moveFieldUp(selection);
        // repopulate Fields List
        int[] currentNativeFields = DefineRelationScreen.getCurrentDTTable().getNativeFieldsArray();
        jlDTFieldsTablesAll.clearSelection();
        dlmDTFieldsTablesAll.removeAllElements();
        for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
            dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
        }
        jlDTFieldsTablesAll.setSelectedIndex(selection - 1);
        EdgeConvertGUI.setDataSaved(false);
    }

    private void handleField() {
        int selIndex = jlDTFieldsTablesAll.getSelectedIndex();

        if (selIndex == 0) {
            jbDTMoveUp.setEnabled(false);
        }

        if (selIndex == (dlmDTFieldsTablesAll.getSize() - 1)) {
            jbDTMoveDown.setEnabled(false);
        }

        String selText = dlmDTFieldsTablesAll.getElementAt(selIndex);
        setCurrentDTField(selText); // set pointer to the selected field
        enableControls();
        jrbDataType[DefineRelationScreen.getCurrentDTField().getDataType()].setSelected(true); // select the appropriate
                                                                                               // radio
        // button, based on value of
        // dataType
        if (jrbDataType[0].isSelected()) { // this is the Varchar radio button
            jbDTVarchar.setEnabled(true); // enable the Varchar button
            jtfDTVarchar.setText(Integer.toString(DefineRelationScreen.getCurrentDTField().getVarcharValue())); // fill
                                                                                                                // text
            // field with
            // varcharValue
        } else { // some radio button other than Varchar is selected
            jtfDTVarchar.setText(""); // clear the text field
            jbDTVarchar.setEnabled(false); // disable the button
        }
        jcheckDTPrimaryKey.setSelected(DefineRelationScreen.getCurrentDTField().getIsPrimaryKey()); // clear or set
                                                                                                    // Primary
        // Key checkbox
        jcheckDTDisallowNull.setSelected(DefineRelationScreen.getCurrentDTField().getDisallowNull()); // clear or set
                                                                                                      // Disallow
        // Null checkbox
        jtfDTDefaultValue.setText(DefineRelationScreen.getCurrentDTField().getDefaultValue()); // fill text field with
        // defaultValue
    }

    private void handleElement() {
        int selIndex = jlDTTablesAll.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDTTablesAll.getElementAt(selIndex);
            setCurrentDTTable(selText); // set pointer to the selected table
            int[] currentNativeFields = DefineRelationScreen.getCurrentDTTable().getNativeFieldsArray();
            jlDTFieldsTablesAll.clearSelection();
            dlmDTFieldsTablesAll.removeAllElements();
            jbDTMoveUp.setEnabled(false);
            jbDTMoveDown.setEnabled(false);
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
            }
        }
        disableControls();
    }

    private static void enableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].setEnabled(true);
        }
        jcheckDTPrimaryKey.setEnabled(true);
        jcheckDTDisallowNull.setEnabled(true);
        jbDTVarchar.setEnabled(true);
        jbDTDefaultValue.setEnabled(true);
    }

    public static void disableControls() {
        for (int i = 0; i < strDataType.length; i++) {
            jrbDataType[i].setEnabled(false);
        }
        jcheckDTPrimaryKey.setEnabled(false);
        jcheckDTDisallowNull.setEnabled(false);
        jbDTDefaultValue.setEnabled(false);
        jtfDTVarchar.setText("");
        jtfDTDefaultValue.setText("");
    }

    private void clearDTControls() {
        jlDTTablesAll.clearSelection();
        jlDTFieldsTablesAll.clearSelection();
    }

    private static void setCurrentDTTable(String selText) {
        for (int tIndex = 0; tIndex < EdgeConvertGUI.getTables().length; tIndex++) {
            if (selText.equals(EdgeConvertGUI.getTables()[tIndex].getName())) {
                DefineRelationScreen.setCurrentDTTable(EdgeConvertGUI.getTables()[tIndex]);
                return;
            }
        }
    }

    private static void setCurrentDTField(String selText) {
        for (int fIndex = 0; fIndex < EdgeConvertGUI.getFields().length; fIndex++) {
            if (selText.equals(EdgeConvertGUI.getFields()[fIndex].getName())
                    && EdgeConvertGUI.getFields()[fIndex].getTableID() == DefineRelationScreen.getCurrentDTTable()
                            .getNumFigure()) {
                DefineRelationScreen.setCurrentDTField(EdgeConvertGUI.getFields()[fIndex]);
                return;
            }
        }
    }

    private static String getFieldName(int numFigure) {
        for (int fIndex = 0; fIndex < EdgeConvertGUI.getFields().length; fIndex++) {
            if (EdgeConvertGUI.getFields()[fIndex].getNumFigure() == numFigure) {
                return EdgeConvertGUI.getFields()[fIndex].getName();
            }
        }
        return "";
    }

}
