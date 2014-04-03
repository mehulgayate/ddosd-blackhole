package com.ddosd.facade.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.DemonEvent.DemonType;
import com.ddosd.facade.entity.FacadeRepository;
import com.ddosd.facade.entity.Session;
import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.UserSession;
import com.ddosd.facade.entity.User.UserRole;
import com.ddosd.facade.entity.User.UserStatus;
import com.ddosd.facade.entity.UserSession.SessionStatus;
import com.ddosd.facade.entity.support.UserForm;
import com.ddosd.facade.web.support.FacadeService;
import com.evalua.entity.support.DataStoreManager;

@Controller
public class FacadeResponseController {

	@Resource
	private DataStoreManager dataStoreManager;

	@Resource
	private FacadeService facadeService;

	@Resource
	private FacadeRepository facadeRepository;

	@RequestMapping("/seed-admin")
	public ModelAndView seedUsers(){
		ModelAndView mv=new ModelAndView("redirect:/user/login");
		User user=facadeRepository.findUserByEmail("admin@dgmail.com");
		if(user==null){
		user=new User();
		user.setEmail("admin@gmail.com");
		user.setPassword("1234");
		user.setName("Mehul Gayate");
		user.setStatus(UserStatus.ACTIVE);
		user.setRole(UserRole.ADMIN);
		dataStoreManager.save(user);
		}
		return mv;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpSession httpSession){
		ModelAndView mv=new ModelAndView("login");
		User userl=(User) httpSession.getAttribute("user");
		if(userl!=null){
			return new ModelAndView("redirect:/admin");
		}
		return mv;
	}
	

	@RequestMapping("/")
	public ModelAndView home(HttpSession httpSession){
		ModelAndView mv=new ModelAndView("index");
		
		return mv;
	}


	@RequestMapping("/admin-login")
	public ModelAndView showAdminLogin(){
		ModelAndView mv=new ModelAndView("in-admin-panel/login");

		return mv;
	}


	@RequestMapping("/admin")
	public ModelAndView showAdminScreen(HttpSession httpSession){
		User userl=(User) httpSession.getAttribute("user");
		if(userl==null){
			return new ModelAndView("redirect:/admin-login");
		}
		
		ModelAndView mv=new ModelAndView("in-admin-panel/index");
		List<User> users=facadeRepository.listAllUsers();

		for (User user : users) {
			Session currentSession=facadeRepository.findUserRequestCount(user);
			if(currentSession!=null){
				user.setLastSessionCount(currentSession.getRequestCount());
			}
		}
		mv.addObject("users", users);
		return mv;
	}


	@RequestMapping("/blocked-users")
	public ModelAndView showBlockedUsersScreen(HttpSession httpSession){
		User userl=(User) httpSession.getAttribute("user");
		if(userl==null){
			return new ModelAndView("redirect:/admin-login");
		}
		ModelAndView mv=new ModelAndView("in-admin-panel/blocked-users");
		List<User> users=facadeRepository.listAllBlockedUsers();

		for (User user : users) {
			Session currentSession=facadeRepository.findUserRequestCount(user);
			if(currentSession!=null){
				user.setLastSessionCount(currentSession.getRequestCount());
			}
		}
		mv.addObject("users", users);
		return mv;
	}

	@RequestMapping("/register")
	public ModelAndView register(){
		ModelAndView mv=new ModelAndView("signup/signup");

		return mv;
	}

	@RequestMapping("/register/add")
	public ModelAndView registerNewUser(HttpServletRequest request,@ModelAttribute(UserForm.key) UserForm userForm){
		ModelAndView mv=new ModelAndView("signup/complete");
		facadeService.addUser(userForm);
		return mv;
	}


	@RequestMapping("/user/login")
	public ModelAndView userLogin(HttpServletRequest request){
		ModelAndView mv=new ModelAndView("service-invoke/login");
		return mv;
	}

	@RequestMapping("/invoke-service")
	public ModelAndView invokePage(HttpServletRequest request,@RequestParam String accessToken,@RequestParam String userId){
		ModelAndView mv=new ModelAndView("service-invoke/invoke");
		mv.addObject("userId", userId);
		mv.addObject("accessToken", accessToken);
		return mv;
	}
	
	@RequestMapping("/admin/delete-user")
	public ModelAndView deleteUser(HttpServletRequest request,@RequestParam Long userId){
		ModelAndView mv=new ModelAndView("redirect:/admin");
		User user=facadeRepository.findUserById(userId);
		user.setStatus(UserStatus.DELETED);
		dataStoreManager.save(user);
		return mv;
	}
	
	@RequestMapping("/admin/activate-user")
	public ModelAndView unblockUser(HttpServletRequest request,@RequestParam Long userId){
		ModelAndView mv=new ModelAndView("redirect:/admin");
		User user=facadeRepository.findUserById(userId);
		user.setStatus(UserStatus.ACTIVE);
		AccessToken accessToken=user.getAccessToken();
		if(accessToken!=null){
			user.setAccessToken(null);
			UserSession userSession=facadeRepository.findActiveUserSessionByUser(user);
			userSession.setStatus(SessionStatus.INACTIVE);
			dataStoreManager.save(userSession);
			dataStoreManager.delete(accessToken);
		}
		dataStoreManager.save(user);
		return mv;
	}
	
	@RequestMapping("/admin/activate-monitor")
	public ModelAndView demonMonitor(HttpServletRequest request,HttpSession httpSession){
		User userl=(User) httpSession.getAttribute("user");
		if(userl==null){
			return new ModelAndView("redirect:/admin-login");
		}
		ModelAndView mv=new ModelAndView("in-admin-panel/demon-monitor");
		mv.addObject("queueDemon",facadeRepository.findLatestDemonEvent(DemonType.BUFFERED_REQUEST_DEMON));
		mv.addObject("sessionDemon",facadeRepository.findLatestDemonEvent(DemonType.SESSION_VALIDATOR_DEMON));
//		mv.addObject("sessionDemon",facadeRepository.findLatestDemonEvent(DemonType.SESSION_VALIDATOR_DEMON));


		return mv;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session){
		session.invalidate();
		return new ModelAndView("redirect:/user/login");
	}
}
