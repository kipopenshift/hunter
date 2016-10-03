
function HunterConstants(){};

HunterConstants.HUNTER_BASE_URL = "http://localhost:8080/Hunter";
HunterConstants.HUNTER_DATE_FORMAT_MILIS = "yyyy-MM-dd HH:mm:ss.SSS";
HunterConstants.HUNTER_DATE_FORMAT_SECS = "yyyy-MM-dd HH:mm:ss";
HunterConstants.HUNTER_DATE_FORMAT_MIN = "yyyy-MM-dd HH:mm";

HunterConstants.MESSAGE_STRING = "message";
HunterConstants.STATUS_STRING = "status";
HunterConstants.STATUS_FAILED = "Failed";
HunterConstants.STATUS_SUCCESS = "Success";
HunterConstants.getBaseURL = function(loc) {
    return location.protocol + "//" + location.hostname +
       (location.port && ":" + location.port) + "/" + loc;
};

HunterConstants.getHunterBaseURL = function(loc) {
    return HunterConstants.getBaseURL("Hunter/" + loc);
};


HunterConstants.OVERLAY_ID = "hunterKendoOverLay";

HunterConstants.TASK_TYPES_ARRAY = 
	[
	 	{"text" : "Political", "value" : "Political"},
	 	{"text" : "Corporate", "value" : "Corporate"},
	 	{"text" : "Educational", "value" : "Educational"},
	 	{"text" : "Testing", "value" : "Testing"}
    ];

HunterConstants.GATE_WAY_CLIENT_ARRAY = 
	[
	 	{"text" : "CM", "value" : "CM"},
	 	{"text" : "Ozeki", "value" : "OZEKI"},
	 	{"text" : "Safaricom", "value" : "SAFARICOM"},
	 	{"text" : "Tech Masters", "value" : "TECH MASTERS"},
	 	{"text" : "Hunter Email", "value" : "Hunter Email"},
	 	{"text" : "Hunter Social", "value" : "Hunter Social"}
    ];

HunterConstants.SERVICE_PROVIDER_DATA = [
		{"providerId" : "1", "providerName" : "Safaricom"},
		{"providerId" : "2", "providerName" : "Celtel"},
		{"providerId" : "3", "providerName" : "Verizon"},
		{"providerId" : "4", "providerName" : "AT and T"},
		{"providerId" : "5", "providerName" : "Tech Masters"},
		{"providerId" : "6", "providerName" : "Hunter Email Provider"},
    ];

HunterConstants.HUNTER_LIFE_STATUSES = [
		{"statusValue" : "Draft", "statusText" : "Draft"},
		{"statusValue" : "Review", "statusText" : "Review"},
		{"statusValue" : "Approved", "statusText" : "Approved"},
		{"statusValue" : "Processed", "statusText" : "Processed"}
      ];

HunterConstants.TASK_MSG_TYP_ARRAY = [
			{"msgTypVal" : "Email", "msgTypText" : "Email"},
			{"msgTypVal" : "Text", "msgTypText" : "Text"},
			{"msgTypVal" : "Voice Mail", "msgTypText" : "Voice Mail"},
			{"msgTypVal" : "Social", "msgTypText" : "Social Message"}
        ];

HunterConstants.RECEIVER_TYP_ARRAY = [
			{"value" : "Email", "text" : "Email"},
			{"value" : "Text", 	"text" : "Text"},
			{"value" : "Voice", "text" : "Voice"},
			{"value" : "Call", 	"text" : "Call"}
      ];

HunterConstants.SOCIAL_MEDIA_TYPS_ARRAY = [
  			{"value" : "mediaPhoto", "text" : "Photo"},
  			{"value" : "mediaSound", "text" : "Sound"},
  			{"value" : "mediaVideo", "text" : "Video"},
  			{"value" : "mediaGIF",   "text" : "GIF"}
        ];



/*
public static final String SOCIAL_POST_TYPE_LINK_NEWS = "socialPostLinkNews";
public static final String SOCIAL_POST_TYPE_LINK_OTHER = "socialPostLinkOther";
public static final String SOCIAL_POST_TYPE_TEXT = "socialPostText";
public static final String SOCIAL_POST_TYPE_IMAGE = "socialPostImage";
public static final String SOCIAL_POST_TYPE_VIDEO = "socialPostVideo";
public static final String SOCIAL_POST_TYPE_AUDIO = "socialPostAudio";
*/

HunterConstants.SOCIAL_POST_ARRAY = [
            {"value" : "socialPostText", 		"text" : "Text"},
			{"value" : "socialPostImage",   	"text" : "Image"},
			{"value" : "socialPostVideo", 		"text" : "Video"},
			{"value" : "socialPostAudio",   	"text" : "Audio"},
 			{"value" : "socialPostLinkNews",   	"text" : "New Link"},
 			{"value" : "socialPostLinkOther", 	"text" : "Other Links"}
       ];

/*
 * public static final String SOCIAL_TYPE_FACEBOOK_GROUP = "Facebook Group";
	public static final String SOCIAL_TYPE_INSTAGRAM_GROUP = "InstagramG roup";
	public static final String SOCIAL_TYPE_TWITTER_GROUP = "Twitter Group";
	public static final String SOCIAL_TYPE_BLOG_GROUP = "Blog Group";
 * */

HunterConstants.SOCIAL_TYPES_ARRAY = [
            {"value" : "Facebook", 		"text" : "Facebook"},
 			{"value" : "Instagram",   	"text" : "Instagram"},
 			{"value" : "Twitter", 		"text" : "Twitter"},
 			{"value" : "Blog",   		"text" : "Blog"}
        ];
