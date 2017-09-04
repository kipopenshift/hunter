package com.techmaster.hunter.util;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HunterSessionFactory {

	private static org.hibernate.SessionFactory sessionFactory;
	
	static {
		if(sessionFactory == null){
			Configuration configuration = new Configuration().configure("hibernate/hibernate/hibernate.cfg.xml");
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
			applySettings(configuration.getProperties());
			sessionFactory = configuration.buildSessionFactory(builder.build());
		}
	}
	
	
	public static org.hibernate.SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
}
