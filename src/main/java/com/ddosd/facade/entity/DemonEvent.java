package com.ddosd.facade.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.evalua.entity.support.EntityBase;

@Entity
public class DemonEvent extends EntityBase{

	public enum DemonType{
		BUFFERED_REQUEST_DEMON,SESSION_VALIDATOR_DEMON,TRUST_SCORE_CALCULATOR_DEMON;
	}
	
	private Date startTime;
	private Date endTime;
	private DemonType type;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public DemonType getType() {
		return type;
	}
	public void setType(DemonType type) {
		this.type = type;
	}
	
	
}
