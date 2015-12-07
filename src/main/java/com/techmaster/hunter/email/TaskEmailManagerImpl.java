package com.techmaster.hunter.email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.techmaster.hunter.obj.beans.Task;

public class TaskEmailManagerImpl extends AbstractEmailManager{

	@Autowired private MailSender mailSender;
	@Autowired private SimpleMailMessage templateMessage;
	private Logger logger = Logger.getLogger(TaskEmailManagerImpl.class);
	
	public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
	
    
    @Override
	public void send(Task task, String mailType) {
    	 SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
    	 msg.setTo("hillangat@gmail.com"); 
    	 msg.setText("Hello Kip, we are testing the email thing!!"); 
    	 
    	 try {
    		 this.mailSender.send(msg);
    		 logger.debug("Successfully sent email to >> " + msg.getTo()); 
		} catch (MailException e) {
			e.printStackTrace();
		}
    	
	}
	
    
    
    
}
