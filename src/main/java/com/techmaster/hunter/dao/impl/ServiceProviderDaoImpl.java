package com.techmaster.hunter.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.types.ServiceProviderDao;
import com.techmaster.hunter.obj.beans.ServiceProvider;
import com.techmaster.hunter.util.HunterHibernateHelper;

public class ServiceProviderDaoImpl implements ServiceProviderDao{
	
	private Logger logger = Logger.getLogger(ServiceProviderDaoImpl.class);

	@Override
	public void insertServiceProvider(ServiceProvider serviceProvider) {
		logger.debug("Inserting service provider : " + serviceProvider);
		HunterHibernateHelper.saveEntity(serviceProvider); 
		logger.debug("Finished inserting Service provider!");
	}

	@Override
	public void insertServiceProviders(List<ServiceProvider> serviceProviders) {
		logger.debug("Inserting service providers : size( " + serviceProviders.size() + " )");
		HunterHibernateHelper.saveEntities(serviceProviders); 
		logger.debug("Finished inserting Service providers!");
	}

	@Override
	public ServiceProvider getServiceProviderById(Long id) {
		
		logger.debug("Getting service provider by id : " + id); 
		ServiceProvider serviceProvider = HunterHibernateHelper.getEntityById(id, ServiceProvider.class);
		logger.debug("Finished gettting Service provider : " + serviceProvider); 
		return serviceProvider;
	}

	@Override
	public ServiceProvider getServiceProviderByName(String name) {
		
		logger.debug("Getting service provider by name : " + name); 
		String query = "FROM ServiceProvider s where s.providerName = '" + name + "'";
		List<ServiceProvider> serviceProviders = HunterHibernateHelper.executeQueryForObjList(ServiceProvider.class, query);
		logger.debug("Service providers returned for the name ( " + name + " ) , size ( " + serviceProviders.size() + " )");  
		ServiceProvider serviceProvider = null;
		if(serviceProviders != null && !serviceProviders.isEmpty())
			serviceProvider = serviceProviders.get(0);
		logger.debug("Finished getting service provider by name : " + serviceProvider); 
		return serviceProvider;

	}

	@Override
	public List<ServiceProvider> getAllServiceProviders() {
		
		logger.debug("Getting all service providers..." ); 
		List<ServiceProvider> serviceProviders = HunterHibernateHelper.getAllEntities(ServiceProvider.class);
		logger.debug("Finished getting Service providers  " + serviceProviders);
		return serviceProviders;
	}

	@Override
	public void updateServiceProvider(ServiceProvider update) {
		
		logger.debug("Updating service provider : " + update); 
		HunterHibernateHelper.updateEntity(update);;
		logger.debug("Finished updating service providers!");
		
		
	}

}
