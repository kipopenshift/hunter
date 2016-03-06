package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.ReceiverGroupDao;
import com.techmaster.hunter.json.ReceiverGroupJson;
import com.techmaster.hunter.obj.beans.ReceiverGroup;
import com.techmaster.hunter.obj.converters.ReceiverGroupConverter;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class ReceiverGroupDaoImpl implements ReceiverGroupDao{
	
	private Logger logger = Logger.getLogger(ReceiverRegionDaoImpl.class);
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;

	@Override
	public void insertReceiverGroup(ReceiverGroup receiverGroup) {
		logger.debug("Inserting receiver group : " + receiverGroup); 
		HunterHibernateHelper.saveEntity(receiverGroup);
		logger.debug("Finished inserting receiver group!"); 
	}

	@Override
	public ReceiverGroup getReceiverGroupById(Long groupId) {
		logger.debug("Getting receiver group by Id : " + groupId); 
		ReceiverGroup group = HunterHibernateHelper.getEntityById(groupId, ReceiverGroup.class);
		logger.debug("Finished getting receiver group by id : " + group); 
		return group;
	}

	@Override
	public List<ReceiverGroup> getAllReceiverGroups() {
		logger.debug("Getting all receiver groups"); 
		List<ReceiverGroup> groups = HunterHibernateHelper.getAllEntities(ReceiverGroup.class);
		logger.debug("Finished getting all receiver groups ( " + groups.size() + " )");  
		return groups;
	}

	@Override
	public List<ReceiverGroup> getAllRcvrGrpForUsrNam(String userName) {
		String query = "FROM ReceiverGroup r WHERE r.ownerUserName = '" + userName + "'";
		logger.debug("Executing query : " + query); 
		List<ReceiverGroup> groups = HunterHibernateHelper.executeQueryForObjList(ReceiverGroup.class, query);
		logger.debug("Finished getting receiver groups by user name ( " + groups.size() + " )");
		return groups;
	}

	@Override
	public String deleteGroup(ReceiverGroup receiverGroup) {
		String isUsedQuery = "select ts.TSK_ID from TSK_GRPS ts where ts.GRP_ID = ?";
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(isUsedQuery, hunterJDBCExecutor.getValuesList(new Object[]{receiverGroup.getGroupId()}));
		List<Object> rowList = new ArrayList<>();
		for(Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
			List<Object> values = entry.getValue();
			rowList.addAll(values);
		}
		if(!rowList.isEmpty()){
			StringBuilder builder = new StringBuilder();
			for(Object obj : rowList){
				builder.append(obj);
				builder.append(",");
			}
			String idStr = builder.toString();
			idStr = idStr.endsWith(",") ? idStr.substring(0,idStr.length()-1) : idStr;
			String message = "Group is being used in Tasks ( " + idStr + " )";
			logger.debug("Cannot delete group since. " + message); 
			return message;
		}
		logger.debug("Deleting receiver group...");
		HunterHibernateHelper.deleteEntity(receiverGroup);
		logger.debug("Finished deleting group");
		return null;
	}

	@Override
	public void updateGroup(ReceiverGroup update) {
		logger.debug("Upadting receiver group : " + update);		 
		HunterHibernateHelper.updateEntity(update);
		logger.debug("Finished updating receiver group!"); 
	}


	@Override
	public List<ReceiverGroupJson> getAllGrouspJson() {
		logger.debug("Fetching all groups..."); 
		List<ReceiverGroup> receiverGroups = HunterHibernateHelper.getAllEntities(ReceiverGroup.class);
		logger.debug("Converting to jsons..."); 
		List<ReceiverGroupJson> receiverGroupJsons = ReceiverGroupConverter.getInstance().getGroupsJsons(receiverGroups);
		logger.debug("Done fetching grous. Size( " + receiverGroups.size() + " )");
		return receiverGroupJsons;
	}

	@Override
	public String getGroupImportBeansDetails(Long groupId) {
		logger.debug("Fetching import bean details for group id ( " + groupId + " )");  
		String query = hunterJDBCExecutor.getQueryForSqlId("getGroupImportBeanDetails");
		Map<Integer, List<Object>> rowMapLists = hunterJDBCExecutor.executeQueryRowList(query, Arrays.asList(new Object[]{groupId}));
		logger.debug("Found " + rowMapLists.size() + " records"); 
		StringBuilder builder = new StringBuilder().append("[");
		builder.append(getAllGroupReceiversJsonStr(groupId)).append(rowMapLists.isEmpty()? ""  : ",");  
		int indx = 0;
		for(Map.Entry<Integer, List<Object>> entry : rowMapLists.entrySet()){
			List<Object> rowList = entry.getValue(); 
			String fullName = HunterUtility.getNullOrStrimgOfObj( rowList.get(0));
			String originalFileName = HunterUtility.getNullOrStrimgOfObj( rowList.get(1));
			String creationDate = HunterUtility.getNullOrStrimgOfObj( rowList.get(2));
			String importId = HunterUtility.getNullOrStrimgOfObj( rowList.get(3));
			String status = HunterUtility.getNullOrStrimgOfObj( rowList.get(4));
			JSONObject json = new JSONObject();
			json.put("fullName", fullName);
			json.put("originalFileName", originalFileName);
			json.put("creationDate", creationDate);
			json.put("importId", importId);
			json.put("status", status);
			builder.append(json.toString());
			if(indx < rowMapLists.size() - 1){
				builder.append(",");
			}
			indx++;
			
		}
		builder.append("]");
		return builder.toString();
	}
	
	private String getAllGroupReceiversJsonStr(Long groupId){
		JSONObject json = new JSONObject();
		json.put("fullName", "Hunter Administrator");
		json.put("originalFileName", "All Receivers Excel Sheet");
		json.put("creationDate", HunterUtility.getNewDateString(HunterConstants.DATE_FORMAT_STRING)); 
		json.put("importId", "allReceivers");
		json.put("status", HunterConstants.STATUS_SUCCESS);
		return json.toString();
	}

	@Override
	public List<ReceiverGroup> getAllGroupsOfMsgType(String messageType) {
		logger.debug("Getting all groups of message type : " + messageType);
		String query = "FROM ReceiverGroup r where lower(r.receiverType) like lower('%"+ messageType +"%')";
		List<ReceiverGroup> groups = HunterHibernateHelper.executeQueryForObjList(ReceiverGroup.class, query);
		logger.debug("Successfully retrieved groups. Size ( " + (groups != null ? groups.size() : 0) + " )" );
		return groups;
	}
	

}
