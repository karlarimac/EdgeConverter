package rit.g3.parser;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import rit.g3.model.Connector;
import rit.g3.model.Field;
import rit.g3.model.Table;
import rit.g3.screen.DefineRelationScreen;

public abstract class FileParser {

   protected Table[] tables;
   protected Field[] fields;
   protected Connector[] connectors;
   protected File parseFile;

   protected ArrayList<Table> alTables;
   protected ArrayList<Field> alFields;
   protected ArrayList<Connector> alConnectors;

   public static final String DELIM = "|";

   public abstract void parse(File file);

   public abstract void analyze() throws Exception;

   public Table[] getTables() {
      return tables;
   }

   public Field[] getFields() {
      return fields;
   }

   public void resolveConnectors() { // Identify nature of Connector endpoints
      int endPoint1;
      int endPoint2;
      int fieldIndex = 0;
      int table1Index = 0;
      int table2Index = 0;
      for (int cIndex = 0; cIndex < connectors.length; cIndex++) {
         endPoint1 = connectors[cIndex].getEndPoint1();
         endPoint2 = connectors[cIndex].getEndPoint2();
         fieldIndex = -1;
         for (int fIndex = 0; fIndex < fields.length; fIndex++) { // search fields array for endpoints
            if (endPoint1 == fields[fIndex].getNumFigure()) { // found endPoint1 in fields array
               connectors[cIndex].setIsEP1Field(true); // set appropriate flag
               fieldIndex = fIndex; // identify which element of the fields array that endPoint1 was found in
            }
            if (endPoint2 == fields[fIndex].getNumFigure()) { // found endPoint2 in fields array
               connectors[cIndex].setIsEP2Field(true); // set appropriate flag
               fieldIndex = fIndex; // identify which element of the fields array that endPoint2 was found in
            }
         }
         for (int tIndex = 0; tIndex < tables.length; tIndex++) { // search tables array for endpoints
            if (endPoint1 == tables[tIndex].getNumFigure()) { // found endPoint1 in tables array
               connectors[cIndex].setIsEP1Table(true); // set appropriate flag
               table1Index = tIndex; // identify which element of the tables array that endPoint1 was found in
            }
            if (endPoint2 == tables[tIndex].getNumFigure()) { // found endPoint1 in tables array
               connectors[cIndex].setIsEP2Table(true); // set appropriate flag
               table2Index = tIndex; // identify which element of the tables array that endPoint2 was found in
            }
         }

         if (connectors[cIndex].getIsEP1Field() && connectors[cIndex].getIsEP2Field()) { // both endpoints are fields,
                                                                                         // implies lack of
                                                                                         // normalization
            JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile
                  + "\ncontains composite attributes. Please resolve them and try again.");
            DefineRelationScreen.setReadSuccess(false); // this tells GUI not to populate JList components
            break; // stop processing list of Connectors
         }

         if (connectors[cIndex].getIsEP1Table() && connectors[cIndex].getIsEP2Table()) { // both endpoints are tables
            if ((connectors[cIndex].getEndStyle1().indexOf("many") >= 0) &&
                  (connectors[cIndex].getEndStyle2().indexOf("many") >= 0)) { // the connector represents a many-many
                                                                              // relationship, implies lack of
                                                                              // normalization
               JOptionPane.showMessageDialog(null,
                     "There is a many-many relationship between tables\n\"" + tables[table1Index].getName()
                           + "\" and \"" + tables[table2Index].getName() + "\""
                           + "\nPlease resolve this and try again.");
               DefineRelationScreen.setReadSuccess(false); // this tells GUI not to populate JList components
               break; // stop processing list of Connectors
            } else { // add Figure number to each table's list of related tables
               tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
               tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
               continue; // next Connector
            }
         }

         if (fieldIndex >= 0 && fields[fieldIndex].getTableID() == 0) { // field has not been assigned to a table yet
            if (connectors[cIndex].getIsEP1Table()) { // endpoint1 is the table
               tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); // add to the appropriate table's
                                                                                      // field list
               fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); // tell the field what table it
                                                                                  // belongs to
            } else { // endpoint2 is the table
               tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); // add to the appropriate table's
                                                                                      // field list
               fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); // tell the field what table it
                                                                                  // belongs to
            }
         } else if (fieldIndex >= 0) { // field has already been assigned to a table
            JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName()
                  + " is connected to multiple tables.\nPlease resolve this and try again.");
            DefineRelationScreen.setReadSuccess(false); // this tells GUI not to populate JList components
            break; // stop processing list of Connectors
         }
      }
   } // resolveConnectors()

}
