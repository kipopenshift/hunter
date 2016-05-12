package com.techmaster.hunter.gateway.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.TaskProcessConstants;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.HunterEmailTemplateBean;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;

public class HunterEmailClientSenderBean {
	
	private String name;
	private Properties props;
	private Task task;
	private String[] receivers;
	private Logger logger = Logger.getLogger(HunterEmailClientSenderBean.class);
	
	public HunterEmailClientSenderBean(String name, Task task, String[] receivers) {
		super();
		if(!validatePropsName(name)){
			// if it's not found, set it to default.
			this.name = HunterConstants.CONFIG_HUNTER_DEFAULT_EMAIL_CONFIG_NAME;
			logger.debug("Did not find the props with the name given. Set it to default : " + name);
		}
		this.task = task;
		this.name = name;
		this.receivers = receivers;
		this.props = GateWayClientHelper.getInstance().getEmailConfigsByName(name);
	}
	
	private boolean validatePropsName(String propsName){
		String xPath = "//configs/config[@name=\""+ propsName +"\"]/@name";
		XMLService configService = HunterCacheUtil.getInstance().getXMLService(HunterConstants.EMAIL_CONFIG_CACHED_SERVICE);
		String name = configService.getTextValue(xPath); 
		if(HunterUtility.notNullNotEmpty(name)){
			logger.debug("configuration name found : " + name); 
			return name.equals(propsName);
		}else{
			logger.debug("Cannot find the properties with the name ( " + propsName + " )");  
		}
		return false;
	}
	
	private InternetAddress getInternetAddressFor(String address){
		try {
			return new InternetAddress(address);
		} catch (AddressException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Session getSession (){
	 Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String username = props.getProperty("userName"); 
                String password = props.getProperty("password");
                return new PasswordAuthentication(username,password); 
            }
        });
	 return session;
	}
	
	private InternetAddress[] getAddressesForType(String propName){
		logger.debug("Obtaining addresses : " + name + "..."); 
		InternetAddress[] addresses = null;
		String to = props.getProperty(propName);
		if(HunterUtility.notNullNotEmpty(to)){
			String[] tos = to.split(",");
			addresses = new InternetAddress[tos == null ? 0 : tos.length];
			for(int i=0; i<tos.length;i++){
				String to_ = tos[i];
				InternetAddress toInternetAddress = getInternetAddressFor(to_);
				if(HunterUtility.notNullNotEmpty(toInternetAddress)){
					addresses[i] = toInternetAddress;
				}
			}
		}
		logger.debug("Successfully obtained addresses for type : " + propName + "!"); 
		return addresses;
	}
	
	public InternetAddress[] getAllTaskAddresses(){
		InternetAddress[] addresses = null;
		if(receivers != null && receivers.length > 0){
			addresses = new InternetAddress[receivers.length];
			for(int i=0; i<receivers.length;i++){
				String receiver = receivers[i];
				InternetAddress address = getInternetAddressFor(receiver);
				addresses[i] = address;
			}
			logger.debug("Created internet addresses for task receivers. Size ( " + addresses.length +" )");
		}
		return addresses;
	}
	
	private void setRecipients(MimeMessage  msg,RecipientType type, InternetAddress[] recients){
		
		if(recients == null || recients.length < 1){
			logger.debug("Receivers passed in for ( " + type.toString() + " ) is : " + recients); 
			return;
		}
		
		logger.debug("Setting recipients for type : " + type);
		boolean isAllBcc = Boolean.valueOf(props.getProperty("isAllBcc") == null ? "true" : props.getProperty("isAllBcc"));
		if(isAllBcc && !type.equals(RecipientType.BCC) && !type.equals(RecipientType.TO)){
			logger.debug("All email configured must be sent as bcc."); 
			type = RecipientType.BCC;
		}else if(isAllBcc && type.equals(RecipientType.TO)){
			InternetAddress[] allAddresses = getAllTaskAddresses();
			InternetAddress[] combinedAddresses = null;
			if(allAddresses != null && allAddresses.length > 0){
				combinedAddresses = new InternetAddress[recients.length + allAddresses.length];
				int j = 0;
				for(int i=0; i<allAddresses.length;i++){
					InternetAddress allAddress = allAddresses[i];
					combinedAddresses[i] = allAddress;
					j = i;
				}
				for(int i=0; i<recients.length;i++){
					InternetAddress recipient = recients[i];
					combinedAddresses[j] = recipient;
				}
			}
			logger.debug("Combined receivers obtained : " + HunterUtility.getCommaDelimitedStrings(combinedAddresses) );
			recients = combinedAddresses;
		}
		try {
			recients = removeNulls(recients);
			if(recients != null && recients.length > 0){
				msg.setRecipients(type, recients);
			}
			logger.debug("Successfully set recipients for type : " + type); 
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private InternetAddress[] removeNulls(InternetAddress[] recipients) {
		if(recipients == null || recipients.length < 1){
			logger.debug("Null or empty recipients : " + recipients); 
			return new InternetAddress[0];
		}
		List<InternetAddress> recipientsList = new ArrayList<>();
		for(int i=0; i<recipients.length;i++){
			InternetAddress address = recipients[i];
			if(address != null){
				recipientsList.add(address);
			}
		}
		recipients = new InternetAddress[recipientsList.size()];
		for(int i=0; i<recipientsList.size();i++){
			recipients[i] = recipientsList.get(i);
		}
		return recipients;
	}

	private List<String> validateEmailTask(Task task){
		logger.debug("Validating email for email task processing..."); 
		List<String> errors = new ArrayList<String>();
		Message message = task.getTaskMessage();
		if(message != null){
			if(message instanceof EmailMessage){
				if(!HunterUtility.notNullNotEmpty(message.getMsgText())){
					errors.add("Message text does not exist");
				}else if(!message.getMsgLifeStatus().equals(HunterConstants.STATUS_APPROVED)){
					errors.add("Email message is not approved!");
				}
			}else{
				errors.add("Task is not an email task!"); 
			}
		}else{
			errors.add("Task does not have an email");
		}
		if(!errors.isEmpty()) 
			logger.debug("Task failed validation errors : " + HunterUtility.stringifyList(errors)); 
		else {
			logger.debug("Task passed validation!!"); 
		}
		return errors;
	}
	
	public BodyPart prepareBodyPart(Task task) throws MessagingException{
		BodyPart messageBodyPart = new MimeBodyPart();
		String templateName = ((EmailMessage)task.getTaskMessage()).getEmailTemplateName();
		logger.debug("Template name : " + templateName); 
		HunterEmailTemplateBean bean = HunterCacheUtil.getInstance().getEmailTemplateBean(templateName);
		String contentType = props.get(HunterConstants.CONFIG_TEXT_HTML_UTF8_KEY).toString();
		String emailContent = bean.getTemplate().replaceAll("#TEMPLATE_EMAIL_CONTENTS#", task.getTaskMessage().getMsgText());
		messageBodyPart.setContent(emailContent, contentType);
		return messageBodyPart;
	}
	
	public Multipart getBodyMultiPart(Task task, BodyPart bodyPart) throws MessagingException{
		logger.debug("Preparing message body..."); 
		Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart); 
        return multipart; 
	}
	
	public BodyPart getAttachmentMultiPart(Task task, BodyPart bodyPart, Multipart multiPart, String[] fileLocations) throws MessagingException{
		logger.debug("Preparing message attachment..."); 
		BodyPart attachmentBodyPart = new MimeBodyPart();
		if(fileLocations != null && fileLocations.length > 0){
			for(String fileLocation : fileLocations){
			     logger.debug("Preparing attachment for file location : " + fileLocation); 
		         DataSource source = new FileDataSource(fileLocation);
		         attachmentBodyPart.setDataHandler(new DataHandler(source));
		         attachmentBodyPart.setFileName(fileLocation);
		         multiPart.addBodyPart(attachmentBodyPart);
			}
		}
		return attachmentBodyPart;
	}
	
	@SuppressWarnings("static-access")
	public Map<String,Object> sendEmail() throws MessagingException{
		
		logger.debug("sending email for properties set : " + name + "...");  
		Map<String,Object> results = new HashMap<String, Object>();
		List<String> errors = validateEmailTask(task);
		
		if(!errors.isEmpty()){
			results.put(GatewayClient.TASK_PROCESS_STATUS, HunterConstants.STATUS_FAILED);
			results.put(GatewayClient.TASK_PROCESS_ERRORS, errors);
			results.put(TaskProcessConstants.ERROR_TYPE, TaskProcessConstants.ERROR_TYPE_VALIDATION);
			results.put(TaskProcessConstants.ERROR_TEXT, "Task validation errors encountered.");
			results.put(TaskProcessConstants.WORKER_STATUS, HunterConstants.STATUS_FAILED);
			return results;
		};
		
		try {
			
			MimeMessage msg = new MimeMessage(getSession());
			
			InternetAddress[] toList = getAddressesForType("to");
			InternetAddress[] ccList = getAddressesForType("cc");
			InternetAddress[] bccList = getAddressesForType("bcc");
			
			setRecipients(msg, RecipientType.TO, toList); 
			setRecipients(msg, RecipientType.BCC, ccList);
			setRecipients(msg, RecipientType.CC, bccList);
			
			String subject = ((EmailMessage)task.getTaskMessage()).geteSubject();
			subject = subject == null ? task.getTaskObjective() : subject;
			msg.setSubject(subject);
			InternetAddress from = getInternetAddressFor(props.getProperty("from")); 
			msg.setFrom(from);
			logger.debug("Email subject : " + subject); 
			
			 BodyPart bodyPart = prepareBodyPart(task);// creates mimebodypart and sets text
			 Multipart multiPart = getBodyMultiPart(task, bodyPart); // creates mimeMultiPart and add above mimebodypart to it.
			 
			 //getAttachmentMultiPart(task, bodyPart, multiPart, new String[]{HunterURLConstants.TESTING_ATTCHMENT_PATH}); // create another body part and add multipart above
			 
			 if(props.get(HunterConstants.CONFIG_TEXT_HTML_UTF8_KEY) != null){ 
				 String contentType = props.get(HunterConstants.CONFIG_TEXT_HTML_UTF8_KEY).toString();
				 logger.debug("Setting content type as : " + contentType); 
				 msg.setContent(multiPart, contentType);
			 }else{
				 msg.setContent(multiPart);
			 }
			 
			 logger.debug("Getting transport session and sending email..."); 
			 Transport transport = getSession().getTransport("smtp");
			 transport.send(msg);
			 logger.debug("Successfully finished sending email for properties set : " + name);
			 results.put(TaskProcessConstants.CONN_STATUS, HunterConstants.STATUS_SUCCESS);
			 results.put(GatewayClient.TASK_PROCESS_ERRORS, errors);
			 
			 results.put(TaskProcessConstants.WORKER_STATUS, HunterConstants.STATUS_SUCCESS);
			 results.put(GatewayClient.TASK_PROCESS_STATUS, HunterConstants.STATUS_SUCCESS);
			 return results;
			 
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(e.getMessage());
			results.put(TaskProcessConstants.CONN_STATUS, HunterConstants.STATUS_FAILED);
			results.put(GatewayClient.TASK_PROCESS_STATUS, HunterConstants.STATUS_FAILED);
			results.put(GatewayClient.TASK_PROCESS_ERRORS, errors);
			results.put(TaskProcessConstants.ERROR_TYPE, TaskProcessConstants.ERROR_TYPE_EXCEPTION);
			results.put(TaskProcessConstants.ERROR_TEXT, e.getMessage());
			results.put(TaskProcessConstants.WORKER_STATUS, HunterConstants.STATUS_FAILED);
			return results;
		}
		 
	}
	

}
