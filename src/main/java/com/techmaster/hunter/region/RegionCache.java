package com.techmaster.hunter.region;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.types.HunterMessageReceiverDao;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.obj.beans.Country;
import com.techmaster.hunter.obj.beans.County;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.ReceiverRegion;
import com.techmaster.hunter.util.HunterUtility;

public class RegionCache {
	
	private static RegionCache instance;
	private static volatile List<Country> countries = new ArrayList<Country>();
	private static volatile List<HunterMessageReceiver> hunterMessageReceivers = new ArrayList<>();
	private static ReceiverRegionDao receiverRegionDao;
	private static HunterMessageReceiverDao hunterMessageReceiverDao;
	
	private static Logger logger = Logger.getLogger(RegionCache.class);
	
	static{
		if(instance == null){
			synchronized (RegionCache.class) {
				instance = new RegionCache();
			}
		}
	}

	public static void setReceiverRegionDao(ReceiverRegionDao receiverRegionDao, HunterMessageReceiverDao hunterMessageReceiverDao) {
		RegionCache.receiverRegionDao = receiverRegionDao;
		RegionCache.hunterMessageReceiverDao = hunterMessageReceiverDao;
		logger.debug("Successfully wired DAO for Region Cache!!");
		loadCountries();
		loadReceivers();
	}

	private static void loadReceivers() {
		logger.debug("Loading receivers cache..");
		hunterMessageReceivers.clear();
		List<HunterMessageReceiver> hunterMessageReceivers_ = hunterMessageReceiverDao.getAllHunterMessageReceivers();
		hunterMessageReceivers.addAll(hunterMessageReceivers_); 
		logger.debug("Done Loading receivers cache!!");
		logger.debug("Done Loading receivers cache!! Size ( " + hunterMessageReceivers.size() + " )");
	}

	private static void loadCountries(){
		logger.debug("Loading regions cache..");
		countries.clear();
		List<Country> countries_ = receiverRegionDao.getAllCountries();
		countries.addAll(countries_);
		logger.debug("Done Loading regions cache!! Size ( " + countries.size() + " )");
	}
	
	public static Set<County> getCountiesForCountryName(String countryName){
		Set<County> counties_ = new HashSet<>();
		Set<County> counties = new HashSet<>();;
		for(Country country : countries){
			if(country.getCountryName().equals(countryName)){
				counties = country.getCounties();
				break;
			}
		}
		counties_.addAll(counties);
		return counties_;
	}
	
	public static List<HunterMessageReceiver> getReceiversForWard(String countryName, String stateName,String countyName, String consName, String consWardName){
		List<HunterMessageReceiver> wardReceivers = new ArrayList<>();
		for(HunterMessageReceiver hunterMessageReceiver : hunterMessageReceivers){
			boolean isCountryNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountryName(), countryName);
			boolean isCountyNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountyName(), countyName);
			boolean isConsNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getConsName(), consName);
			boolean isConsWardNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getConsWardName(), consWardName);
			if(isCountryNotNullAndEqual && isCountyNotNullAndEqual && isConsNotNullAndEqual && isConsWardNotNullAndEqual){
				wardReceivers.add(hunterMessageReceiver);
			}
		}
		return wardReceivers;
	}
	
	public static List<HunterMessageReceiver> getReceiversForConstituency(String countryName, String stateName,String countyName, String consName){
		List<HunterMessageReceiver> wardReceivers = new ArrayList<>();
		for(HunterMessageReceiver hunterMessageReceiver : hunterMessageReceivers){
			boolean isCountryNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountryName(), countryName);
			boolean isCountyNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountyName(), countyName);
			boolean isConsNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getConsName(), consName);
			if(isCountryNotNullAndEqual && isCountyNotNullAndEqual && isConsNotNullAndEqual){
				wardReceivers.add(hunterMessageReceiver);
			}
		}
		return wardReceivers;
	}
	
	public static List<HunterMessageReceiver> getReceiversForCounty(String countryName, String stateName,String countyName){
		List<HunterMessageReceiver> wardReceivers = new ArrayList<>();
		for(HunterMessageReceiver hunterMessageReceiver : hunterMessageReceivers){
			boolean isCountryNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountryName(), countryName);
			boolean isCountyNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountyName(), countyName);
			if(isCountryNotNullAndEqual && isCountyNotNullAndEqual){
				wardReceivers.add(hunterMessageReceiver);
			}
		}
		return wardReceivers;
	}
	
	public static List<HunterMessageReceiver> getReceiversForCountry(String countryNam){
		List<HunterMessageReceiver> wardReceivers = new ArrayList<>();
		for(HunterMessageReceiver hunterMessageReceiver : hunterMessageReceivers){
			boolean isCountryNotNullAndEqual = HunterUtility.notNullNotEmptyAndEquals(hunterMessageReceiver.getCountryName(), countryNam);
			if(isCountryNotNullAndEqual){
				wardReceivers.add(hunterMessageReceiver);
			}
		}
		return wardReceivers;
	}
	
	public static int getReceiverCountForRegion(ReceiverRegion receiverRegion){
		List<HunterMessageReceiver> receivers = getReceiversForReceiverRegion(receiverRegion);
		return receivers.size();
	}
	
	public static List<HunterMessageReceiver> getReceiversForReceiverRegion(ReceiverRegion receiverRegion){
		List<HunterMessageReceiver> receivers = new ArrayList<>();
		if(receiverRegion.getCountry() != null && receiverRegion.getCounty() != null && receiverRegion.getConstituency() != null && receiverRegion.getWard() != null){
			receivers = getReceiversForWard(receiverRegion.getCountry(), receiverRegion.getState() ,receiverRegion.getCounty(), receiverRegion.getConstituency(), receiverRegion.getWard());
			return receivers;
		}else if(receiverRegion.getCountry() != null && receiverRegion.getCounty() != null && receiverRegion.getConstituency() != null && receiverRegion.getWard() == null){
			receivers = getReceiversForConstituency(receiverRegion.getCountry(), receiverRegion.getState() ,receiverRegion.getCounty(), receiverRegion.getConstituency());
			return receivers;
		}else if(receiverRegion.getCountry() != null && receiverRegion.getCounty() != null && receiverRegion.getConstituency() == null && receiverRegion.getWard() == null){
			receivers = getReceiversForCounty(receiverRegion.getCountry(), receiverRegion.getState() ,receiverRegion.getCounty());
			return receivers;
		}else if(receiverRegion.getCountry() != null && receiverRegion.getCounty() == null && receiverRegion.getConstituency() == null && receiverRegion.getWard() == null){
			receivers = getReceiversForCountry(receiverRegion.getCountry());
			return receivers;
		}
		return receivers;
	}
	
	public static Set<County> getCountiesForCountryId(Long countryId){
		Set<County> counties_ = new HashSet<>();
		Set<County> counties = new HashSet<>();;
		for(Country country : countries){
			if(country.getCountryId().equals(countryId)){
				counties = country.getCounties();
				break;
			}
		}
		counties_.addAll(counties);
		return counties_;
	}

	public static List<Country> getCountries(){
		List<Country> copy = new ArrayList<>();
		copy.addAll(countries);
		return copy;
	}
	
}
