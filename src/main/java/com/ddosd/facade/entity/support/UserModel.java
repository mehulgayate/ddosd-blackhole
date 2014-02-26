package com.ddosd.facade.entity.support;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.User.UserRole;
import com.ddosd.facade.entity.User.UserStatus;

public class UserModel {
	
	private String name;
	private String email;	
	private Integer trustScore=10;	
	private UserStatus status;
	private Integer lastSessionRequestCount;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getTrustScore() {
		return trustScore;
	}
	public void setTrustScore(Integer trustScore) {
		this.trustScore = trustScore;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public Integer getLastSessionRequestCount() {
		return lastSessionRequestCount;
	}
	public void setLastSessionRequestCount(Integer lastSessionRequestCount) {
		this.lastSessionRequestCount = lastSessionRequestCount;
	}	

}
