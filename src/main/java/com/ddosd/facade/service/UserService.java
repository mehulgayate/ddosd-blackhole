package com.ddosd.facade.service;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.ddosd.facade.entity.FacadeRepository;
import com.ddosd.facade.entity.User;

public class UserService {

	@Resource
	private FacadeRepository facadeRepository;	
	
	public void setFacadeRepository(FacadeRepository repository) {
		this.facadeRepository = repository;
	}
	
	public User validate(String email,String password){
		User user=facadeRepository.findUserByEmail(email);
		if(user!=null){
			if(user.getPassword().equals(password)){
				return user;
			}		
		}
		return null;
	}
}
