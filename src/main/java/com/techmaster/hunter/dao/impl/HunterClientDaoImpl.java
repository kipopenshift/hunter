package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterClientDao;
import com.techmaster.hunter.obj.beans.HunterClient;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterSessionFactory;
import com.techmaster.hunter.util.HunterUtility;

public class HunterClientDaoImpl implements HunterClientDao{
	
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void insertHunterClient(HunterClient client) {
		
		
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.save(client);
			trans.commit();
			HunterHibernateHelper.closeSession(session);
			HunterLogFactory.getLog(getClass()).debug("Successfully inserted client >> " + client); 
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		} finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
	}

	@Override
	public void insertHunterClients(List<HunterClient> hunterClients) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			for(HunterClient client : hunterClients){
				session.save(client);
			}
			trans.commit();
			HunterHibernateHelper.closeSession(session);
			HunterLogFactory.getLog(getClass()).debug("Successfully inserted client >> " + HunterUtility.stringifyList(hunterClients));  
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		} finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
	}

	@Override
	public void deleteHunterClientById(Long clientId) {

		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HunterClient client = (HunterClient)session.get(HunterClient.class, clientId);
			session.delete(client);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted of id >> " + clientId);
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
	}

	@Override
	public void deleteHunterClient(HunterClient client) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HunterUser user = (HunterUser)session.get(HunterUser.class, client.getClientId());
			session.delete(user);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted user >> " + user.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
	}

	@Override
	public HunterClient getHunterClientById(Long id) {
		logger.debug("Getting hunter client by id >> " + id);
		HunterClient client = HunterHibernateHelper.getEntityById(id, HunterClient.class);
		logger.debug("Loaded client >> " + client); 
		logger.debug("Finished loading hunter client by Id!!");
		return client;
	}

	@Override
	public List<HunterClient> getAllclients() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		List<HunterClient> hunterClients = new ArrayList<HunterClient>();
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query query = session.createQuery("From HunterClient");
			List<?> list = query.list();
			
			for(Object obj : list){
				HunterClient client = (HunterClient)obj;
				hunterClients.add(client);
			}
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		
		return hunterClients;
	}

	@Override
	public void updateClient(HunterClient update) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		HunterClient client = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.save(update);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull updated client >> " + client);
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans); 
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
		
	}

	@Override
	public Long nextClientId() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		
		Long nextId = null;
		
		try {
			session = sessionFactory.openSession();
			
			Criteria criteria = session.createCriteria(HunterClient.class).setProjection(Projections.max("clientId"));
			Long maxId = (Long)criteria.uniqueResult();
			
			nextId = maxId == null ? 1 : (maxId + 1);
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		HunterLogFactory.getLog(getClass()).debug("Obtained next hunter client id >> " + nextId); 
		return nextId;
		
	}
	
	
	public static void main(String[] args) {
		HunterClient client = new HunterClientDaoImpl().getHunterClientForUserId(1L); 
		System.out.println(client);   
	}

	@Override
	public HunterClient getHunterClientForUserId(Long userId) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		HunterClient client = null;
		
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(HunterClient.class).add(Restrictions.eq("user.userId", userId));  
			client = (HunterClient) criteria.uniqueResult();
			HunterLogFactory.getLog(getClass()).debug("Successfully obtained client for userId("+userId+") >> \n" +  client); 
			HunterHibernateHelper.closeSession(session); 
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}

		return client;
	}

	@Override
	public HunterClient editReceiverAndBudget(Long clientId, float budget, boolean isReceiver) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		HunterClient client = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			client = (HunterClient)session.get(HunterClient.class, clientId);
			HunterLogFactory.getLog(getClass()).debug("Successfully obtained client for clientId("+clientId+") >> \n" +  client); 
			client.setClientTotalBudget(budget);
			client.setReceiver(isReceiver); 
			client.setLastUpdate(new Date());
			client.setLastUpdatedBy(HunterConstants.HUNTER_ADMIN_USER_NAME); 
			session.update(client);
			trans.commit();
			HunterHibernateHelper.closeSession(session); 
			HunterLogFactory.getLog(getClass()).debug("Successfull updated receiver and budget for client >> " + client);
		} catch (HibernateException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans); 
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		
		return client;
		
	}
	
	

}
