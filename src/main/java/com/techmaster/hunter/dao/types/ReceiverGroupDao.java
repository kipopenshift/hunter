package com.techmaster.hunter.dao.types;

import java.util.List;

import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.ReceiverGroup;

public interface ReceiverGroupDao {
	
	public abstract void insertReceiverGroup(ReceiverGroup receiverGroup);
	public ReceiverGroup getReceiverGroupById(Long groupId);
	public List<ReceiverGroup> getAllReceiverGroups();
	public List<ReceiverGroup> getAllRcvrGrpForUsrNam(String userName);
	public void deleteGroup(ReceiverGroup receiverGroup);
	public void updateGroup(ReceiverGroup update);
	public void removeReceiversFromGroup(Long groupId, List<HunterMessageReceiver> hntMsgRcvrs);
	public void addReceiversToGroup(Long groupId, List<HunterMessageReceiver> hntMsgRcvrs);

}
