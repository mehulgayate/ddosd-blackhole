package com.ddosd.facade.entity;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ddosd.facade.entity.DemonEvent.DemonType;
import com.ddosd.facade.entity.User.UserRole;
import com.ddosd.facade.entity.User.UserStatus;
import com.ddosd.facade.entity.UserSession.SessionStatus;

@Transactional
public class FacadeRepository {

	@Resource
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	public User findUserByEmail(String email){
		return (User) getSession().createQuery("From "+User.class.getName()+" where email=:email")
				.setParameter("email",email).uniqueResult();
	}

	public User findUserById(Long id){
		return (User) getSession().createQuery("From "+User.class.getName()+" where id=:id")
				.setParameter("id",id).uniqueResult();	
	}
	
	public com.ddosd.facade.entity.Session findActiveSessionByUser(User user){
		return (com.ddosd.facade.entity.Session) getSession().createQuery("select session From "+UserSession.class.getName()+" us where us.user=:user AND us.status=:status")
				.setParameter("user",user)
				.setParameter("status", SessionStatus.ACTIVE).uniqueResult();	
	}
	
	public com.ddosd.facade.entity.UserSession findActiveUserSessionByUser(User user){
		return (com.ddosd.facade.entity.UserSession) getSession().createQuery("From "+UserSession.class.getName()+" us where us.user=:user AND us.status=:status")
				.setParameter("user",user)
				.setParameter("status", SessionStatus.ACTIVE).uniqueResult();	
	}
	
	public List<User> listAllUsers(){		
		return getSession().createQuery("FROM "+User.class.getName() +" u where u.status=:status AND u.role=:role")
				.setParameter("status", UserStatus.ACTIVE)
				.setParameter("role", UserRole.NORMAL).list();
	}
	
	public List<User> listAllBlockedUsers(){		
		return getSession().createQuery("FROM "+User.class.getName() +" u where u.status=:status")
				.setParameter("status", UserStatus.BLOCKED).list();
	}
	
	public Date getMaxDate(User user){
		
		return (Date) getSession().createQuery("select max(usinnr.session.startTime) from "+UserSession.class.getName()+" usinnr where usinnr.user=:user")
				.setParameter("user", user).uniqueResult();
	}
	public com.ddosd.facade.entity.Session findUserRequestCount(User user){
		
		return (com.ddosd.facade.entity.Session) getSession().createQuery("select us.session from "+UserSession.class.getName()+" us where us.session.startTime=(select max(usinnr.session.startTime) from "+UserSession.class.getName()+" usinnr where usinnr.user=:user)")
				.setParameter("user", user)
				.uniqueResult();
	}
	
	public DemonEvent findLatestDemonEvent(DemonType demonType){
		
		return (DemonEvent) getSession().createQuery("from "+DemonEvent.class.getName()+" de where de.startTime=(select max(innr.startTime) from "+DemonEvent.class.getName()+" innr where innr.type=:type) AND de.type=:type")
				.setParameter("type", demonType)
				.uniqueResult();
	}

}
