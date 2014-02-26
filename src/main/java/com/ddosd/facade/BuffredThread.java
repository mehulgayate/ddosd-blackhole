package com.ddosd.facade;

import com.ddosd.facade.entity.User;

public class BuffredThread {
	
	private Thread thread;
	private Integer priority;
	private User user;	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
