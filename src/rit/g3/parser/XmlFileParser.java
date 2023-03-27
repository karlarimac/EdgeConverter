package rit.g3.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rit.g3.edgeconverter.DataType;
import rit.g3.model.Connector;
import rit.g3.model.Field;
import rit.g3.model.Table;

public class XmlFileParser extends FileParser {

    private static final Logger logger = LogManager.getLogger(XmlFileParser.class);

    private static final String NUM_FIG = "numFigure";

    private final SecureRandom random;

    public XmlFileParser() {
        random = new SecureRandom();
        alTables = new ArrayList<Table>();
        alFields = new ArrayList<Field>();
        alConnectors = new ArrayList<Connector>();
    }

    @Override
    public void parse(File file) {
        this.parseFile = file;

        try {
            analyze();
        } catch (Exception e) {
            logger.fatal(e);
        }

    }

    @Override
    public void analyze() throws Exception {
        // an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        // an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(parseFile);
        doc.getDocumentElement().normalize();

        // get tables
        NodeList tableList = doc.getElementsByTagName("table");
        for (int i = 0; i < tableList.getLength(); i++) {
            Node node = tableList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element tableElement = (Element) node;

                // get table name
                String tableName = tableElement.getElementsByTagName("name").item(0).getTextContent();

                // get number figure
                String numFigure = String.valueOf(random.nextInt());

                // add table to list
                alTables.add(new Table(numFigure + "|" + tableName));

                // get fields Node
                NodeList columnsNodeList = tableElement.getElementsByTagName("fields");

                Node columnsNode = columnsNodeList.item(0);

                resolveNodes(node, numFigure, columnsNode);

            }
        }

        // fet relations
        NodeList relationList = doc.getElementsByTagName("relation");
        for (int i = 0; i < relationList.getLength(); i++) {
            Node node = relationList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element relationElement = (Element) node;

                // get relation name
                relationElement.getElementsByTagName("name").item(0).getTextContent();

                // get parent element
                Element parentElement = (Element) relationElement.getElementsByTagName("parent").item(0);
                parentElement.getElementsByTagName("tablename").item(0).getTextContent();

                // get child element
                Element childElement = (Element) relationElement.getElementsByTagName("child").item(0);
                childElement.getElementsByTagName("tablename").item(0).getTextContent();
                childElement.getElementsByTagName("foreignkey").item(0).getTextContent();

            }
        }

        tables = alTables.toArray(new Table[0]);
        fields = alFields.toArray(new Field[0]);
        connectors = alConnectors.toArray(new Connector[0]);

        resolveConnectors();
    }

    private void resolveNodes(Node node, String numFigure, Node columnsNode) {
        if (columnsNode.getNodeType() == Node.ELEMENT_NODE) {
            Element columnsElement = (Element) node;

            NodeList columnNodeList = columnsElement.getElementsByTagName("field");
            for (int j = 0; j < columnNodeList.getLength(); j++) {

                Node columnNode = columnNodeList.item(j);

                Element columnElement = (Element) columnNode;

                String parentNode = columnElement.getParentNode().getNodeName();
                String columnName = columnElement.getTextContent().trim();

                String columnNumber = String.valueOf(random.nextInt());

                // fields - only the ones inside columns element
                if (parentNode.equals("fields")) {
                    String columnType = columnElement.getAttribute("type");

                    // add field to list
                    Field edgeField = new Field(columnNumber + "|" + columnName);
                    edgeField.setDataType(DataType.valueOf(columnType.toUpperCase()).getValue());

                    if (columnElement.getAttribute("pkey").equals("true")) {
                        edgeField.setIsPrimaryKey(true);
                    }

                    alFields.add(edgeField);
                    int randomNumber = random.nextInt(10);
                    alConnectors.add(new Connector(randomNumber + DELIM + numFigure + DELIM
                            + Integer.parseInt(columnNumber) + DELIM + null
                            + DELIM + null));
                }
            }

        }
    }

}