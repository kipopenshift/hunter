package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.types.EmailTemplateObjDao;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.json.EmailTemplateObjJson;
import com.techmaster.hunter.obj.beans.EmailTemplateObj;
import com.techmaster.hunter.util.EmailTemplateUtil;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class EmailTemplateObjDaoImpl implements EmailTemplateObjDao{
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void insertTemplateObj(EmailTemplateObj templateObj) {
		logger.debug("Saving template object : " + templateObj);
		HunterHibernateHelper.saveEntity(templateObj); 
		logger.debug("Done saving template object");
	}

	@Override
	public EmailTemplateObj getTemplateObjById(Long templateId) {
		logger.debug("Getting template object by ID : " + templateId);
		EmailTemplateObj emailTemplateObj = HunterHibernateHelper.getEntityById(templateId, EmailTemplateObj.class);
		logger.debug("Done retrieving template object : " + emailTemplateObj);
		return emailTemplateObj;
	}

	@Override
	public List<EmailTemplateObj> getAllTemplateObjs() {
		logger.debug("Getting all template objects...");
		List<EmailTemplateObj> emailTemplateObjs = HunterHibernateHelper.getAllEntities(EmailTemplateObj.class);
		logger.debug("Done getting all template objects. Size( "+ emailTemplateObjs == null ? 0 : emailTemplateObjs.size() +" )");
		return emailTemplateObjs;
	}

	@Override
	public String deleteEmailTemplateObj(EmailTemplateObj emailTemplateObj) {
		logger.debug("Deleting template object : " + emailTemplateObj);
		String results = EmailTemplateUtil.getInstance().validateTemplateDeletion(emailTemplateObj);
		logger.debug(results); 
		if( results == null ){
			HunterHibernateHelper.deleteEntity(emailTemplateObj);
		}
		return results;
	}

	@Override
	public String deleteEmailTemplateObjById(Long templateId) {
		logger.debug("Deleting template object with id : " + templateId);
		EmailTemplateObj emailTemplateObj  = getTemplateObjById(templateId);
		return deleteEmailTemplateObj(emailTemplateObj);
	}

	@Override
	public List<EmailTemplateObjJson> getAllEmailTemplateObjJsons() {
		logger.debug("Getting all template object jsons...");
		List<EmailTemplateObjJson> emailTemplateObjs = new ArrayList<EmailTemplateObjJson>();
		HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
		String query = hunterJDBCExecutor.getQueryForSqlId("getAllEmailTemplateObjJsons");
		List<Map<String, Object>> rowListMap = hunterJDBCExecutor.executeQueryRowMap(query, null);
		if( HunterUtility.isCollectionNotEmpty(rowListMap) ){
			for(Map<String,Object> rowMap : rowListMap){
				EmailTemplateObjJson json = createJsonFromMap(rowMap);
				emailTemplateObjs.add(json);
			}
		}
		logger.debug("Done getting all template object jsons. Size( "+ emailTemplateObjs == null ? 0 : emailTemplateObjs.size() +" )");
		return emailTemplateObjs;
	}

	@Override
	public void updateEmailTemplateObj(EmailTemplateObj emailTemplateObj) {
		logger.debug("Updating template object...");
		HunterHibernateHelper.updateEntity(emailTemplateObj); 
		logger.debug("Updating template object");
	}

	@Override
	public EmailTemplateObjJson getEmailTemplateJsonById(Long templateId) {
		logger.debug("Retrieving email template json from id : " + templateId); 
		HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
		String query = hunterJDBCExecutor.getQueryForSqlId("getEmailTemplateObjJsonForId");
		List<Object> values = new ArrayList<>();
		values.add(templateId);
		Map<String, Object>  rowMap = hunterJDBCExecutor.executeQueryFirstRowMap(query, values);
		EmailTemplateObjJson json = createJsonFromMap(rowMap);
		logger.debug(json); 
		return json;
	}
	
	public EmailTemplateObjJson createJsonFromMap( Map<String,Object> rowMap ){
		EmailTemplateObjJson json = new EmailTemplateObjJson();
		json.setCreatedBy(HunterUtility.getStringOrNullOfObj(rowMap.get("LST_UPDTD_BY")));
		json.setCretDate(HunterUtility.getStringOrNullOfObj(rowMap.get("CRET_DATE")));
		json.setLastUpdate(HunterUtility.getStringOrNullOfObj(rowMap.get("LST_UPDT_DATE"))); 
		json.setLastUpdatedBy(HunterUtility.getStringOrNullOfObj(rowMap.get("LST_UPDTD_BY"))); 
		json.setTemplateDescription(HunterUtility.getStringOrNullOfObj(rowMap.get("TMPLT_DESC"))); 
		json.setTemplateId(HunterUtility.getLongFromObject(rowMap.get("TMPLT_ID"))); 
		json.setTemplateName(HunterUtility.getStringOrNullOfObj(rowMap.get("TMPLT_NAM")));
		json.setStatus( HunterUtility.getStringOrNullOfObj(rowMap.get("STS")) );
		
		return json;
	}

	@Override
	public List<EmailTemplateObj> getApprovedTemplateObjs() {
		logger.debug("Finished all approved template objects..."); 
		String query = "FROM EmailTemplateObj e WHERE e.status = 'Approved'";
		List<EmailTemplateObj> emailTemplateObjs = HunterHibernateHelper.executeQueryForObjList(EmailTemplateObj.class, query);
		logger.debug("Finished fetching email template objects. Size ( " + emailTemplateObjs.size() + " )");  
		return emailTemplateObjs;
	}
	
	
	

}
