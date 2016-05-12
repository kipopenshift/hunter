package com.techmaster.hunter.rawreceivers;

import java.util.List;
import java.util.Map;

import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterRawReceiver;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.obj.beans.HunterUser;

public interface RawReceiverService {
	
	public HunterRawReceiverUser registerRawReceiverUser(HunterRawReceiverUser rawReceiverUser);
	public List<HunterRawReceiver> getPayableRawReceivers(HunterRawReceiverUser hunterRawReceiverUser);
	public List<HunterRawReceiver> getAllRawReceiversForUser(HunterRawReceiverUser hunterRawReceiverUser);
	public int getRawReceiverVersion (String givenByUserName);
	public List<HunterRawReceiver> getDefaultRawReceivers();
	public Map<String, String> validateRawReceiver(HunterRawReceiver hunterRawReceiver);
	public Map<String,String> payRawReceiverUser(HunterRawReceiverUser rawReceiverUser);
	public HunterRawReceiver createRawReceiver(Map<String,String> params, AuditInfo auditInfo);
	public HunterRawReceiver updateRawReceiver(Map<String,String> params, AuditInfo auditInfo);

}
