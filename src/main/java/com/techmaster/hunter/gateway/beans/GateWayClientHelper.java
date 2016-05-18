package com.techmaster.hunter.gateway.beans;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.MessageDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.json.ReceiverGroupJson;
import com.techmaster.hunter.json.ReceiverRegionJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.HunterJacksonMapper;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.ReceiverGroupReceiver;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskClientConfigBean;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;

public class GateWayClientHelper {
	
	private RegionService regionService = HunterDaoFactory.getInstance().getDaoObject(RegionService.class);
	private HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
	private HunterJacksonMapper hunterJacksonMapper = HunterDaoFactory.getInstance().getDaoObject(HunterJacksonMapper.class);;
	private MessageDao messageDao = HunterDaoFactory.getInstance().getDaoObject(MessageDao.class);
	private TaskDao taskDao = HunterDaoFactory.getInstance().getDaoObject(TaskDao.class);
	
	
	private static GateWayClientHelper instance = null;
	private Logger logger = Logger.getLogger(GateWayClientHelper.class);
	
	private GateWayClientHelper(){}
	
	static{
		if(instance == null){
			synchronized (GateWayClientHelper.class) {
				instance = new GateWayClientHelper();
			}
		}
	}
	
	public static GateWayClientHelper getInstance(){
		return instance;
	}
	
	/**
	 * This method locks to pending and unlocks task to any desired status.
	 * It should only be used while processing task to avoid multiple processing submissions and no where else. 
	 * @param taskId The id of the task to be locked or unlocked.
	 * @param status the status to which the task is to be set to. If left null, system shall lock the task by setting it to pending.
	 */
	public void lockTask(Long taskId, String status){
		if(status == null || HunterConstants.STATUS_PENDING.equals(status)){ 
			status = HunterConstants.STATUS_PENDING;
			logger.debug("Locking task...: " + taskId);
		}else{
			logger.debug("Unlocking task..." + taskId + " and putting status to : " + status);
		}
		String lockQ = "UPDATE TASK t SET t.TSK_DEL_STS = ? WHERE t.TSK_ID = ?";
		List<Object> values = new ArrayList<>();
		values.add(HunterConstants.STATUS_PENDING);
		values.add(taskId);
		HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class).executeUpdate(lockQ, values);
	}
	
	public boolean isTaskLocked(Task task){
		String status = taskDao.getTaskStatuses(task.getTaskId()).get(HunterConstants.STATUS_TYPE_DELIVERY);
		return HunterConstants.STATUS_PENDING.equals(status);
	}
	
	public Map<String, Object> getDefaultGetParams(TaskClientConfigBean configBean){
		Map<String, Object> getParams = new HashMap<>();
		getParams.put("Accept-Charset", "UTF-8");
		getParams.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		return getParams;
	}
	
	public TaskMessageReceiver convertHunterMsgRcvrToTaskMsgRcvr(HunterMessageReceiver hunterMessageReceiver){
		TaskMessageReceiver tskMessageReceiver = hunterJacksonMapper.convertValue(hunterMessageReceiver, TaskMessageReceiver.class);
		return tskMessageReceiver;
	}
	
	public void updateTaskMsgLifeStatus(Long taskId, String msgLifeStatus) {
		messageDao.updateTaskMsgDelStatus(taskId, msgLifeStatus); 
	}
	
	public void updateTaskMsgDelStatus(Long taskId, String msgDelStatus) {
		messageDao.updateTaskMsgDelStatus(taskId, msgDelStatus); 
	}
	
	public List<ReceiverGroupReceiver> getAllTaskGroupReceivers(Task task){
		List<ReceiverGroupReceiver> groupReceivers = new ArrayList<>();
		String query = hunterJDBCExecutor.getQueryForSqlId("getAllTaskGroupReceiversForTask"); 
		logger.debug("Executing query to get receiver Id : " + query + ". With parameters : " + task.getTaskId());
		List<Object> values = new ArrayList<>();
		values.add(task.getTaskId());
		Map<Integer, List<Object>> rowListMap = hunterJDBCExecutor.executeQueryRowList(query, values);
		if(rowListMap != null && !rowListMap.isEmpty()){
			for(Map.Entry<Integer, List<Object>> entry : rowListMap.entrySet()){
				ReceiverGroupReceiver receiver = new ReceiverGroupReceiver();
				AuditInfo auditInfo = new AuditInfo();
				List<Object> rowList = entry.getValue();
				int i=0;
				for(Object obj : rowList){
					String strVal = HunterUtility.getStringOrNullOfObj(obj);
					switch (i) {
					case 0:
						receiver.setReceiverId(HunterUtility.getLongFromObject(obj));
						break;
					case 1:
						receiver.setReceiverContact(strVal);
						break;
					case 2:
						receiver.setReceiverType(strVal);
						break;
					case 3:
						receiver.setApproved(HunterUtility.getBooleanForYN(strVal)); 
						break;
					case 4:
						receiver.setApprover(strVal);
						break;
					case 5:
						auditInfo.setCretDate(HunterUtility.parseDate(strVal, HunterConstants.DATE_FORMAT_STRING));  
						break;
					case 6:
						auditInfo.setCreatedBy(strVal);
						break;
					case 7:
						auditInfo.setLastUpdate(HunterUtility.parseDate(strVal, HunterConstants.DATE_FORMAT_STRING));  
						break;
					case 8:
						auditInfo.setLastUpdatedBy(strVal);
						break;
					case 9:
						receiver.setActive(HunterUtility.getBooleanForYN(strVal)); 
						break;
					default:
						break;
					}
					i++;
				}
				receiver.setAuditInfo(auditInfo);
				groupReceivers.add(receiver);
			}
		}
		return groupReceivers;
	}
	
	public Map<Long, String> getTskGrpRcvrIdRcvrCntctMap(Task task){
		String groupIds = getCmmSprtdTskGrpIdStrForTask(task);
		String query = hunterJDBCExecutor.getQueryForSqlId("getGroupReceiverContactForReceiverIds");
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{groupIds});
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query,values);
		Map<Long, String> rcvrCntctMap = new HashMap<>();
		if(rowMapList != null && !rowMapList.isEmpty()){
			for(Map.Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
				List<Object> rowList_ = entry.getValue();
				Long rcvrId = rowList_.get(0) == null ? 0L : HunterUtility.getLongFromObject(rowList_.get(0)); 
				String contact = rowList_.get(1)+"";
				rcvrCntctMap.put(rcvrId, contact);
			}
		}
		return rcvrCntctMap;
		
	}
	
	public String getCmmSprtdTskGrpIdStrForTask(Task task){
		StringBuilder builder = new StringBuilder();
		Set<ReceiverGroupJson> taskGroups = task.getTaskGroups();
		for(ReceiverGroupJson groupJson : taskGroups){
			if(groupJson.getReceiverType().equalsIgnoreCase(task.getTskMsgType())){
				builder.append(groupJson.getGroupId()).append(",");
			}
		}
		String quoted = builder.toString();
		if(quoted.endsWith(",")){
			quoted = quoted.substring(0, quoted.length() - 1);
		}
		return quoted;
	}
	
	public boolean validateTaskConfigName(String configName){
		logger.debug("Validating client : " + configName); 
		String xPath = "//client/@clientName";
		NodeList nodeList = HunterCacheUtil.getInstance().getXMLService(HunterConstants.CLIENT_CONFIG_XML_CACHED_SERVICE).getNodeListForPathUsingJavax(xPath); 
		int length = nodeList.getLength();
		logger.debug("Number of templates configured : " + length);
		for( int i=0; i<length; i++) {
		    Attr attr = (Attr) nodeList.item(i);
		    String value = attr.getValue();
		    if(value.equals(configName)){
		    	return true;
		    }
		}
		throw new HunterRunTimeException("Configuration name does not exist ( " + configName + " )"); 
	}
	
	public List<HunterMessageReceiver> getTaskRegionReceivers(Long taskId){
		
		Object [] receiversNumberObj = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(taskId);
		List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<ReceiverRegionJson> receiverRegionJsons = (List<ReceiverRegionJson>)receiversNumberObj[1];
		
		if(receiverRegionJsons == null || receiverRegionJsons.isEmpty()){
			logger.debug("Task does not have region receivers. Returning..."); 
			return hunterMessageReceivers;
		}
		
		String query = hunterJDBCExecutor.getQueryForSqlId("getHntrMsgRcvrsFrAllRegionsNames");
		logger.debug("Query before replacement : " + query); 
		String receiverType = taskDao.getTaskMsgType(taskId);
		logger.debug("Task message type : " + receiverType); 
		
		for(ReceiverRegionJson receiverRegionJson : receiverRegionJsons){
			
			String copy = new String(query);
			
			String countryName = receiverRegionJson.getCountry();
			String countyName = receiverRegionJson.getCounty();
			String constituencyName = receiverRegionJson.getConstituency();
			String constituencyWardsNames = receiverRegionJson.getWard();
			
			copy = replaceForNullable(copy, "=:countryName", HunterUtility.singleQuote(countryName)); 
			copy = replaceForNullable(copy, "=:countyName", HunterUtility.singleQuote(countyName)); 
			copy = replaceForNullable(copy, "=:consName", HunterUtility.singleQuote(constituencyName)); 
			copy = replaceForNullable(copy, "=:consWardName", HunterUtility.singleQuote(constituencyWardsNames));
			copy = replaceForNullable(copy, "=:active", HunterUtility.singleQuote("Y")); // pull only active receivers
			copy = replaceForNullable(copy, "=:receiverType", HunterUtility.singleQuote(receiverType));

			logger.debug("Replaced query : " + copy);
			
			List<HunterMessageReceiver> hntrMsgRcvrs = HunterHibernateHelper.executeQueryForObjList(HunterMessageReceiver.class, copy);
			hunterMessageReceivers.addAll(hntrMsgRcvrs);
			
			logger.debug("Receivers count for query : " + hntrMsgRcvrs.size()); 
			
			
		}
		
		return hunterMessageReceivers;
	}
	
	private static String replaceForNullable(String query, String key, String value){
		if(value == null){
			value = " IS NULL ";
		}else{
			value = " = " + value;
		}
		String replaced = query.replaceAll(key, value);
		return replaced;
	}
	
	public Properties getEmailConfigsByName(String configName){
		Properties props = new Properties();
		XMLService configService = HunterCacheUtil.getInstance().getXMLService(HunterConstants.EMAIL_CONFIG_CACHED_SERVICE);
		String refPath = "//configs/config[@name=\""+ configName +"\"]/smtp[@type=\"stmpConfigs\"]/@ref";
		String refName = configService.getTextValue(refPath);
		if(HunterUtility.notNullNotEmpty(refName)){
			logger.equals("Refererence smtp properties found for ("+ refName +"). Fetching..."); 
			Map<String, String> smtpMap = getRefSMTPProps(refName);
			setPropsFromMap(props, smtpMap);
			logger.equals("Done fetching reference data : " + HunterUtility.stringifyMap(smtpMap));  
		}else{
			logger.debug("Did not find reference data. Using configured smtp properties..."); 
			String configPath = "//config[@name=\"" + configName +"\"]/smtp[@type=\"stmpConfigs\"]/props" ;
			NodeList configs = configService.getNodeListForPathUsingJavax(configPath).item(0).getChildNodes();
			for(int i=0; i<configs.getLength();i++){
				Node prop = configs.item(i);
				if(prop.getNodeName().equals("prop")){
					String name = prop.getAttributes().getNamedItem("name").getTextContent();
					String value = prop.getTextContent();
					props.setProperty(name, value);
				}
			}
			logger.debug("Done reading configured smtp properties : " + HunterUtility.stringifyMap(props)); 
		}
		
		logger.debug("Configuring context parameters..."); 
		Map<String,String> contextParams = getContextPropsMap(configName);
		setPropsFromMap(props, contextParams);
		
		logger.debug("Final properties after all reading :  " + props); 
		
		return props;
	}
	
	private void setPropsFromMap(Properties props, Map<String,String> propsMap){
		if(propsMap != null && !propsMap.isEmpty()){
			for(Map.Entry<String, String> entry : propsMap.entrySet()){
				String name = entry.getKey();
				String value = entry.getValue();
				props.setProperty(name, value);
			}
		}
	}
	
	public Set<GateWayMessage> doSaveOrUpdateInHibernate(final Set<GateWayMessage> messages){
		logger.debug("Storing messages in hibernate..."); 
		List<Object> msgList = new ArrayList<>();
		msgList.addAll(messages);
		HunterHibernateHelper.saveOrUpdateEntities(msgList); 
		logger.debug("Done storing messages in hibernate!");  
		return messages;
	}
	
	public Map<String,String> getContextPropsMap(String configName){
		logger.debug("Getting context properties for config name : " + configName); 
		Map<String,String> propsMap = new HashMap<>(); 
		XMLService configService = HunterCacheUtil.getInstance().getXMLService(HunterConstants.EMAIL_CONFIG_CACHED_SERVICE);
		String mainPath = "//configs/config/context";
		String userName = configService.getTextValue(mainPath+"/security/userName");
		String password = configService.getTextValue(mainPath+"/security/password");
		propsMap.put("userName", userName); 
		propsMap.put("password", password);
		String propsPath = mainPath + "/props";
		NodeList props = configService.getNodeListForPathUsingJavax(propsPath).item(0).getChildNodes();
		for(int i=0; i<props.getLength();i++){
			Node prop = props.item(i);
			if(prop.getNodeName().equals("prop")){
				String name = prop.getAttributes().getNamedItem("name").getTextContent();
				String value = prop.getTextContent();
				propsMap.put(name, value);
			}
		}
		logger.debug("Finished");
		return propsMap;
	}
	
	public Map<String,String> getRefSMTPProps(String ref){
		logger.debug("Retrieving smtp props for ref name : " + ref); 
		Map<String,String> props = new HashMap<String, String>();
		String mainPath = "//configs/smtp/config[@type=\"stmpConfigs\"][@name=\""+ ref +"\"]/props";
		XMLService configService = HunterCacheUtil.getInstance().getXMLService(HunterConstants.EMAIL_CONFIG_CACHED_SERVICE);
		NodeList configs = configService.getNodeListForPathUsingJavax(mainPath).item(0).getChildNodes();
		for(int i=0; i<configs.getLength();i++){
			Node prop = configs.item(i);
			if(prop.getNodeName().equals("prop")){
				String name = prop.getAttributes().getNamedItem("name").getTextContent();
				String value = prop.getTextContent();
				props.put(name, value);
			}
		}
		logger.debug("Finished retrieving reference data : " + HunterUtility.stringifyMap(props));  
		return props;
	}
	
	public String createGetStringFromParams(Map<String, Object> params){
		String getStr = "";
		int index = 0;
		for(Map.Entry<String, Object> entry : params.entrySet()){
			String key = entry.getKey();
			if(key != null && !key.equals(HunterConstants.BASE_URL)){
				String value = entry.getValue() != null ? entry.getValue().toString() : null;
				if(index == 0){
					getStr = key + "=" + value;
				}else{
					getStr += "&" + key + "=" + value;
				}
				index++;
			}
		}
		String baseUrl = params.get(HunterConstants.BASE_URL).toString(); 
		getStr = baseUrl +  "?" + getStr; 
		getStr = getStr.trim().replaceAll(" ", "%20");
		logger.debug("Returning get string >> " + getStr );
		return getStr;
	}
	
	public String getCMGetRequestBody(TaskClientConfigBean configBean, Set<GateWayMessage> gateWayMessages){
		String paramStr = null;
		Map<String, Object> getParams = new HashMap<>();
		getParams.put(HunterConstants.CONFIG_UUID, configBean.getConfigs().get(HunterConstants.CONFIG_UUID));
		getParams.put(HunterConstants.TOKEN, configBean.getConfigs().get(HunterConstants.CONFIG_UUID));
		getParams.put(HunterConstants.FROM, configBean.getConfigs().get("sender")); 
		getParams.put(HunterConstants.BASE_URL, configBean.getMethodURL());
		for(GateWayMessage message : gateWayMessages){
			getParams.put("body", message.getText());
			getParams.put("to", message.getContact());
			getParams.put("reference", message.getClientTagKey());
			paramStr = createGetStringFromParams(getParams);
			/* 	This will take one message for now. 
			 * 	Later, we'll modify to allow more than one message per request. 
			 */
			break;
		}
		logger.debug("Get request string created : " + paramStr); 
		return paramStr;
	}
	
	public String getOzekiGetRequestBody(TaskClientConfigBean configBean, Set<GateWayMessage> gateWayMessages){
		
		String baseUrl = configBean.getMethodURL();
		Map<String, Object> params = new HashMap<>();
		
		params.put(HunterConstants.BASE_URL, baseUrl);
		params.put("action", "sendmessage");
		params.put("messagecount", Integer.toString(gateWayMessages.size()));
		params.put("username", configBean.getUserName());
		params.put("password", configBean.getPassword());
		
		int i=0;
		
		for(GateWayMessage message : gateWayMessages){
			params.put("recipient"+i, message.getContact());
			params.put("messagetyp"+i, configBean.getConfigs().get("messagetype")); 
			params.put("messagedata"+i, message.getText());
			i++;
		}
		
		String requestBody = null;
		try {
			requestBody = HunterUtility.urlEncodeRequestMap(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.debug(requestBody); 
		return requestBody;
	}
	
	public String getOzekiPostRequestBody(TaskClientConfigBean configBean, Set<GateWayMessage> gateWayMessages){
		
		logger.debug("Getting ozeki post request body..."); 
		String requestBody = null;
		List<NameValuePair> pairs = new ArrayList<>();
		
		NameValuePair action = new BasicNameValuePair("action", "sendmessage");
		NameValuePair messagecount = new BasicNameValuePair("messagecount", Integer.toString(gateWayMessages.size())); 
		NameValuePair username = new BasicNameValuePair("username", configBean.getUserName());
		NameValuePair password = new BasicNameValuePair("password", configBean.getPassword());
		NameValuePair submit = new BasicNameValuePair("submit", "OK");
		
		pairs.add(action);
		pairs.add(messagecount);
		pairs.add(username);
		pairs.add(password);
		pairs.add(submit);
		
		
		int i=0;
		
		for(GateWayMessage message : gateWayMessages){
			NameValuePair recipient = new BasicNameValuePair("recipient"+i, message.getContact());
			NameValuePair messagetype = new BasicNameValuePair("messagetyp"+i, configBean.getConfigs().get("messagetype")); 
			NameValuePair messagedata = new BasicNameValuePair("messagedata"+i, message.getText());
			pairs.add(recipient);
			pairs.add(messagetype);
			pairs.add(messagedata);
			i++;
		}
		
		requestBody = URLEncodedUtils.format(pairs,"UTF-8");
		logger.debug("Finished retrieving ozeki post request body : \n" + requestBody); 
		return requestBody;
	}
	
	public String getCMPostRequestBody(TaskClientConfigBean configBean, Set<GateWayMessage> gateWayMessages) {
		
		String requestBody = null;
		
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
            Text productTokenValue = doc.createTextNode("" + configBean.getConfigs().get(HunterConstants.CONFIG_UUID));
            productTokenElement.appendChild(productTokenValue);
            root.appendChild(authenticationElement);

            for(GateWayMessage message : gateWayMessages){
            	
            	Element msgElement = doc.createElement("MSG");

                Element fromElement = doc.createElement("FROM");
                String sender = configBean.getConfigs().get("sender");
                sender = sender == null ? HunterConstants.CONFIG_SENDER : sender;
                Text fromValue = doc.createTextNode(sender); 
                fromElement.appendChild(fromValue);
                msgElement.appendChild(fromElement);

                Element bodyElement = doc.createElement("BODY");
                Text bodyValue = doc.createTextNode(message.getText());
                bodyElement.appendChild(bodyValue);
                msgElement.appendChild(bodyElement);

                Element toElement = doc.createElement("TO");
                Text toValue = doc.createTextNode(message.getContact()); 
                toElement.appendChild(toValue);
                msgElement.appendChild(toElement);
                
                root.appendChild(msgElement);
                String messageBody = HunterUtility.stringifyElement(msgElement, true);
                logger.debug("message request body for message ("+ message.getMsgId() +") : " + messageBody);  
                byte[] bytes = messageBody == null ? new byte[0] : messageBody.getBytes(); 
                message.setRequestBody(bytes);
            }

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Source src = new DOMSource(doc);
            Result dest = new StreamResult(xml);
            aTransformer.transform(src, dest);
            
            String xmlStr = xml.toString();
            return xmlStr;

        } catch (TransformerException ex) {
        	ex.printStackTrace();
        } catch (ParserConfigurationException p) {
            p.printStackTrace();
        }
		
		return requestBody;
	}
	
	public Map<String, Object> getCntntParamsFrTskBsnsEmail(String emailType, Long taskId) {
		logger.debug("Retrieving email template content params for email type : " + emailType + ", task id : " + taskId);
		Map<String, Object> params = new HashMap<>();
		String query = null;
		if(emailType != null && emailType.equals(HunterConstants.MAIL_TYPE_TASK_PROCESS_NOTIFICATION)){
			query = hunterJDBCExecutor.getQueryForSqlId("getClientDetailsForTaskOwnerForTaskId");
			List<Object> values = new ArrayList<>();
			values.add(taskId);
			List<Map<String, Object>> rowMapList = hunterJDBCExecutor.executeQueryRowMap(query, values);
			if(rowMapList != null &&  !rowMapList.isEmpty()){
				Map<String, Object> data = rowMapList.get(0);
				Map<String, Object> pounded = new HashMap<>();
				for(Map.Entry<String, Object> entry : data.entrySet()){
					String key = entry.getKey();
					key = HunterUtility.doublePoundQuote(key);
					pounded.put(key, entry.getValue());
				}
				logger.debug("Obtained values : " + HunterUtility.stringifyMap(data)); 
				return pounded;
			}else{
				logger.debug("No data found for the params : Mail type : " + emailType + ", task id : " + taskId); 
			}
		}
		return params;
	}
	
	
	public String getMessageIds(Set<GateWayMessage> messages){
		StringBuilder builder = new StringBuilder();
		if(messages != null && !messages.isEmpty()){
			for(GateWayMessage message : messages){
				builder.append(message.getMsgId());
				builder.append(",");
			}
		}
		String msgIdStr = builder.toString();
		if(msgIdStr.endsWith(",")){
			msgIdStr = msgIdStr.substring(0, msgIdStr.length() - 1);
		}
		logger.debug("Message Ids : " + msgIdStr); 
		return msgIdStr;
	}
	
	
	
	
	
	
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setHunterJDBCExecutor(HunterJDBCExecutor hunterJDBCExecutor) {
		this.hunterJDBCExecutor = hunterJDBCExecutor;
	}

	public void setHunterJacksonMapper(HunterJacksonMapper hunterJacksonMapper) {
		this.hunterJacksonMapper = hunterJacksonMapper;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
	
}
