package com.techmaster.hunter.dao.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.types.ReceiverGroupDao;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.ReceiverGroup;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class ReceiverGroupDaoImpl implements ReceiverGroupDao{
	
	private Logger logger = Logger.getLogger(ReceiverRegionDaoImpl.class);

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
	public void deleteGroup(ReceiverGroup receiverGroup) {
		logger.debug("Deleting receiver group...");
		HunterHibernateHelper.deleteEntity(receiverGroup);
		logger.debug("Finished deleting group");
	}

	@Override
	public void updateGroup(ReceiverGroup update) {
		logger.debug("Upadting receiver group : " + update);		 
		HunterHibernateHelper.updateEntity(update);
		logger.debug("Finished updating receiver group!"); 
	}

	@Override
	public void removeReceiversFromGroup(Long groupId, List<HunterMessageReceiver> hntMsgRcvrs) {
		logger.debug("Removing receiver groups : " + HunterUtility.stringifyList(hntMsgRcvrs)); 
		ReceiverGroup receiverGroup = getReceiverGroupById(groupId);
		Set<HunterMessageReceiver> groupReceivers = receiverGroup.getHunterMessageReceivers();
		logger.debug("Existent groups before removing size ( " + groupReceivers.size() + " )");
		for(HunterMessageReceiver hunterMessageReceiver : hntMsgRcvrs){
			groupReceivers.remove(hunterMessageReceiver);
		}
		receiverGroup.setHunterMessageReceivers(groupReceivers);
		updateGroup(receiverGroup);
		logger.debug("Existent groups after updating size ( " + receiverGroup.getHunterMessageReceivers().size() + " )");
	}

	@Override
	public void addReceiversToGroup(Long groupId, List<HunterMessageReceiver> hntMsgRcvrs) {
		logger.debug("Adding receiver groups : " + HunterUtility.stringifyList(hntMsgRcvrs)); 
		ReceiverGroup receiverGroup = getReceiverGroupById(groupId);
		Set<HunterMessageReceiver> groupReceivers = receiverGroup.getHunterMessageReceivers();
		logger.debug("Existent groups before adding size ( " + groupReceivers.size() + " )");
		for(HunterMessageReceiver hunterMessageReceiver : hntMsgRcvrs){
			groupReceivers.add(hunterMessageReceiver);
		}
		receiverGroup.setHunterMessageReceivers(groupReceivers);
		updateGroup(receiverGroup);
		logger.debug("Existent groups after adding size ( " + receiverGroup.getHunterMessageReceivers().size() + " )");
	}

	

}
