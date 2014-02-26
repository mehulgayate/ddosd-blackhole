package com.ddosd.facade.entity;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

import com.evalua.entity.support.EntityBase;

@Entity
public class AccessToken extends EntityBase{

	private String accessToken;	

	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}	

	public static AccessToken generateToken(User user){
		SecureRandom random = new SecureRandom();
		AccessToken accessToken=new AccessToken();
		accessToken.setAccessToken(new BigInteger(130, random).toString(32));
		return accessToken;
	}


}
