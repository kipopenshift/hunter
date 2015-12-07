package com.techmaster.hunter.dao.impl;

import java.util.Map;

import com.techmaster.hunter.dao.types.EmailMessageDao;
import com.techmaster.hunter.dao.types.GateWayMessageDao;
import com.techmaster.hunter.dao.types.HunterAddressDao;
import com.techmaster.hunter.dao.types.HunterClientDao;
import com.techmaster.hunter.dao.types.HunterCreditCardDao;
import com.techmaster.hunter.dao.types.TaskMessageReceiverDao;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.dao.types.MessageDao;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.dao.types.ServiceProviderDao;
import com.techmaster.hunter.dao.types.TaskDao;

public class HunterDaoFactory {
	
	public static Map<Class<?>, Object> daosMap; 
	
	static {
		popuplateMap();
	}

	public HunterDaoFactory() {
		super();
	}

	private static void popuplateMap() {
		daosMap.put(HunterClientDao.class, new HunterClientDaoImpl());
		daosMap.put(EmailMessageDao.class, new EmailMessageDaoImpl());
		daosMap.put(GateWayMessageDao.class, new GateWayMessageDaoImpl());
		daosMap.put(HunterAddressDao.class, new HunterAddressDaoImpl());
		daosMap.put(HunterCreditCardDao.class, new HunterCreditCardDaoImpl());
		daosMap.put(TaskMessageReceiverDao.class, new TaskMessageReceiverDaoImpl());
		daosMap.put(HunterUserDao.class, new HunterUserDaoImpl());
		daosMap.put(MessageDao.class, new MessageDaoImpl());
		daosMap.put(ReceiverRegionDao.class, new ReceiverRegionDaoImpl());
		daosMap.put(ServiceProviderDao.class, new ServiceProviderDaoImpl());
		daosMap.put(TaskDao.class, new TaskDaoImpl());
	}

	public Map<Class<?>, Object> getDaosMap() {
		return daosMap;
	}
	public void setDaosMap(Map<Class<?>, Object> daosMap) {
		HunterDaoFactory.daosMap = daosMap; 
	}
	public static Object get(Class<?> clzz){
		Object obj = daosMap.get(clzz);
		if(!obj.getClass().isAssignableFrom(clzz)){
			throw new IllegalArgumentException("Found a class not assignable to the class for which it was store. Key class >> " + clzz.getCanonicalName());
		}
		return obj;
	}
	public void put(Class<?> clzz, Object obj){
		daosMap.put(clzz, obj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((daosMap == null) ? 0 : daosMap.hashCode());
		return result;
	}

	

}
