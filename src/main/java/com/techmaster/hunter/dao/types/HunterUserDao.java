package com.techmaster.hunter.dao.types;

import java.util.List;

import com.techmaster.hunter.obj.beans.HunterUser;

public interface HunterUserDao {
	
	
	public void insertHunterUser(HunterUser user);
	public void insertHunterUsers(List<HunterUser> hunterUsers);
	public void deleteHunterUserById(Long userId);
	public void deleteHunterUser(HunterUser user);
	public HunterUser getHunterUserById(Long id);
	public List<HunterUser> getAllUsers();
	public void updateUser(HunterUser update);
	public Long getNextUserId();
	
	

}
