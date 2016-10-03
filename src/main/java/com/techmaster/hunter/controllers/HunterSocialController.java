package com.techmaster.hunter.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.json.HunterSelectValue;
import com.techmaster.hunter.json.HunterSocialAppJson;
import com.techmaster.hunter.json.HunterSocialGroupJson;
import com.techmaster.hunter.json.HunterSocialRegionJson;
import com.techmaster.hunter.obj.beans.HunterSocialRegion;
import com.techmaster.hunter.social.HunterSocialHelper;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping("/social")
public class HunterSocialController extends HunterBaseController { 
	
	private Logger logger = Logger.getLogger(getClass());

	@RequestMapping(value = "/action/profileHome", method = RequestMethod.GET)
	public String goToProfile(){
		return "views/hunterSocialAssociate";
	}
	
	@RequestMapping(value = "/action/socialApp/home", method = RequestMethod.GET)
	public String goToSocialApp(){
		return "views/socialApp";
	}
	
	@RequestMapping(value = "/action/groups/home", method = RequestMethod.GET)
	public String socialGroupsHome(){
		return "views/socialGroups";
	}
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/regions/readSelVal", method = RequestMethod.POST)
	public @ResponseBody List<HunterSelectValue> readAllSocialRegions(){ 
		List<HunterSelectValue> selectValues = new ArrayList<HunterSelectValue>();
		List<HunterSocialRegion> socialRegions = HunterHibernateHelper.getAllEntities(HunterSocialRegion.class);
		if( HunterUtility.isCollectionNotEmpty(socialRegions)){
			for(HunterSocialRegion socialRegion : socialRegions){
				HunterSelectValue selectValue = new HunterSelectValue();
				selectValue.setText(socialRegion.getRegionName()); 
				selectValue.setValue(Long.toString(socialRegion.getRegionId())); 
				selectValues.add(selectValue);
			}
		}
		return selectValues;
	}
	

	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/groups/read", method = RequestMethod.POST)
	public @ResponseBody List<HunterSocialGroupJson> readSocialGroups(){ 
		List<HunterSocialGroupJson> groupJsons = HunterSocialHelper.getInstance().getAllSocialGroupsJsons();
		return groupJsons;
	}
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/groups/changeStatus", method = RequestMethod.POST)
	public @ResponseBody String editSocialGroups(@RequestBody Map<String,Object> params){
		try{
			logger.debug("Changing social group status : " + HunterUtility.stringifyMap(params)); 
			String update = "UPDATE HNTR_SCL_GRP SET STS = ? WHERE GP_ID = ?";
			List<Object> values = new ArrayList<>();
			values.add(params.get("toStatus"));
			values.add(params.get("groupId"));
			HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class).executeUpdate(update, values);
			return HunterUtility.setJSONObjectForSuccess(null, "Successfully changed group status.").toString();
		}catch(Exception e){
			e.printStackTrace();
			return HunterUtility.setJSONObjectForFailure(null, e.getMessage()).toString();
		}
	}
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/groups/create", method = RequestMethod.POST)
	public @ResponseBody String createSocialGroups(@RequestBody HunterSocialGroupJson hunterSocialGroupJson){ 
		logger.debug(hunterSocialGroupJson);
		HunterSocialHelper.getInstance().createOrUpdateSocialGroup(hunterSocialGroupJson, getAuditInfo());
		return HunterUtility.setJSONObjectForSuccess(null, "Successfully created social group").toString();
	}
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/groups/destroy", method = RequestMethod.POST)
	public @ResponseBody String  destroySocialGroups(@RequestBody Map<String,Object> regParams){
		logger.debug("Deleting social group with params : " + HunterUtility.stringifyMap(regParams)); 
		Long groupId = HunterUtility.getLongFromObject(regParams.get("groupdId")); 
		HunterSocialHelper.getInstance().tryDeleteSocialGroup(groupId);
		return HunterUtility.setJSONObjectForSuccess(null, "Successfully deleted social group!").toString();
	}
	
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/apps/read", method = RequestMethod.POST)
	public @ResponseBody List<HunterSocialAppJson> readSocialApps(){ 
		List<HunterSocialAppJson> socialAppJsons = HunterSocialHelper.getInstance().getAllSocialAppsJsons();
		return socialAppJsons;
	}
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/regions/read", method = RequestMethod.POST)
	public @ResponseBody List<HunterSocialRegionJson> getAllSocialRegions(){ 
		List<HunterSocialRegionJson> socialAppJsons = HunterSocialHelper.getInstance().getAllSocialRegionsJsons();
		return socialAppJsons;
	}
	
	@Consumes("application/json")
	@Produces("application/json")
	@RequestMapping(value = "/action/regions/createOrUpdate", method = RequestMethod.POST)
	public @ResponseBody String createSocialRegion(@RequestBody HunterSocialRegionJson hunterSocialGroupJson){ 
		logger.debug(hunterSocialGroupJson);
		HunterSocialHelper.getInstance().createOrUpdateSocialRegion(hunterSocialGroupJson, getAuditInfo());
		return HunterUtility.setJSONObjectForSuccess(null, "Successfully created social region").toString();
	}
	
	@RequestMapping(value = "/action/regions/home", method = RequestMethod.GET)
	public String regionsHome(){
		return "views/socialRegions";
	}
	
	
}
