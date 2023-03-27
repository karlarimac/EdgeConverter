package rit.g3.screen;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import rit.g3.edgeconverter.EdgeConvertGUI;
import rit.g3.listener.CreateDDLButtonListener;
import rit.g3.listener.EdgeMenuListener;
import rit.g3.listener.EdgeWindowListener;
import rit.g3.model.Field;
import rit.g3.model.Table;

import static rit.g3.screen.DefineTableScreen.*;
import static rit.g3.utils.Constants.*;

public class DefineRelationScreen {

    public static final JFrame jfDR = new JFrame(DEFINE_RELATIONS);
    public static final JFileChooser jfcEdge = new JFileChooser();
    public static final JFileChooser jfcOutputDir = new JFileChooser();
    public final JList<String> jlDRTablesRelations;
    public final JList<String> jlDRTablesRelatedTo;
    public final JList<String> jlDRFieldsTablesRelations;
    public final JList<String> jlDRFieldsTablesRelatedTo;
    public static final JButton jbDRCreateDDL = new JButton("Create DDL");
    public static final JButton jbDRDefineTables = new JButton(DEFINE_TABLES);
    public static final JButton jbDRBindRelation = new JButton("Bind/Unbind Relation");
    public static final JMenuItem jmiDROpenEdge = new JMenuItem("Open Edge File");
    public static final JMenuItem jmiDROpenSave = new JMenuItem("Open Save File");
    public static final JMenuItem jmiDRSave = new JMenuItem("Save");
    public static final JMenuItem jmiDRSaveAs = new JMenuItem("Save As...");
    public static final JMenuItem jmiDRExit = new JMenuItem("Exit");
    public static final JMenuItem jmiDROptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
    public static final JMenuItem jmiDROptionsShowProducts = new JMenuItem("Show Database Products Available");
    public static final JMenuItem jmiDRHelpAbout = new JMenuItem("About");

    public static final JLabel jlabDRTablesRelations = new JLabel("Tables With Relations", SwingConstants.CENTER);
    public static final JLabel jlabDRTablesRelatedTo = new JLabel("Related Tables", SwingConstants.CENTER);
    public static final JLabel jlabDRFieldsTablesRelations = new JLabel("Fields in Tables with Relations",
            SwingConstants.CENTER);
    public static final JLabel jlabDRFieldsTablesRelatedTo = new JLabel("Fields in Related Tables",
            SwingConstants.CENTER);
    public final JScrollPane jspDRTablesRelations;
    public final JScrollPane jspDRTablesRelatedTo;
    public final JScrollPane jspDRFieldsTablesRelations;
    public final JScrollPane jspDRFieldsTablesRelatedTo;
    public static final JMenuBar jmbDRMenuBar = new JMenuBar();
    public static final JMenu jmDRFile = new JMenu("File");
    public static final JMenu jmDROptions = new JMenu("Options");
    public static final JMenu jmDRHelp = new JMenu("Help");

    JPanel jpDRBottom;
    JPanel jpDRCenter;
    JPanel jpDRCenter1;
    JPanel jpDRCenter2;
    JPanel jpDRCenter3;
    JPanel jpDRCenter4;

    private static boolean readSuccess = true; // this tells GUI whether to populate JList components or not
    private static DefaultListModel<String> dlmDRTablesRelations = new DefaultListModel<>();
    private static DefaultListModel<String> dlmDRTablesRelatedTo = new DefaultListModel<>();
    private static DefaultListModel<String> dlmDRFieldsTablesRelations = new DefaultListModel<>();
    private static DefaultListModel<String> dlmDRFieldsTablesRelatedTo = new DefaultListModel<>();

    private static Table currentDTTable;
    private static Table currentDRTable1;
    private static Table currentDRTable2; // pointers to currently selected table(s)
                                          // on Define Tables (DT) and Define
                                          // Relations (DR) screens
    private static Field currentDTField;
    private static Field currentDRField1;
    private static Field currentDRField2; // pointers to currently selected field(s)
                                          // on Define Tables (DT) and Define
                                          // Relations (DR) screens
    private EdgeMenuListener menuListener;
    private EdgeWindowListener edgeWindowListener;
    private CreateDDLButtonListener createDDLListener;

    public static Table getCurrentDTTable() {
        return currentDTTable;
    }

    public static void setCurrentDTTable(Table currentDTTable) {
        DefineRelationScreen.currentDTTable = currentDTTable;
    }

    public static Table getCurrentDRTable1() {
        return currentDRTable1;
    }

    public static void setCurrentDRTable1(Table currentDRTable1) {
        DefineRelationScreen.currentDRTable1 = currentDRTable1;
    }

    public static Table getCurrentDRTable2() {
        return currentDRTable2;
    }

    public static void setCurrentDRTable2(Table currentDRTable2) {
        DefineRelationScreen.currentDRTable2 = currentDRTable2;
    }

    public static Field getCurrentDTField() {
        return currentDTField;
    }

    public static void setCurrentDTField(Field currentDTField) {
        DefineRelationScreen.currentDTField = currentDTField;
    }

    public static Field getCurrentDRField1() {
        return currentDRField1;
    }

    public static void setCurrentDRField1(Field currentDRField1) {
        DefineRelationScreen.currentDRField1 = currentDRField1;
    }

    public static Field getCurrentDRField2() {
        return currentDRField2;
    }

    public static void setCurrentDRField2(Field currentDRField2) {
        DefineRelationScreen.currentDRField2 = currentDRField2;
    }

    public DefineRelationScreen() {
        this.menuListener = new EdgeMenuListener();
        this.edgeWindowListener = new EdgeWindowListener();

        jlDRTablesRelations = new JList<>(dlmDRTablesRelations);
        jspDRTablesRelations = new JScrollPane(jlDRTablesRelations);

        jlDRTablesRelatedTo = new JList<>(dlmDRTablesRelatedTo);
        jspDRTablesRelatedTo = new JScrollPane(jlDRTablesRelatedTo);

        jlDRFieldsTablesRelations = new JList<>(dlmDRFieldsTablesRelations);
        jspDRFieldsTablesRelations = new JScrollPane(jlDRFieldsTablesRelations);

        jlDRFieldsTablesRelatedTo = new JList<>(dlmDRFieldsTablesRelations);
        jspDRFieldsTablesRelatedTo = new JScrollPane(jlDRFieldsTablesRelatedTo);

        this.createDRScreen();
    }

    public static void setReadSuccess(boolean value) {
        readSuccess = value;
    }

    public static boolean getReadSuccess() {
        return readSuccess;
    }

    private static void depopulateLists() {
        dlmDTTablesAll.clear();
        dlmDTFieldsTablesAll.clear();
        dlmDRTablesRelations.clear();
        dlmDRFieldsTablesRelations.clear();
        dlmDRTablesRelatedTo.clear();
        dlmDRFieldsTablesRelatedTo.clear();
    }

    public static void populateLists() {
        if (readSuccess) {
            jfDT.setVisible(true);
            jfDR.setVisible(false);
            disableControls();
            depopulateLists();
            for (int tIndex = 0; tIndex < EdgeConvertGUI.getTables().length; tIndex++) {
                String tempName = EdgeConvertGUI.getTables()[tIndex].getName();
                dlmDTTablesAll.addElement(tempName);
                int[] relatedTables = EdgeConvertGUI.getTables()[tIndex].getRelatedTablesArray();
                if (relatedTables.length > 0) {
                    dlmDRTablesRelations.addElement(tempName);
                }
            }
        }
        readSuccess = true;
    }

    public void createDRScreen() {

        // create Define Relations screen
        jfDR.setSize(HORIZ_SIZE, VERT_SIZE);
        jfDR.setLocation(HORIZ_LOC, VERT_LOC);
        jfDR.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jfDR.addWindowListener(edgeWindowListener);
        jfDR.getContentPane().setLayout(new BorderLayout());

        // setup menubars and menus
        jfDR.setJMenuBar(jmbDRMenuBar);
        jmDRFile.setMnemonic(KeyEvent.VK_F);
        jmbDRMenuBar.add(jmDRFile);

        jmiDROpenEdge.setMnemonic(KeyEvent.VK_E);
        jmiDROpenEdge.addActionListener(menuListener);

        jmiDROpenSave.setMnemonic(KeyEvent.VK_V);
        jmiDROpenSave.addActionListener(menuListener);

        jmiDRSave.setMnemonic(KeyEvent.VK_S);
        jmiDRSave.setEnabled(false);
        jmiDRSave.addActionListener(menuListener);

        jmiDRSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDRSaveAs.setEnabled(false);
        jmiDRSaveAs.addActionListener(menuListener);
        jmiDRExit.setMnemonic(KeyEvent.VK_X);
        jmiDRExit.addActionListener(menuListener);
        jmDRFile.add(jmiDROpenEdge);
        jmDRFile.add(jmiDROpenSave);
        jmDRFile.add(jmiDRSave);
        jmDRFile.add(jmiDRSaveAs);
        jmDRFile.add(jmiDRExit);

        jmDROptions.setMnemonic(KeyEvent.VK_O);
        jmbDRMenuBar.add(jmDROptions);
        jmiDROptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDROptionsOutputLocation.addActionListener(menuListener);
        jmiDROptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDROptionsShowProducts.setEnabled(false);
        jmiDROptionsShowProducts.addActionListener(menuListener);
        jmDROptions.add(jmiDROptionsOutputLocation);
        jmDROptions.add(jmiDROptionsShowProducts);

        jmDRHelp.setMnemonic(KeyEvent.VK_H);
        jmbDRMenuBar.add(jmDRHelp);
        jmiDRHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmiDRHelpAbout.addActionListener(menuListener);
        jmDRHelp.add(jmiDRHelpAbout);

        jpDRCenter = new JPanel(new GridLayout(2, 2));
        jpDRCenter1 = new JPanel(new BorderLayout());
        jpDRCenter2 = new JPanel(new BorderLayout());
        jpDRCenter3 = new JPanel(new BorderLayout());
        jpDRCenter4 = new JPanel(new BorderLayout());

        jlDRTablesRelations.addListSelectionListener(lse -> {
            int selIndex = jlDRTablesRelations.getSelectedIndex();
            handleElement(selIndex);
        }

        );

        jlDRFieldsTablesRelations.addListSelectionListener(lse -> handleField());

        jlDRTablesRelatedTo.addListSelectionListener(lse -> handleTable());

        jlDRFieldsTablesRelatedTo.addListSelectionListener(lse -> handleTableFields());

        jpDRCenter1.add(jlabDRTablesRelations, BorderLayout.NORTH);
        jpDRCenter2.add(jlabDRFieldsTablesRelations, BorderLayout.NORTH);
        jpDRCenter3.add(jlabDRTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter4.add(jlabDRFieldsTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter1.add(jspDRTablesRelations, BorderLayout.CENTER);
        jpDRCenter2.add(jspDRFieldsTablesRelations, BorderLayout.CENTER);
        jpDRCenter3.add(jspDRTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter4.add(jspDRFieldsTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter.add(jpDRCenter1);
        jpDRCenter.add(jpDRCenter2);
        jpDRCenter.add(jpDRCenter3);
        jpDRCenter.add(jpDRCenter4);
        jfDR.getContentPane().add(jpDRCenter, BorderLayout.CENTER);
        jpDRBottom = new JPanel(new GridLayout(1, 3));

        jbDRDefineTables.addActionListener(ae -> {
            jfDT.setVisible(true); // show the Define Tables screen
            jfDR.setVisible(false);
            clearDRControls();
            depopulateLists();
            populateLists();
        });

        jbDRBindRelation.setEnabled(false);
        jbDRBindRelation.addActionListener(ae -> {
            int nativeIndex = jlDRFieldsTablesRelations.getSelectedIndex();
            int relatedField = currentDRField2.getNumFigure();
            if (currentDRField1.getFieldBound() == relatedField) { // the selected fields are already bound
                                                                   // to each other
                unbind(nativeIndex);
            }
            if (currentDRField1.getFieldBound() != 0) { // field is already bound to a different field
                int answer = JOptionPane.showConfirmDialog(null,
                        "There is already a relation defined on field "
                                + currentDRField1.getName() + ", do you wish to overwrite it?",
                        "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
                    jlDRTablesRelatedTo.setSelectedValue(getTableName(currentDRField1.getTableBound()),
                            true); // revert selections to saved settings
                    jlDRFieldsTablesRelatedTo
                            .setSelectedValue(getFieldName(currentDRField1.getFieldBound()), true); // revert
                                                                                                    // selections
                                                                                                    // to
                                                                                                    // saved
                                                                                                    // settings
                    return;
                }
            }
            if (currentDRField1.getDataType() != currentDRField2.getDataType()) {
                JOptionPane.showMessageDialog(null, "The datatypes of " + currentDRTable1.getName() + "."
                        + currentDRField1.getName() + " and " + currentDRTable2.getName()
                        + "." + currentDRField2.getName()
                        + " do not match.  Unable to bind this relation.");
                return;
            }
            if ((currentDRField1.getDataType() == 0) && (currentDRField2.getDataType() == 0) &&
                    (currentDRField1.getVarcharValue() != currentDRField2.getVarcharValue())) {
                JOptionPane.showMessageDialog(null,
                        "The varchar lengths of " + currentDRTable1.getName() + "."
                                + currentDRField1.getName() + " and " + currentDRTable2.getName()
                                + "." + currentDRField2.getName()
                                + " do not match.  Unable to bind this relation.");
                return;

            }
            currentDRTable1.setRelatedField(nativeIndex, relatedField);
            currentDRField1.setTableBound(currentDRTable2.getNumFigure());
            currentDRField1.setFieldBound(currentDRField2.getNumFigure());
            JOptionPane.showMessageDialog(null, "Table " + currentDRTable1.getName() + ": native field "
                    + currentDRField1.getName() + " bound to table " + currentDRTable2.getName()
                    + " on field " + currentDRField2.getName());
            EdgeConvertGUI.setDataSaved(false);
        });

        jbDRCreateDDL.setEnabled(false);
        jbDRCreateDDL.addActionListener(createDDLListener);

        jpDRBottom.add(jbDRDefineTables);
        jpDRBottom.add(jbDRBindRelation);
        jpDRBottom.add(jbDRCreateDDL);
        jfDR.getContentPane().add(jpDRBottom, BorderLayout.SOUTH);
    } // createDRScreen

    private void unbind(int nativeIndex) {
        int answer = JOptionPane.showConfirmDialog(null,
                "Do you wish to unbind the relation on field "
                        + currentDRField1.getName() + "?",
                "Are you sure?", JOptionPane.YES_NO_OPTION);
        selectYes(nativeIndex, answer);
    }

    private void selectYes(int nativeIndex, int answer) {
        if (answer == JOptionPane.YES_OPTION) {
            currentDRTable1.setRelatedField(nativeIndex, 0); // clear the related field
            currentDRField1.setTableBound(0); // clear the bound table
            currentDRField1.setFieldBound(0); // clear the bound field
            jlDRFieldsTablesRelatedTo.clearSelection(); // clear the listbox selection
        }
    }

    private void handleTableFields() {
        int selIndex = jlDRFieldsTablesRelatedTo.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRFieldsTablesRelatedTo.getElementAt(selIndex);
            setCurrentDRField2(selText);
            jbDRBindRelation.setEnabled(true);
        } else {
            jbDRBindRelation.setEnabled(false);
        }
    }

    private void handleTable() {
        int selIndex = jlDRTablesRelatedTo.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRTablesRelatedTo.getElementAt(selIndex);
            setCurrentDRTable2(selText);
            int[] currentNativeFields = currentDRTable2.getNativeFieldsArray();
            dlmDRFieldsTablesRelatedTo.removeAllElements();
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDRFieldsTablesRelatedTo.addElement(getFieldName(currentNativeFields[fIndex]));
            }
        }
    }

    private void handleField() {
        int selIndex = jlDRFieldsTablesRelations.getSelectedIndex();
        if (selIndex >= 0) {
            String selText = dlmDRFieldsTablesRelations.getElementAt(selIndex);
            setCurrentDRField1(selText);
            if (currentDRField1.getFieldBound() == 0) {
                jlDRTablesRelatedTo.clearSelection();
                jlDRFieldsTablesRelatedTo.clearSelection();
                dlmDRFieldsTablesRelatedTo.removeAllElements();
            } else {
                jlDRTablesRelatedTo.setSelectedValue(getTableName(currentDRField1.getTableBound()), true);
                jlDRFieldsTablesRelatedTo.setSelectedValue(getFieldName(currentDRField1.getFieldBound()), true);
            }
        }
    }

    private void handleElement(int selIndex) {
        if (selIndex >= 0) {
            String selText = dlmDRTablesRelations.getElementAt(selIndex);
            setCurrentDRTable1(selText);
            int[] currentNativeFields;
            int[] currentRelatedTables;
            currentNativeFields = currentDRTable1.getNativeFieldsArray();
            currentRelatedTables = currentDRTable1.getRelatedTablesArray();
            jlDRFieldsTablesRelations.clearSelection();
            jlDRTablesRelatedTo.clearSelection();
            jlDRFieldsTablesRelatedTo.clearSelection();
            dlmDRFieldsTablesRelations.removeAllElements();
            dlmDRTablesRelatedTo.removeAllElements();
            dlmDRFieldsTablesRelatedTo.removeAllElements();
            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                dlmDRFieldsTablesRelations.addElement(getFieldName(currentNativeFields[fIndex]));
            }
            for (int rIndex = 0; rIndex < currentRelatedTables.length; rIndex++) {
                dlmDRTablesRelatedTo.addElement(getTableName(currentRelatedTables[rIndex]));
            }
        }
    }

    private String getTableName(int numFigure) {
        for (int tIndex = 0; tIndex < EdgeConvertGUI.getTables().length; tIndex++) {
            if (EdgeConvertGUI.getTables()[tIndex].getNumFigure() == numFigure) {
                return EdgeConvertGUI.getTables()[tIndex].getName();
            }
        }
        return "";
    }

    private String getFieldName(int numFigure) {
        for (int fIndex = 0; fIndex < EdgeConvertGUI.getFields().length; fIndex++) {
            if (EdgeConvertGUI.getFields()[fIndex].getNumFigure() == numFigure) {
                return EdgeConvertGUI.getFields()[fIndex].getName();
            }
        }
        return "";
    }

    private static void setCurrentDRTable1(String selText) {
        for (int tIndex = 0; tIndex < EdgeConvertGUI.getTables().length; tIndex++) {
            if (selText.equals(EdgeConvertGUI.getTables()[tIndex].getName())) {
                currentDRTable1 = EdgeConvertGUI.getTables()[tIndex];
                return;
            }
        }
    }

    private static void setCurrentDRTable2(String selText) {
        for (int tIndex = 0; tIndex < EdgeConvertGUI.getTables().length; tIndex++) {
            if (selText.equals(EdgeConvertGUI.getTables()[tIndex].getName())) {
                currentDRTable2 = EdgeConvertGUI.getTables()[tIndex];
                return;
            }
        }
    }

    private static void setCurrentDRField1(String selText) {
        for (int fIndex = 0; fIndex < EdgeConvertGUI.getFields().length; fIndex++) {
            if (selText.equals(EdgeConvertGUI.getFields()[fIndex].getName())
                    && EdgeConvertGUI.getFields()[fIndex].getTableID() == currentDRTable1.getNumFigure()) {
                currentDRField1 = EdgeConvertGUI.getFields()[fIndex];
                return;
            }
        }
    }

    private static void setCurrentDRField2(String selText) {
        for (int fIndex = 0; fIndex < EdgeConvertGUI.getFields().length; fIndex++) {
            if (selText.equals(EdgeConvertGUI.getFields()[fIndex].getName())
                    && EdgeConvertGUI.getFields()[fIndex].getTableID() == currentDRTable2.getNumFigure()) {
                currentDRField2 = EdgeConvertGUI.getFields()[fIndex];
                return;
            }
        }
    }

    private void clearDRControls() {
        jlDRTablesRelations.clearSelection();
        jlDRTablesRelatedTo.clearSelection();
        jlDRFieldsTablesRelations.clearSelection();
        jlDRFieldsTablesRelatedTo.clearSelection();
    }

}
