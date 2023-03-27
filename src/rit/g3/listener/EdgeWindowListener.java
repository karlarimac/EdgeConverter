package rit.g3.listener;

import java.awt.event.*;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rit.g3.edgeconverter.EdgeConvertGUI;

import static rit.g3.listener.CreateDDLButtonListener.saveFile;
import static rit.g3.screen.DefineTableScreen.jfDT;
import static rit.g3.edgeconverter.EdgeConvertGUI.*;
import static rit.g3.screen.DefineRelationScreen.jfDR;

/**
 * Checks the window status and logs the information.
 * Uppon closing the window, windowClosing method checks for saved data
 */
public class EdgeWindowListener implements WindowListener {

    /**
     * Logger which is used to log information
     */
    private static final Logger logger = LogManager.getLogger(EdgeWindowListener.class);

    /**
     * Logs the info of when the window activates
     * 
     * @param we Checks for the window event
     */
    public void windowActivated(WindowEvent we) {
        logger.info("Window activated");
    }

    /**
     * Logs the info of when the window is closed
     * 
     * @param we Checks for the window event
     */
    public void windowClosed(WindowEvent we) {
        logger.info("Window closed");
    }

    /**
     * Logs the info of when the window is deactivated
     * 
     * @param we Checks for the window event
     */
    public void windowDeactivated(WindowEvent we) {
        logger.info("Window deactivated");
    }

    /**
     * Logs the info of when the window deinconifiez
     * 
     * @param we Checks for the window event
     */
    public void windowDeiconified(WindowEvent we) {
        logger.info("Window deinconified");
    }

    /**
     * Logs the info of when the window iconifiez
     * 
     * @param we Checks for the window event
     */
    public void windowIconified(WindowEvent we) {
        logger.info("Window iconified");
    }

    /**
     * Logs the info of when the window opens
     * 
     * @param we Checks for the window event
     */
    public void windowOpened(WindowEvent we) {
        logger.info("Window open");
    }

    /**
     * Upon closing the window the method checks if the data is saved.
     * If the data is saved, the window will close. If the data is not saved the method will
     * inform the user that the data is not saved, and will provide the user with a choice
     * asking if the user would like to save the data. If the user chooses YES, the data will
     * be saved and the window will close. If the user chooses CANCEL or CLOSE, the data will
     * not be saved and the window will close.
     * 
     * @param we Checks for the window event
     */
    public void windowClosing(WindowEvent we) {
        if (!EdgeConvertGUI.isDataSaved()) {
            int answer = JOptionPane.showOptionDialog(null,
                    "You currently have unsaved data. Would you like to save?",
                    "Are you sure?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            if (answer == JOptionPane.YES_OPTION) {
                if (saveFile == null) {
                    saveAs();
                }
                writeSave();
            }
            if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                if (we.getSource() == jfDT) {
                    jfDT.setVisible(true);
                }
                if (we.getSource() == jfDR) {
                    jfDR.setVisible(true);
                }
                return;
            }
        }
        System.exit(0); //No was selected
    }
}