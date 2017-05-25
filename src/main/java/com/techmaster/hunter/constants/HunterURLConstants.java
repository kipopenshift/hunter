package com.techmaster.hunter.constants;

public interface HunterURLConstants {
	
	public static final String COMPUTER_USER_BASE_URL = "C:\\Users\\hillangat\\git\\hunter\\";
	
	public static final String RESOURCE_BASE_PATH = COMPUTER_USER_BASE_URL + "src\\main\\webapp\\resources\\";
	public static final String RESOURCE_BASE_IMAGES_PATH = COMPUTER_USER_BASE_URL + "src\\main\\webapp\\resources\\images\\";
	public static final String RESOURCE_BASE_XML_PATH = COMPUTER_USER_BASE_URL + "src\\main\\webapp\\resources\\xml\\";
	public static final String RESOURCE_BASE_WORKBOOK_PATH = COMPUTER_USER_BASE_URL + "src\\main\\webapp\\resources\\workbooks\\";
	
	public static final String HUNTER_CONFIG_XML_PATH = RESOURCE_BASE_XML_PATH + "\\ClientConfig.xml";
	public static final String QRY_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "Query.xml";
	public static final String UI_MSG_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "UIMessages.xml";
	public static final String CLIENT_CONFIG_LOC_PATH = RESOURCE_BASE_XML_PATH + "ClientConfig.xml";
	public static final String RESPONSE_CONFIG_LOC_PATH = RESOURCE_BASE_XML_PATH + "ResponseConfig.xml";
	public static final String EMAIL_TEMPLATES_LOCL_PATH = RESOURCE_BASE_XML_PATH + "emailTemplates.xml";
	public static final String EMAIL_CONFIGS__LOCL_PATH = RESOURCE_BASE_XML_PATH + "emailConfigs.xml";
	public static final String TASK_PROCESS_JOBS_TEMPLATE = RESOURCE_BASE_XML_PATH + "tskPcssJobTemplate.xml";
	public static final String OZEKI_TEST_RSPONSE_XML_LOCL_PATH = RESOURCE_BASE_XML_PATH + "ozekiTestResponse.xml";
	public static final String RESOURCE_TEMPL_FOLDER = "C:\\Users\\hillangat\\programming\\workspaces\\Hunter\\Hunter\\src\\main\\webapp\\resources\\tempFolder";
	public static final String POST_LOGIN_URL = "http://localhost:8080/Hunter/hunter/login/after";
	public static final String TESTING_ATTCHMENT_PATH = RESOURCE_BASE_PATH + "\\Notes\\testing_attachment.txt";
	public static final String LOGIN_DATA_SEE_XML  = RESOURCE_BASE_XML_PATH +  "loginDataSeed.xml";
	public static final String HUNTER_SOCIAL_APP_CONFIG_PATH = RESOURCE_BASE_XML_PATH + "hunterSocialAppConfig.xml";  

}
