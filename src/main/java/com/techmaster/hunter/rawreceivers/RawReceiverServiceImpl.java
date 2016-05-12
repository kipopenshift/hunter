package com.techmaster.hunter.rawreceivers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterRawReceiverDao;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterRawReceiver;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class RawReceiverServiceImpl implements RawReceiverService {
	
	@Autowired private HunterUserDao hunterUserDao;
	@Autowired private HunterRawReceiverDao hunterRawReceiverDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public HunterRawReceiverUser registerRawReceiverUser(HunterRawReceiverUser rawReceiverUser) {
		logger.debug("Registering lograwReceiverUser ..."); 
		hunterUserDao.insertHunterUser(rawReceiverUser); 
		logger.debug("Done registering lograwReceiverUser!!"); 
		return rawReceiverUser;
	}

	@Override
	public List<HunterRawReceiver> getPayableRawReceivers(HunterRawReceiverUser hunterRawReceiverUser) {
		return hunterRawReceiverDao.getAllHunterRawReceiversByRawUser(hunterRawReceiverUser);
	}


	@Override
	public int getRawReceiverVersion(String givenByUserName) {
		List<Object> values = new ArrayList<>();
		values.add("givenByUserName"); 
		Object nextVersion = hunterJDBCExecutor.executeQueryForOnReturn(hunterJDBCExecutor.getQueryForSqlId("getNextRawReceiverVersion"), values);
		if(nextVersion != null){
			return Integer.parseInt(nextVersion.toString());
		}
		logger.debug("Returning version one.."); 
		return 1;
	}

	@Override
	public Map<String, String> validateRawReceiver(HunterRawReceiver hunterRawReceiver) {
		return null;
	}
	

	@Override
	public Map<String, String> payRawReceiverUser(HunterRawReceiverUser rawReceiverUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HunterRawReceiver> getDefaultRawReceivers() {
		List<HunterRawReceiver>  receivers = new ArrayList<HunterRawReceiver>();
		for(int i=0; i<500; i++){
			HunterRawReceiver receiver = new HunterRawReceiver();
			receiver.setAuditInfo(HunterUtility.getAuditInfoFromRequestForNow(null, "admin"));
			receiver.setConsId(9L);
			receiver.setConsName("Bomet East");
			receiver.setConsWardId(37L);
			receiver.setConsWardName("Chemaner");
			receiver.setCountryId(1L);
			receiver.setCountryName("Kenya"); 
			receiver.setCountyId(4L); 
			receiver.setCountyName("Bomet"); 
			receiver.setErrorMessage("");
			receiver.setFirstName("Hillary " + i); 
			receiver.setLastName("Langat " + i); 
			receiver.setGivenByUserName("admin" + i); 
			receiver.setRawReceiverId(i); 
			receiver.setReceiverContact("73247048" + i); 
			receiver.setReceiverType("text"); 
			receiver.setReceiverVersion(1);
			receiver.setVerified(i%3 == 0 ? false : true);
			receiver.setVerifiedBy(i%3 == 0 ? null : "admin");  
			receiver.setVillage(null);
			receiver.setVillageId(0L); 
			receivers.add(receiver);
		}
		return receivers;
	}


	@Override
	public List<HunterRawReceiver> getAllRawReceiversForUser(HunterRawReceiverUser hunterRawReceiverUser) {
		String query = "FROM HunterRawReceiver r WHERE r.givenByUserName = '" + hunterRawReceiverUser.getUserName() + "'";
		logger.debug("Executing query : " + query); 
		List<HunterRawReceiver> list = HunterHibernateHelper.executeQueryForObjList(HunterRawReceiver.class, query);
		logger.debug("Found ( " + list.size() + " ) raw receivers for user ( " + hunterRawReceiverUser.getUserName() + " )");  
		return list;
	}

	@Override
	public HunterRawReceiver createRawReceiver(Map<String, String> params, AuditInfo auditInfo) {
		logger.debug("Creating hunter raw receiver..." + params.get("receiverContact"));  
		HunterRawReceiver receiver = new HunterRawReceiver();
		receiver.setRawReceiverId(0);
		receiver.setReceiverContact(params.get("receiverContact"));
		receiver.setReceiverType(params.get("receiverType"));
		receiver.setFirstName(params.get("firstName"));
		receiver.setLastName(params.get("lastName"));
		receiver.setCountryName(params.get("countryName"));
		receiver.setCountyName(params.get("countyName"));
		receiver.setConsName(params.get("consName"));
		receiver.setConsWardName(params.get("consWardName"));
		receiver.setVerified(Boolean.valueOf(params.get("verified")));
		receiver.setGivenByUserName(params.get("givenByUserName"));
		hunterRawReceiverDao.insertHunterRawReceiver(receiver); 
		logger.debug("Successfully creatred hunter raw receiver..." + params.get("receiverContact")); 
		return receiver;
	}

	@Override
	public HunterRawReceiver updateRawReceiver(Map<String, String> params, AuditInfo auditInfo) {
		String rawReceiverId = params.get("rawReceiverId");
		logger.debug("Updating contact with id : " + rawReceiverId + ", and contact : " + params.get("receiverContact")); 
		Long receiverId = HunterUtility.getLongFromObject(rawReceiverId); 
		HunterRawReceiver receiver = hunterRawReceiverDao.getHunterRawReceiverById(receiverId);
		receiver.setRawReceiverId(0);
		receiver.setReceiverContact(params.get("receiverContact"));
		receiver.setReceiverType(params.get("receiverType"));
		receiver.setFirstName(params.get("firstName"));
		receiver.setLastName(params.get("lastName"));
		receiver.setCountryName(params.get("countryName"));
		receiver.setCountyName(params.get("countyName"));
		receiver.setConsName(params.get("consName"));
		receiver.setConsWardName(params.get("consWardName"));
		receiver.setVerified(Boolean.valueOf(params.get("verified")));
		receiver.setGivenByUserName(params.get("givenByUserName"));
		receiver.setReceiverVersion(getRawReceiverVersion(params.get("givenByUserName")));
		receiver.getAuditInfo().setLastUpdate(new Date()); 
		receiver.getAuditInfo().setLastUpdatedBy(auditInfo.getLastUpdatedBy());
		hunterRawReceiverDao.insertHunterRawReceiver(receiver);
		logger.debug("Successfully updated contact!");
		return receiver;
	}
	
	

}
