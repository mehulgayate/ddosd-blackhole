package com.ddosd.facade.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.User.UserStatus;
import com.evalua.entity.support.DataStoreManager;

@Controller
public class DataController {
	
	@Resource
	private DataStoreManager  dataStoreManager;

	@RequestMapping("/seedData")
	public ModelAndView seedUser(){
		ModelAndView mv= new ModelAndView("json-string");
		
		
		return mv;
	}
}
