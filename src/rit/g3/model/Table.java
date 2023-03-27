package rit.g3.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

import rit.g3.parser.EdgeConvertFileParser;

/**
 * The {@code Table} class represents a Figure with Style <i>Entity</i> from the
 * {@code EdgeDiagram}, and stores information about the database tables, like
 * the tables and the fields that it is related to.
 * 
 * @author Kristina Marasovic, G3
 * @see java.util.AbstractCollection
 * @see java.util.AbstractList
 * @see java.util.ArrayList
 * @see java.util.StringTokenizer
 * @since 1.0
 */
public class Table {

   /**
    * Unique identifier from the diagram, its value is set by the constructor
    */
   private int numFigure;

   /**
    * Name of the field, its value is set by the constructor
    */
   private String name;

   /**
    * Stores the list of the tables connected to this table (before parsing is
    * complete)
    */
   private ArrayList<Integer> alRelatedTables;

   /**
    * Stores the list of the native fields for this table (before parsing is
    * complete)
    */
   private ArrayList<Integer> alNativeFields;

   /**
    * Stores the list of the tables connected to this table (after parsing is
    * complete)
    */
   private int[] relatedTables;

   /**
    * Stores the list of the fields (one for each element of nativeFields) that the
    * native fields are bound to
    */
   private int[] relatedFields;

   /**
    * Stores the list of the fields that belong to this table (after parsing is
    * complete)
    */
   private int[] nativeFields;

   /**
    * Takes a single String as an argument, which is passed to a StringTokenizer
    * object and parsed according to delimiter constant FileParser.DELIM
    * into numFigure and name
    * 
    * @param inputString the name of the table
    */
   public Table(String inputString) {
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      numFigure = Integer.parseInt(st.nextToken());
      name = st.nextToken();
      alRelatedTables = new ArrayList<>();
      alNativeFields = new ArrayList<>();
   }

   /**
    * Returns the name of the field whose value is set by constructor
    * 
    * @return {@code String} - the name of the field
    */
   public String getName() {
      return name;
   }

   /**
    * Returns the unique identifier from the diagram whose value is set by the
    * constructor
    * 
    * @return {@code int} - unique identifier from the diagram
    */
   public int getNumFigure() {
      return numFigure;
   }

   /**
    * Adds Integer wrapper of relatedTable to the list of the related tables
    * 
    * @param relatedTable number of tables connected to this table
    */
   public void addRelatedTable(int relatedTable) {
      alRelatedTables.add((relatedTable));
   }

   /**
    * Returns the list of connected tables after parsing is completed
    * 
    * @return {@code int[]} - list of the tables that are connected to this table
    *         (after
    *         parsing is complete)
    */
   public int[] getRelatedTablesArray() {
      return relatedTables;
   }

   /**
    * Returns the list of the fields that the native fields are bound to
    * 
    * @return {@code int[]} - the list of the fields (one for each element of
    *         nativeFields)
    *         that the native fields are bound to
    */
   public int[] getRelatedFieldsArray() {
      return relatedFields;
   }

   /**
    * This is the method that is called when the <i>Bind Relation</i> button is
    * pressed; it stores the figure number of the foreign field that is bound to
    * the native field referenced by index
    * 
    * @param index        reference number; numerical value of the position in the
    *                     array
    * @param relatedValue figure number of the foreign field that is bound to the
    *                     native field referenced by the index
    */
   public void setRelatedField(int index, int relatedValue) {
      relatedFields[index] = relatedValue;
   }

   /**
    * Returns the list of the fields that belong to the table after parsing is
    * completed
    * 
    * @return {@code int[]} - the list of the fields that belong to this table
    *         (after
    *         parsing is complete)
    */
   public int[] getNativeFieldsArray() {
      return nativeFields;
   }

   /**
    * Adds Integer wrapper of value to alNativeFields
    * 
    * @param value an integer wrapper to be added to alNativeFields
    */
   public void addNativeField(int value) {
      alNativeFields.add((value));
   }

   /**
    * Moves nativeField[index] closer to the beginning of the array by one position
    * 
    * @param index numerical value of the position in the array
    */
   public void moveFieldUp(int index) { // move the field closer to the beginning of the list
      if (index == 0) {
         return;
      }
      int tempNative = nativeFields[index - 1]; // save element at destination index
      nativeFields[index - 1] = nativeFields[index]; // copy target element to destination
      nativeFields[index] = tempNative; // copy saved element to target's original location
      int tempRelated = relatedFields[index - 1]; // save element at destination index
      relatedFields[index - 1] = relatedFields[index]; // copy target element to destination
      relatedFields[index] = tempRelated; // copy saved element to target's original location
   }

   /**
    * Moves nativeField[index] closer to the end of the array by one position
    * 
    * @param index numerical value of the position in the array
    */
   public void moveFieldDown(int index) { // move the field closer to the end of the list
      if (index == (nativeFields.length - 1)) {
         return;
      }
      int tempNative = nativeFields[index + 1]; // save element at destination index
      nativeFields[index + 1] = nativeFields[index]; // copy target element to destination
      nativeFields[index] = tempNative; // copy saved element to target's original location
      int tempRelated = relatedFields[index + 1]; // save element at destination index
      relatedFields[index + 1] = relatedFields[index]; // copy target element to destination
      relatedFields[index] = tempRelated; // copy saved element to target's original location
   }

   /**
    * Converts the ArrayLists into int[] and initializes the relatedFields[]
    * 
    */
   public void makeArrays() { // convert the ArrayLists into int[]
      Integer[] temp;
      temp = alNativeFields.toArray(new Integer[alNativeFields.size()]);
      nativeFields = new int[temp.length];
      for (int i = 0; i < temp.length; i++) {
         nativeFields[i] = temp[i].intValue();
      }

      temp = alRelatedTables.toArray(new Integer[alRelatedTables.size()]);
      relatedTables = new int[temp.length];
      for (int i = 0; i < temp.length; i++) {
         relatedTables[i] = temp[i].intValue();
      }

      relatedFields = new int[nativeFields.length];
      for (int i = 0; i < relatedFields.length; i++) {
         relatedFields[i] = 0;
      }
   }

   /**
    * Returns a String using the template below; this is used to write to the
    * application's save file for later retrieval and object recreation, using this
    * template:
    * Table: <numFigure>
    * {
    * TableName: <name>
    * NativeFields: <elements of nativeFields[], separated by
    * FileParser.DELIM>
    * RelatedTables: <elements of relatedTables[], separated by
    * FileParser.DELIM>
    * RelatedFields: <elements of relatedFields[], separated by
    * FileParser.DELIM>
    * }
    * 
    * @return {@code String} in the form of the template
    * 
    */
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Table: " + numFigure + "\r\n");
      sb.append("{\r\n");
      sb.append("TableName: " + name + "\r\n");
      sb.append("NativeFields: ");
      for (int i = 0; i < nativeFields.length; i++) {
         sb.append(nativeFields[i]);
         if (i < (nativeFields.length - 1)) {
            sb.append(EdgeConvertFileParser.DELIM);
         }
      }
      sb.append("\r\nRelatedTables: ");
      for (int i = 0; i < relatedTables.length; i++) {
         sb.append(relatedTables[i]);
         if (i < (relatedTables.length - 1)) {
            sb.append(EdgeConvertFileParser.DELIM);
         }
      }
      sb.append("\r\nRelatedFields: ");
      for (int i = 0; i < relatedFields.length; i++) {
         sb.append(relatedFields[i]);
         if (i < (relatedFields.length - 1)) {
            sb.append(EdgeConvertFileParser.DELIM);
         }
      }
      sb.append("\r\n}\r\n");

      return sb.toString();
   }
}
