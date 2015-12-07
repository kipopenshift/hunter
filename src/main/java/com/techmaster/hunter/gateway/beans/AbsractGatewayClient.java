package com.techmaster.hunter.gateway.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.HunterURLConstants;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.xml.XMLService;
import com.techmaster.hunter.xml.XMLServiceImpl;
import com.techmaster.hunter.xml.XMLTree;


public abstract class AbsractGatewayClient implements GatewayClient {
	
	protected static XMLService xmlService;
	private Logger logger = Logger.getLogger(getClass());
	
	static{
		loadConfigXMLService();
	}
	

	protected static void loadConfigXMLService(){
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(HunterURLConstants.HUNTER_CONFIG_XML_PATH);
			if(doc != null){
				XMLTree tree = new XMLTree(doc);
				XMLService service = new XMLServiceImpl(tree);
				xmlService = service;
				HunterLogFactory.getLog(AbsractGatewayClient.class).debug("Successfully loaded configuration xml file"); 
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}


	@Override
	public String doGet(Map<String, Object> params, GateWayMessage gateWayMessage) {
		
		boolean isTesting  = true;
		
		String getString = createGetStringFromParams(params);
		HunterLogFactory.getLog(getClass()).debug("Starting to make get request for get  >> " + getString); 
		gateWayMessage.setRequestBody(getString == null ? new byte[]{} : getString.getBytes());
		
		if(isTesting){
			return "Successfully tested gateway message";
		}
		
		
		StringBuffer response = new StringBuffer();
		
		try {
			URL obj = new URL(getString);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			//con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = connection.getResponseCode();
			logger.debug("Sending 'GET' request to URL : " + getString);
			logger.debug("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			HunterLogFactory.getLog(getClass()).debug("Successfully did get request. Response >> " + response.toString()); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			response.append(e.getMessage());
		} catch (ProtocolException e) {
			e.printStackTrace();
			response.append(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			response.append(e.getMessage());
		}
		HunterLogFactory.getLog(getClass()).debug("FResponse >> " + response.toString()); 
		return response.toString();
	}
	
	public static String createGetStringFromParams(Map<String, Object> params){
		String getStr = "";
		int index = 0;
		for(Map.Entry<String, Object> entry : params.entrySet()){
			String key = entry.getKey();
			if(key != null && !key.equals(HunterConstants.BASE_URL)){
				String value = entry.getValue() != null ? entry.getValue().toString() : null;
				if(index == 0){
					getStr = key + "=" + value;
				}else{
					getStr += "&" + key + "=" + value;
				}
				index++;
			}
		}
		String baseUrl = params.get(HunterConstants.BASE_URL).toString(); 
		getStr = baseUrl + getStr;
		getStr = getStr.trim().replaceAll(" ", "%20");
		HunterLogFactory.getLog(AbsractGatewayClient.class).debug("Returning get string >> " + getStr );
		return getStr;
	}
	
	public static void main(String[] args) {
		
		Map<String, Object> params = new HashMap<>();
		
		String baseUrl = "https://sgw01.cm.nl/gateway.ashx?";
		String token = "b9dc662c-254a-4947-948a-a28acb0f17e3";
		String body = "Example+message+text";
		String to = "0017324704894";
		String from = "TechMasters";
		String reference = "your_reference";
		
		params.put(HunterConstants.BASE_URL, baseUrl);
		params.put(HunterConstants.TOKEN, token);
		params.put(HunterConstants.BODY, body);
		params.put(HunterConstants.TO, to);
		params.put(HunterConstants.FROM, from);
		params.put(HunterConstants.REFERENCE, reference);
		
		createGetStringFromParams(params); 
	}
	
		
	
}
