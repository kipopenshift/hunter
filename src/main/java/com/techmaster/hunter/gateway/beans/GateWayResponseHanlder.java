package com.techmaster.hunter.gateway.beans;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.HunterURLConstants;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.xml.XMLService;
import com.techmaster.hunter.xml.XMLServiceImpl;
import com.techmaster.hunter.xml.XMLTree;

public class GateWayResponseHanlder {

	
	private static XMLService xmlService;
	private static GateWayResponseHanlder instance;
	private GateWayResponseHanlder(){};
	
	private Logger logger = Logger.getLogger(GateWayResponseHanlder.class);
	
	static{
		
		if(instance == null){
			synchronized (GateWayResponseHanlder.class) {
				instance = new GateWayResponseHanlder();
			}
		}
		
		try {
			String configPath = HunterURLConstants.RESOURCE_BASE_XML_PATH + "ResponseConfig.xml";
			XMLTree tree = new XMLTree(configPath, false);
			xmlService = new XMLServiceImpl(tree); 
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
				
	}
	
	public static GateWayResponseHanlder getInstance(){
		return instance;
	}
	
	public void updateGateWayMessageForStatus(String clientName, String responseCode, GateWayMessage gateWayMessage, String statusType){
		
		if(clientName == null && statusType != null && responseCode != null){
			if(statusType.equals(HunterConstants.STATUS_TYPE_CLIENT)){
				gateWayMessage.setClntRspCode(responseCode);
				gateWayMessage.setClntRspText(responseCode); 
			}else if(statusType.equals(HunterConstants.STATUS_TYPE_DELIVERY)){
				gateWayMessage.setSubsRspnsCode(responseCode); 
				gateWayMessage.setSubsRspnsText(responseCode);
			}
			
			return;
		}
		
		String statusText = null;
		
		if (statusType == HunterConstants.STATUS_TYPE_DELIVERY)
			statusText = getClientTextForCode(clientName, responseCode, statusType);

		if(clientName != null && statusType == HunterConstants.STATUS_TYPE_CLIENT){
			gateWayMessage.setClntRspCode(responseCode);
			gateWayMessage.setClntRspText(responseCode); 
		}else if(clientName != null &&  statusType.equals(HunterConstants.STATUS_TYPE_DELIVERY)){
			gateWayMessage.setSubsRspnsCode(responseCode); 
			gateWayMessage.setSubsRspnsText(statusText);
		}
		
		
		
	}
	
	
	public String getClientTextForCode(String clientName, String responseCode, String statusType){
		
		String xPath = "//client[@clientName=\""+ clientName +"\"]/configs/config[@configType=\""+ statusType +"\"]/metadata[code=\""+ responseCode +"\"]";
		NodeList nodeList = xmlService.getNodeListForXPath(xPath);
		
		Node node = nodeList.item(0); 
		NodeList metadata = node.getChildNodes();
		
		String code = null;
		String text = null;
		String desc = null;
		
		for(int i=0; i<metadata.getLength(); i++){
			Node datum = metadata.item(i);
			if(datum.getNodeName().equals("code"))
				code = datum.getTextContent();
			else if(datum.getNodeName().equals("text")){
				text = datum.getTextContent();
			}else if(datum.getNodeName().equals("desc")){
				desc = datum.getTextContent();
			}
		}
		
		logger.debug("Code  :  " + code);
		logger.debug("text  :  " + text);
		logger.debug("desc  :  " + desc);
		
		return text;
		
	}
	
	public static void main(String[] args) {
		
		new GateWayResponseHanlder().getClientTextForCode("CM", "7", HunterConstants.STATUS_TYPE_CLIENT); 
		
	}
	
	
	
	
	
	
}
