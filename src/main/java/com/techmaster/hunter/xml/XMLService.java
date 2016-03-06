package com.techmaster.hunter.xml;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface XMLService {
	
	public String getElementVal(String xPath);
	public void addElement(String xPath, Element e);
	public String getTextValue(String xPath);
	public void transform(Document xml, Document xsl); 
	public Node getTheFirstElement() throws XPathExpressionException;
	public void transform(String xmlLoc, String xslLoc);
	public void addAttribute(Element element, String name, String val);
	public void removeAttribute(Element element, String name);
	public NodeList getNodeList();
	public NodeList getAllElementsUnderTag(String string);
	public NamedNodeMap getAttrNodeMap(NodeList nodeList, String nodeName);
	public NodeList getDocChildNodes();
	public NodeList getElementsByTagName(String tagName);
	public String getCData(String xPath);
	public NodeList getListOfElementsUnderTag(String tag);
	public void insertCDataToRootEle(String elementName, String cdata);
	public void insertCDataToElement(Element toAppend, String elementName, String cdata);
	public NodeList getNodeListForCompiledXpath(String xPath);
	public NodeList getNodeListForXPath(String xPath);
	public NodeList getNodeListForPathUsingJavax(String xPath);
	
	

}