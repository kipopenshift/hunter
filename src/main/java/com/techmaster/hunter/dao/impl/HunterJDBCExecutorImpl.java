package com.techmaster.hunter.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class HunterJDBCExecutorImpl implements HunterJDBCExecutor {
	
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(HunterJDBCExecutorImpl.class);
	
	public HunterJDBCExecutorImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String getQueryForSqlId(String id) {
		
		if(id == null || id.trim().equalsIgnoreCase("")){			
			String message = "Id provided is null or empty >> " + id;
			logger.debug(message);
			throw new IllegalArgumentException(message);
		}
		
		String 
		query = HunterUtility.getQueryForSqlId(id),
		desc = HunterUtility.getQueryDescForSqlId(id);
		
		logger.debug("\n Description :  " + desc + "\n");  
		logger.debug("Retrieved query for id = " + id + " \n" + query);
		
		return query;
	}
	
	@Override
	public Map<Integer, List<Object>>  executeQueryRowList(String query, List<Object> values) {
		
		ResultSet rs = null;
		Connection conn = null;
		Map<Integer, List<Object>> objects = new HashMap<Integer, List<Object>>();
		
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			if(values != null && values.size() >= 1){
				for(int i=0; i<values.size(); i++){
					Object obj = values.get(i);
					ps.setObject(i+1, obj); 
				}
			}
			rs = ps.executeQuery();
			int indx = 1;
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while(rs.next()){
				List<Object> row = new ArrayList<>();
				for(int j=1; j<=columnsNumber; j++){
					Object obj = rs.getObject(j);
					row.add(obj);
				}
				objects.put(indx,row);
				indx++;
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new HunterRunTimeException(e.getMessage());
		}finally{
			HunterHibernateHelper.closeConnection(conn);
		}
		return objects;
	}
	
	@Override
	public List<Map<String, Object>>  executeQueryRowMap(String query, List<Object> values) {
		
		ResultSet rs = null;
		Connection conn = null;
		List<Map<String, Object>> rowMapList = new ArrayList<>();
		
		
		try {
			
			conn = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			
			if(values != null && values.size() >= 1){
				for(int i=0; i<values.size(); i++){
					Object obj = values.get(i);
					ps.setObject(i+1, obj); 
				}
			}
			
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			while(rs.next()){
				Map<String, Object> rowMap = new HashMap<>();
				for(int j=1; j<=columnsNumber; j++){
					Object obj = rs.getObject(j);
					String colName = rsmd.getColumnName(j);
					rowMap.put(colName, obj);
				}
				rowMapList.add(rowMap);
			}
			
			rs.close();
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeConnection(conn);
		}
		return rowMapList;
	}

	@Override
	public List<Map<String, Object>> replaceAndExecuteQuery(String query, Map<String, Object> params) {
		
		logger.debug("Before replacing values : " + query);
		
		for(Map.Entry<String, Object> entry : params.entrySet() ){
			String key = entry.getKey();
			String val = entry.getValue()+"";
			query = query.replaceAll(key, val);
		}
		
		logger.debug("After replacing values : " + query);
		List<Map<String, Object>> roMap = executeQueryRowMap(query, new ArrayList<>());
		
		return roMap;
	}
	
	private static String replaceValues(String query,Map<String, Object>  params){
		for(Map.Entry<String, Object> entry : params.entrySet() ){
			String key = entry.getKey();
			String val = entry.getValue()+"";
			query = query.replaceAll(key, val);
		}
		return query;
	}

	@Override
	public void replaceAndExecuteUpdate(String query,Map<String, Object> params) {
		logger.debug("Query before replacement : " + query); 
		query = replaceValues(query, params);
		logger.debug("Query after replacement : " + query); 
		Connection conn = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate(); 
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int executeUpdate(String query,List<Object> values) {
		logger.debug("Executing update query : " + query); 
		Connection conn = null;
		int rows = 0;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			if(values != null && values.size() >= 1){
				for(int i=0; i<values.size(); i++){
					Object obj = values.get(i);
					ps.setObject(i+1, obj);
				}
			}
			rows = ps.executeUpdate(); 
			ps.close();
			conn.close();
			return rows;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public List<Object> getValuesList(Object[] array) {
		List<Object> values = new ArrayList<>();
		if(array != null && array.length > 0){
			for(Object obj : array){
				values.add(obj);
			}
		}
		return values;
	}

	@Override
	public Object executeQueryForOneReturn(String query, List<Object> values) {
		Map<Integer, List<Object>>  rowListMap = executeQueryRowList(query, values);
		if(!rowListMap.isEmpty() && rowListMap.get(1) != null && !rowListMap.get(1).isEmpty()){
			if(rowListMap.get(1).size() > 1){
				logger.warn("Size is greater than one for this query!!!! Returning the first element..."); 
			}
			Object obj = rowListMap.get(1).get(0);
			return obj;
		}
		return null;
	}

	@Override
	public Map<String, Object> executeQueryFirstRowMap(String query, List<Object> values) {
		Map<String, Object> firstMap = new HashMap<>();
		List<Map<String, Object>> list = executeQueryRowMap(query, values);
		if(list != null && !list.isEmpty() && list.get(0) != null && !list.get(0).isEmpty() ){
			firstMap.putAll(list.get(0));  
		}else{
			logger.debug("No data found for query : " + query); 
		}
		return firstMap;
	}

	@Override
	public String replaceAllColonedParams(String query, Map<String, Object> params) {
		logger.debug("Replacing parameters : " + HunterUtility.stringifyMap(params)); 
		if( query == null || !HunterUtility.isMapNotEmpty(params) ){ 
			String message = "Either the query is null or no parameters to replace. Returning the as is query...";
			logger.debug( message );
			throw new IllegalArgumentException( message   );
		}
		for(Map.Entry<String, Object> entry : params.entrySet()){
			String key = entry.getKey();
			String value = HunterUtility.getStringOrNullOfObj( entry.getValue() );
			if( query.contains(key) ){
				value = value == null ? " IS NULL " : value;
				query = query.replaceAll(key, value);
			}else{
				logger.debug("Exception while processing query for params : " + HunterUtility.stringifyMap(params)); 
				throw new IllegalArgumentException( "Key : "+ key +" cannot be found in Query : \n "+ query + "\n"   );
			}
		}
		logger.debug("Replaced query : \n " + query); 
		return query;
	}

	@Override
	public String getReplacedAllColonedParamsQuery(String queryName, Map<String, Object> params) {
		String replaced = replaceAllColonedParams(getQueryForSqlId(queryName), params); 
		return replaced;
	}

	
	
	
	
	
	
	
}
