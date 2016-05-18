package com.techmaster.hunter.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.types.HunterRawReceiverDao;
import com.techmaster.hunter.obj.beans.HunterRawReceiver;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.util.HunterHibernateHelper;

public class HunterRawReceiverDaoImpl implements HunterRawReceiverDao{
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public void insertHunterRawReceiver(HunterRawReceiver hunterRawReceiver) {
		logger.debug("Inserting hunter raw receiver...");
		HunterHibernateHelper.saveEntity(hunterRawReceiver);
		logger.debug("Successfully inserted hunter raw receiver!");
	}

	@Override
	public void insertHunterRawReceivers(List<HunterRawReceiver> hunterRawReceivers) {
		logger.debug("Inserting hunter raw receivers...");
		HunterHibernateHelper.saveEntities(hunterRawReceivers);
		logger.debug("Successfully inserted hunter raw receivers!");
	}

	@Override
	public void deleteHunterRawReceiver(HunterRawReceiver hunterRawReceiver) {
		logger.debug("Deleting hunter raw receiver...");
		HunterHibernateHelper.deleteEntity(hunterRawReceiver); 
		logger.debug("Finished deleting hunter raw receiver");
	}

	@Override
	public void deleteHunterRawReceiverById(Long rawReceiverId) {
		logger.debug("Deleting hunter raw receiver by id...");
		HunterRawReceiver hunterRawReceiver = HunterHibernateHelper.getEntityById(rawReceiverId, HunterRawReceiver.class);
		HunterHibernateHelper.deleteEntity(hunterRawReceiver);
		logger.debug("Finished deleting hunter raw receiver by id");
	}

	@Override
	public HunterRawReceiver getHunterRawReceiverById(Long rawReceiverId) {
		logger.debug("Getting hunter raw receivers by id...");
		String query = "FROM HunterRawReceiver h WHERE h.rawReceiverId = " + rawReceiverId;
		List<HunterRawReceiver> hunterRawReceivers  = HunterHibernateHelper.executeQueryForObjList(HunterRawReceiver.class, query);
		if(hunterRawReceivers.size() > 1) logger.debug("Warning!!! obtained more than one record. Will returned the first one in the list."); 
		logger.debug("Finished retrieving raw receivers : " + hunterRawReceivers.size());
		return hunterRawReceivers == null || hunterRawReceivers.isEmpty() ? null : hunterRawReceivers.get(0); 
	}

	@Override
	public List<HunterRawReceiver> getAllHunterRawReceivers() {
		logger.debug("Fetching all raw receivers...");
		List<HunterRawReceiver> hunterRawReceivers  = HunterHibernateHelper.getAllEntities(HunterRawReceiver.class);
		logger.debug("Finished retrieving all raw receivers : " + hunterRawReceivers.size());
		return hunterRawReceivers;
	}

	@Override
	public void updateHunterRawReceiver(HunterRawReceiver hunterRawReceiver) {
		logger.debug("Updating raw receiver : " + hunterRawReceiver);
		HunterHibernateHelper.updateEntity(hunterRawReceiver);
		logger.debug("Finished updating hunter raw receiver!");
	}

	@Override
	public List<HunterRawReceiver> getAllHunterRawReceiversByRawUser(HunterRawReceiverUser hunterRawReceiverUser) {
		logger.debug("Getting all rawreceivers for user : " + hunterRawReceiverUser.getRawUserName()); 
		List<HunterRawReceiver>  receivers = HunterHibernateHelper.executeQueryForObjList(HunterRawReceiver.class, "FROM HunterRawReceiver r WHERE r.givenByUserName ='" +hunterRawReceiverUser.getRawUserName()+"'");
		return receivers;
	}

	
	
	
}
