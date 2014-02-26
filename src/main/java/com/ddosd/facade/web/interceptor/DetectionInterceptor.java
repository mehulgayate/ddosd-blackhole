package com.ddosd.facade.web.interceptor;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ddosd.facade.entity.AccessToken;
import com.ddosd.facade.entity.FacadeRepository;
import com.ddosd.facade.entity.Session;
import com.ddosd.facade.entity.User;
import com.ddosd.facade.entity.User.UserRole;
import com.ddosd.facade.entity.User.UserStatus;
import com.ddosd.facade.entity.UserSession;
import com.ddosd.facade.entity.UserSession.SessionStatus;
import com.ddosd.facade.service.UserService;
import com.ddosd.facade.web.support.FacadeService;
import com.evalua.entity.support.DataStoreManager;

public class DetectionInterceptor implements HandlerInterceptor {

	@Resource
	private UserService userService;	

	@Resource
	private FacadeService facadeService;

	@Resource
	private DataStoreManager dataStoreManager;	

	@Resource
	private FacadeRepository repository;	
	
	List<String> ignoreAuthenticationActions;

	
	public List<String> getIgnoreAuthenticationActions() {
		return ignoreAuthenticationActions;
	}

	public void setIgnoreAuthenticationActions(
			List<String> ignoreAuthenticationActions) {
		this.ignoreAuthenticationActions = ignoreAuthenticationActions;
	}


	public void setRepository(FacadeRepository repository) {
		this.repository = repository;
	}

	public void setDataStoreManager(DataStoreManager dataStoreManager) {
		this.dataStoreManager = dataStoreManager;
	}

	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		
		
		if (ignoreAuthenticationActions.contains(requestURI)) {
			return true;
		}

		System.out.println("*********** "+requestURI);
		if(requestURI.indexOf("login")>=0){
			String email=request.getParameter("email");
			String password=request.getParameter("password");
			User user=userService.validate(email, password);
			response.setContentType("application/json");		

			if(user!=null){
				
				if(user.getRole()==UserRole.ADMIN){
					request.getSession().setAttribute("user", user);
					return true;
				}
				
				if(user.getStatus()==UserStatus.BLOCKED){
					PrintWriter out=response.getWriter();
					out.print(facadeService.getErrorResponse(new Long(104), "User is blocked, Kindly contact admin@ddosd.com"));
					out.flush();
					return false;
				}
				
				AccessToken accessToken=user.getAccessToken();
				if(accessToken==null){
					accessToken=AccessToken.generateToken(user);
					dataStoreManager.save(accessToken);
					user.setAccessToken(accessToken);
					dataStoreManager.save(user);
					
					Session session=new Session();
					session.setStartTime(new Date());
					session.setRequestCount(0);
					
					UserSession userSession=new UserSession();
					userSession.setSession(session);
					userSession.setUser(user);
					userSession.setStatus(SessionStatus.ACTIVE);
					
					dataStoreManager.save(session);
					dataStoreManager.save(userSession);
				}else{
					Session session=repository.findActiveSessionByUser(user);
					session.setRequestCount(session.getRequestCount()+1);
					dataStoreManager.save(session);
				}
				PrintWriter out=response.getWriter();
				out.print(facadeService.getAccessTokenResponse(accessToken, user));
				out.flush();
				return false;
			}else{
				PrintWriter out=response.getWriter();
				out.print(facadeService.getErrorResponse(new Long(101), "email / password wrong."));
				out.flush();
				return false;
			}
		}else {
			String userIdString=request.getParameter("userId");
			if(userIdString==null){
				PrintWriter out=response.getWriter();
				out.print(facadeService.getErrorResponse(new Long(102), "Required Param userId Missing"));
				out.flush();
				return false;
			}
			User user=repository.findUserById(new Long(userIdString));
			if(user==null){
				PrintWriter out=response.getWriter();
				out.print(facadeService.getErrorResponse(new Long(103), "User Not Found with id : "+userIdString));
				out.flush();
				return false;
			}
			if(request.getParameter("access_token")==null || user.getAccessToken()==null || user.getAccessToken().getAccessToken()==null || !user.getAccessToken().getAccessToken().equals(request.getParameter("access_token"))){
				PrintWriter out=response.getWriter();
				out.print(facadeService.getErrorResponse(new Long(103), "Acess Token Not Valid : "+userIdString));
				out.flush();
				return false;
			}
			if(user.getStatus()==UserStatus.BLOCKED){
				PrintWriter out=response.getWriter();
				out.print(facadeService.getErrorResponse(new Long(104), "User is blocked, Kindly contact admin@ddosd.com"));
				out.flush();
				return false;
			}
			if(!facadeService.checkUserForDdosAttack(user)){
				PrintWriter out=response.getWriter();
				out.print(facadeService.getErrorResponse(new Long(104), "User is blocked, Kindly contact admin@ddosd.com"));
				out.flush();
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
					throws Exception {
		// TODO Auto-generated method stub

	}

}
