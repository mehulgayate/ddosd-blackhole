package com.ddosd.facade.demon;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ddosd.facade.BuffredThread;
import com.ddosd.facade.BuffredThreadQueue;
import com.ddosd.facade.entity.DemonEvent;
import com.ddosd.facade.entity.DemonEvent.DemonType;

public class BuffredThredQueueDemon extends TimerTask{


	public static Logger logger = LoggerFactory.getLogger(SessionValidatorDemon.class);

	public static Timer sessionTimer = new Timer("Buffered Thread Schudular", true);   

	private int initialDelay; 
	private int period;
	private SessionFactory sessionFactory;


	public BuffredThredQueueDemon(SessionFactory sessionFactory,int initialDelay,int period){
		this.initialDelay=initialDelay;
		this.period=period;
		this.sessionFactory=sessionFactory;
	}


	public void init() {
		logger.info("Scedulaing Buffered Thread Schudular *******");
		sessionTimer.schedule(this, initialDelay,period);
	}

	@Override
	public void run() {

		logger.info("Running Buffered Thread Schudular *******");
		Session hibernateSession=sessionFactory.openSession();
		Transaction transaction=hibernateSession.beginTransaction();
		DemonEvent demonEvent=new DemonEvent();
		demonEvent.setStartTime(new Date());
		demonEvent.setType(DemonType.BUFFERED_REQUEST_DEMON);
		for(int i=0;i<BuffredThreadQueue.buffredThreads.size();i++){
			BuffredThread buffredThread=BuffredThreadQueue.buffredThreads.poll();
			synchronized (buffredThread.getThread()) {
				buffredThread.getThread().notify();
			}
		}		
		demonEvent.setEndTime(new Date());
		hibernateSession.save(demonEvent);
		transaction.commit();
		hibernateSession.close();
	}

}
