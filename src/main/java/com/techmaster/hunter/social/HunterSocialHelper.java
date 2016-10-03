package com.techmaster.hunter.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.HunterURLConstants;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.json.HunterSocialAppJson;
import com.techmaster.hunter.json.HunterSocialGroupJson;
import com.techmaster.hunter.json.HunterSocialRegionJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterSocialApp;
import com.techmaster.hunter.obj.beans.HunterSocialGroup;
import com.techmaster.hunter.obj.beans.HunterSocialRegion;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;
import com.techmaster.hunter.xml.XMLServiceImpl;
import com.techmaster.hunter.xml.XMLTree;

public class HunterSocialHelper {
	
	private static HunterSocialHelper instance;
	private static Logger logger = Logger.getLogger(HunterSocialHelper.class);
	private HunterSocialHelper(){}
	
	static{
		if( instance == null ){
			instance = new HunterSocialHelper();
		}
	}
	

	public static HunterSocialHelper getInstance(){
		return instance;
	}
	
	public XMLService getSocialAppConfig(HunterSocialApp socialApp){
		logger.debug("Getting social app configurations..."); 
		try {
			XMLService xmlService = new XMLServiceImpl(new XMLTree(HunterURLConstants.HUNTER_SOCIAL_APP_CONFIG_PATH, false));
			xmlService = socialApp.getAppConfigs() == null ? xmlService : new XMLServiceImpl(new XMLTree(HunterUtility.getBlobStr(socialApp.getAppConfigs()), true));  
			logger.debug("Successfully retrieved social app configurations : " + xmlService); 
			return xmlService;
		} catch (HunterRunTimeException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	public List<HunterSocialAppJson> getAllSocialAppsJsons(){
		logger.debug("Getting all social apps jsons...");
		List<HunterSocialApp>  socialApps = HunterHibernateHelper.getAllEntities(HunterSocialApp.class);
		List<HunterSocialAppJson> socialAppJsons = new ArrayList<>();
		if( HunterUtility.isCollectionNotEmpty(socialApps) ){
			for(HunterSocialApp socialApp : socialApps){
				HunterSocialAppJson socialAppJson = new HunterSocialAppJson();
				socialAppJson.setAppDesc(socialApp.getAppDesc());
				socialAppJson.setAppId(socialApp.getAppId()); 
				socialAppJson.setAppName(socialApp.getAppName()); 
				socialAppJson.setCreatedBy(socialApp.getAuditInfo().getCreatedBy()); 
				socialAppJson.setCretDate(socialApp.getAuditInfo().getCretDate()); 
				socialAppJson.setExtrnalPassCode(socialApp.getExtrnalPassCode()); 
				socialAppJson.setExtrnlId(socialApp.getExtrnlId()); 
				socialAppJson.setLastUpdate(socialApp.getAuditInfo().getLastUpdate());
				socialAppJson.setLastUpdatedBy(socialApp.getAuditInfo().getLastUpdatedBy()); 
				socialAppJson.setSocialType(socialApp.getSocialType()); 
				socialAppJsons.add(socialAppJson);
			}
		}
		logger.debug("Finished getting social apps jsons. Size : " + socialAppJsons.size()); 
		return socialAppJsons;
	}
	
	public HunterSocialApp createHunterSocialApp(HunterSocialApp socialApp){
		logger.debug("creating social app..." + socialApp); 
		HunterHibernateHelper.saveEntity(socialApp);
		logger.debug("Successfully created social app " + socialApp); 
		return socialApp;
	}
	
	public HunterSocialApp updateHunterSocialApp(HunterSocialApp socialApp){
		logger.debug("Updating social app..." + socialApp); 
		HunterHibernateHelper.updateEntity(socialApp); 
		logger.debug("Successfully updated social app!"); 
		return socialApp;
	}
	
	public void deleteHunterSocialApp(HunterSocialApp socialApp){
		logger.debug("Deleting social app : " + socialApp); 
		HunterHibernateHelper.deleteEntity(socialApp);
		logger.debug("Successfully deleted social app!"); 
	}
	
	public String tryDeleteSocialGroup(Long groupId){
		logger.debug("Deleting social group : " + groupId);
		HunterHibernateHelper.deleteEntityByLongId(groupId, HunterSocialGroup.class);
		logger.debug("Successfully deleted social app!"); 
		return null;
	}
	
	public void addSocialConfig(HunterSocialApp socialApp,String name, String value){
		XMLService appConfigs = getSocialAppConfig(socialApp);
		Document document = appConfigs.getXmlTree().getDoc();
		Element configs = document.getDocumentElement();
		Element config = document.createElement("config");
		config.setAttribute("name", name); 
		config.setTextContent(value);
		configs.appendChild(config);
	}
	
	public String getSocialConfig(HunterSocialApp socialApp,String name){
		XMLService appConfigs = getSocialAppConfig(socialApp);
		Document document = appConfigs.getXmlTree().getDoc();
		Element configs = document.getDocumentElement();
		NodeList configss = configs.getChildNodes();
		if( configss != null && configss.getLength() > 0 ){
			for(int i=0; i<configss.getLength();i++){
				Node node = configss.item(i);
				if( node.getNodeName().equals("config") && node.getAttributes().getNamedItem("name").toString().equals(name) ){ 
					return node.getTextContent();
				}
			}
		}
		return null;
	}
	
	public HunterSocialGroup createOrUpdateSocialGroup(HunterSocialGroupJson socialGroupJson, AuditInfo auditInfo){
		
		logger.debug("Converting json to social group : " + socialGroupJson); 
		
		HunterSocialGroup socialGroup = new HunterSocialGroup();
		HunterSocialRegion socialRegion = HunterHibernateHelper.getEntityById(socialGroupJson.getRegionId(), HunterSocialRegion.class);
		
		if( socialGroupJson.getGroupId() != null && socialGroupJson.getGroupId() != 0 ){
			socialGroup = HunterHibernateHelper.getEntityById(socialGroupJson.getGroupId(), HunterSocialGroup.class);
			auditInfo.setCreatedBy(socialGroup.getAuditInfo().getCreatedBy()); 
			auditInfo.setCretDate(socialGroup.getAuditInfo().getCretDate()); 
		}
		
		socialGroup.setSocialRegion(socialRegion);
		socialGroup.setAcquired(socialGroupJson.isAcquired());
		socialGroup.setAcquiredFromFullName(socialGroupJson.getAcquiredFromFullName());
		socialGroup.setActive(socialGroupJson.isActive());
		socialGroup.setAuditInfo(auditInfo);
		socialGroup.setClientUserName(socialGroupJson.getClientUserName()); 
		socialGroup.setExistent(socialGroupJson.isExistent()); 
		socialGroup.setExternalId(socialGroupJson.getExternalId()); 
		socialGroup.setGroupDescription(socialGroupJson.getGroupDescription());
		socialGroup.setGroupName(socialGroupJson.getGroupName()); 
		socialGroup.setHunterGroupAdmin(socialGroupJson.getHunterGroupAdmin());
		socialGroup.setHunterOwned(socialGroupJson.isHunterOwned());
		socialGroup.setReceiversCount(socialGroupJson.getReceiversCount());
		socialGroup.setSocialType(socialGroupJson.getSocialType()); 
		socialGroup.setSuspended(socialGroupJson.isSuspended()); 
		socialGroup.setSuspensionDescription(socialGroupJson.getSuspensionDescription()); 
		socialGroup.setVerifiedBy(socialGroupJson.getVerifiedBy()); 
		
		String verifiedDate = socialGroupJson.getVerifiedDate();
		socialGroup.setVerifiedDate(verifiedDate == null ? null : HunterUtility.parseDate( verifiedDate, HunterConstants.DATE_FORMAT_STRING )); 
		
		logger.debug("Successfully converted the json. Resultant group : " + socialGroup); 
		
		if( socialGroup.getGroupId() != null &&  socialGroup.getGroupId() != 0){
			logger.debug("Updating the social group...");
			HunterHibernateHelper.updateEntity(socialGroup); 
		}else{
			logger.debug("Creating the social group...");
			HunterHibernateHelper.saveEntity(socialGroup); 
		}
		
		return socialGroup;
	}
	
	private Map<String, String> getSocialRegionNames(HunterSocialRegion socialRegion){
		
		logger.debug("Getting region names for region : " + socialRegion ); 
		
		Map<String,String> regionNames = new HashMap<>();
		
		if( socialRegion == null )
			return regionNames;
		
		Map<String,Long> regionsIds = new HashMap<>();
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_COUNTRY, socialRegion.getCountryId());
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_COUNTY, socialRegion.getCountyId());
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_CONSITUENCY, socialRegion.getConsId());
		regionsIds.put(HunterConstants.RECEIVER_LEVEL_WARD, socialRegion.getWardId());
		
		Map<Long,String> country = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_COUNTRY, regionsIds);
		Map<Long,String> county = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_COUNTY, regionsIds);
		Map<Long,String> constituency = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_CONSITUENCY, regionsIds);
		Map<Long,String> ward = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_WARD, regionsIds);
		
		String countryName = country.get(regionsIds.get(HunterConstants.RECEIVER_LEVEL_COUNTRY)); 
		String countyName = county.get(regionsIds.get(HunterConstants.RECEIVER_LEVEL_COUNTY));
		String constituencyName = constituency.get(regionsIds.get(HunterConstants.RECEIVER_LEVEL_CONSITUENCY));
		String wardName = ward.get(regionsIds.get(HunterConstants.RECEIVER_LEVEL_WARD));
		
		regionNames.put(HunterConstants.RECEIVER_LEVEL_COUNTRY, countryName);
		regionNames.put(HunterConstants.RECEIVER_LEVEL_COUNTY, countyName);
		regionNames.put(HunterConstants.RECEIVER_LEVEL_CONSITUENCY, constituencyName);
		regionNames.put(HunterConstants.RECEIVER_LEVEL_WARD, wardName);
		
		logger.debug("Region names retrieved : " + HunterUtility.stringifyMap(regionNames) ); 
		
		return regionNames;
	}

	public List<HunterSocialGroupJson> getAllSocialGroupsJsons(){
		
		logger.debug("Getting all social group jsons..." ); 
		
		List<HunterSocialGroup> socialGroups = HunterHibernateHelper.getAllEntities(HunterSocialGroup.class);
		List<HunterSocialGroupJson> socialGroupJsons = new ArrayList<HunterSocialGroupJson>();
		
		if( HunterUtility.isCollectionNotEmpty(socialGroups) ){
			
			for(HunterSocialGroup socialGroup : socialGroups){
				
				HunterSocialGroupJson socialGroupJson = new HunterSocialGroupJson();
				socialGroupJson.setAcquired(socialGroup.isAcquired()); 
				socialGroupJson.setAcquiredFromFullName(socialGroup.getAcquiredFromFullName());
				socialGroupJson.setActive(socialGroup.isActive());
				socialGroupJson.setClientUserName(socialGroup.getClientUserName()); 
				socialGroupJson.setExistent(socialGroup.isExistent());
				socialGroupJson.setExternalId(socialGroup.getExternalId());
				socialGroupJson.setGroupDescription(socialGroup.getGroupDescription());
				socialGroupJson.setGroupId(socialGroup.getGroupId());
				socialGroupJson.setGroupName(socialGroup.getGroupName());
				socialGroupJson.setHunterGroupAdmin(socialGroup.getHunterGroupAdmin());
				socialGroupJson.setHunterOwned(socialGroup.isHunterOwned());
				socialGroupJson.setReceiversCount(socialGroup.getReceiversCount());
				socialGroupJson.setSocialType(socialGroup.getSocialType());
				socialGroupJson.setSuspended(socialGroup.isSuspended());
				socialGroupJson.setSuspensionDescription(socialGroup.getSuspensionDescription());
				socialGroupJson.setVerifiedBy(socialGroup.getVerifiedBy());
				socialGroupJson.setVerifiedDate(HunterUtility.formatDate(socialGroup.getVerifiedDate(), HunterConstants.DATE_FORMAT_STRING));  
				socialGroupJson.setStatus(socialGroup.getStatus()); 
				
				socialGroupJson.setCreatedBy(socialGroup.getAuditInfo().getCreatedBy());
				socialGroupJson.setCretDate(HunterUtility.formatDate(socialGroup.getAuditInfo().getCretDate(), HunterConstants.DATE_FORMAT_STRING));
				socialGroupJson.setLastUpdate(HunterUtility.formatDate(socialGroup.getAuditInfo().getLastUpdate(), HunterConstants.DATE_FORMAT_STRING));
				socialGroupJson.setLastUpdatedBy(socialGroup.getAuditInfo().getLastUpdatedBy());
				
				
				
				Map<String,String> regionNames = getSocialRegionNames(socialGroup.getSocialRegion());
				
				String 
				countryName 		= regionNames.get(HunterConstants.RECEIVER_LEVEL_COUNTRY), 
				countyName 			= regionNames.get(HunterConstants.RECEIVER_LEVEL_COUNTY),
				constituencyName 	= regionNames.get(HunterConstants.RECEIVER_LEVEL_CONSITUENCY),
				wardName 			= regionNames.get(HunterConstants.RECEIVER_LEVEL_WARD);
				
				socialGroupJson.setRegionCountryName(countryName);
				socialGroupJson.setRegionCountyName(countyName);
				socialGroupJson.setRegionWardName(wardName);
				socialGroupJson.setRegionConsName(constituencyName);
				
				socialGroupJson.setReceiversCount(socialGroup.getSocialRegion().getPopulation());
				socialGroupJson.setRegionAssignedTo(socialGroup.getSocialRegion().getAssignedTo());
				socialGroupJson.setRegionCoordinates(HunterUtility.getBlobStr(socialGroup.getSocialRegion().getCoordinates())); 
				socialGroupJson.setRegionDesc(socialGroup.getSocialRegion().getRegionDesc());
				socialGroupJson.setRegionId(socialGroup.getSocialRegion().getRegionId());
				socialGroupJson.setRegionName(socialGroup.getSocialRegion().getRegionName());
				socialGroupJson.setRegionPopulation(socialGroup.getSocialRegion().getPopulation());
				
				socialGroupJsons.add(socialGroupJson);
				
			}
		}
		
		logger.debug("Returning groups. Size ( "+ socialGroupJsons.size() +" )" ); 
		
		return socialGroupJsons;
	}
	
	
	
	public List<HunterSocialRegionJson> getAllSocialRegionsJsons(){
		
		List<HunterSocialRegion> socialRegions = HunterHibernateHelper.getAllEntities(HunterSocialRegion.class);
		List<HunterSocialRegionJson> socialRegionJsons = new ArrayList<>();
		
		for(HunterSocialRegion socialRegion : socialRegions){
			
			Map<String,String> regionNames = getSocialRegionNames(socialRegion);
			HunterSocialRegionJson socialRegionJson = new HunterSocialRegionJson();
			
			String 
			countryName 		= regionNames.get(HunterConstants.RECEIVER_LEVEL_COUNTRY), 
			countyName 			= regionNames.get(HunterConstants.RECEIVER_LEVEL_COUNTY),
			constituencyName 	= regionNames.get(HunterConstants.RECEIVER_LEVEL_CONSITUENCY),
			wardName 			= regionNames.get(HunterConstants.RECEIVER_LEVEL_WARD);
			
			socialRegionJson.setCountryName(countryName);
			socialRegionJson.setCountyName(countyName);
			socialRegionJson.setWardName(wardName);
			socialRegionJson.setConsName(constituencyName);
			
			socialRegionJson.setCountryId(socialRegion.getCountryId());
			socialRegionJson.setCountyId(socialRegion.getCountyId()); 
			socialRegionJson.setConsId(socialRegion.getConsId()); 
			socialRegionJson.setWardId(socialRegion.getWardId()); 
			
			socialRegionJson.setPopulation(socialRegion.getPopulation());
			socialRegionJson.setAssignedTo(socialRegion.getAssignedTo());
			socialRegionJson.setCoordinates(HunterUtility.getBlobStr(socialRegion.getCoordinates())); 
			socialRegionJson.setRegionDesc(socialRegion.getRegionDesc());
			socialRegionJson.setRegionId(socialRegion.getRegionId());
			socialRegionJson.setRegionName(socialRegion.getRegionName());
			socialRegionJson.setPopulation(socialRegion.getPopulation());
			
			socialRegionJson.setCreatedBy(socialRegion.getAuditInfo().getCreatedBy());
			socialRegionJson.setCretDate(HunterUtility.formatDate(socialRegion.getAuditInfo().getCretDate(), HunterConstants.DATE_FORMAT_STRING));
			socialRegionJson.setLastUpdate(HunterUtility.formatDate(socialRegion.getAuditInfo().getLastUpdate(), HunterConstants.DATE_FORMAT_STRING));
			socialRegionJson.setLastUpdatedBy(socialRegion.getAuditInfo().getLastUpdatedBy());
			
			socialRegionJsons.add(socialRegionJson);
		}
		
		return socialRegionJsons;
	}

	public HunterSocialRegion createOrUpdateSocialRegion( HunterSocialRegionJson socialRegionJson, AuditInfo auditInfo ) {
		
		HunterSocialRegion socialRegion = new HunterSocialRegion();
		
		if( socialRegionJson.getRegionId() != null && socialRegionJson.getRegionId() != 0 ){
			socialRegion = HunterHibernateHelper.getEntityById( socialRegionJson.getRegionId(), HunterSocialRegion.class);
			auditInfo.setCreatedBy(socialRegionJson.getCreatedBy()); 
			auditInfo.setCretDate(HunterUtility.parseDate(socialRegionJson.getCretDate(), HunterConstants.DATE_FORMAT_STRING));  
		}
		
		socialRegion.setAssignedTo(socialRegionJson.getAssignedTo());
		socialRegion.setAuditInfo(auditInfo); 
		socialRegion.setConsId(socialRegionJson.getConsId()); 
		socialRegion.setCountryId(socialRegionJson.getCountryId()); 
		socialRegion.setCountyId(socialRegionJson.getCountyId()); 
		socialRegion.setPopulation(socialRegionJson.getPopulation()); 
		socialRegion.setRegionDesc(socialRegionJson.getRegionDesc());
		socialRegion.setRegionId(socialRegionJson.getRegionId());
		socialRegion.setRegionName(socialRegionJson.getRegionName()); 
		socialRegion.setWardId(socialRegionJson.getWardId()); 
		
		logger.debug("Successfully converted the json. Resultant social region : " + socialRegion); 
		
		if( socialRegionJson.getRegionId() != null &&  socialRegionJson.getRegionId() != 0){
			logger.debug("Updating the social region...");
			HunterHibernateHelper.updateEntity(socialRegion); 
		}else{
			logger.debug("Creating the social region...");
			HunterHibernateHelper.saveEntity(socialRegion); 
		}
		
		return socialRegion;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

