package com.ddosd.facade.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.evalua.entity.support.EntityBase;

@Entity
public class Session extends EntityBase {

	
	
	private Date startTime;
	private Date endTime;
	private Integer requestCount=0;
	
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
	public Integer getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(Integer requestCount) {
		this.requestCount = requestCount;
	}
	
	
}
