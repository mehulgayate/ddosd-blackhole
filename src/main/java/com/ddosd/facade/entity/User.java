package com.ddosd.facade.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.evalua.entity.support.EntityBase;

@Entity
public class User extends EntityBase{
	
	public enum UserStatus{
		ACTIVE,BLOCKED,DELETED;
	}
	
	public enum UserRole{
		ADMIN,NORMAL;
	}
	
	private String name;
	private String email;
	private String password;
	private AccessToken accessToken;
	private Integer trustScore=10;	
	private UserStatus status=UserStatus.ACTIVE;
	private UserRole role=UserRole.NORMAL;
	private Integer lastSessionCount;
	
	public Integer getLastSessionCount() {
		return lastSessionCount;
	}
	public void setLastSessionCount(Integer lastSessionCount) {
		this.lastSessionCount = lastSessionCount;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public Integer getTrustScore() {
		return trustScore;
	}
	public void setTrustScore(Integer trustScore) {
		this.trustScore = trustScore;
	}
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	public AccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
