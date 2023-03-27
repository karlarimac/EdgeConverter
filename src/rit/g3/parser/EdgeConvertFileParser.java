package rit.g3.parser;

import java.io.*;
import java.util.*;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rit.g3.model.Connector;
import rit.g3.model.Field;
import rit.g3.model.Table;
import rit.g3.screen.DefineRelationScreen;

public class EdgeConvertFileParser extends FileParser {

   private static final Logger logger = LogManager.getLogger(EdgeConvertFileParser.class);

   private BufferedReader br;
   private String currentLine;
   private boolean isEntity;
   private boolean isAttribute;
   private boolean isUnderlined = false;
   private int numFigure;
   private int numConnector;
   private int numLine;
   public static final String EDGE_ID = "EDGE Diagram File"; // first line of .edg files should be this
   public static final String SAVE_ID = "EdgeConvert Save File"; // first line of save files should be this

   public EdgeConvertFileParser() {
      numFigure = 0;
      numConnector = 0;
      alTables = new ArrayList<>();
      alFields = new ArrayList<>();
      alConnectors = new ArrayList<>();
      isEntity = false;
      isAttribute = false;
      numLine = 0;
   }

   @Override
   public void analyze() throws Exception {
      while ((currentLine = br.readLine()) != null) {
         currentLine = currentLine.trim();
         if (currentLine.startsWith("Figure ")) { // this is the start of a Figure entry
            numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); // get the Figure number
            currentLine = br.readLine().trim(); // this should be "{"
            currentLine = br.readLine().trim();
            currentLine = br.readLine().trim(); // this should be "SheetNumber 1"
            if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
               continue;
            } else {
               String style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); // get
                                                                                                                   // the
                                                                                                                   // Style
                                                                                                                   // parameter
               if (style.startsWith("Relation")) { // presence of Relations implies lack of normalization
                  JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile
                        + "\ncontains relations.  Please resolve them and try again.");
                  DefineRelationScreen.setReadSuccess(false);
                  break;
               }
               setEA(style);
               if (!(isEntity || isAttribute)) { // these are the only Figures we're interested in
                  continue;
               }
               currentLine = br.readLine().trim(); // this should be Text
               String text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""))
                     .replace(" ", ""); // get the Text parameter
               if (text.equals("")) {
                  JOptionPane.showMessageDialog(null,
                        "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
                  DefineRelationScreen.setReadSuccess(false);
                  break;
               }
               int escape = text.indexOf("\\");
               text = checkText(text, escape);

               if (isEntity) { // create a new Table object and add it to the alTables ArrayList
                  if (isTableDup(text)) {
                     JOptionPane.showMessageDialog(null, "There are multiple tables called " + text
                           + " in this diagram.\nPlease rename all but one of them and try again.");
                     DefineRelationScreen.setReadSuccess(false);
                     break;
                  }
                  alTables.add(new Table(numFigure + DELIM + text));
               }
               createFieldObject(text);
               // reset flags
               isEntity = false;
               isAttribute = false;
               isUnderlined = false;
            }
         }
         handleConnectors();
      }
   } // parseEdgeFile()

   private void handleConnectors() throws IOException {
      if (currentLine.startsWith("Connector ")) { // this is the start of a Connector entry
         int endPoint1;
         int endPoint2;
         String endStyle1;
         String endStyle2;

         numConnector = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); // get the Connector
                                                                                               // number
         currentLine = br.readLine().trim(); // this should be "{"
         currentLine = br.readLine().trim(); // not interested in SheetNumber 1
         currentLine = br.readLine().trim(); // not interested in Style
         currentLine = br.readLine().trim(); // Figure1
         endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
         currentLine = br.readLine().trim(); // Figure2
         endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
         currentLine = br.readLine().trim(); // not interested in EndPoint1
         currentLine = br.readLine().trim(); // not interested in EndPoint2
         currentLine = br.readLine().trim(); // not interested in SuppressEnd1
         currentLine = br.readLine().trim(); // not interested in SuppressEnd2
         currentLine = br.readLine().trim(); // End1
         endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); // get the
                                                                                                          // End1
                                                                                                          // parameter
         currentLine = br.readLine().trim(); // End2
         endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); // get the
                                                                                                          // End2
                                                                                                          // parameter
         do { // advance to end of record
            currentLine = br.readLine().trim();
         } while (!currentLine.equals("}")); // this is the end of a Connector entry

         alConnectors.add(new Connector(
               numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2));
      }
   }

   private void createFieldObject(String text) {
      if (isAttribute) { // create a new Field object and add it to the alFields ArrayList
         Field tempField = new Field(numFigure + DELIM + text);
         tempField.setIsPrimaryKey(isUnderlined);
         alFields.add(tempField);
      }
   }

   private String checkText(String text, int escape) throws IOException {
      if (escape > 0) { // Edge denotes a line break as "\line", disregard anything after a backslash
         text = text.substring(0, escape);
      }

      do { // advance to end of record, look for whether the text is underlined
         currentLine = br.readLine().trim();
         if (currentLine.startsWith("TypeUnderl")) {
            isUnderlined = true;
         }
      } while (!currentLine.equals("}")); // this is the end of a Figure entry
      return text;
   }

   private void setEA(String style) {
      if (style.startsWith("Entity")) {
         isEntity = true;
      }
      if (style.startsWith("Attribute")) {
         isAttribute = true;
      }
   }

   @Override
   public void parse(File file) {
      this.parseFile = file;
      try {
         FileReader fr = new FileReader(parseFile);
         br = new BufferedReader(fr);
         // test for what kind of file we have
         currentLine = br.readLine().trim();
         numLine++;
         if (currentLine.startsWith(EDGE_ID)) { // the file chosen is an Edge Diagrammer file
            this.analyze(); // parse the file
            br.close();
            this.makeArrays(); // convert ArrayList object into arrays of the appropriate Class type
            this.resolveConnectors(); // Identify nature of Connector endpoints
         } else {
            JOptionPane.showMessageDialog(null, "Unrecognized file format");

         }
      } // try
      catch (FileNotFoundException fnfe) {
         logger.fatal("Cannot find \"" + parseFile.getName() + "\".");
         System.exit(0);
      } // catch FileNotFoundException
      catch (Exception ioe) {
         logger.fatal(ioe);
         System.exit(0);
      } // catch IOException
   }

   private void makeArrays() { // convert ArrayList object into arrays of the appropriate Class type
      if (alTables != null) {
         tables = (Table[]) alTables.toArray(new Table[alTables.size()]);
      }
      if (alFields != null) {
         fields = (Field[]) alFields.toArray(new Field[alFields.size()]);
      }
      if (alConnectors != null) {
         connectors = (Connector[]) alConnectors.toArray(new Connector[alConnectors.size()]);
      }
   }

   private boolean isTableDup(String testTableName) {
      for (int i = 0; i < alTables.size(); i++) {
         Table tempTable = (Table) alTables.get(i);
         if (tempTable.getName().equals(testTableName)) {
            return true;
         }
      }
      return false;
   }

   public Table[] getEdgeTables() {
      return tables;
   }

   public Field[] getEdgeFields() {
      return fields;
   }

   public void openFile(File inputFile) throws UnsupportedOperationException {
      // empty for a reason
   }

} // EdgeConvertFileHandler
