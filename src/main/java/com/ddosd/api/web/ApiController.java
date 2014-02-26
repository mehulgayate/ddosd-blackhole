package com.ddosd.api.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.FacadeRepository;
import com.ddosd.facade.entity.User;

@Controller
public class ApiController {

	@Resource
	private FacadeRepository repository;
	
	@RequestMapping("/user/{userId}")
	public ModelAndView showUserData(HttpServletRequest request,@PathVariable("userId") Long userId){
		ModelAndView mv=new ModelAndView("json-string");
		User user=repository.findUserById(userId);
		JSONObject jsonObject=new JSONObject();
		if(user!=null){		
		jsonObject.put("id",user.getId());
		jsonObject.put("name", user.getName());
		jsonObject.put("email", user.getEmail());
		jsonObject.put("trustScore", user.getTrustScore());
		jsonObject.put("role", user.getRole());
		}
		mv.addObject("userId",jsonObject);
		return mv;
	
	}
}
