package rit.g3.listener;

import static rit.g3.screen.DefineTableScreen.*;

import java.awt.event.*;

import javax.swing.JRadioButton;

import rit.g3.edgeconverter.EdgeConvertGUI;
import rit.g3.model.Field;
import rit.g3.screen.DefineTableScreen;
import rit.g3.screen.DefineRelationScreen;

public class EdgeRadioButtonListener implements ActionListener {

    private final DefineTableScreen defaultTableScreen;

    public EdgeRadioButtonListener(DefineTableScreen defaultTableScreen){
this.defaultTableScreen = defaultTableScreen;
    }

    public void actionPerformed(ActionEvent ae) {
        JRadioButton[] jrbDataType = defaultTableScreen.getJrbDataType();
        for (int i = 0; i < jrbDataType.length; i++) {
            if (jrbDataType[i].isSelected()) {
                DefineRelationScreen.getCurrentDTField().setDataType(i);
                break;
            }
        }
        if (jrbDataType[0].isSelected()) {
            jtfDTVarchar.setText(Integer.toString(Field.VARCHAR_DEFAULT_LENGTH));
            jbDTVarchar.setEnabled(true);
        } else {
            jtfDTVarchar.setText("");
            jbDTVarchar.setEnabled(false);
        }
        jtfDTDefaultValue.setText("");
        DefineRelationScreen.getCurrentDTField().setDefaultValue("");
        EdgeConvertGUI.setDataSaved(false) ;
    }
}
