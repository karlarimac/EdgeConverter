package rit.g3.listener;

import java.awt.event.*;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rit.g3.ddl.CreateDDL;
import rit.g3.ddl.CreateDDLMySQL;
import rit.g3.ddl.CreateDDLOracle;
import rit.g3.ddl.DDLGeneratorFactory;
import rit.g3.edgeconverter.EdgeConvertGUI;

import java.io.*;
import java.util.*;

import static rit.g3.screen.DefineTableScreen.*;
import static rit.g3.screen.DefineRelationScreen.*;

import rit.g3.utils.Constants;

public class CreateDDLButtonListener implements ActionListener {

    private static final Logger logger = LogManager.getLogger(CreateDDLButtonListener.class);

    PrintWriter pw;
    static File parseFile;
    static File saveFile;
    private static File outputDir;

    private static String[] productNames;
    private String databaseName;
    private CreateDDL convertCreateDDL;

    public void actionPerformed(ActionEvent ae) {
        setOutputDir();
        EdgeConvertGUI.setSqlString(getSQLStatements());
        if (EdgeConvertGUI.getSqlString().equals(Constants.CANCELLED)) {
            return;
        }
        writeSQL(EdgeConvertGUI.getSqlString());
    }

    public static void setOutputDir() {
        int returnVal;
        EdgeConvertGUI.setOutputDirOld(outputDir);
        EdgeConvertGUI.setAlSubclasses(new ArrayList<>());
        EdgeConvertGUI.setAlProductNames(new ArrayList<>());

        returnVal = jfcOutputDir.showOpenDialog(null);

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputDir = jfcOutputDir.getSelectedFile();
        }

        productNames = CreateDDL.getProducts();

        if ((parseFile != null || saveFile != null) && outputDir != null) {
            jbDTCreateDDL.setEnabled(true);
            jbDRCreateDDL.setEnabled(true);
        }

    }

    public static String displayProductNames() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < productNames.length; i++) {
            sb.append(productNames[i] + "\n");
        }
        return sb.toString();
    }

    private String getSQLStatements() {
        String strSQLString = "";
        String response = (String) JOptionPane.showInputDialog(
                null,
                "Select a product:",
                "Create DDL",
                JOptionPane.PLAIN_MESSAGE,
                null,
                productNames,
                null);

        if (response == null) {
            return Constants.CANCELLED;
        }

        int selected;
        for (selected = 0; selected < productNames.length; selected++) {
            if (response.equals(productNames[selected])) {
                break;
            }
        }

        try {

            EdgeConvertGUI.setSaveFile(new File("././resources/test.sql"));
            saveFile = EdgeConvertGUI.getSaveFile();
            
           /* if (selected == 0) {
                convertCreateDDL = new CreateDDLMySQL(EdgeConvertGUI.getTables(), EdgeConvertGUI.getFields());
            } else if (selected == 1) {
                convertCreateDDL = new CreateDDLOracle(EdgeConvertGUI.getTables(), EdgeConvertGUI.getFields());
            }*/

            convertCreateDDL = DDLGeneratorFactory.get(String.valueOf(selected));
convertCreateDDL.setTables(EdgeConvertGUI.getTables());
convertCreateDDL.setFields(EdgeConvertGUI.getFields());
convertCreateDDL.initialize();

            strSQLString = convertCreateDDL.getSQLString();
            databaseName = convertCreateDDL.getDatabaseName();
        } catch (Exception e) {
            logger.fatal(e);
        }

        return strSQLString;
    }

    private void writeSQL(String output) {
        databaseName = convertCreateDDL.getDatabaseName();
        System.out.println("Database name: " + databaseName);

        jfcEdge.resetChoosableFileFilters();
        if (parseFile != null) {
            EdgeConvertGUI.setOutputFile(new File(parseFile.getAbsolutePath().substring(0,
                    (parseFile.getAbsolutePath().lastIndexOf(File.separator) + 1)) + databaseName + ".sql"));
        } else {
            EdgeConvertGUI.setOutputFile(new File(saveFile.getAbsolutePath().substring(0,
                    (saveFile.getAbsolutePath().lastIndexOf(File.separator) + 1)) + databaseName + ".sql"));
        }
        if (databaseName.equals("")) {
            return;
        }
        jfcEdge.setSelectedFile(EdgeConvertGUI.getOutputFile());
        int returnVal = jfcEdge.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            EdgeConvertGUI.setOutputFile(jfcEdge.getSelectedFile());
            if (EdgeConvertGUI.getOutputFile().exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(EdgeConvertGUI.getOutputFile(), false)));
                // write the SQL statements
                pw.println(output);
                // close the file
                pw.close();
            } catch (IOException ioe) {
                logger.fatal(ioe);
            }
        }
    }

}
