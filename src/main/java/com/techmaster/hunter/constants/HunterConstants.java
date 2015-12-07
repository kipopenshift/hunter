package com.techmaster.hunter.constants;

public class HunterConstants {
	
	//http://localhost:9998/Login.aspx
	//1000:QaBdjB/so2UtXtQTTL4v0/3Kki2ajvtC:m62HrhKB/2MD6+gh+EnpRDxLEBZLEToh
	
	// Hunter specific
	public static final String HUNTER_ADMIN_USER_NAME = "hunterAdmin";
	public static final String HUNTER_ADMIN_PSSWRD = "hunterAdmin.1900";
	public static final String HUNTER_SYSTEM_USER_NAME = "hunterSystem";
	
	// Methods
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	
	// Methods
	public static final String SECURITY_USER_NAME_STR = "userName";
	public static final String SECURITY_USER_PSWRD_STR = "password";
	
	// Hunter user types
	public static final String HUNTER_EMPL_USER = "1000";
	public static final String HUNTER_SUPER_ADMIN_USER = "1001";
	public static final String HUNTER_TECH_ADMIN_USER = "1002";
	public static final String RECEIVER_USER = "2000";
	public static final String HUNTER_CLIENT_USER = "2001";
	
	// Message delivery status
	public static final String STATUS_STRING = "status";
	public static final String STATUS_STARTED = "Started";
	public static final String STATUS_CONCEPTUAL = "Conceptual";
	public static final String STATUS_PROCESSED = "Processed";
	public static final String STATUS_PARTIAL = "Partial";
	public static final String STATUS_SENT = "Sent";
	public static final String STATUS_DELEIVERED = "Delivered";
	public static final String STATUS_PENDING = "Pending";
	public static final String STATUS_SUCCESS = "Success";
	public static final String STATUS_FAILED = "Failed";
	
	//GateWayMessage status types
	
	public static final String STATUS_TYPE_CLIENT = "respStatus";
	public static final String STATUS_TYPE_DELIVERY = "deliveryStatus";
	
	// message type audio, message, email, etc.
	public static final String MESSAGE_TYPE_AUDIO = "AUDIO";
	public static final String MESSAGE_TYPE_EMAIL = "EMAIL";
	public static final String MESSAGE_TYPE_TEXT = "TEXT";
	public static final String MESSAGE_TYPE_PHONE_CALL = "CALL";
	public static final String MESSAGE_TYPE_VOICE_MAIL = "VOICE_MAIL";
	
	// Contact type 
	public static final String CONTACT_TYPE_TEXT = "TEXT";
	public static final String CONTACT_TYPE_EMAIL = "EMAIL";
	public static final String CONTACT_TYPE_VOICE_MAIL = "VOICE_MAIL";
	
	// payment card types
	public static final String CARD_TYPE_VISA = "Visa";
	public static final String CARD_TYPE_MASTER_CARD = "Master Card";
	public static final String CARD_TYPE_DISCOVER = "Dicover";
	public static final String CARD_TYPE_AMERICAN_EXPRESS = "American Express";
	
	//JMS types
	public static final String JSMS_TYPE_POINT_TO_POINT = "POINT_TO_POINT";
	public static final String JSMS_TYPE_PUBLISH = "PUBLISH";
	
	// hunter default phone number
	public static final String HUNTER_DEFAULT_PHONE_NUMBER = "0017324704894"; 
	
	
	// message life cycle
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REVIEW = "Review";
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_COMPLETED = "Completed";
	public static final String STATUS_REJECTED = "Rejected";
	
	
	// task types 
	public static final String TASK_TYPE_POLITICAL = "Polytical";
	public static final String TASK_TYPE_CORPORATE = "Corporate";
	public static final String TASK_TYPE_EDUCATIONAL = "Educational";
	public static final String TASK_TYPE_TESTING = "Testing";
	
	
	// receiver region level types 
	public static final String HUNTER_REGION_COUNTRY = "Country";
	public static final String HUNTER_REGION_COUNTY = "County";
	public static final String HUNTER_REGION_CNSTNCY = "Constituency";
	public static final String HUNTER_REGION_WARD = "Ward";
	public static final String HUNTER_REGION_CITY = "City";
	public static final String HUNTER_REGION_VILLAGE = "Village";
	
	
	// date 
	public static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	
	
	// Media types 
	public static final String MEDIA_TYPE_PDF = "PDF";
	public static final String MEDIA_TYPE_PNG = "PNG";
	public static final String MEDIA_TYPE_JPG = "JPG";
	public static final String MEDIA_TYPE_AV = "AV";
	
	// Address type 
	public static final String ADDRESS_TYPE_BILLING = "Billing";
	public static final String ADDRESS_TYPE_SHIPPING = "Shipping";
	
	
	// Receiver levels
	public static final String RECEIVER_LEVEL_COUNTRY = "Country";
	public static final String RECEIVER_LEVEL_STATE = "State";
	public static final String RECEIVER_LEVEL_COUNTY = "County";
	public static final String RECEIVER_LEVEL_CONSITUENCY = "Constituency";
	public static final String RECEIVER_LEVEL_WARD = "Ward";
	public static final String RECEIVER_LEVEL_CITY = "City";
	public static final String RECEIVER_LEVEL_VILLAGE = "Village";
	
	//Receiver Types 
	public static final String RECEIVER_TYPE_EMAIL = "Email";
	public static final String RECEIVER_TYPE_TEXT = "Text";
	public static final String RECEIVER_TYPE_VOICE_MAIL = "Voice";
	public static final String RECEIVER_TYPE_CALL = "Call";
	
	// Clients business names
	public static final String CLIENT_CM = "CM";
	public static final String CLIENT_OZEKI = "OZEKI";
	public static final String CLIENT_SAFARICOM = "SAFARICOM";
	public static final String CLIENT_TECH_MASTERS = "TECH MASTERS";
	
	// Client Configurations Element Names

	public static final String CONFIG_USER_NAME = "userName";
	public static final String CONFIG_PASSWORD = "password";
	public static final String CONFIG_ACTIVE_METHOD = "actvMethod";
	public static final String CONFIG_ACTIVE_METHOD_URL = "actvURL";
	public static final String CONFIG_DENOMINATION = "denomination";
	public static final String CONFIG_UUID = "UUID";
	public static final String CONFIG_TXT_PRICE = "txtPrice";
	public static final String CONFIG_MEDIA_PRICE = "mediaPrice";
	public static final String CONFIG_VOICE_PRICE = "voicedPrice";
	public static final String CONFIG_CALL_PRICE = "callPriceS";
	public static final String CONFIG_HUNTER_BALANCE = "hunterBalance";
	public static final String CONFIG_HUNTER_MSG_BAL_COUNT = "blncMsgCnt";
	public static final String CONFIG_HUNTER_RATING = "hunterRating";
	public static final String CONFIG_SENDER = "TechMasters";
	
	public static final String BASE_URL = "GET_BASE_URL";
	public static final String TOKEN = "producttoken";
	public static final String BODY = "body";
	public static final String TO = "to";
	public static final String FROM = "from";
	public static final String  REFERENCE = "reference";
	
	// Hunter client Data format with seconds
	public static final String HUNTER_DATE_FORMAT_MILIS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String HUNTER_DATE_FORMAT_SECS = "yyyy-MM-dd HH:mm:ss";
	public static final String HUNTER_DATE_FORMAT_MIN = "yyyy-MM-dd HH:mm";
	
	
	
	
	
	
	

}
