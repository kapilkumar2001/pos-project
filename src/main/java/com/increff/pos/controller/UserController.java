package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.UserApi;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;

import io.swagger.annotations.ApiOperation;

@Controller
public class UserController {
    @Autowired
	private UserApi api;
	@Autowired
	private InfoData info;
    @Value("${signup.email}")
	private String email;
	
	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm loginForm) throws ApiException {
		UserPojo userPojo = api.get(loginForm.getEmail());
		boolean authenticated = (Objects.nonNull(userPojo) && Objects.equals(userPojo.getPassword(), loginForm.getPassword()));
		if (!authenticated) {
			throw new ApiException("Invalid username or password");
		}
		
		Authentication authentication = convertUserPojo(userPojo);
		HttpSession session = req.getSession(true);
		SecurityUtil.createContext(session);
		SecurityUtil.setAuthentication(authentication);
		info.setRole(userPojo.getRole());
		return new ModelAndView("redirect:/ui/brands");
	}

	@RequestMapping(path = "/session/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/login");
	}

    // signup
    @ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView addUser(HttpServletRequest req, UserForm form) throws ApiException {
		UserPojo userPojo = convertUserForm(form);
		api.add(userPojo);
		return new ModelAndView("redirect:/site/login");
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/session/signup", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = api.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo userPojo : list) {
			list2.add(convert(userPojo));
		}
		return list2;
	}

	private static UserData convert(UserPojo userPojo) {
		UserData userData = new UserData();
		userData.setEmail(userPojo.getEmail());
		userData.setRole(userPojo.getRole());
		userData.setId(userPojo.getId());
		return userData;
	}

	private UserPojo convertUserForm(UserForm userForm) {
		UserPojo userPojo = new UserPojo();
		userPojo.setEmail(userForm.getEmail());
		if(userForm.getEmail().equals(email)){
			userPojo.setRole("supervisor");	
		} else{
			userPojo.setRole("operator");
		}
		userPojo.setPassword(userForm.getPassword());
		return userPojo;
	}

	private static Authentication convertUserPojo(UserPojo userPojo) {
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(userPojo.getEmail());
		principal.setId(userPojo.getId());
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(userPojo.getRole()));
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}
}
