package com.techmaster.hunter.gateway.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.HunterURLConstants;
import com.techmaster.hunter.dao.impl.GateWayMessageDaoImpl;
import com.techmaster.hunter.dao.impl.TaskDaoImpl;
import com.techmaster.hunter.dao.types.GateWayMessageDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;

public class OzekiClient extends AbsractGatewayClient{
	
	private Task task;
	private String message;
	private String messageType;
	private String activeMethod = null;
	private String activeMethodUrl = null;
	private String [] receivers;
	private List<String> taskProcessErrors = new ArrayList<>();
	private Logger logger = Logger.getLogger(CMClient.class);
	private Set<GateWayMessage> gateWayMessages = new HashSet<>();
	private GateWayMessageDao gateWayMessageDao = new GateWayMessageDaoImpl();
	public static TaskDao taskDao = new TaskDaoImpl();
	public static String CLIENT_NAME = HunterConstants.CLIENT_OZEKI;
	Map<String, Object> configMap = new HashMap<>();
	
	private static String REQUEST_BODY = null;
	
	public OzekiClient(Task task) {
		super();
		this.task = task;
	}

	@Override
	public Map<String, Object>  execute(Map<String, Object> params) {
		
		Map<String, Object> results = new HashMap<>();
		assemble();
		
		logger.debug("OZEKI active configured method : " + activeMethod);
		logger.debug("OZEKI active configured method ur : " + activeMethodUrl);
		List<GateWayMessage> nullList = null;
		REQUEST_BODY = createRequestBody(configMap, nullList);
		
		if(activeMethod != null && activeMethod.equals(HunterConstants.METHOD_POST)){
			
			@SuppressWarnings("unused")
			String response_ = doPost(params, null);
			XMLService xmlService = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.OZEKI_TEST_RSPONSE_XML_LOCL_PATH);
			String response = xmlService.toString();
			OzekiResponseHelper.getInstance().execute(response, gateWayMessages);
			gateWayMessageDao.updateGatewayMessages(gateWayMessages);
			
		}else if(activeMethod != null && activeMethod.equals(HunterConstants.METHOD_GET)){
			doGet(params, null);
		}else{
			logger.debug("No OZEKI active method is configured. Please check."); 
		}
		
		return results;
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
			gateWayMsg.setTaskId(task.getTaskId()); 
			gateWayMsg.setText(message);
			gateWayMsg.setMessageType(messageType); 
			gateWayMessages.add(gateWayMsg); 
		}
		gateWayMessageDao.insertMessages(gateWayMessages);
	}

	@Override
	public Object assemble() {
		
		refresh();
		readConfigurations();
		prepareTaskAndCreateGateWayMessages(task);
		
		return null;
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
		
		logger.debug("Size of receivers from task regions ( " + receivers.length + " )"); 
		
		Set<String> taskGroupReceivers = getUniqueContactForTaskGroups(task.getTaskId());
		logger.debug("Size of receivers from task groups ( " + taskGroupReceivers.size() + " )");
		
		if(taskGroupReceivers != null && !taskGroupReceivers.isEmpty()){
			for(String contact : taskGroupReceivers){
				receivers = HunterUtility.initArrayAndInsert(receivers, contact);
			}
		}
		
		logger.debug("Total number of receivers for task ( " + (receivers.length + taskGroupReceivers.size()) + " )");
		
		Message messageObj = task.getTaskMessage();
		
		if(messageObj instanceof TextMessage){
			messageObj = (TextMessage)messageObj;
			message = ((TextMessage) messageObj).getText();
			messageType = HunterConstants.MESSAGE_TYPE_TEXT;
		}
		
	}

	@Override
	public Map<String, Object> readConfigurations() {
		
		if(xmlService == null){
			logger.debug("xmlService is null. Trying to load it."); 
			loadConfigXMLService();
		}
		
		String configsPath = "//client[@clientName=\""+ HunterConstants.CLIENT_OZEKI +"\"]/configurations";
		
		String userName = xmlService.getTextValue(configsPath + "/context/security/userName");
		String password = xmlService.getTextValue(configsPath + "/context/security/password");
		String actvMethod = xmlService.getTextValue(configsPath + "/context/methods/method[@active=\"true\"]/@type");
		String actvURL = xmlService.getTextValue(configsPath + "/context/methods/method[@active=\"true\"]/@url");
		
		configMap.put( "username", userName);
		configMap.put( HunterConstants.CONFIG_PASSWORD, password);
		configMap.put( "action", "sendmessage");
		configMap.put( "messagecount", 0);
		
		activeMethod = actvMethod;
		activeMethodUrl = actvURL;
		
		logger.debug("Successfully read configurations values"); 
		
		for (Map.Entry<String, Object> entry : configMap.entrySet()) {
			logger.debug(entry.getKey()+" : "+ entry.getValue()); 
		}
		
		return configMap;
	}

	@Override
	public String createRequestBody(Map<String, Object> params, GateWayMessage gateWayMessage) {
		throw new IllegalArgumentException("Service unavailable yet.");
	}
	
	@Override
	public String createRequestBody(Map<String, Object> params, List<GateWayMessage> gateWayMessages_) {
		logger.debug("Creating request body for OZEKI client. Message size ( " + gateWayMessages.size() + " )");  
		if(gateWayMessages.isEmpty() || gateWayMessages == null){
			taskProcessErrors.add("GatewayMessage list is either null or mepty!!");
			throw new IllegalArgumentException("GatewayMessage list is either null or mepty!!");
		}
		
		String staticParams = null;
		StringBuilder gtwyMsgsReqData = new StringBuilder();
		Map<String,String> msgParams = new HashMap<>();
		int messageCount = 0;
		
		if(gateWayMessages_ == null){
			gateWayMessages_ = new ArrayList<>();
			gateWayMessages_.addAll(gateWayMessages);
		}
		
		for(int i=0; i<gateWayMessages.size();i++){
			msgParams.clear();
			GateWayMessage gateWayMessage = gateWayMessages_.get(i);
			msgParams.put("recipient" + i, gateWayMessage.getContact());
			msgParams.put("messagetype"+i, "SMS:TEXT");
			msgParams.put("messagedata"+i, gateWayMessage.getText());
			try {
				String encoded = HunterUtility.urlEncodeRequestMap(msgParams, "UTF-8");
				if(i != 0){
					gtwyMsgsReqData.append("&");
				}
				gtwyMsgsReqData.append(encoded);
				gateWayMessage.setClientTagKey(i+""); 
				gateWayMessage.setRequestBody(encoded.getBytes());
				messageCount++;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				gateWayMessage.setStatus(HunterConstants.STATUS_FAILED);
				gateWayMessage.setRequestBody(null);
				gateWayMessage.setErrorText(e.getMessage()); 
			}
		}
		

		try {
			configMap.put( "messagecount", messageCount);
			staticParams = HunterUtility.urlEncodeRequestMap(params, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			throw new HunterRunTimeException(e1.getMessage()); 
		} 
		
		String gtwyMsgsReqDataStr = gtwyMsgsReqData.toString();
		gtwyMsgsReqDataStr = staticParams + "&" + gtwyMsgsReqDataStr;
		logger.debug("Finished fetching request body for gateway messages :  \n " + gtwyMsgsReqDataStr); 
		return gtwyMsgsReqDataStr;
	}

	@Override
	public String doPost(Map<String, Object> params, GateWayMessage gateWayMessage) {
		
		try {
            
			URL url = new URL(activeMethodUrl); 
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(new String(REQUEST_BODY));
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

            return response;
            
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
		
		return null;
	}

	@Override
	public String doGet(Map<String, Object> params, GateWayMessage gateWayMessage) {
		
		
		
		return null;
	}

}

 