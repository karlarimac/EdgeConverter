package rit.g3.model;

import java.util.StringTokenizer;

import rit.g3.parser.EdgeConvertFileParser;
import rit.g3.parser.FileParser;

/**
 * The {@code Field} class represents a Figure with Style <i>Entity</i> from the
 * {@code EdgeDiagram}, and stores information about the database fields
 * 
 * @author Kristina Marasovic, G3
 * @see java.util.StringTokenizer
 * @since 1.0
 */

public class Field {

   /**
    * Unique identifier from the diagram, its value is set by the constructor
    */
   private int numFigure;

   /**
    * Unique identifier of a table object
    */
   private int tableID;

   /**
    * Attribute which signifies the bound of table object
    */
   private int tableBound; 

   /**
    * Attribute which signifies the bound of field object
    */
   private int fieldBound;

   /**
    * Attribute which signifies the data type of field object
    */
   private int dataType;

   /**
    * Attribute which signifies the varchar vakue of field object
    */ 
   private int varcharValue;

   /**
    * Name of the field which value is set by the constructor
    */
   private String name;

   /**
    * Attribute which signifies the default value of a field object, which 
    * is set by the constructor
    */
   private String defaultValue;

   /**
    * Attribute which sets whether the disallow null is either true or false
    */
   private boolean disallowNull;

   /**
    * Unique identifier (primary key) of a field object which is set by the constructor
    */
   private boolean isPrimaryKey;

   /**
    * Stores the list of data types of a field object 
    */
   private static String[] strDataType = {"Varchar", "Boolean", "Integer", "Double"};

   /**
    * Attribute which sets the default value of a varchar of the field object
    * which is set by the constructor (VALUE = 1)
    */
   public static final int VARCHAR_DEFAULT_LENGTH = 1;
   
   /**
    * The constuctor of this class
    * Responsible for creating a Field object by taking a string input 
    * example of input "3|ID"
    * @param inputString the identify of the field (3|ID)
    */
   public Field(String inputString) {
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      numFigure = Integer.parseInt(st.nextToken());
      name = st.nextToken();
      tableID = 0;
      tableBound = 0;
      fieldBound = 0;
      disallowNull = false;
      isPrimaryKey = false;
      defaultValue = "";
      varcharValue = VARCHAR_DEFAULT_LENGTH;
      dataType = 0;
   }
   
   
   /** 
    * This method returns the number figure of a field
    * @return int
    */
   public int getNumFigure() {
      return numFigure;
   }
   
   
   /** 
    * This method is responsible for returning the name of the field
    * in the form of a String
    *
    * @return String
    */
   public String getName() {
      return name;
   }
   
   
   /** 
    * This method returns the ID of a table object as an int
    * @return int
    */
   public int getTableID() {
      return tableID;
   }
   
   
   /** 
    * This method sets a new ID to a table object
    * @param value the new ID number in form of an int
    */
   public void setTableID(int value) {
      tableID = value;
   }
   
   
   /** 
    * This method return the bound of a Table object as an int
    * @return int
    */
   public int getTableBound() {
      return tableBound;
   }
   
   
   /** 
    * This method sets the bound of a Table object
    * @param value the new bound value in the form of an int
    */
   public void setTableBound(int value) {
      tableBound = value;
   }

   
   /** 
    * This method return the bound of a Field object as an int
    * @return int
    */
   public int getFieldBound() {
      return fieldBound;
   }
   
   
   /** 
    * This method sets the bound of a Field object
    * @param value the new bound value in the form of an int
    */
   public void setFieldBound(int value) {
      fieldBound = value;
   }

   
   /** 
    * This method returns the dissallow null attribute of a Field object
    * as a boolean (true or false)
    * @return boolean
    */
   public boolean getDisallowNull() {
      return disallowNull;
   }
   
   
   /** 
    * This method sets the dissallow null attribute of a Field object
    * as a boolean (true or false)
    * @param value the boolean data type responsible for setting the dissallow null value
    */
   public void setDisallowNull(boolean value) {
      disallowNull = value;
   }
   
   
   /** 
    * This method gets the primary key of field object
    * @return boolean
    */
   public boolean getIsPrimaryKey() {
      return isPrimaryKey;
   }
   
   
   /** 
    * This method sets the primary key of field object
    * @param value the new boolean value for the primary key of field object
    */
   public void setIsPrimaryKey(boolean value) {
      isPrimaryKey = value;
   }
   
   
   /** 
    * This method gets the default value of field object
    * @return String
    */
   public String getDefaultValue() {
      return defaultValue;
   }
   
   
   /** 
    * This method sets the default value of field object
    * @param value a new deault value in the from of a String 
    */
   public void setDefaultValue(String value) {
      defaultValue = value;
   }
   
   
   /** 
    * This method gets the varchar value of field object (1)
    * @return int
    */
   public int getVarcharValue() {
      return varcharValue;
   }
   
   
   /** 
    * This method sets the varchar value of field object
    * @param value the new varchar value in the form of an int
    */
   public void setVarcharValue(int value) {
      if (value > 0) {
         varcharValue = value;
      }
   }
   
   /** 
    * This method gets the data type of field object
    * @return int
    */
   public int getDataType() {
      return dataType;
   }
   
   
   /** 
    * This method sets the data type of field object
    * @param value the new data type value in the form of an int
    */
   public void setDataType(int value) {
      if (value >= 0 && value < strDataType.length) {
         dataType = value;
      }
   }
   
   
   /** 
    * This method gets the string data type of field object as a
    * string array
    * @return String[]
    */
   public static String[] getStrDataType() {
      return strDataType;
   }
   
   
   /** 
    * This method is a toSting method which returns all the information
    * regarding the table and field object as a String
    * @return String
    */
   public String toString() {
      return numFigure + FileParser.DELIM +
      name + FileParser.DELIM +
      tableID + FileParser.DELIM +
      tableBound + FileParser.DELIM +
      fieldBound + FileParser.DELIM +
      dataType + FileParser.DELIM +
      varcharValue + FileParser.DELIM +
      isPrimaryKey + FileParser.DELIM +
      disallowNull + FileParser.DELIM +
      defaultValue;
   }
}

