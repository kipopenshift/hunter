package com.techmaster.hunter.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.techmaster.hunter.dao.types.HunterCreditCardDao;
import com.techmaster.hunter.obj.beans.HunterCreditCard;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterSessionFactory;

public class HunterCreditCardDaoImpl implements HunterCreditCardDao{

	@Override
	public void insertCreditCard(HunterCreditCard creditCard) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.saveOrUpdate(creditCard);
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
	public HunterCreditCard getCreditCardById(Long id) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		HunterCreditCard card = null;
		
		try {
			session = sessionFactory.openSession();
			card = (HunterCreditCard)session.get(HunterCreditCard.class, id);
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted user >> " + card.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
		return card;
	}

	@Override
	public void deleteCreditCard(HunterCreditCard creditCard) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.delete(creditCard);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted credit card >> " + creditCard.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
	}

	@Override
	public List<HunterCreditCard> getAllCreditCards() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		List<HunterCreditCard> cards = new ArrayList<HunterCreditCard>();
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query query = session.createQuery("From HunterCreditCard");
			List<?> list = query.list();
			
			for(Object obj : list){
				HunterCreditCard card = (HunterCreditCard)obj;
				cards.add(card);
			}
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		
		return cards;
	}

	@Override
	public void deleteCreditCardById(Long id) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HunterCreditCard card = (HunterCreditCard)session.get(HunterCreditCard.class, id);
			session.delete(card);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted user >> " + card.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
	}

	@Override
	public void updateCreditCard(HunterCreditCard update) {
		
		if(update == null || update.getCardId() == 0) return;
		Session session = null;
		Transaction trans = null;
		
		try {
			SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HunterCreditCard existent = (HunterCreditCard) session.get(HunterCreditCard.class, update.getCardId());
			if(existent == null){
				throw new IllegalArgumentException("The HunterCreditCard passed in for update does not exist >> " + update);
			}
			HunterLogFactory.getLog(getClass()).info("HunterCreditCard before updates >> " + existent);
			HunterLogFactory.getLog(getClass()).info("HunterCreditCard to update with >> " + update); 
			BeanUtils.copyProperties(existent, update);
			HunterLogFactory.getLog(getClass()).info("HunterCreditCard after update >> " + existent);
			trans.commit();
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans); 
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans);
		}finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
	}

	@Override
	public void insertCreditCards(List<HunterCreditCard> creditCards) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			for(HunterCreditCard card : creditCards){
				session.save(card);
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
	public Long getNextCreditCardId() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		Long nextId = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(HunterCreditCard.class).setProjection(Projections.max("cardId"));
			Long maxId = (Long)criteria.uniqueResult();
			
			nextId = maxId == null ? 1 : (maxId + 1);
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		HunterLogFactory.getLog(getClass()).debug("Obtained next hunter credit card id >> " + nextId); 
		return nextId;
	}


}
