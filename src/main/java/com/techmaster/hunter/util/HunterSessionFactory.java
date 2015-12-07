package com.techmaster.hunter.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HunterSessionFactory {

	private static org.hibernate.SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
	static {
		if(sessionFactory == null){
			Configuration cfg=new Configuration();
			cfg.configure("hibernate/hibernate/hibernate.cfg.xml");
			serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
			sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		}
	}
	
	
	public static org.hibernate.SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
}
