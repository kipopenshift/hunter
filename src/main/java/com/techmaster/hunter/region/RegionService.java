package com.techmaster.hunter.region;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.techmaster.hunter.json.ReceiverRegionJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.Constituency;
import com.techmaster.hunter.obj.beans.ConstituencyWard;
import com.techmaster.hunter.obj.beans.Country;
import com.techmaster.hunter.obj.beans.County;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.ReceiverRegion;
import com.techmaster.hunter.obj.beans.State;
import com.techmaster.hunter.obj.beans.Task;



public interface RegionService {

	public boolean validateEqualityForCountry(Country country, Country newCountry);
	public boolean validateEqualityForCounty(County county, County newCounty);
	public boolean validateEqualityForConstituency(Constituency constituency, Constituency newConstituency);
	public boolean validateEqualityForConstituencyWard(ConstituencyWard constituencyWard, ConstituencyWard newConstituencyWard);
	public boolean validateEqualityForState(State state, State newState);
	
	public void addCountiesToCountry(Country country, List<County> county);
	public void addConsituenciesToCounty(County county, List<Constituency> constituency);
	public void addConstituencyWardsToConsituency(Constituency constituency, List<ConstituencyWard> constituencyWard);  
	
	public void addCountiesToCountry(Long countryId, List<County> county);
	public void addConsituenciesToCounty(Long countyId, List<Constituency> constituency);
	public void addConstituencyWardsToConsituency(Long constituencyId, List<ConstituencyWard> constituencyWard); 
	
	public ReceiverRegion createReceiverRegion(String regionLevel, Long countryId, Long stateId, Long countyId,Long consituencyId, Long constituencyWardId);
	public Task deleteTaskReceiversForRegion(ReceiverRegion taskRegion);
	public Task addRandomReceiversToTask(List<HunterMessageReceiver> hunterMessageReceivers);
	
	public void populateRandomReceiversToCountry(Long countryId, int countryCount);
	public void populateRandomReceiversToState(Long stateId, int stateCount);
	public void populateRandomReceiversToCounty(Long countyId, int countyCount);
	public void populateRandomReceiversToConstituency(Long constituencyId, int constituencyCount);
	public void populateRandomReceiversToConstituencyWard(Long constituencyWardId, int constituencyWardCount);
	
	public void addHunterMessageReceiversToRegion(String country, String reginLevel, String regionLevelName,String contactInfo, AuditInfo auditInfo);
	public void addHunterMessageRecersToRegion(List<Map<String, String>> params);
	
	public Map<String, Object> getNamesAndIdsForRegionsNamesForWard(String countryName, String countyName, String constituencyName, String constituencyWardName);
	public Map<String, Object> getNamesAndIdsForRegionsNamesForConstituency(String countryName, String countyName, String constituencyName);
	public Map<String, Object> getNamesAndIdsForRegionsNamesForCounty(String countryName, String countyName);
	public List<Map<String, Object>> getNmsAndIdsFrRgnNmsFrCnsttncs(String countryName, String countyName, List<String> constituencies);
	
	public List<Long> getRegionsIdForNames(String country, String county, String constituency, String ward);
	public String addRegionToTask(Long taskId, String country, String county, String constituency, String ward);
	public String removeTaskRegion(Long taskId, Long taskRegionId);
	public void removeAllRegionsForTask(Long taskId);
	public boolean isRegionAlreadyAddedToTask(Long regionId, Long taskId);
	
	public ReceiverRegionJson creatRcvrRgnJsnFrmRcvrRgn(ReceiverRegion receiverRegion);
	public Object[] getTrueHntrMsgRcvrCntFrTaskRgns(Long taskId);
	
	public void editReceiverRegion(Map<String,Object> params);
	
	public JSONArray getCountriesNameAndIds(String countryName);
	public JSONArray getCountiesNameAndIds(String countryName);
	public JSONArray getConsNameAndIds(String countryName,String countyName);
	public JSONArray getConsWardNameAndIds(String countryName,String countyName, String consName);
	
}

