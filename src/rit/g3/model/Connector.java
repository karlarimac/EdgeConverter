package rit.g3.model;

import java.util.StringTokenizer;

import rit.g3.parser.EdgeConvertFileParser;

/*
 * instantization of all varables for the Connector class
 */

public class Connector {
   private int numConnector;
   private int endPoint1;
   private int endPoint2;
   private String endStyle1;
   private String endStyle2;
   private boolean isEP1Field;
   private boolean isEP2Field;
   private boolean isEP1Table;
   private boolean isEP2Table;
      
/*
 * Connector as a parametarized constructor, requiring inouted String
 * Within the Connector, a new StringTokenizer is initialized with the required paramater and a call for EdgeConverterParser
 * The number of Connectors parses the Integer to Int, using the nextToken
 * Each end point is parsed from Integer to Int
 * Each end style takes the next Token input by the String Tokenizer
 * Both Field and Table flags are set to false, meaning not set yet
 * 
 * @param String inputString
 */

   public Connector(String inputString) {
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      numConnector = Integer.parseInt(st.nextToken());
      endPoint1 = Integer.parseInt(st.nextToken());
      endPoint2 = Integer.parseInt(st.nextToken());
      endStyle1 = st.nextToken();
      endStyle2 = st.nextToken();
      isEP1Field = false;
      isEP2Field = false;
      isEP1Table = false;
      isEP2Table = false;
   }
   
 
   
   
   /** 
    * accessor that returns the number of connectors 
    * @return int
    */
   public int getNumConnector() {
      return numConnector;
   }
   
   
   /** 
    * accessor that returns the first end point in fields array for endpoints
    * @return int
    */
   public int getEndPoint1() {
      return endPoint1;
   }
   
   
   /** 
    * accessor that returns the second end point in fields array for endpoints
    * @return int
    */
   public int getEndPoint2() {
      return endPoint2;
   }
   
   
   /** 
    * string that returns the relationship within the connector, being many-to- many, showing lack of normalization
    * @return String
    */
   public String getEndStyle1() {
      return endStyle1;
   }
   
   
   /** 
    *  string that returns the relationship within the connector, being many-to- many, showing lack of normalization
    * @return String
    */
   public String getEndStyle2() {
      return endStyle2;
   }
   
   /** 
    * accessor that returns that the first endpoint is field
    * @return boolean
    */
   public boolean getIsEP1Field() {
      return isEP1Field;
   }
   
   
   /** 
    * accessor that returns that the second endpoint is field
    * @return boolean
    */
   public boolean getIsEP2Field() {
      return isEP2Field;
   }

   
   /** 
    * accessor that returns that the first endpoint is table
    * @return boolean
    */
   public boolean getIsEP1Table() {
      return isEP1Table;
   }

   
   /** 
    * accessor that returns that the second endpoint is table
    * @return boolean
    */
   public boolean getIsEP2Table() {
      return isEP2Table;
   }

   
   /** 
    * mutator that set the first endpoint as apporpirate flag with the given value to place in the end point array for the field
    * @param value
    */
   public void setIsEP1Field(boolean value) {
      isEP1Field = value;
   }
   
   
   /** 
    * mutator that set the second endpoint as apporpirate flag with the given value to place in the end point array for the field
    * @param value
    */
   public void setIsEP2Field(boolean value) {
      isEP2Field = value;
   }

   
   /** 
    * mutator that set the second endpoint as apporpirate flag with the given value to place in the end point array for the table
    * @param value
    */
   public void setIsEP1Table(boolean value) {
      isEP1Table = value;
   }

   
   /** 
    * mutator that set the second endpoint as apporpirate flag with the given value to place in the end point array for the table
    * @param value
    */
   public void setIsEP2Table(boolean value) {
      isEP2Table = value;
   }
}


