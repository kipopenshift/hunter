package com.techmaster.hunter.gateway.beans;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.impl.GateWayMessageDaoImpl;
import com.techmaster.hunter.dao.impl.TaskDaoImpl;
import com.techmaster.hunter.dao.types.GateWayMessageDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.util.HunterUtility;

public class CMClientBean extends AbsractGatewayClient{
	
	private Task task;
	private String message;
	private String messageType;
	private String [] receivers;
	private List<String> taskProcessErrors = new ArrayList<>();
	private String taskProcessStatus = null;
	private Logger logger = Logger.getLogger(CMClientBean.class);
	
	private Set<GateWayMessage> gateWayMessages = new HashSet<>();
	private GateWayMessageDao gateWayMessageDao = new GateWayMessageDaoImpl();
	public static TaskDao taskDao = new TaskDaoImpl();
	
	public static String CLIENT_NAME = HunterConstants.CLIENT_CM;
	
	Map<String, Object> configMap = new HashMap<>();
	
	
	public CMClientBean(Task task) {
		super();
		this.task = task;
	}
	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public Map<String, Object> execute(Map<String, Object> map) { 
		
		Map<String, Object> results = new HashMap<>();
		
		assemble();
		
		boolean isPartialProcessing = false;
		boolean isSuccessProcessing = true;
		int indexProcessed = 0;
		
		// put them into a list to preserve the order of processing.
		
		List<GateWayMessage> gateWayMessages_ = new ArrayList<>(); 
		gateWayMessages_.addAll(gateWayMessages);
		
		logger.debug("Copied all gateway messages to a list size( "  + gateWayMessages_.size() + " )");  
		
		try{
			
			if(gateWayMessages_ != null && gateWayMessages_.size() > 0 ){
				
				for(GateWayMessage gateWayMessage : gateWayMessages_){
					
					String activeMethod = configMap.get(HunterConstants.CONFIG_ACTIVE_METHOD).toString();
					
					if(activeMethod != null && activeMethod.equalsIgnoreCase(HunterConstants.METHOD_POST)){
						
						String document = createRequestBody(configMap, gateWayMessage);
						if(document != null){
							gateWayMessage.setStatus(HunterConstants.STATUS_PROCESSED);
							gateWayMessage.setSendDate(new Date()); 
							long time1 = System.currentTimeMillis();
							String reponse = doPost(configMap, gateWayMessage);
							long time2 = System.currentTimeMillis();
							long time = time2 - time1;
							GateWayResponseHanlder.getInstance().updateGateWayMessageForStatus(HunterConstants.CLIENT_CM, reponse, gateWayMessage, HunterConstants.STATUS_TYPE_CLIENT);
							gateWayMessage.setDuration(time); 
							gateWayMessage.setClntRspCode(HunterConstants.STATUS_TYPE_CLIENT); 
							gateWayMessage.setClntRspText(reponse); 
							if(reponse == null || reponse.trim().equals("")){
								gateWayMessage.setStatus(HunterConstants.STATUS_PROCESSED);
								isPartialProcessing = true;
							}else{
								gateWayMessage.setStatus(HunterConstants.STATUS_FAILED); 
								gateWayMessage.setErrorText("Client Error"); 
								isSuccessProcessing = false;
							}
						}else{
							gateWayMessage.setStatus(HunterConstants.STATUS_FAILED);
							gateWayMessage.setErrorText("Could not create document"); 
						}
						
					}else if(activeMethod != null && activeMethod.equalsIgnoreCase(HunterConstants.METHOD_GET)){
						
						Map<String, Object> params = new HashMap<>();
						
						String baseUrl = "https://sgw01.cm.nl/gateway.ashx?";
						String token = configMap.get(HunterConstants.CONFIG_UUID).toString(); 
						String body = message;
						String to = gateWayMessage.getContact();
						String from = HunterConstants.CONFIG_SENDER;
						String reference = Long.toString(gateWayMessage.getMsgId()); 
						
						params.put(HunterConstants.BASE_URL, baseUrl);
						params.put(HunterConstants.TOKEN, token);
						params.put(HunterConstants.BODY, body);
						params.put(HunterConstants.TO, to);
						params.put(HunterConstants.FROM, from);
						params.put(HunterConstants.REFERENCE, reference);
						
						gateWayMessage.setStatus(HunterConstants.STATUS_PROCESSED);
						gateWayMessage.setSendDate(new Date()); 
						long time1 = System.currentTimeMillis();
						
						String response = doGet(params, gateWayMessage);
						
						long time2 = System.currentTimeMillis();
						long time = time2 - time1;
						
						GateWayResponseHanlder.getInstance().updateGateWayMessageForStatus(HunterConstants.CLIENT_CM, response, gateWayMessage, HunterConstants.STATUS_TYPE_CLIENT);
						
						gateWayMessage.setDuration(time); 
						gateWayMessage.setClntRspCode(HunterConstants.STATUS_TYPE_CLIENT); 
						gateWayMessage.setClntRspText(response); 
						
						if(response == null || response.trim().equals("")){
							gateWayMessage.setStatus(HunterConstants.STATUS_PROCESSED);
							isPartialProcessing = true;
						}else{
							gateWayMessage.setStatus(HunterConstants.STATUS_FAILED); 
							gateWayMessage.setErrorText("Client Error"); 
							isSuccessProcessing = false;
						}
						
					}else{
						gateWayMessage.setStatus(HunterConstants.STATUS_FAILED);
						gateWayMessage.setErrorText("Method provided is invalid : " + activeMethod); 
						this.getTask().setTaskDeliveryStatus(HunterConstants.STATUS_FAILED); 
					}
						
					gateWayMessageDao.update(gateWayMessage);
					indexProcessed++;
				}
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			this.getTask().setTaskDeliveryStatus(HunterConstants.STATUS_FAILED);
			
			if(indexProcessed > 0 && indexProcessed < gateWayMessages.size()){
				for(int i=indexProcessed+1; i<gateWayMessages.size();i++){
					GateWayMessage gateWayMessage = gateWayMessages_.get(i);
					gateWayMessage.setStatus(HunterConstants.STATUS_FAILED); 
				}
			}
			
		}
		
		if(isPartialProcessing && !isSuccessProcessing){
			this.getTask().setTaskDeliveryStatus(HunterConstants.STATUS_PARTIAL);  
		}else{
			this.getTask().setTaskDeliveryStatus(HunterConstants.STATUS_PARTIAL); 
		}
		
		taskDao.update(task); 
		
		results.put(GatewayClient.GATE_WAY_MESSAGES, gateWayMessages); // should return the real gateway messages
		results.put(GatewayClient.TASK_PROCESS_ERRORS, new ArrayList<>().addAll(this.taskProcessErrors)); // should only return a copy 
		results.put(GatewayClient.TASK_PROCESS_STATUS, new String(this.taskProcessStatus == null ? "" : this.taskProcessStatus)); // should return a new String.
		
		return results;
	}

	@Override
	public Object assemble() {
		refresh();
		readConfigurations();
		prepareTaskAndCreateGateWayMessages(this.getTask());
		task.getTaskMessage().setMsgLifeStatus(HunterConstants.STATUS_PROCESSED); 
		taskDao.update(task); 
		return null;
	}
	
	@Override
	public void prepareTaskAndCreateGateWayMessages(Task task) {
		
		task.setLastUpdate(new Date());
		task.setUpdatedBy(HunterConstants.HUNTER_SYSTEM_USER_NAME);
		task.setTaskDeliveryStatus(HunterConstants.STATUS_STARTED); 
		
		// receivers must have been set by now!
		// message must be set by now also
		
		if(receivers.length == 0){
			logger.warn("Receivers have not been set for this task!!"); 
		}
		
		logger.info("Creating gateway messages...");
		
		for(int i=0; i<receivers.length;i++){
			String receiver = receivers[i];
			GateWayMessage gateWayMsg = new GateWayMessage();
			gateWayMsg.setContact(receiver);
			gateWayMsg.setCreatedBy(HunterConstants.HUNTER_SYSTEM_USER_NAME);
			gateWayMsg.setCreatedOn(new Date()); 
			gateWayMsg.setgClient(CLIENT_NAME); 
			gateWayMsg.setSendDate(null);
			gateWayMsg.setStatus(HunterConstants.STATUS_DRAFT);
			gateWayMsg.setTaskId(this.getTask().getTaskId()); 
			gateWayMsg.setText(HunterUtility.getStringBlob(message)); 
			gateWayMsg.setMessageType(messageType); 
			gateWayMessages.add(gateWayMsg); 
		}
		
		gateWayMessageDao.insertMessages(gateWayMessages);
		
		
	}
	
	@Override
	public void refresh() {
		
		Set<TaskMessageReceiver> taskReceivers = getTskMsgRcvrsFrTskId(task.getTaskId());
		
		int size = taskReceivers != null ? taskReceivers.size() : 0; 
		receivers = new String[size];
		
		int receiverIndx = 0;
		
		if(taskReceivers != null && taskReceivers.size() > 0){
			for(TaskMessageReceiver receiver : taskReceivers){
				String type = receiver.getReceiverType();
				String contact = receiver.getReceiverContact();
				if(type != null && type.equalsIgnoreCase(HunterConstants.RECEIVER_TYPE_TEXT)){
					// we believe that it comes only as a phone number.
					receivers[receiverIndx] = contact;
					receiverIndx++;
				}
			}
			logger.debug("Done populating phone numbers >> " + HunterUtility.stringifySet(taskReceivers)); 
		}
		
		Message messageObj = task.getTaskMessage();
		
		if(messageObj instanceof TextMessage){
			messageObj = (TextMessage)messageObj;
			message = ((TextMessage) messageObj).getText();
			messageType = HunterConstants.MESSAGE_TYPE_TEXT;
		}
		
	}

	
	@Override
	public  Map<String, Object> readConfigurations() {
		
		Map<String, Object> preMap = new HashMap<String, Object>();
		
		if(xmlService == null){
			logger.debug("xmlService is null. Trying to load it."); 
			loadConfigXMLService();
		}
		
		String configsPath = "//client[@clientName=\""+ HunterConstants.CLIENT_CM +"\"]/configurations";
		
		String userName = xmlService.getTextValue(configsPath + "/context/security/userName");
		String password = xmlService.getTextValue(configsPath + "/context/security/password");
		String actvMethod = xmlService.getTextValue(configsPath + "/context/methods/method[@active=\"true\"]/@type");
		String actvURL = xmlService.getTextValue(configsPath + "/context/methods/method[@active=\"true\"]/@url");
		
		String UUID = "";
		float txtPrice = 0;
		float mediaPrice = 0;
		float voicedPrice = 0;
		float callPrice = 0;
		float hunterBalance = 0;
		int blncMsgCnt = 0;
		int hunterRating = 0;
		String sender = "";
		
		String denomination = xmlService.getTextValue(configsPath + "/context/denomination");
		
		// read other elements
		
		NodeList nodeList = xmlService.getAllElementsUnderTag("configs");
		
		for(int i=0; i<nodeList.getLength();i++){
			Node config = nodeList.item(i);
			String configName = config.getNodeName();
			if(configName == "config"){
				NodeList data = config.getChildNodes();
				String label = data.item(1).getTextContent();
				String value = data.item(3).getTextContent();
				switch (label) {
					case "UUID" :
						UUID = value;
						break;
					case "txtPrice" :
						txtPrice = Float.parseFloat(value); 
						break;
					case "mediaPrice" :
						mediaPrice = Float.parseFloat(value); 
						break;
					case "voicedPrice" :
						voicedPrice = Float.parseFloat(value); 
						break;
					case "callPrice" :
						callPrice = Float.parseFloat(value); 
						break;
					case "hunterBalance" :
						hunterBalance = Float.parseFloat(value); 
						break;
					case "blncMsgCnt" :
						blncMsgCnt = Integer.parseInt(value); 
						break;
					case "hunterRating" :
						hunterRating = Integer.parseInt(value);;
						break;
					case "sender" :
						sender = value;
						break;
					default:
						break;
				}
			}
		}
		
		
		preMap.put( HunterConstants.CONFIG_USER_NAME, userName);
		preMap.put( HunterConstants.CONFIG_PASSWORD, password);
		preMap.put( HunterConstants.CONFIG_ACTIVE_METHOD, actvMethod);
		preMap.put( HunterConstants.CONFIG_ACTIVE_METHOD_URL, actvURL);
		preMap.put( HunterConstants.CONFIG_DENOMINATION, denomination);
		preMap.put( HunterConstants.CONFIG_UUID, UUID);
		preMap.put( HunterConstants.CONFIG_TXT_PRICE, txtPrice);
		preMap.put( HunterConstants.CONFIG_MEDIA_PRICE, mediaPrice);
		preMap.put( HunterConstants.CONFIG_VOICE_PRICE, voicedPrice);
		preMap.put( HunterConstants.CONFIG_CALL_PRICE, callPrice);
		preMap.put( HunterConstants.CONFIG_HUNTER_BALANCE, hunterBalance);
		preMap.put( HunterConstants.CONFIG_HUNTER_MSG_BAL_COUNT, blncMsgCnt);
		preMap.put( HunterConstants.CONFIG_HUNTER_RATING, hunterRating);
		preMap.put( HunterConstants.CONFIG_SENDER, sender);
		
		for(Map.Entry<String, Object> entry : preMap.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			configMap.put(key, value);
		}
		
		logger.debug("Successfully read configurations values"); 
		
		for (Map.Entry<String, Object> entry : configMap.entrySet()) {
			logger.debug(entry.getKey()+" : "+ entry.getValue()); 
		}
		
		
		return configMap;
	}
	
	@Override
	public String createRequestBody(Map<String, Object> params, GateWayMessage gateWayMessage) {
		
		try {
            ByteArrayOutputStream xml = new ByteArrayOutputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            // Get the DocumentBuilder
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            // Create blank DOM Document
            DOMImplementation impl = docBuilder.getDOMImplementation();
            Document doc = impl.createDocument(null, "MESSAGES", null);

            // create the root element
            Element root = doc.getDocumentElement();
            Element authenticationElement = doc.createElement("AUTHENTICATION");
            Element productTokenElement = doc.createElement("PRODUCTTOKEN");
            authenticationElement.appendChild(productTokenElement);
            Text productTokenValue = doc.createTextNode("" + params.get(HunterConstants.CONFIG_UUID));
            productTokenElement.appendChild(productTokenValue);
            root.appendChild(authenticationElement);

            Element msgElement = doc.createElement("MSG");
            root.appendChild(msgElement);

            Element fromElement = doc.createElement("FROM");
            Text fromValue = doc.createTextNode(HunterConstants.CONFIG_SENDER);
            fromElement.appendChild(fromValue);
            msgElement.appendChild(fromElement);

            Element bodyElement = doc.createElement("BODY");
            Text bodyValue = doc.createTextNode(HunterUtility.getBlobStr(gateWayMessage.getText())); 
            bodyElement.appendChild(bodyValue);
            msgElement.appendChild(bodyElement);

            Element toElement = doc.createElement("TO");
            Text toValue = doc.createTextNode(gateWayMessage.getContact()); 
            toElement.appendChild(toValue);
            msgElement.appendChild(toElement);

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Source src = new DOMSource(doc);
            Result dest = new StreamResult(xml);
            aTransformer.transform(src, dest);
            
            String xmlStr = xml.toString();
            gateWayMessage.setRequestBody(xmlStr.getBytes()); 
            return xml.toString();

        } catch (TransformerException ex) {
        	ex.printStackTrace();
            String errorMessage = ex.getMessage();
            logger.warn("Encountered exception while processing GateWayMessage >> " + gateWayMessage); 
            gateWayMessage.setRequestBody(errorMessage.getBytes()); 
            gateWayMessage.setStatus(HunterConstants.STATUS_FAILED); 
            return null;
        } catch (ParserConfigurationException p) {
            p.printStackTrace();
            String errorMessage = p.getMessage();
            logger.warn("Encountered exception while processing GateWayMessage >> " + gateWayMessage); 
            gateWayMessage.setRequestBody(errorMessage.getBytes()); 
            gateWayMessage.setStatus(HunterConstants.STATUS_FAILED);
            return null;
        }
	}
	
	@Override
	public String doPost(Map<String, Object> params, GateWayMessage gateWayMessage) {
		try {
            URL url = new URL(params.get(HunterConstants.CONFIG_ACTIVE_METHOD_URL).toString()); 
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(new String(gateWayMessage.getRequestBody()));
            wr.flush();
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String response = "";
            while ((line = rd.readLine()) != null) {
                response += line;
            }
            wr.close();
            rd.close();

            gateWayMessage.setClntRspText(response);

            return response;
            
        } catch (IOException ex) {
        	ex.printStackTrace();
        	String errorMessage = ex.getMessage();
        	logger.warn("Encountered exception while processing GateWayMessage >> " + gateWayMessage); 
            gateWayMessage.setRequestBody(errorMessage.getBytes()); 
            gateWayMessage.setStatus(HunterConstants.STATUS_FAILED); 
            return null;
        }
	}

	@Override
	public String doGet(Map<String, Object> params, GateWayMessage gateWayMessage) {
		return super.doGet(params, gateWayMessage); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CMClientBean other = (CMClientBean) obj;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		
		CMClientBean cml = new CMClientBean(null);
		cml.readConfigurations();
		
	}

	@Override
	public String createRequestBody(Map<String, Object> params,
			List<GateWayMessage> gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
	}



}
