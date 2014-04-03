package com.ddosd.facade.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.evalua.entity.support.EntityBase;

@Entity
public class UserSession extends EntityBase {

	public enum SessionStatus{
		ACTIVE,INACTIVE,SUSPENDED;
	}
	
	private User user;
	private Session session;
	private SessionStatus status;
	
	public SessionStatus getStatus() {
		return status;
	}
	public void setStatus(SessionStatus status) {
		this.status = status;
	}
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
	
}
