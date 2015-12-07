package com.techmaster.hunter.xml;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterUtility;


public class XMLServiceImpl implements XMLService{
	
	private static final Logger log = Logger.getLogger(XMLServiceImpl.class);
	
	public XMLServiceImpl(XMLTree xmlTree) throws HunterRunTimeException{ 
		super();
		if(xmlTree.getDoc() == null){
			throw new HunterRunTimeException("XML Document not set for the xml tree");
		}
		this.xmlTree = xmlTree;
	}
	
	private XMLTree xmlTree = null;
	private XPath xpath = XPathFactory.newInstance().newXPath();
	
	public XMLTree getXmlTree() {
		return xmlTree;
	}
	public void setXmlTree(XMLTree xmlTree) throws HunterRunTimeException {
		if(xmlTree.getDoc() == null){
			throw new HunterRunTimeException("XML Document not set for the xml tree");
		}
		this.xmlTree = xmlTree;
	}

	@Override
	public String getElementVal(String xPath) {
		String val = null;
		try {
			val = xpath.compile(xPath).evaluate(this.xmlTree.getDoc());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return val;
	}

	@Override
	public void transform(Document xml, Document xsl) {
		
	}

	@Override
	public void transform(String xmlLoc, String xslLoc) {
		
	}

	@Override
	public void addAttribute(Element element, String name, String value) {
		element.setAttribute(name, value); 
	}
	
	@Override
	public String getTextValue(String xPath) {
		try {
			return xpath.compile(xPath).evaluate(this.xmlTree.getDoc());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public Node getTheFirstElement() throws XPathExpressionException {
		return getNodeList().item(0);
	}
	@Override
	public NodeList getNodeList() {
		Document xmlTreeDoc = this.xmlTree.getDoc();
		Element root = xmlTreeDoc.getDocumentElement();
		NodeList nodeList =  root.getChildNodes();
		if(nodeList.getLength() < 1)
			log.info(">>>>>>>>>>>> node list is empty"); 
		return nodeList;
	}
	
	/**
	 * @param tag the tag name for which child elements are sought for.
	 * @return Returns an empty nodelist if elements under search are not found.
	 * @throws NullPointerException if tag is null.
	 */
	
	@Override
	public NodeList getAllElementsUnderTag(String tag) {
		
		// return empty node list if the tag is null
		if(tag == null)
			return getEmptyNodeList();
		 
		Document xmlTreeDoc = this.xmlTree.getDoc();
		NodeList nodeList = xmlTreeDoc.getElementsByTagName(tag);
		
		if(nodeList.getLength() >= 1){
			Node node = nodeList.item(0);
			if(node != null){
				NodeList subNodeList = node.getChildNodes();
				return subNodeList;
			}
		}
		
		// an empty nodeList
		return getEmptyNodeList();
	}
	
	public static NodeList getEmptyNodeList(){
		return new NodeList() {
			@Override
			public Node item(int i) {
				return null;
			}
			@Override
			public int getLength() {
				return 0;
			}
		};
	}
	
	@Override
	public NodeList getListOfElementsUnderTag(String tag) {
		if(tag == null)
			throw new NullPointerException();
		Document xmlTreeDoc = this.xmlTree.getDoc();
		NodeList nodeList = xmlTreeDoc.getElementsByTagName(tag);
		return nodeList;
	}
	
	
	@Override
	public NamedNodeMap getAttrNodeMap(NodeList nodeList, String nodeName) {
		NamedNodeMap map = null;
		for(int i=0; i<nodeList.getLength(); i++){
			if(nodeList.item(i).getNodeName().equals(nodeName)){
				map = nodeList.item(i).getAttributes();
				return map;
			}
		}
		return map;
	}
	@Override
	public NodeList getDocChildNodes() {
		NodeList nodeList = this.getXmlTree().getDoc().getDocumentElement().getChildNodes();
		return nodeList;
	}
	
	@Override
	public NodeList getElementsByTagName(String tagName) {
		NodeList nodeList = this.getXmlTree().getDoc().getElementsByTagName(tagName);
		return nodeList;
	}
	
	@Override
	public String getCData(String xPath) {
		if(!HunterUtility.notNullNotEmpty(xPath))
			throw new IllegalArgumentException();
		String end = "text()";
		String path = xPath.toLowerCase();
		if(!path.endsWith(end)){
			xPath += end;
		}
		return getElementVal(xPath);
	}
	
	@Override
	public void insertCDataToRootEle(String elementName, String cdata) {
		Document doc = this.xmlTree.getDoc();
		CDATASection cData = doc.createCDATASection(elementName);
		Element root = doc.getDocumentElement();
		root.appendChild(cData);
	}
	
	@Override
	public void insertCDataToElement(Element toAppend, String elementName, String cdata) {
		Document doc = this.xmlTree.getDoc();
		CDATASection cData = doc.createCDATASection(elementName);
		toAppend.appendChild(cData);
	}
	
	@Override
	public void addElement(String xPath, Element e) {
		
		NodeList nodes = null;
		try {
			nodes = (NodeList)this.xpath.evaluate(xPath, this.xmlTree.getDoc().getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}
		
		nodes.item(0).getParentNode().appendChild(e);
		
	}
	@Override
	public void removeAttribute(Element element, String name) {
		element.removeAttribute(name);
	}
	
	
	@Override
	public String toString() {
		String xml = null;
		
		StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
		try { 
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		}
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        try {
			transformer.transform(new DOMSource(this.xmlTree.getDoc()), new StreamResult(sw));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
        xml = sw.toString();
        HunterLogFactory.getLog(getClass()).info("converted the documet to string >> " + xml);
        return sw.toString();
		
	}
	@Override
	public NodeList getNodeListForCompiledXpath(String xPath) {
		return null;
	}
	@Override
	public NodeList getNodeListForXPath(String xPath) {
		NodeList nodes = null;
		try {
			nodes = (NodeList)this.xpath.evaluate(xPath, this.xmlTree.getDoc().getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
		}
		
		return nodes;
	}

	
	
	
	


}
