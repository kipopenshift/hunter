package com.techmaster.hunter.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.HunterURLConstants;
import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.HunterMessageReceiverDao;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.obj.beans.Country;
import com.techmaster.hunter.obj.beans.HunterEmailTemplateBean;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.TaskClientConfigBean;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;


public class HunterCacheUtil {

	private static HunterCacheUtil instance;
	private static Logger logger = Logger.getLogger(HunterCacheUtil.class);
	private HunterCacheUtil (){};
	
	static{
		if(instance == null){
			synchronized (HunterCache.class) {
				instance = new HunterCacheUtil();
			}
		}
	}
	
	public static HunterCacheUtil getInstance(){
		return instance;
	}

	public void refreshAllXMLServices(){
		logger.debug("Caching xmlQuery..."); 
		XMLService queryService = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.QRY_XML_FL_LOC_PATH);
		HunterCache.getInstance().put(HunterConstants.QUERY_XML_CACHED_SERVICE, queryService);
		logger.debug("Done caching xmlQuery!!");
		logger.debug("Caching ui message xml..."); 
		XMLService uiMsgService = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.UI_MSG_XML_FL_LOC_PATH);
		HunterCache.getInstance().put(HunterConstants.UI_MSG_CACHED_SERVICE, uiMsgService);
		logger.debug("Done caching ui message xml!!");
		logger.debug("Caching ui clieConfigService xml..."); 
		XMLService clieConfigService = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.CLIENT_CONFIG_LOC_PATH); 
		HunterCache.getInstance().put(HunterConstants.CLIENT_CONFIG_XML_CACHED_SERVICE, clieConfigService);
		logger.debug("Done caching clieConfigService xml!!");
		logger.debug("Caching ui responseConfigXml xml..."); 
		XMLService responseConfigXml = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.RESPONSE_CONFIG_LOC_PATH);
		HunterCache.getInstance().put(HunterConstants.RESPONSE_CONFIG_CACHED_SERVICE, responseConfigXml);
		logger.debug("Done caching responseConfigXml xml...");
		logger.debug("Caching emailTemplates xml...");
		XMLService emailTemplates = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.EMAIL_TEMPLATES_LOCL_PATH);
		HunterCache.getInstance().put(HunterConstants.EMAIL_TEMPLATES_CACHED_SERVICE, emailTemplates);
		logger.debug("Cachingg emailConfigs xml...");
		XMLService emailConfigs = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.EMAIL_CONFIGS__LOCL_PATH);
		HunterCache.getInstance().put(HunterConstants.EMAIL_CONFIG_CACHED_SERVICE, emailConfigs);
		logger.debug("Done caching emailConfigs xml!!");
		logger.debug("Cachingg taskProcessJobsTemplate xml...");
		XMLService taskProcessJobsTemplate = HunterUtility.getXMLServiceForFileLocation(HunterURLConstants.TASK_PROCESS_JOBS_TEMPLATE);
		HunterCache.getInstance().put(HunterConstants.TASK_PROCESS_JOBS_TEMPLATE, taskProcessJobsTemplate);
		logger.debug("Done caching taskProcessJobsTemplate xml!!");
	}
	
	public XMLService getXMLService(String xmlName) {
		XMLService service = (XMLService)HunterCache.getInstance().get(xmlName);
		if(service == null) logger.debug("Service ( " + xmlName + " ) cannot be found!!") ; 
		return service;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getExistentEmailTemplateNames(){
		List<String> existentTemplates = (List<String>)HunterCache.getInstance().get(HunterConstants.EXISTENT_EMAIL_TEMPLATES);
		if(existentTemplates == null || existentTemplates.isEmpty()){
			return loadExistentEmailTemplatesNames();
		}
		return existentTemplates;
	}
	
	public List<String> loadExistentEmailTemplatesNames(){
		logger.debug("Loading existent templates...");
		String xPath = "//template/@*";
		List<String> existentTemplates = new ArrayList<>();
		NodeList nodeList = getInstance().getXMLService(HunterConstants.EMAIL_TEMPLATES_CACHED_SERVICE).getNodeListForPathUsingJavax(xPath); 
		int length = nodeList.getLength();
		logger.debug("Number of templates configured : " + length);
		for( int i=0; i<length; i++) {
		    Attr attr = (Attr) nodeList.item(i);
		    //String name = attr.getName();
		    String value = attr.getValue();
		    existentTemplates.add(value);
		}
		existentTemplates.addAll(existentTemplates);
		HunterCache.getInstance().put(HunterConstants.EXISTENT_EMAIL_TEMPLATES, existentTemplates);
		logger.debug("Done loading existent templates!"); 
		return existentTemplates;
	}
	
	public Map<String, HunterEmailTemplateBean> loadEmailTemplateBeans(){
		logger.debug("Loading email template beans..."); 
		Map<String, HunterEmailTemplateBean> emailTemplateBeans = new HashMap<>();
		List<String> existentTemplates = getInstance().getExistentEmailTemplateNames();
		for(String mailTypeName : existentTemplates){
			HunterEmailTemplateBean  emailTemplateBean = getInstance().createEmailTemplateBean(mailTypeName);
			emailTemplateBeans.put(mailTypeName, emailTemplateBean);
		}
		logger.debug("Done loading email template beans!"); 
		HunterCache.getInstance().put(HunterConstants.EMAIL_TEMPLATE_BEANS, emailTemplateBeans);
		return emailTemplateBeans;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, HunterEmailTemplateBean> getAllEmailTemplateBeanMap(){
		Map<String,HunterEmailTemplateBean> emailTemplateBeanMap = (Map<String,HunterEmailTemplateBean> )HunterCache.getInstance().get(HunterConstants.EMAIL_TEMPLATE_BEANS);
		return emailTemplateBeanMap;
	}
	
	public HunterEmailTemplateBean getEmailTemplateBean(String templateName){
		Map<String,HunterEmailTemplateBean> emailTemplateBeanMap = getAllEmailTemplateBeanMap();
		HunterEmailTemplateBean emailTemplateBean = null;
		if(emailTemplateBeanMap != null && !emailTemplateBeanMap.isEmpty()){
			emailTemplateBean = emailTemplateBeanMap.get(templateName);
		}
		return emailTemplateBean;
	}
	
	public HunterEmailTemplateBean createEmailTemplateBean(String templateName){
		
		StringBuilder templateCheckPath = new StringBuilder().append("templates/template[@name=\"").append(templateName).append("\"]");
		XMLService xmlService = getInstance().getXMLService(HunterConstants.EMAIL_TEMPLATES_CACHED_SERVICE);
		
		try {
			NodeList check  = xmlService.getAllElementsUnderTag(templateCheckPath.toString()); 
			if(check == null){
				logger.debug("Cannot find the template with the path : " + templateCheckPath.toString());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		StringBuilder templateDescPath = new StringBuilder().append("templates/template[@name=\"").append(templateName).append("\"]/description");
		StringBuilder templateContentPath = new StringBuilder().append("templates/template[@name=\"").append(templateName).append("\"]/content/text()");
		StringBuilder templatePath = new StringBuilder().append("templates/template[@name=\"").append(templateName).append("\"]/template/text()");
		StringBuilder contextPath = new StringBuilder().append("templates/template[@name=\"").append(templateName).append("\"]/context");
		StringBuilder contextFromPath = new StringBuilder().append(contextPath.toString()).append("/from");
		StringBuilder contextCCListPath = new StringBuilder().append(contextPath.toString()).append("/ccList");
		StringBuilder contextToListPath = new StringBuilder().append(contextPath.toString()).append("/toList");
		StringBuilder contextSubjectPath = new StringBuilder().append(contextPath.toString()).append("/subject");
		StringBuilder contextContentTypePath = new StringBuilder().append(contextPath.toString()).append("/contentType");

		String templateDesc = xmlService.getTextValue(templateDescPath.toString()); 
		String templateContent = xmlService.getTextValue(templateContentPath.toString());
		String contextFrom = xmlService.getTextValue(contextFromPath.toString());
		String contextCCList = xmlService.getTextValue(contextCCListPath.toString());
		String contextToList = xmlService.getTextValue(contextToListPath.toString());
		String contextSubject = xmlService.getTextValue(contextSubjectPath.toString());
		String contextContentType = xmlService.getTextValue(contextContentTypePath.toString());
		String template = xmlService.getTextValue(templatePath.toString());
		
		Map<String, String> miscelaneousMap = new HashMap<String, String>();
		
		NodeList nodeList = xmlService.getNodeListForPathUsingJavax("//template[@name='"+ templateName +"']/context/miscelaneous/*");
		if(nodeList != null && nodeList.getLength() >= 1){
			if(nodeList != null && nodeList.getLength() >= 1){
				for(int i=0; i<nodeList.getLength(); i++){
					Node node = nodeList.item(i);
					if(node.getNodeName().equals("config")){
						NodeList configs = node.getChildNodes();
						String key = configs.item(1).getTextContent();
						String value = configs.item(3).getTextContent();
						miscelaneousMap.put(key, value);
					}
				}
			}
		}
		logger.debug("Miscelaneous values for template bean : \n" + HunterUtility.stringifyMap(miscelaneousMap));  
		
		HunterEmailTemplateBean templateBean = new HunterEmailTemplateBean();
		templateBean.setCcList(contextCCList); 
		templateBean.setFromList(contextFrom);
		templateBean.setMiscelaneous(miscelaneousMap);
		templateBean.setSubject(contextSubject);
		templateBean.setTemplateContent(templateContent);
		templateBean.setTemplateDesc(templateDesc);
		templateBean.setTemplateName(templateName);
		templateBean.setToList(contextToList);
		templateBean.setContentType(contextContentType);
		templateBean.setTemplate(template); 
		
		logger.debug("Successfully created email template bean for template name ( " + templateName + " )");
		
		return templateBean;
		
	}
	
	public void populateUIMessages(){
		Map<String,String> uiMessages = new HashMap<>();
		logger.debug("populating ui messages..." ); 
		XMLService service = getXMLService(HunterConstants.UI_MSG_CACHED_SERVICE); 
		NodeList messages = service.getNodeListForXPath("//message");
		for(int i=0; i<messages.getLength(); i++){
			Node message = messages.item(i);
			if(message.getNodeName().equals("message")){
				String id = message.getAttributes().getNamedItem("id").getTextContent(); 
				NodeList metadata = message.getChildNodes();
				String desc = null;
				String text = null;
				for(int j=0; j<metadata.getLength(); j++){
					Node datum = metadata.item(j);
					if(datum.getNodeName().equals("desc"))
						desc = datum.getTextContent();
					else if(datum.getNodeName().equals("text")){
						text = datum.getTextContent();
					}
				}
				uiMessages.put(id+"_DESC", desc);
				uiMessages.put(id+"_TEXT", text);
			}
		}
		HunterCache.getInstance().put(HunterConstants.UI_MSG_CACHED_BEANS, uiMessages);
		logger.debug("Succesfully populated ui messages!!" );
	}
	
	public String getUIMsgTxtForMsgId(String msgId){
		@SuppressWarnings("unchecked")
		Map<String, String> uiMessages = (Map<String, String>)HunterCache.getInstance().get(HunterConstants.UI_MSG_CACHED_BEANS);
		return uiMessages.get(msgId+"_TEXT");
	}
	
	public String getUIMsgDescForMsgId(String msgId){
		@SuppressWarnings("unchecked")
		Map<String, String> uiMessages = (Map<String, String>)HunterCache.getInstance().get(HunterConstants.UI_MSG_CACHED_BEANS);
		return uiMessages.get(msgId+"_DESC");
	}
	
	public void loadCountries(){
		List<Country> countries = HunterDaoFactory.getInstance().getDaoObject(ReceiverRegionDao.class).getAllCountries(); 
		HunterCache.getInstance().put(HunterConstants.COUNTRIES, countries);
	}
	
	public List<Country> getAllCountries(){
		@SuppressWarnings("unchecked")
		List<Country> countries = (List<Country> )HunterCache.getInstance().get(HunterConstants.COUNTRIES); 
		return countries;
	}
	
	public void loadReceivers(){
		List<HunterMessageReceiver> hunterMessageReceivers_ = HunterDaoFactory.getInstance().getDaoObject(HunterMessageReceiverDao.class).getAllHunterMessageReceivers();
		HunterCache.getInstance().put(HunterConstants.RECEIVERS, hunterMessageReceivers_);
	}
	
	public List<HunterMessageReceiver> getAllReceivers(){
		@SuppressWarnings("unchecked")
		List<HunterMessageReceiver> hunterMessageReceivers_ = (List<HunterMessageReceiver>)HunterCache.getInstance().get(HunterConstants.RECEIVERS);
		return hunterMessageReceivers_;
	}
	
	public TaskClientConfigBean getTaskClientConfigBean (String key){
		Object obj = HunterCache.getInstance().get(key);
		TaskClientConfigBean configBean = null;
		if(obj != null && obj instanceof TaskClientConfigBean){
			configBean = (TaskClientConfigBean)obj;
		}
		return configBean;
	}
	
	
}