package rit.g3.ddl;

import rit.g3.model.Field;
import rit.g3.model.Table;

public abstract class CreateDDL {

   protected Table[] tables; // master copy of Table object
   protected Field[] fields; // master copy of Field object
   protected int[] numBoundTables;
   protected int maxBound;
   protected int selected;

   protected static final String[] products = { "MySQL", "Oracle" };
   protected StringBuilder sb;

   public static String[] getProducts() {
      return products.clone();
   }

      protected abstract String[] getStringDataTypes();

   protected CreateDDL(Table[] tables, Field[] fields) {
      this.tables = tables;
      this.fields = fields;
      initialize();
   }

   public void setTables(Table[] tables){
      this.tables = tables;
   }

   public void setFields(Field[] fields){
      this.fields = fields;
   }

   protected CreateDDL() { // default constructor with empty arg list for to allow output dir to be set
                                  // before there are table and field object

   }

   public void initialize() {
      numBoundTables = new int[tables.length];
      maxBound = 0;

      for (int i = 0; i < tables.length; i++) { // step through list of tables
         int numBound = 0; // initialize counter for number of bound tables
         int[] relatedFields = tables[i].getRelatedFieldsArray();
         for (int j = 0; j < relatedFields.length; j++) { // step through related fields list
            if (relatedFields[j] != 0) {
               numBound++; // count the number of non-zero related fields
            }
         }
         numBoundTables[i] = numBound;
         if (numBound > maxBound) {
            maxBound = numBound;
         }
      }
   }

   protected Table getTable(int numFigure) {
      for (int tIndex = 0; tIndex < tables.length; tIndex++) {
         if (numFigure == tables[tIndex].getNumFigure()) {
            return tables[tIndex];
         }
      }
      return null;
   }

   protected Field getField(int numFigure) {
      for (int fIndex = 0; fIndex < fields.length; fIndex++) {
         if (numFigure == fields[fIndex].getNumFigure()) {
            return fields[fIndex];
         }
      }
      return null;
   }

   protected void hasForeignKey(int tableCount, int[] nativeFields, int[] relatedFields, int numForeignKey) {
      if (numForeignKey > 0) { // table has foreign keys
         int currentFK = 1;
         setForeignKey(tableCount, nativeFields, relatedFields, numForeignKey, currentFK);
         sb.append("\r\n");
      }
   }

   protected void hasPrimaryKey(int tableCount, int[] nativeFields, boolean[] primaryKey, int numPrimaryKey,
         int numForeignKey) {
      if (numPrimaryKey > 0) { // table has primary key(s)
         sb.append("CONSTRAINT " + tables[tableCount].getName() + "_PK PRIMARY KEY (");
         setPrimaryKey(nativeFields, primaryKey, numPrimaryKey, numForeignKey);
         sb.append("\r\n");
      }
   }

   protected void setForeignKey(int tableCount, int[] nativeFields, int[] relatedFields, int numForeignKey,
         int currentFK) {
      for (int i = 0; i < relatedFields.length; i++) {
         if (relatedFields[i] != 0) {
            sb.append("CONSTRAINT " + tables[tableCount].getName() + "_FK" + currentFK +
                  " FOREIGN KEY(" + getField(nativeFields[i]).getName() + ") REFERENCES " +
                  getTable(getField(nativeFields[i]).getTableBound()).getName() + "("
                  + getField(relatedFields[i]).getName() + ")");
            if (currentFK < numForeignKey) {
               sb.append(",\r\n");
            }
            currentFK++;
         }
      }
   }

   protected void setPrimaryKey(int[] nativeFields, boolean[] primaryKey, int numPrimaryKey, int numForeignKey) {
      for (int i = 0; i < primaryKey.length; i++) {
         if (primaryKey[i]) {
            sb.append(getField(nativeFields[i]).getName());
            numPrimaryKey--;
            if (numPrimaryKey > 0) {
               sb.append(", ");
            }
         }
      }
      sb.append(")");
      if (numForeignKey > 0) {
         sb.append(",");
      }
   }

   protected void createTable(int tableCount) {
      sb.append("CREATE TABLE " + tables[tableCount].getName() + " (\r\n");
      int[] nativeFields = tables[tableCount].getNativeFieldsArray();
      int[] relatedFields = tables[tableCount].getRelatedFieldsArray();
      boolean[] primaryKey = new boolean[nativeFields.length];
      int numPrimaryKey = 0;
      int numForeignKey = 0;
      for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { // print out the
                                                                                                   // fields
         Field currentField = getField(nativeFields[nativeFieldCount]);
         sb.append("\t" + currentField.getName() + " " + getStringDataTypes()[currentField.getDataType()]);
         if (currentField.getDataType() == 0) { // varchar
            sb.append("(" + currentField.getVarcharValue() + ")"); // append varchar length in () if data type is
                                                                   // varchar
         }
         if (currentField.getDisallowNull()) {
            sb.append(" NOT NULL");
         }
         defineDataType(currentField);
         numPrimaryKey = findPrimaryKey(primaryKey, numPrimaryKey, nativeFieldCount, currentField);
         numForeignKey = findForeignKey(numForeignKey, currentField);
         sb.append(",\r\n"); // end of field
      }
      hasPrimaryKey(tableCount, nativeFields, primaryKey, numPrimaryKey, numForeignKey);
      hasForeignKey(tableCount, nativeFields, relatedFields, numForeignKey);
      sb.append(");\r\n\r\n"); // end of table
   }

   protected void defineDataType(Field currentField) {
      if (!currentField.getDefaultValue().equals("")) {
         if (currentField.getDataType() == 1) { // boolean data type
            sb.append(" DEFAULT " + convertStrBooleanToInt(currentField.getDefaultValue()));
         } else { // any other data type
            sb.append(" DEFAULT " + currentField.getDefaultValue());
         }
      }
   }

   protected int findForeignKey(int numForeignKey, Field currentField) {
      if (currentField.getFieldBound() != 0) {
         numForeignKey++;
      }
      return numForeignKey;
   }

   protected int findPrimaryKey(boolean[] primaryKey, int numPrimaryKey, int nativeFieldCount, Field currentField) {
      if (currentField.getIsPrimaryKey()) {
         primaryKey[nativeFieldCount] = true;
         numPrimaryKey++;
      } else {
         primaryKey[nativeFieldCount] = false;
      }
      return numPrimaryKey;
   }

   protected int convertStrBooleanToInt(String input) { // MySQL uses '1' and '0' for boolean types
      if (input.equals("true")) {
         return 1;
      } else {
         return 0;
      }
   }

   public abstract String getDatabaseName();

   public abstract String getProductName();

   public abstract String getSQLString();

   public abstract void createDDL();

}// EdgeConvertCreateDDL
