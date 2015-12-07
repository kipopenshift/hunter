package com.techmaster.hunter.obj.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.techmaster.hunter.constants.HunterConstants;



public class HunterJacksonMapper extends ObjectMapper {

	public HunterJacksonMapper() {
		
        this.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        
        DateFormat myDateFormat = new SimpleDateFormat(HunterConstants.HUNTER_DATE_FORMAT_MIN);
        this.getSerializationConfig().setDateFormat(myDateFormat);
        this.getDeserializationConfig().setDateFormat(myDateFormat); 
    }
	
}
