package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.techmaster.hunter.dao.types.HunterAddressDao;
import com.techmaster.hunter.dao.types.HunterCreditCardDao;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.obj.beans.HunterAddress;
import com.techmaster.hunter.obj.beans.HunterCreditCard;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterSessionFactory;

public class HunterUserDaoImpl implements HunterUserDao{
	
	private static HunterCreditCardDao hunterCreditCardDao = new HunterCreditCardDaoImpl();
	private static HunterAddressDao hunterAddressDao = new HunterAddressDaoImpl();
	
	private static Long maxAddressId;
	private static Long maxCreditCardId;
	
	
	static{
		maxAddressId = hunterAddressDao.getNextAddressId() - 1;
		maxCreditCardId = hunterCreditCardDao.getNextCreditCardId() - 1;
	}
	
	public void refreshAllMaxIds(){
		maxAddressId = hunterAddressDao.getNextAddressId() - 1;
		maxCreditCardId = hunterCreditCardDao.getNextCreditCardId() - 1;
	}
	
	@Override
	public void insertHunterUser(HunterUser user) {
		

		HunterMessageDaoHelper.refreshMapAndCurrentIds();
		refreshAllMaxIds();
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		// set up parent keys for hunter user.
		
		if(user.getUserId() == null){
			
			Long nextId = getNextUserId();
			HunterLogFactory.getLog(getClass()).debug("Set next id for the user >> " + nextId);
			user.setUserId(nextId);
			
			HunterLogFactory.getLog(getClass()).debug("Setting id for the addresses");
			Set<HunterAddress> addresses = user.getAddresses();
			for(HunterAddress address : addresses){
				address.setUserId(user.getUserId());
				maxAddressId++;
				address.setId(maxAddressId);
			}
			
			HunterLogFactory.getLog(getClass()).debug("Setting credit card and user id for the creditCards");
			Set<HunterCreditCard> creditCards = user.getCreditCards();
			for(HunterCreditCard card : creditCards){
				card.setUserId(user.getUserId()); 
				maxCreditCardId++;
				card.setCardId(maxCreditCardId); 
			}
			
		}
		
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.save(user);
			trans.commit();
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		} finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
		
	}

	@Override
	public void insertHunterUsers(List<HunterUser> hunterUsers) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			for(HunterUser user : hunterUsers){
				session.save(user);
			}
			trans.commit();
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		} finally{
			HunterHibernateHelper.closeSession(session); 
		}
	}

	@Override
	public void deleteHunterUserById(Long userId) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HunterUser user = (HunterUser)session.get(HunterUser.class, userId);
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
	public void deleteHunterUser(HunterUser user) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
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
	public HunterUser getHunterUserById(Long id) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		HunterUser user = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			user = (HunterUser)session.get(HunterUser.class, id);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted user >> " + user.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
		return user;
	}

	@Override
	public List<HunterUser> getAllUsers() {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		List<HunterUser> hunterUsers = new ArrayList<HunterUser>();
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query query = session.createQuery("From HunterUser");
			List<?> list = query.list();
			
			for(Object obj : list){
				HunterUser user = (HunterUser)obj;
				hunterUsers.add(user);
			}
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		
		return hunterUsers;
	}

	@Override
	public void updateUser(HunterUser update) {
		
		if(update == null || update.getUserId() == null) return;
		Session session = null;
		Transaction trans = null;
		
		try {
			SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.update(update);
			trans.commit();
			HunterHibernateHelper.closeSession(session); 
			HunterLogFactory.getLog(getClass()).info("Successfully updated user >> " + update);
			
		} catch (HibernateException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans); 
		}finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
	}

	@Override
	public Long getNextUserId() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		Long nextId = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(HunterUser.class).setProjection(Projections.max("userId"));
			Long maxId = (Long)criteria.uniqueResult();
			
			nextId = maxId == null ? 1 : (maxId + 1);
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		HunterLogFactory.getLog(getClass()).debug("Obtained next hunter user id >> " + nextId); 
		return nextId;
	
	
	}

	


}
