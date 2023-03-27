package rit.g3.listener;

import java.awt.event.*;
import javax.swing.*;

import java.io.*;

import rit.g3.parser.EdgeConvertFileParser;
import rit.g3.edgeconverter.EdgeConvertGUI;
import rit.g3.parser.FileParserFactory;
import rit.g3.parser.SaveFileParser;
import rit.g3.parser.XmlFileParser;

import static rit.g3.screen.DefineRelationScreen.*;
import static rit.g3.utils.Constants.*;

import static rit.g3.listener.CreateDDLButtonListener.saveFile;
import static rit.g3.screen.DefineTableScreen.*;
import static rit.g3.edgeconverter.EdgeConvertGUI.*;
import static rit.g3.screen.DefineRelationScreen.jfDR;

public class EdgeMenuListener implements ActionListener {

    public void actionPerformed(ActionEvent ae) {
        int returnVal;
        if ((ae.getSource() == jmiDTOpenEdge) || (ae.getSource() == jmiDROpenEdge)) {
            if (!EdgeConvertGUI.isDataSaved()) {
                int answer = JOptionPane.showConfirmDialog(null, "You currently have unsaved data. Continue?",
                        "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            jfcEdge.addChoosableFileFilter(effEdge);
            returnVal = jfcEdge.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                readFile();
            } else {
                return;
            }
            EdgeConvertGUI.setDataSaved(true);
        }

        save(ae);

        if ((ae.getSource() == jmiDTExit) || (ae.getSource() == jmiDRExit)) {
            if (!EdgeConvertGUI.isDataSaved()) {
                int answer = JOptionPane.showOptionDialog(null,
                        "You currently have unsaved data. Would you like to save?",
                        "Are you sure?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (answer == JOptionPane.YES_OPTION || saveFile == null) {
                    saveAs();
                }
                if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                    return;
                }
            }
            System.exit(0); // No was selected
        }

        showAboutInfo(ae);
    } // EdgeMenuListener.actionPerformed()

    private void save(ActionEvent ae) {
        if ((ae.getSource() == jmiDTSaveAs) || (ae.getSource() == jmiDRSaveAs)
                || (ae.getSource() == jmiDTSave) || (ae.getSource() == jmiDRSave)) {
            if ((ae.getSource() == jmiDTSaveAs) || (ae.getSource() == jmiDRSaveAs)) {
                saveAs();
            } else {
                writeSave();
            }
        }
    }

    private void showAboutInfo(ActionEvent ae) {
        if ((ae.getSource() == jmiDTHelpAbout) || (ae.getSource() == jmiDRHelpAbout)) {
            JOptionPane.showMessageDialog(null, "EdgeConvert ERD To DDL Conversion Tool\n"
                    + "by Stephen A. Capperell\n"
                    + "© 2007-2008");
        }
    }

    private void readFile() {
        EdgeConvertGUI.setParseFile(jfcEdge.getSelectedFile()) ;

      /*   if (EdgeConvertGUI.getParseFile().getName().endsWith(".edg")) {
            EdgeConvertGUI.setEcfp(new EdgeConvertFileParser(getParseFile()));
        } else if (EdgeConvertGUI.getParseFile().getName().endsWith(".xml")) {
            EdgeConvertGUI.setEcfp(new XmlFileParser(getParseFile()));
        } else if (EdgeConvertGUI.getParseFile().getName().endsWith(".sav")) {
            EdgeConvertGUI.setEcfp(new SaveFileParser(getParseFile()));
        }*/

        // test.xml
        
        int index = EdgeConvertGUI.getParseFile().getName().lastIndexOf(".");
        String fileExtension = EdgeConvertGUI.getParseFile().getName().substring(index);

        EdgeConvertGUI.setEcfp(FileParserFactory.get(fileExtension));
        EdgeConvertGUI.getEcfp().parse(getParseFile());      

        EdgeConvertGUI.setTables(EdgeConvertGUI.getEcfp().getTables());
        for (int i = 0; i < EdgeConvertGUI.getTables().length; i++) {
            EdgeConvertGUI.getTables()[i].makeArrays();
        }

        EdgeConvertGUI.setFields(getEcfp().getFields());
        EdgeConvertGUI.setEcfp(null);
        populateLists();
        EdgeConvertGUI.setSaveFile(null);
        jmiDTSave.setEnabled(false);
        jmiDRSave.setEnabled(false);
        jmiDTSaveAs.setEnabled(true);
        jmiDRSaveAs.setEnabled(true);
        jbDTDefineRelations.setEnabled(true);

        jbDTCreateDDL.setEnabled(true);
        jbDRCreateDDL.setEnabled(true);

        EdgeConvertGUI.setTruncatedFilename(getParseFile().getName().substring(getParseFile().getName().lastIndexOf(File.separator) + 1));
        jfDT.setTitle(DEFINE_TABLES + " - " + EdgeConvertGUI.getTruncatedFilename());
        jfDR.setTitle(DEFINE_RELATIONS + " - " + EdgeConvertGUI.getTruncatedFilename());
    }
} // EdgeMenuListener
