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

import com.techmaster.hunter.dao.types.MessageDao;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterSessionFactory;

public class MessageDaoImpl implements MessageDao{

	@Override
	public void insertMessage(Message message) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.save(message);
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
	public void insertMessages(List<Message> messages) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			for(Message message : messages){
				session.save(message);
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
	public void deleteMessage(Message message) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.delete(message);
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted message >> " + message.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
		
	}

	@Override
	public void deleteMessageById(Long msgId) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Message message = (Message)session.get(Message.class, msgId);
			session.delete(message); 
			trans.commit();
			HunterLogFactory.getLog(getClass()).debug("Successfull deleted message >> " + message.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
	}

	@Override
	public Message getMessageById(Long msgId) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		Message message = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			message = (Message)session.get(Message.class, msgId);
			HunterHibernateHelper.closeSession(session); 
			HunterLogFactory.getLog(getClass()).debug("Successfull obtained >> " + message.toString());
		}catch(HibernateException e){
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
		return message;
	}

	@Override
	public List<Message> getAllMessages() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		List<Message> messages = new ArrayList<Message>();
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Query query = session.createQuery("From Message");
			List<?> list = query.list();
			
			for(Object obj : list){
				Message message = (Message)obj;
				messages.add(message);
			}
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		
		return messages;
		
	}

	@Override
	public void updateMessage(Message update) {
		
		if(update == null || update.getMsgId() == 0) return;
		Session session = null;
		Transaction trans = null;
		try {
			SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			Message existent = (Message) session.get(Message.class, update.getMsgId());
			if(existent == null){
				throw new IllegalArgumentException("The message passed in for update does not exist >> " + update);
			}
			HunterLogFactory.getLog(getClass()).info("Message before updates >> " + existent);
			HunterLogFactory.getLog(getClass()).info("Message to update with >> " + update); 
			BeanUtils.copyProperties(existent, update);
			HunterLogFactory.getLog(getClass()).info("Message after update >> " + existent);
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
	public Long getNextMessageId(Class<?> clss) { 
		
		if(clss == null) throw new IllegalArgumentException("Class for which the id is sought is required. clss >> " + clss);  
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		Long nextId = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(clss).setProjection(Projections.max("msgId"));
			Long maxId = (Long)criteria.uniqueResult();
			
			nextId = maxId == null ? 1 : (maxId + 1);
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		HunterLogFactory.getLog(getClass()).debug("Obtained next hunter address id >> " + nextId); 
		return nextId;
	}

}
