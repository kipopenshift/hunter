package com.techmaster.hunter.rawreceivers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.proc.ProcedureHandler;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterRawReceiverDao;
import com.techmaster.hunter.dao.types.HunterRawReceiverUserDao;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterRawReceiver;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class RawReceiverServiceImpl implements RawReceiverService {
	
	@Autowired private HunterRawReceiverDao hunterRawReceiverDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private HunterRawReceiverUserDao hunterRawReceiverUserDao;
	@Autowired private ProcedureHandler get_region_names_for_ids;
	@Autowired private RegionService regionService;
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public HunterRawReceiverUser registerRawReceiverUser(HunterRawReceiverUser rawReceiverUser) {
		logger.debug("Registering lograwReceiverUser ..."); 
		hunterRawReceiverUserDao.insertRawUser(rawReceiverUser); 
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
		List<HunterRawReceiver> existing = getAllRawReceiversForUser(hunterRawReceiverUserDao.getRawUserByUserName("admin"));
		boolean insert = existing != null && !existing.isEmpty();
		
		if(insert){
			return existing;
		}
		
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
			receiver.setGivenByUserName("rlangat");  
			receiver.setRawReceiverId(i); 
			receiver.setReceiverContact("73247048" + i); 
			receiver.setReceiverType("text"); 
			receiver.setReceiverVersion(1);
			receiver.setVerified(i%3 == 0 ? false : true);
			receiver.setVerifiedBy(i%3 == 0 ? null : "admin");  
			receiver.setVillage(null);
			receiver.setVillageId(0L); 
			receivers.add(receiver);
			setRegionIdsForReceiver(receiver);
		}
		
		
		if(!insert){
			hunterRawReceiverDao.insertHunterRawReceivers(receivers); 
		}
		
		return receivers;
	}


	@Override
	public List<HunterRawReceiver> getAllRawReceiversForUser(HunterRawReceiverUser hunterRawReceiverUser) {
		String query = "FROM HunterRawReceiver r WHERE r.givenByUserName = '" + hunterRawReceiverUser.getRawUserName() + "'";
		logger.debug("Executing query : " + query); 
		List<HunterRawReceiver> list = HunterHibernateHelper.executeQueryForObjList(HunterRawReceiver.class, query);
		if(list != null && !list.isEmpty()){
			logger.debug("Setting region ids for raw receivers..."); 
			for(HunterRawReceiver rawReceiver : list){
				setRegionIdsForReceiver(rawReceiver); 
			}
			logger.debug("Finished setting region IDS for raw receivers...");
		}
		logger.debug("Found ( " + list.size() + " ) raw receivers for user ( " + hunterRawReceiverUser.getRawUserName() + " )");  
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

	@Override
	public void setRegionIdsForReceiver(HunterRawReceiver hunterRawReceiver) {
		
		String countryName = hunterRawReceiver.getCountryName();
		String countyName = hunterRawReceiver.getCountyName();
		String consName = hunterRawReceiver.getConsName();
		String wardName = hunterRawReceiver.getConsWardName();
		
		String query = hunterJDBCExecutor.getQueryForSqlId("getRegionIdsForNamesToWard");
		List<Object> values = new ArrayList<>();
		values.add(countryName);
		values.add(countyName);
		values.add(consName);
		values.add(wardName);
		
		Map<String,Object> firstRow = hunterJDBCExecutor.executeQueryFirstRowMap(query, values);
		
		if(!firstRow.isEmpty()){
			hunterRawReceiver.setCountryId(HunterUtility.getLongFromObject(firstRow.get("CNTRY_ID")));
			hunterRawReceiver.setCountyId(HunterUtility.getLongFromObject(firstRow.get("CNTY_ID")));
			hunterRawReceiver.setConsId(HunterUtility.getLongFromObject(firstRow.get("CNSTTNCY_ID")));
			hunterRawReceiver.setConsWardId(HunterUtility.getLongFromObject(firstRow.get("WRD_ID"))); 
		}
		
	}

	@Override
	public JSONObject createOrUpdateRawReceiver(Map<String, String> rawReceiverData, AuditInfo auditInfo) {
		
		JSONObject json = new JSONObject();
		HunterRawReceiver receiver = new HunterRawReceiver();
		
		Map<String,Long> regionsIds = new HashMap<>();
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_COUNTRY, Long.parseLong(rawReceiverData.get("country")+""));
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_COUNTY, Long.parseLong(rawReceiverData.get("county")+""));
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_CONSITUENCY, Long.parseLong(rawReceiverData.get("cons")+""));
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_WARD, Long.parseLong(rawReceiverData.get("ward")+""));
		
		Map<String, Object> inParams = new HashMap<>();
		inParams.put("country_id", rawReceiverData.get("country"));
		inParams.put("county_id", rawReceiverData.get("county"));
		inParams.put("constituency_id", rawReceiverData.get("cons"));
		inParams.put("constituency_ward_id", rawReceiverData.get("ward"));
		
		Map<String, Object> countryNames = get_region_names_for_ids.execute_(inParams);
		logger.debug(HunterUtility.stringifyMap(countryNames));  
		
		String country = HunterUtility.getStringOrNullOfObj(countryNames.get("country_name")); 
		String county = HunterUtility.getStringOrNullOfObj(countryNames.get("county_name")); 
		String constituency = HunterUtility.getStringOrNullOfObj(countryNames.get("constituency_name"));
		String consWard = HunterUtility.getStringOrNullOfObj(countryNames.get("constituency_ward_name"));
		
		Long rawReceiverId = HunterUtility.getLongFromObject(rawReceiverData.get("id"));
		int version = 1;
		boolean update = false;
				
		if( rawReceiverId != null && rawReceiverId > 0 ){
			version = getRawReceiverVersion(receiver.getGivenByUserName());
			update = true;
		}
		
		try {
			
			receiver.setRawReceiverId(rawReceiverId);
			receiver.setReceiverContact(rawReceiverData.get("contact"));
			receiver.setReceiverType(rawReceiverData.get("conactType"));
			receiver.setFirstName(rawReceiverData.get("firstName"));
			receiver.setLastName(rawReceiverData.get("lastName"));
			receiver.setGivenByUserName(auditInfo.getLastUpdatedBy());
			receiver.setCountryName(country);
			receiver.setCountyName(county);
			receiver.setConsName(constituency);
			receiver.setConsWardName(consWard);
			receiver.setReceiverVersion(version); 
			receiver.setAuditInfo(HunterUtility.getAuditInfoFromRequestForNow(null, auditInfo.getLastUpdatedBy())); 
			
			receiver.setVerified(false); 
			receiver.setErrorMessage(null); 
			receiver.setVerifiedBy(null);
			receiver.setVillageId(null);
			receiver.setVillage(null); 
			
			Map<String, String> validate = validateRawReceiver(receiver);
			
			if( validate != null && !validate.isEmpty()){
				StringBuilder message = new StringBuilder();
				for(Map.Entry<String, String> entry : validate.entrySet()){
					String value = entry.getValue();
					message.append(value).append(",");
				}
				String msgStr = message.toString();
				if(msgStr.endsWith(",")){
					msgStr = msgStr.substring(0,msgStr.length() - 1);
				}
				HunterUtility.setJSONObjectForFailure(json, "Invalid data : " +msgStr);
			}else{
				if(update){
					hunterRawReceiverDao.updateHunterRawReceiver(receiver);
				}else{
					hunterRawReceiverDao.insertHunterRawReceiver(receiver);
				}
				HunterUtility.setJSONObjectForSuccess(json, "Successfully "+ ( update? "updated" : "created"  )+" contact!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			HunterUtility.setJSONObjectForFailure(json, "And error occurred while "+ ( update? "updating" : "creating"  )+" this contact! Please contact Hunter Support!");
		} 
		
		return json;
	}
	
	
	
	
	
	
	
	
	
	

}
