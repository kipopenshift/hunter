package com.techmaster.hunter.imports.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.util.HunterHibernateHelper;

public class ImportHelper {
	
	private static Logger logger = Logger.getLogger(ImportHelper.class);

	public static HunterImportBean createHntrImprtBnFrmWrkbk(Workbook workbook, AuditInfo auditInfo, String fileName, String beanName, String status){
		
		ByteArrayOutputStream bos = null;
		
		try {
			bos = new ByteArrayOutputStream();
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		    try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		byte[] bytes = bos.toByteArray();
		
		HunterImportBean hunterImportBean = new HunterImportBean();
		hunterImportBean.setWorkbook(workbook);
		hunterImportBean.setOriginalFileName(fileName);
		hunterImportBean.setBeanName(beanName); 
		hunterImportBean.setByteLen(bytes.length); 
		hunterImportBean.setExcelBytes(bytes);
		hunterImportBean.setAuditInfo(auditInfo);
		hunterImportBean.setStatus(status);
		
		return hunterImportBean;
	}
	
	public static Workbook getWorkBookForImportBean(HunterImportBean hunterImportBean){
		byte[] bytes = hunterImportBean.getExcelBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(byteArrayInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	public static void createImportBeanBlobAndSave(HunterImportBean hunterImportBean, Session session){
		Blob blob = null;
		Transaction trans = null;
		try {
			trans = session.beginTransaction();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(hunterImportBean.getExcelBytes()); 
			blob = Hibernate.getLobCreator(session).createBlob(byteArrayInputStream, (long)hunterImportBean.getByteLen()); 
			hunterImportBean.setExcelBlob(blob);
			session.save(hunterImportBean);
			trans.commit();
			logger.debug("Successfully saved the import bean with it's blob!!"); 
		} catch (HibernateException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans);
		}finally{
			HunterHibernateHelper.closeSession(session); 
		}
	}
	
}
