package com.techmaster.hunter.dao.types;

import java.util.List;
import java.util.Map;

public interface HunterJDBCExecutor {
	
	public String getQueryForSqlId(String id);
	public String replaceAllColonedParams(String query, Map<String,Object> params);
	public String getReplacedAllColonedParamsQuery(String queryName, Map<String,Object> params);
	public Map<Integer, List<Object>>  executeQueryRowList(String query, List<Object> values);
	public List<Map<String, Object>>  executeQueryRowMap(String query, List<Object> values);
	public List<Map<String, Object>> replaceAndExecuteQuery(String query, Map<String, Object> params);
	public  void  replaceAndExecuteUpdate(String query, Map<String, Object> params);
	public int executeUpdate(String query,List<Object> values);
	public List<Object> getValuesList(Object[] array);
	public Object executeQueryForOneReturn(String query, List<Object> values);
	public Map<String, Object> executeQueryFirstRowMap(String query, List<Object> values);
	
	

}
