package com.techmaster.hunter.obj.converters;

import java.util.Date;

import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.json.RawReceiverUserJson;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;


public class RawReceiverUserConverter {
	
	public HunterRawReceiverUser convertToRawReceiverUser(RawReceiverUserJson userJson){
		HunterRawReceiverUser rawUser = (HunterRawReceiverUser)HunterDaoFactory.getInstance().getDaoObject(HunterUserDao.class).getUserByUserName(userJson.getUserName());  
		if(rawUser == null) rawUser = new HunterRawReceiverUser();
		rawUser.setAllContctNo(userJson.getAllContctNo());
		rawUser.setCompensation(userJson.getCompensation());
		rawUser.setConstituency(userJson.getConstituency());
		rawUser.setConsWard(userJson.getConsWard());
		rawUser.setCountry(userJson.getCountry());
		rawUser.setCounty(userJson.getCounty());
		rawUser.setCreatedBy(userJson.getUserName());
		rawUser.setCretDate(new Date());
		rawUser.setEmail(userJson.getEmail());
		rawUser.setFirstName(userJson.getFirstName());
		rawUser.setLastName(userJson.getLastName());
		rawUser.setLastUpdate(new Date());
		rawUser.setLastUpdatedBy(userJson.getUserName());
		rawUser.setMiddleName(userJson.getMiddleName());
		rawUser.setPhoneNumber(userJson.getPhoneNumber()); 
		rawUser.setRawUserName(userJson.getRawUserName());
		return rawUser;
	}
	

}
