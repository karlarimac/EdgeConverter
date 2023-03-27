package rit.g3.ddl;

import javax.swing.*;

import rit.g3.model.Field;
import rit.g3.model.Table;
import rit.g3.screen.DefineRelationScreen;

public class CreateDDLOracle extends CreateDDL {

   protected String databaseName;
   // this array is for determining how Oracle refers to datatypes
   protected String[] stringDataType = { "VARCHAR2", "NUMBER(1)", "NUMBER(p,s)", "FLOAT(24)" };

   @Override
   protected String[] getStringDataTypes() {
      return stringDataType;
   }

   public CreateDDLOracle() {
      //super(inputTables, inputFields);
      sb = new StringBuilder();
   } // CreateDDLMySQL(Table[], Field[])

   public void createDDL() {
      DefineRelationScreen.setReadSuccess(true);
      databaseName = generateDatabaseName();

      for (int boundCount = 0; boundCount <= maxBound; boundCount++) { // process tables in order from least dependent
                                                                       // (least number of bound tables) to most
                                                                       // dependent
         for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) { // step through list of tables
            if (numBoundTables[tableCount] == boundCount) { //
               createTable(tableCount);
            }
         }
      }
   }

   public String generateDatabaseName() { // prompts user for database name
      String dbNameDefault = "OracleDB";

      do {
         databaseName = (String) JOptionPane.showInputDialog(
               null,
               "Enter the database name:",
               "Database Name",
               JOptionPane.PLAIN_MESSAGE,
               null,
               null,
               dbNameDefault);
         if (databaseName == null) {
            DefineRelationScreen.setReadSuccess(false);

            return "";
         }
         if (databaseName.equals("")) {
            JOptionPane.showMessageDialog(null, "You must select a name for your database.");
         }
      } while (databaseName.equals(""));
      return databaseName;
   }

   public String getDatabaseName() {
      return databaseName;
   }

   public String getProductName() {
      return "Oracle";
   }

   public String getSQLString() {
      createDDL();
      return sb.toString();
   }

}// EdgeConvertCreateDDL
