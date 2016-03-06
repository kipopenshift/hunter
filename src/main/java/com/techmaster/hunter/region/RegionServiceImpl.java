package com.techmaster.hunter.region;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.proc.ProcedureHandler;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterMessageReceiverDao;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.dao.types.TaskMessageReceiverDao;
import com.techmaster.hunter.json.ReceiverRegionJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.Constituency;
import com.techmaster.hunter.obj.beans.ConstituencyWard;
import com.techmaster.hunter.obj.beans.Country;
import com.techmaster.hunter.obj.beans.County;
import com.techmaster.hunter.obj.beans.HunterJacksonMapper;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.ReceiverRegion;
import com.techmaster.hunter.obj.beans.State;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterUtility;

public class RegionServiceImpl extends AbstractRegionService {
	
	@Autowired private TaskMessageReceiverDao taskMessageReceiverDao;
	@Autowired private ReceiverRegionDao receiverRegionDao;
	@Autowired private TaskDao taskDao;
	@Autowired private HunterJacksonMapper hunterJacksonMapper;
	@Autowired private HunterMessageReceiverDao hunterMessageReceiverDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private ProcedureHandler get_region_codes;
	
	private static final Logger logger = HunterLogFactory.getLog(RegionServiceImpl.class);

	@Override
	public boolean validateEqualityForCountry(Country country, Country newCountry) {
		
		String countryName = country.getCountryName();
		String newCountryName = newCountry.getCountryName();
		
		if(countryName == null || newCountryName == null ){
			return false;
		}
		
		if(countryName.equals(newCountryName)){ 
			return false;
		}
		
		return true;
	}

	@Override
	public boolean validateEqualityForCounty(County county, County newCounty) {
		
		// check countries.
		Long countryId = county.getCountryId();
		Long newCountyId = newCounty.getCountryId();
		
		if(countryId == null || newCounty == null)
			return false;
		
		// if they belong to different countries, it's valid
		if(!countryId.equals(newCountyId)){
			return true;
		}
		
		// check county names
		String countryName = county.getCountyName();
		String newCountyName = newCounty.getCountyName();
		
		if(countryName == null || newCountyName == null)
			return false;
		
		if(countryName.equals(newCountyName)){
			return false;
		}
		
		return true;
		
	}

	@Override
	public boolean validateEqualityForConstituency(Constituency constituency, Constituency newConstituency) {
		
		County county = HunterHibernateHelper.getEntityById(constituency.getCountyId(), County.class);
		County newCounty = HunterHibernateHelper.getEntityById(newConstituency.getCountyId(), County.class);
		boolean validateCounties = validateEqualityForCounty(county, newCounty);

		// if they belong to different counties, it's valid.
		if(!validateCounties){
			return true;
		}
		
		// check county names
		String constituencyName = constituency.getCnsttncyName();
		String newConstituencyName = newConstituency.getCnsttncyName();
			
		if(constituencyName == null || newConstituencyName == null)
			return false;
		
		if(constituencyName.equals(newConstituencyName)){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean validateEqualityForConstituencyWard( ConstituencyWard constituencyWard, ConstituencyWard newConstituencyWard) {
		
		Constituency constituencyWardConsituency = HunterHibernateHelper.getEntityById(constituencyWard.getConstituencyId(), Constituency.class);
		Constituency newConstituencyWardConsituency = HunterHibernateHelper.getEntityById(newConstituencyWard.getConstituencyId(), Constituency.class);
		
		boolean validateConsituencies = validateEqualityForConstituency(constituencyWardConsituency, newConstituencyWardConsituency);

		// if they belong to different constituencies, it's valid.
		if(!validateConsituencies){
			return true;
		}
		
		// check constituency wards names
		String constituencyWardName = constituencyWard.getWardName();
		String newConstituencyWardName = newConstituencyWard.getWardName();
			
		if(constituencyWardName == null || newConstituencyWardName == null)
			return false;
		
		if(constituencyWardName.equals(newConstituencyWardName)){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean validateEqualityForState(State state, State newState) {
		
		Country country = HunterHibernateHelper.getEntityById(state.getCountryId(), Country.class);
		Country newCountry = HunterHibernateHelper.getEntityById(newState.getCountryId(), Country.class);
		
		boolean validateCountries = validateEqualityForCountry(country, newCountry);
		
		// if they belong to different countries, it's valid.
		if(!validateCountries){
			return true;
		}
		
		// check constituency wards names
		String stateName = state.getStateName();
		String newStateName = newState.getStateName();
			
		if(stateName == null || newStateName == null)
			return false;
		
		if(stateName.equals(newStateName)){
			return false;
		}
		
		return true;
	}

	@Override
	public void addCountiesToCountry(Country country, List<County> counties) {
		logger.debug("Adding county to country");
		for(County county : counties){
			county.setCountryId(country.getCountryId()); 
		}
		HunterHibernateHelper.saveOrUpdateEntities(counties);
		logger.debug("Done adding counties to country");
	}

	@Override
	public void addConsituenciesToCounty(County county, List<Constituency> constituencies) {
		logger.debug("Adding constituency to county");
		for(Constituency constituency : constituencies){
			constituency.setCountyId(county.getCountyId());
		}
		HunterHibernateHelper.saveOrUpdateEntities(constituencies); 
		logger.debug("Done adding consituencies to county");	
	}

	@Override
	public void addConstituencyWardsToConsituency(Constituency constituency, List<ConstituencyWard> constituencyWards) {
		logger.debug("Adding wards to constituency");
		for(ConstituencyWard constituencyWard : constituencyWards){
			constituency.setCountyId(constituencyWard.getConstituencyId());
		}
		HunterHibernateHelper.saveOrUpdateEntities(constituencyWards); 
		logger.debug("Done adding constituency to county");			
	}

	@Override
	public void addCountiesToCountry(Long countryId, List<County> counties) {
		logger.debug("Adding ward to constituency");
		Country country = HunterHibernateHelper.getEntityById(countryId, Country.class);
		if(country != null){
			for(County county : counties){
				county.setCountryId(country.getCountryId()); 
			}
		}else{
			throw new IllegalArgumentException("Country of id provided is null!!");
		}
		HunterHibernateHelper.saveOrUpdateEntities(counties); 
		logger.debug("Done adding county to country");		
	}

	@Override
	public void addConsituenciesToCounty(Long countyId, List<Constituency> constituencies) {
		logger.debug("Adding consituencies to county...");
		County county = HunterHibernateHelper.getEntityById(countyId, County.class);
		if(county != null){
			for(Constituency constituency : constituencies){
				constituency.setCountyId(county.getCountyId());  
			}
		}else{
			throw new IllegalArgumentException("county of id provided is null!!");
		}
		HunterHibernateHelper.saveOrUpdateEntities(constituencies); 
		logger.debug("Done adding county to country");			
	}

	@Override
	public void addConstituencyWardsToConsituency(Long constituencyId, List<ConstituencyWard> constituencyWards) {
		logger.debug("Adding consituencyward to to consituency...");
		Constituency constituency = HunterHibernateHelper.getEntityById(constituencyId, Constituency.class);
		if(constituency != null){
			for(ConstituencyWard constituencyWard : constituencyWards){
				constituencyWard.setConstituencyId(constituency.getCnsttncyId());  
			}
		}else{
			throw new IllegalArgumentException("constituency of id provided is null!!");
		}
		HunterHibernateHelper.saveOrUpdateEntities(constituencyWards); 
		logger.debug("Done adding consituencywards to consituency");			
	}

	@Override
	public ReceiverRegion createReceiverRegion(String regionLevel, Long countryId, Long stateId, Long countyId, Long consituencyId, Long constituencyWardId) {
		ReceiverRegion receiverRegion = new ReceiverRegion();
		return receiverRegion;
	}

	@Override
	public Task deleteTaskReceiversForRegion(ReceiverRegion taskRegion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task addRandomReceiversToTask(List<HunterMessageReceiver> hunterMessageReceiver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populateRandomReceiversToCountry(Long countryId, int countryCount) {
		
		Country country = receiverRegionDao.getCountryById(countryId);
		
		if(country == null){
			logger.error("Could not populate country. No country found for countryId >> " + countryId);
			return;
		}
		
		logger.debug("Populating random receivers for country >> " + country);
		Long maxPhoneNum = hunterMessageReceiverDao.getMaxPhoneNumberForRnadomReceiverForCountry(country.getCountryName());
		String code = country.getCountryCode();
		
		if(maxPhoneNum == null){
			maxPhoneNum = HunterUtility.getLongFromObject(code + "700000001");
			logger.debug("No phone number seed found for the country. Generated a seed >> " + maxPhoneNum); 
		}else{
			maxPhoneNum++;
		}
		
		List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<HunterMessageReceiver>(countryCount);
		for(int i=0; i< countryCount; i++){
			HunterMessageReceiver hunterMessageReceiver = new HunterMessageReceiver();
			hunterMessageReceiver.setActive(true);
			hunterMessageReceiver.setAuditInfo(HunterUtility.getAuditInforForNow(new Date(), "hlangat01", new Date(), "hlangat01"));
			hunterMessageReceiver.setBlocked(false);
			hunterMessageReceiver.setCountryName(country.getCountryName()); 
			hunterMessageReceiver.setReceiverRegionLevelName(country.getCountryName()); 
			hunterMessageReceiver.setFailDeliveryTimes(0);
			hunterMessageReceiver.setReceiverContact(Long.toString(maxPhoneNum)); 
			maxPhoneNum++;
			hunterMessageReceiver.setReceiverRegionLevel(HunterConstants.RECEIVER_LEVEL_COUNTY); 
			hunterMessageReceiver.setReceiverType(HunterConstants.RECEIVER_TYPE_TEXT);
			hunterMessageReceivers.add(hunterMessageReceiver);
		}
		
		logger.debug("Successfully finished generating random receivers for country ( " + country.getCountryName() + " ) \n " + HunterUtility.stringifyList(hunterMessageReceivers));
		hunterMessageReceiverDao.insertHunterMessageReceivers(hunterMessageReceivers); 
		
	}

	@Override
	public void populateRandomReceiversToState(Long stateId, int stateCount) {
		
		State state = receiverRegionDao.getStateById(stateId);
		
		if(state == null){
			logger.error("Could not populate state. No state found for stateId >> " + stateId);
			return;
		}
		
		Country country = receiverRegionDao.getCountryById(state.getCountryId());
		logger.debug("State belongs to country >> " + country.getCountryName()); 
		
		logger.debug("Populating random receivers for state >> " + state);
		Long maxPhoneNum = hunterMessageReceiverDao.getMaxPhoneNumberForRnadomReceiverForCountry(state.getStateName());
		String code = country.getCountryCode();
		
		if(maxPhoneNum == null){
			maxPhoneNum = HunterUtility.getLongFromObject(code + "700000001");
			logger.debug("No phone number seed found for the country. Generated a seed >> " + maxPhoneNum); 
		}else{
			maxPhoneNum++;
		}
		
		List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<HunterMessageReceiver>(stateCount);
		
		for(int i=0; i< stateCount; i++){
			HunterMessageReceiver hunterMessageReceiver = new HunterMessageReceiver();
			hunterMessageReceiver.setActive(true);
			hunterMessageReceiver.setAuditInfo(HunterUtility.getAuditInforForNow(new Date(), "hlangat01", new Date(), "hlangat01"));
			hunterMessageReceiver.setBlocked(false);
			hunterMessageReceiver.setCountryName(country.getCountryName());
			hunterMessageReceiver.setFailDeliveryTimes(0);
			hunterMessageReceiver.setReceiverContact(Long.toString(maxPhoneNum));
			hunterMessageReceiver.setReceiverRegionLevelName(state.getStateName());
			maxPhoneNum++;
			hunterMessageReceiver.setReceiverRegionLevel(HunterConstants.RECEIVER_LEVEL_STATE); 
			hunterMessageReceiver.setReceiverType(HunterConstants.RECEIVER_TYPE_TEXT); 
			hunterMessageReceivers.add(hunterMessageReceiver);
		}
		
		logger.debug("Successfully finished generating random receivers for state ( " + state.getStateName() + " ) \n " + HunterUtility.stringifyList(hunterMessageReceivers));
	}

	@Override
	public void populateRandomReceiversToCounty(Long countyId, int countyCount) {
		
		String getCntryCntyCodeNameId = hunterJDBCExecutor.getQueryForSqlId("getCntryCntyCodeNameId");
		List<Object> values = new ArrayList<Object>();
		values.add(countyId);
		List<Map<String, Object>> cnsValues = hunterJDBCExecutor.executeQueryRowMap(getCntryCntyCodeNameId, values);
		Map<String, Object> rowMap = cnsValues.get(0);
		logger.debug(HunterUtility.stringifyMap(rowMap));
		
		if(rowMap == null || rowMap.isEmpty()){
			logger.error("Could not populate county. No state found for countyId >> " + countyId);
			return;
		}
		
		logger.debug("Populating random receivers for county >> " + rowMap.get("CNTY_NAM")); 
		Long maxPhoneNum = hunterMessageReceiverDao.getMaxPhoneNumberForRnadomReceiverForCountry(rowMap.get("CNTRY_NAM").toString());
		
		String code = rowMap.get("CNTY_CDE") != null ? rowMap.get("CNTY_CDE").toString() : null;
		
		if(code == null){
			code = rowMap.get("CNTRY_CODE") != null ? rowMap.get("CNTRY_CODE").toString() : null;
			logger.warn("County code is not set for county. Using country code to generate phone numbers >> country code ( " + code + " )");
		}
		
		if(maxPhoneNum == null){
			maxPhoneNum = HunterUtility.getLongFromObject(code + "700000001");
			logger.debug("No phone number seed found for the country. Generated a seed >> " + maxPhoneNum); 
		}else{
			maxPhoneNum++;
		}
		
		List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<HunterMessageReceiver>(countyCount);
		
		for(int i=0; i< countyCount; i++){
			HunterMessageReceiver hunterMessageReceiver = new HunterMessageReceiver();
			hunterMessageReceiver.setActive(true);
			hunterMessageReceiver.setAuditInfo(HunterUtility.getAuditInforForNow(new Date(), "hlangat01", new Date(), "hlangat01"));
			hunterMessageReceiver.setBlocked(false);
			hunterMessageReceiver.setCountryName(rowMap.get("CNTRY_NAM").toString());
			hunterMessageReceiver.setCountyName(rowMap.get("CNTY_NAM").toString());
			hunterMessageReceiver.setFailDeliveryTimes(0);
			hunterMessageReceiver.setReceiverContact(Long.toString(maxPhoneNum)); 
			hunterMessageReceiver.setReceiverRegionLevelName(rowMap.get("CNTY_NAM").toString()); 
			maxPhoneNum++;
			hunterMessageReceiver.setReceiverRegionLevel(HunterConstants.RECEIVER_LEVEL_COUNTY); 
			hunterMessageReceiver.setReceiverType(HunterConstants.RECEIVER_TYPE_TEXT); 
			hunterMessageReceivers.add(hunterMessageReceiver);
		}
		
		logger.debug("Successfully finished generating random receivers for county ( " + rowMap.get("CNTY_NAM") + " ) \n " + HunterUtility.stringifyList(hunterMessageReceivers));
		
	}

	@Override
	public void populateRandomReceiversToConstituency(Long constituencyId,int constituencyCount) {
		
		String getCntryNmIdAndCdeCntyIdNmAndCdeFrCnsttncyId = hunterJDBCExecutor.getQueryForSqlId("getCntryNmIdAndCdeCntyIdNmAndCdeFrCnsttncyId");
		List<Object> values = new ArrayList<Object>();
		values.add(constituencyId);
		List<Map<String, Object>> cnsValues = hunterJDBCExecutor.executeQueryRowMap(getCntryNmIdAndCdeCntyIdNmAndCdeFrCnsttncyId, values);
		Map<String, Object> rowMap = cnsValues.get(0);
		logger.debug(HunterUtility.stringifyMap(rowMap)); 
		
		Constituency constituency = receiverRegionDao.getConstituencyById(constituencyId);
		
		if(constituency == null){
			logger.error("Could not populate constituency. No state found for constituencyId >> " + constituencyId);
			return;
		}
		
		County county = receiverRegionDao.getCountyById(constituency.getCountyId()); 
		logger.debug("State belongs to county >> " + county.getCountyName()); 
		
		logger.debug("Populating random receivers for state >> " + county);
		Long maxPhoneNum = hunterMessageReceiverDao.getMaxPhoneNumberForRnadomReceiverForCountry(rowMap.get("CNTRY_NAM").toString());
		
		if(maxPhoneNum == null){
			maxPhoneNum = HunterUtility.getLongFromObject(rowMap.get("CNTRY_CODE") + "700000001"); 
			logger.debug("No phone number seed found for the country. Generated a seed >> " + maxPhoneNum); 
		}else{
			maxPhoneNum++;
		}
		
		List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<HunterMessageReceiver>(constituencyCount);
		
		for(int i=0; i< constituencyCount; i++){
			HunterMessageReceiver hunterMessageReceiver = new HunterMessageReceiver();
			hunterMessageReceiver.setActive(true);
			hunterMessageReceiver.setAuditInfo(HunterUtility.getAuditInforForNow(new Date(), "hlangat01", new Date(), "hlangat01"));
			hunterMessageReceiver.setBlocked(false);
			hunterMessageReceiver.setCountryName(rowMap.get("CNTRY_NAM").toString());
			hunterMessageReceiver.setCountyName(rowMap.get("CNTY_NAM").toString());
			hunterMessageReceiver.setConsName(rowMap.get("CNSTTNCY_NAM").toString());
			hunterMessageReceiver.setFailDeliveryTimes(0);
			hunterMessageReceiver.setReceiverContact(Long.toString(maxPhoneNum)); 
			hunterMessageReceiver.setReceiverRegionLevelName(rowMap.get("CNSTTNCY_NAM").toString()); 
			maxPhoneNum++;
			hunterMessageReceiver.setReceiverRegionLevel(HunterConstants.RECEIVER_LEVEL_STATE); 
			hunterMessageReceiver.setReceiverType(HunterConstants.RECEIVER_TYPE_TEXT); 
			hunterMessageReceivers.add(hunterMessageReceiver);
		}
		
		logger.debug("Successfully finished generating random receivers for country ( " + rowMap.get("CNTRY_NAM").toString() + " ) \n " + HunterUtility.stringifyList(hunterMessageReceivers));
		
	}

	@Override
	public void populateRandomReceiversToConstituencyWard(Long constituencyWardId, int constituencyWardCount) {
		
		String getCntryCntyConstyConswardIdCodeNameId = hunterJDBCExecutor.getQueryForSqlId("getCntryCntyConstyConswardIdCodeNameId");
		List<Object> values = new ArrayList<Object>();
		values.add(constituencyWardId);
		List<Map<String, Object>> cnsValues = hunterJDBCExecutor.executeQueryRowMap(getCntryCntyConstyConswardIdCodeNameId, values);
		Map<String, Object> rowMap = cnsValues.get(0);
		logger.debug(HunterUtility.stringifyMap(rowMap)); 
		
		
		if(rowMap == null || rowMap.isEmpty()){
			logger.error("Could not populate constituency. No state found for constituencyId >> " + constituencyWardId);
			return;
		}
		
		logger.debug("State belongs to constituency >> " + rowMap.get("CNSTTNCY_NAM")); 
		
		logger.debug("Populating random receivers for constituency ward >> " + rowMap.get("WRD_NAM"));
		Long maxPhoneNum = hunterMessageReceiverDao.getMaxPhoneNumberForRnadomReceiverForCountry(rowMap.get("CNTRY_NAM").toString());
		
		if(maxPhoneNum == null){
			maxPhoneNum = HunterUtility.getLongFromObject(rowMap.get("CNTRY_CODE") + "700000001"); 
			logger.debug("No phone number seed found for the country. Generated a seed >> " + maxPhoneNum); 
		}else{
			maxPhoneNum++;
		}
		
		List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<HunterMessageReceiver>(constituencyWardCount);
		
		for(int i=0; i< constituencyWardCount; i++){
			HunterMessageReceiver hunterMessageReceiver = new HunterMessageReceiver();
			hunterMessageReceiver.setActive(true);
			hunterMessageReceiver.setAuditInfo(HunterUtility.getAuditInforForNow(new Date(), "hlangat01", new Date(), "hlangat01"));
			hunterMessageReceiver.setBlocked(false);
			hunterMessageReceiver.setCountryName(rowMap.get("CNTRY_NAM").toString());
			hunterMessageReceiver.setCountyName(rowMap.get("CNTY_NAM").toString());
			hunterMessageReceiver.setConsName(rowMap.get("CNSTTNCY_NAM").toString());
			hunterMessageReceiver.setConsWardName(rowMap.get("WRD_NAM").toString());
			hunterMessageReceiver.setFailDeliveryTimes(0);
			hunterMessageReceiver.setReceiverContact(Long.toString(maxPhoneNum)); 
			maxPhoneNum++;
			hunterMessageReceiver.setReceiverRegionLevel(HunterConstants.RECEIVER_LEVEL_WARD); 
			hunterMessageReceiver.setReceiverRegionLevelName(rowMap.get("WRD_NAM").toString()); 
			hunterMessageReceiver.setReceiverType(HunterConstants.RECEIVER_TYPE_TEXT); 
			hunterMessageReceivers.add(hunterMessageReceiver);
		}
		
		logger.debug("Successfully finished generating random receivers for constituency ward ( " + rowMap.get("WRD_NAM") + " ) \n " + HunterUtility.stringifyList(hunterMessageReceivers));
		hunterMessageReceiverDao.insertHunterMessageReceivers(hunterMessageReceivers); 
		
	}

	@Override
	public Map<String, Object> getNamesAndIdsForRegionsNamesForWard( String countryName, String countyName, String constituencyName, String constituencyWardName) {
		logger.debug("Getting names and id for regions names for ward..."); 
		String query = hunterJDBCExecutor.getQueryForSqlId("getRegionIdsForNamesToWard");
		List<Object> values = new ArrayList<Object>();
		values.add(countryName);
		values.add(countyName);
		values.add(constituencyName);
		values.add(constituencyWardName);
		logger.debug("Executing query >> " + query); 
		logger.debug("With the values >> " + HunterUtility.stringifyList(values)); 
		List<Map<String, Object>> rowMaps = hunterJDBCExecutor.executeQueryRowMap(query, values);
		if(rowMaps.size() > 1){
			logger.error("Data expected to be only one row. Found multiple rows!! Please check the data!!");
		}
		logger.debug("Obtained the values : "); 
		for(Map<String, Object> rowMap : rowMaps){
			logger.debug(HunterUtility.stringifyMap(rowMap));  
		}
		return rowMaps.get(0);
	}

	@Override
	public Map<String, Object> getNamesAndIdsForRegionsNamesForConstituency(String countryName, String countyName, String constituencyName) {
		logger.debug("Getting names and id for regions names for constituency..."); 
		String query = hunterJDBCExecutor.getQueryForSqlId("getRegionIdsForNamesToCons");
		List<Object> values = new ArrayList<Object>();
		values.add(countryName);
		values.add(countyName);
		values.add(constituencyName);
		logger.debug("Executing query >> " + query); 
		logger.debug("With the values >> " + HunterUtility.stringifyList(values)); 
		List<Map<String, Object>> rowMaps = hunterJDBCExecutor.executeQueryRowMap(query, values);
		if(rowMaps.size() > 1){
			logger.error("Data expected to be only one row. Found multiple rows!! Please check the data!!");
		}
		logger.debug("Obtained the values : "); 
		for(Map<String, Object> rowMap : rowMaps){
			logger.debug(HunterUtility.stringifyMap(rowMap));  
		}
		return rowMaps.get(0);
	}

	@Override
	public Map<String, Object> getNamesAndIdsForRegionsNamesForCounty(String countryName, String countyName) {
		logger.debug("Getting names and id for regions names for county..."); 
		String query = hunterJDBCExecutor.getQueryForSqlId("getRegionIdsForNamesToCounty");
		List<Object> values = new ArrayList<Object>();
		values.add(countryName);
		values.add(countyName);
		logger.debug("Executing query >> " + query); 
		logger.debug("With the values >> " + HunterUtility.stringifyList(values)); 
		List<Map<String, Object>> rowMaps = hunterJDBCExecutor.executeQueryRowMap(query, values);
		if(rowMaps.size() > 1){
			logger.error("Data expected to be only one row. Found multiple rows!! Please check the data!!");
		}
		logger.debug("Obtained the values : "); 
		for(Map<String, Object> rowMap : rowMaps){
			logger.debug(HunterUtility.stringifyMap(rowMap));  
		}
		return rowMaps.get(0);
	}

	@Override
	public List<Map<String, Object>> getNmsAndIdsFrRgnNmsFrCnsttncs(String countryName, String countyName,List<String> constituencies) {
		
		logger.debug("Getting names and Id for regions names under given constituencies.."); 
		String quotedStr = HunterUtility.getCommaSeparatedSingleQuoteStrForList(constituencies);
		logger.debug("Converted constituencies to string >> " + quotedStr);
		String query = hunterJDBCExecutor.getQueryForSqlId("getRgnLvlIdsAndNamesForConstituencies");
		logger.debug("Using query : " + query);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(":countryName", HunterUtility.singleQuote(countyName)); 
		params.put(":constituencyNames", quotedStr);
		params.put(":coutryName", HunterUtility.singleQuote(countryName)); 
		
		List<Map<String, Object>> rowMaps = hunterJDBCExecutor.replaceAndExecuteQuery(query, params);
		return rowMaps;
	}

	@Override
	public void addHunterMessageReceiversToRegion(String country, String reginLevel, String regionLevelName, String contactInfo, AuditInfo auditInfo) {
		
	}

	@Override
	public void addHunterMessageRecersToRegion(List<Map<String, String>> params) {
				
	}

	@Override
	public ReceiverRegionJson creatRcvrRgnJsnFrmRcvrRgn(ReceiverRegion receiverRegion) {
		ReceiverRegionJson regionJson = hunterJacksonMapper.convertValue(receiverRegion, ReceiverRegionJson.class);
		logger.debug(regionJson);
		return regionJson;
	}
	
	private static String[] getChildren(String levelStr, Set<String> levels,String type ){
		String[] realChildren = null;
		for(String level : levels){
			if(level.startsWith(levelStr)){
				String[] children = level.split("::");
				int len = children.length;
				logger.debug("Children NO : " + len);
				if(type.equals(HunterConstants.RECEIVER_LEVEL_COUNTRY) && len > 1){
					realChildren = HunterUtility.initArrayAndInsert(realChildren, level);
				}else if(type.equals(HunterConstants.RECEIVER_LEVEL_COUNTY) && len > 2 ){
					realChildren = HunterUtility.initArrayAndInsert(realChildren, level);
				}else if(type.equals(HunterConstants.RECEIVER_LEVEL_CONSITUENCY) && len > 3 ){
					return children;
				}else if(type.equals(HunterConstants.RECEIVER_LEVEL_WARD) && len > 4 ){
					realChildren = HunterUtility.initArrayAndInsert(realChildren, level);
				}
			}
		}
		logger.debug("Found the children : " + HunterUtility.getCommaDelimitedStrings(realChildren)); 
		return realChildren;
	}
	
	private static void removeChildren(String[] children, Set<String> levels){
		for(String child : children){
			boolean removeChild = false;
			String chldRemov = null;
			for(String level : levels){
				if(child.equals(level)){
					removeChild = true;
					chldRemov = level;
					break;
				}
			}
			if(removeChild && chldRemov != null){
				levels.remove(chldRemov);
			}
		}
	}

	@Override
	public Object[] getTrueHntrMsgRcvrCntFrTaskRgns(Long taskId) {
		
		List<ReceiverRegionJson> receiverJsons = receiverRegionDao.getReceiverRegionsJsonByTaskId(taskId);
		Set<String> levels = new HashSet<>();

		for(ReceiverRegionJson json : receiverJsons){
			
			String country = json.getCountry();
			String county = json.getCounty();
			String cons = json.getConstituency();
			String ward = json.getWard();

			String cntryLevel = country;
			String cntyLevel = country + "::" + county;
			String consLevel = cntyLevel + "::" + cons;
			String wardLevel = consLevel + "::" + ward;
			
			boolean isCntryLevel = country != null && county == null && cons == null && ward == null;
			if(isCntryLevel && !levels.contains(cntryLevel)){ 
				levels.add(cntryLevel);
				json.setCurrentLevel(HunterConstants.RECEIVER_LEVEL_COUNTRY);
			}
			
			boolean isCntyLevel = county != null && cons == null && ward == null;
			if(isCntyLevel && !levels.contains(cntyLevel)){ 
				levels.add(cntyLevel);
				json.setCurrentLevel(HunterConstants.RECEIVER_LEVEL_COUNTY);
			}
			
			boolean isConsLevel = cons != null && ward == null;
			if(isConsLevel && !levels.contains(consLevel)){ 
				levels.add(consLevel);
				json.setCurrentLevel(HunterConstants.RECEIVER_LEVEL_CONSITUENCY);
			}
			
			boolean isWardLevel = ward != null;
			if(isWardLevel && !levels.contains(wardLevel)){ 
				levels.add(wardLevel);
				json.setCurrentLevel(HunterConstants.RECEIVER_LEVEL_WARD);
			}
			
			// If a region parent is there already, remove the children.
			
			boolean remove = false;
			String removeStr = null;
			
			if(isCntryLevel){
				
				Iterator<String> levelsItr = levels.iterator();
				String[] cntyChildren = null;
				
				while (levelsItr.hasNext()) {
				    String curLevel = levelsItr.next();
				    if( curLevel.startsWith(cntryLevel) && !curLevel.equals(cntryLevel) ){ 
				    	remove = true;
				    	removeStr = cntryLevel;
				    }else if(curLevel.startsWith(cntryLevel) && curLevel.equals(cntryLevel)){
				    	cntyChildren = getChildren(curLevel, levels, HunterConstants.RECEIVER_LEVEL_COUNTRY);
				    }
				}
				
				if(cntyChildren != null && cntyChildren.length > 0){
					removeChildren(cntyChildren, levels); 
				}
				
				if(remove){
					levels.remove(removeStr);
					remove = false;
				}
			}else if(isCntyLevel){
				
				Iterator<String> levelsItr = levels.iterator();
				String[] cntyChildren = null;
				
				while (levelsItr.hasNext()) {
				    String curLevel = levelsItr.next();
				    if( curLevel.startsWith(cntyLevel) && !curLevel.equals(cntyLevel) ){
				    	if(levels.contains(cntryLevel)){ 
				    		remove = true;
					    	removeStr = cntyLevel;
				    	}
				    }else if(curLevel.startsWith(cntyLevel) && curLevel.equals(cntyLevel)){
				    	cntyChildren = getChildren(curLevel, levels, HunterConstants.RECEIVER_LEVEL_COUNTY);
				    }
				}
				
				if(cntyChildren != null && cntyChildren.length > 0){
					removeChildren(cntyChildren, levels); 
				}
				
				if(remove){
					levels.remove(removeStr);
					remove = false;
				}
				
			}else if(isConsLevel){
				
				Iterator<String> levelsItr = levels.iterator();
				String[] cntyChildren = null;
				
				while (levelsItr.hasNext()) {
				    String curLevel = levelsItr.next();
				    if( curLevel.startsWith(consLevel) && !curLevel.equals(consLevel) ){ 
				    	if(levels.contains(cntyLevel)){
				    		remove = true;
					    	removeStr = consLevel;
				    	}
				    }else if(curLevel.startsWith(consLevel) && curLevel.equals(cntyLevel)){
				    	cntyChildren = getChildren(curLevel, levels, HunterConstants.RECEIVER_LEVEL_CONSITUENCY);
				    }
				}
				
				if(cntyChildren != null && cntyChildren.length > 0){
					removeChildren(cntyChildren, levels); 
				}
				
				if(remove){
					levels.remove(removeStr);
					remove = false;
				}
			}else if(isWardLevel){
				Iterator<String> levelsItr = levels.iterator();
				while (levelsItr.hasNext()) {
				    String curLevel = levelsItr.next();
				    if( curLevel.startsWith(consLevel) && !curLevel.equals(wardLevel) ){ 
				    	if(levels.contains(consLevel) || levels.contains(cntyLevel) || levels.contains(cntryLevel)){
				    		remove = true;
					    	removeStr = wardLevel;
				    	}
				    }
				}
				if(remove){
					logger.debug("Removing level > " + removeStr); 
					levels.remove(removeStr);
					remove = false;
				}
			}
			
		}
		
		logger.debug("Final count of unique regions : " + levels.size());
		int uniqueCount = 0;
		Set<String> dstnctPrcsdLvls = new HashSet<>();
		List<ReceiverRegionJson> countedRegionsJs = new ArrayList<>();
		
		for(ReceiverRegionJson receiverJson : receiverJsons){
			
			String jsonLevel = null;
			
			if(receiverJson.getCurrentLevel().equals(HunterConstants.RECEIVER_LEVEL_COUNTRY)){
				jsonLevel = receiverJson.getCountry();
			}else if(receiverJson.getCurrentLevel().equals(HunterConstants.RECEIVER_LEVEL_COUNTY)){
				jsonLevel = receiverJson.getCountry() + "::" + receiverJson.getCounty();
			}else if(receiverJson.getCurrentLevel().equals(HunterConstants.RECEIVER_LEVEL_CONSITUENCY)){
				jsonLevel = receiverJson.getCountry() + "::" + receiverJson.getCounty() + "::" + receiverJson.getConstituency();
			}else if(receiverJson.getCurrentLevel().equals(HunterConstants.RECEIVER_LEVEL_WARD)){
				jsonLevel = receiverJson.getCountry() + "::" + receiverJson.getCounty() + "::" + receiverJson.getConstituency() + "::" + receiverJson.getWard();
			}
			
			for(String level : levels){
				if(jsonLevel.equals(level)){
					if(!dstnctPrcsdLvls.contains(level)){
						uniqueCount += receiverJson.getReceiverCount();
						dstnctPrcsdLvls.add(level);
						countedRegionsJs.add(receiverJson);
					}
					break;
				}
			}
		}
		
		logger.debug("returnign totals count > " + uniqueCount);
		
		return new Object[]{uniqueCount, countedRegionsJs};
	}

	@Override
	public String addRegionToTask(Long taskId, String country, String county, String constituency, String ward) {
		
		String query = null;
		List<Object> values = new ArrayList<>();
		
		if(country != null && county == null && constituency == null && ward == null){
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY IS NULL and rgn.CNSTTNCY IS NULL and rgn.WRD IS NULL";
			values.add(country);
		}else if(country != null && county != null && constituency == null && ward == null){
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY = ? and rgn.CNSTTNCY IS NULL and rgn.WRD IS NULL";
			values.add(country);
			values.add(county);
		}else if(country != null && county != null && constituency != null && ward == null){
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY = ? and rgn.CNSTTNCY = ? and rgn.WRD IS NULL";
			values.add(country);
			values.add(county);
			values.add(constituency);
		}else if(country != null && county != null && constituency != null && ward != null){
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY = ? and rgn.CNSTTNCY = ? and rgn.WRD = ?";
			values.add(country);
			values.add(county);
			values.add(constituency);
			values.add(ward);
		}
		
		logger.debug("Executing query : " + query); 
		
		Map<Integer, List<Object>>  rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		if(rowMapList.size() > 1){
			logger.warn("Query returns more than one row!!!!! Using the first row..!");
		}else if(rowMapList.isEmpty()){
			logger.debug("No such region found!!! Returning...");
			return "No such region is found!" ;
		}
		
		Long regionId = null;
		
		for(Map.Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
			List<Object> idList = entry.getValue();
			regionId = HunterUtility.getLongFromObject(idList.get(0)); 
			break;
		}
		
		if(isRegionAlreadyAddedToTask(regionId, taskId)){
			logger.debug("Region is already added to task!! Will not attemp to add again! Returning...");
			return "Region is already added!";
		}
		
		String insert = "INSERT INTO TSK_RGNS (TSK_ID,RGN_ID) VALUES (:TSK_ID,:RGN_ID)";
		
		Map<String, Object> params = new HashMap<>();
		params.put(":TSK_ID", taskId);
		params.put(":RGN_ID", regionId);
		
		// there should be a method in the executor for replacing and inserting.
		hunterJDBCExecutor.replaceAndExecuteUpdate(insert, params); 
		return null;
		
	}
	
	

	@Override
	public boolean isRegionAlreadyAddedToTask(Long regionId, Long taskId) {
		
		String checkQuery = "SELECT COUNT(*) as CNT FROM TSK_RGNS tr WHERE tr.TSK_ID = ? AND tr.RGN_ID = ?";
		List<Object> values = new ArrayList<>();
		values.clear();
		values.add(taskId);
		values.add(regionId);
		
		Map<Integer, List<Object>>  cRowMapList = hunterJDBCExecutor.executeQueryRowList(checkQuery, values);
		int count = new Integer(cRowMapList.get(1).get(0) == null ? 0 : Integer.parseInt(cRowMapList.get(1).get(0)+""));  
		
		if(count >= 1){
			logger.debug("Region is already added to task!! Will not attemp to add again! Returning...");  
		}
	
		return count >= 1;
	}

	@Override
	public String removeTaskRegion(Long taskId, Long taskRegionId) {
		
		try{
		
			String existQuery = hunterJDBCExecutor.getQueryForSqlId("checkIfRegionAndTaskExist");
			Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(existQuery, hunterJDBCExecutor.getValuesList(new Object[]{taskId, taskRegionId, taskId, taskRegionId}));
			List<Object> rowList = rowMapList.get(1);
			StringBuilder builder = new StringBuilder();
			
			for(int i=0; i<rowList.size(); i++){
				Object obj = rowList.get(i);
				if(i == 0){
					int tskCount = obj != null ? Integer.parseInt(obj+"") : 0;
					if(tskCount == 0){
						builder.append("No task of id( " + taskId + " ) found!");
					}
				}else if(i==1){
					int regionCount = obj != null ? Integer.parseInt(obj+"") : 0;
					if(regionCount == 0){
						builder.append(", No region of id( " + taskRegionId + " ) found!");
					}
				}else if(i==2){
					int taskRgnCount = obj != null ? Integer.parseInt(obj+"") : 0;
					if(taskRgnCount == 0){
						builder.append(" , No task region comvination of task id( " + taskRegionId + " ) and region id ("+ taskRegionId + " ) found!");
					}
				}
				
			}
			
			String errMsg = builder.toString();
			errMsg = errMsg.startsWith(",") ? errMsg.substring(0, errMsg.length() - 1) : errMsg;
			
			if(errMsg.length() > 0){
				logger.debug("Error while removing task region from task : " + errMsg); 
				return errMsg;
			}
			
			String query = "DELETE FROM TSK_RGNS tr WHERE tr.TSK_ID = :TSK_ID AND tr.RGN_ID = :RGN_ID";
			Map<String, Object> params = new HashMap<>();
			params.put(":TSK_ID", taskId);
			params.put(":RGN_ID", taskRegionId);
			hunterJDBCExecutor.replaceAndExecuteUpdate(query, params);
			logger.debug("Successfully removed region from taskregions!!"); 
		
		}catch(Exception e){
			return e.getMessage();
		}
		
		return null;
	}

	
	@Override
	public void removeAllRegionsForTask(Long taskId) {
		String query = "DELETE FROM TSK_RGNS tr WHERE tr.TSK_ID = :TSK_ID ";
		Map<String, Object> params = new HashMap<>();
		params.put(":TSK_ID", taskId);
		hunterJDBCExecutor.replaceAndExecuteUpdate(query, params);
		logger.debug("Successfully removed region from taskregions!!");
	}

	@Override
	public List<Long> getRegionsIdForNames(String country, String county, String constituency, String ward) {
		
		 List<Long> regionIds = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		String query = null;
		
		// I am doing this because the hunter jdbc preparepared statement is not working well for null values!!
		
		if(ward != null){
			values.add(country);
			values.add(county);
			values.add(constituency);
			values.add(ward);
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY = ? and rgn.CNSTTNCY = ? and rgn.WRD = ?";
		}else if(constituency != null && ward == null){
			values.add(country);
			values.add(county);
			values.add(constituency);
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY = ? and rgn.CNSTTNCY = ? and rgn.WRD IS NULL";
		}else if(county != null && constituency == null && ward == null){
			values.add(country);
			values.add(county);
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY = ? and rgn.CNSTTNCY IS NULL  and rgn.WRD IS NULL";
		}else if(country != null && county != null && constituency == null && ward == null){
			values.add(country);
			query = "SELECT rgn.RGN_ID  FROM RCVR_RGN rgn WHERE rgn.CNTRY = ? and rgn.CNTY IS NULL and rgn.CNSTTNCY IS NULL and rgn.WRD IS NULL";
		}
		
		logger.debug("Executing query : " + query); 
		Map<Integer, List<Object>>  rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		
		if(rowMapList.isEmpty()){
			logger.debug("No such region found!!! Returning empty list...");
			return regionIds;
		}
		
		for(Map.Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
			List<Object> idList = entry.getValue();
			Long regionId = HunterUtility.getLongFromObject(idList.get(0)); 
			regionIds.add(regionId);
		}
		
		return regionIds;
	}


	
	
	
	
	
	
	
}
