package com.ddosd.facade.demon;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ddosd.facade.entity.DemonEvent;
import com.ddosd.facade.entity.DemonEvent.DemonType;
import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.User.UserStatus;
import com.ddosd.facade.entity.UserSession;

@Transactional
public class TrustScoreCalculatorDemon extends TimerTask{

	public static Logger logger = LoggerFactory.getLogger(TrustScoreCalculatorDemon.class);


	private SessionFactory sessionFactory;

	public static Timer sessionTimer = new Timer("Session Validator", true);    
	private int initialDelay; 
	private int period;


	public TrustScoreCalculatorDemon(SessionFactory sessionFactory,int initialDelay,int period){
		this.sessionFactory=sessionFactory;
		this.initialDelay=initialDelay;
		this.period=period;
	}


	public void init() {
		logger.info("Scedulaing Trust Score calculator *******");
		sessionTimer.schedule(this, initialDelay,period);
	}


	@Override
	public void run() {
		logger.info("Starting Trust Score calculator *******");
		Session hibernateSession=sessionFactory.openSession();
		Query query=hibernateSession.createQuery("From "+User.class.getName()+" u where u.status=:status");
		query.setParameter("status", UserStatus.ACTIVE);
		List<User> users=query.list();
		Transaction tr=hibernateSession.beginTransaction();
		DemonEvent demonEvent=new DemonEvent();
		demonEvent.setStartTime(new Date());
		demonEvent.setType(DemonType.TRUST_SCORE_CALCULATOR_DEMON);
		for (User user: users) {
			Query calcQuery=hibernateSession.createQuery("select avg(us.session.requestCount) From "+UserSession.class.getName()+" us where us.user=:user");
			calcQuery.setParameter("user", user);
			Double trustScore =(Double) calcQuery.uniqueResult();
			if(trustScore!=null){
				if(trustScore>10){
					user.setTrustScore(Math.round(new Float(trustScore)));
				}
			}
			hibernateSession.update(user);
		}
		demonEvent.setEndTime(new Date());
		hibernateSession.save(demonEvent);
		tr.commit();
		hibernateSession.close();
	}

	private Date getChangedDateByMins(Date date,int change){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, change);
		return calendar.getTime();
	}

}
