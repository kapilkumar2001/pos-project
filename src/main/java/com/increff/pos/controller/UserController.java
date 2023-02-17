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

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;

import io.swagger.annotations.ApiOperation;

@Controller
public class UserController {
    @Autowired
	private UserService service;
	@Autowired
	private InfoData info;
    @Value("${signup.email}")
	private String email;
	
	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
		UserPojo p = service.get(f.getEmail());
		boolean authenticated = (Objects.nonNull(p) && Objects.equals(p.getPassword(), f.getPassword()));
		if (!authenticated) {
			throw new ApiException("Invalid username or password");
		}
		
		Authentication authentication = convertUserPojo(p);
		HttpSession session = req.getSession(true);
		SecurityUtil.createContext(session);
		SecurityUtil.setAuthentication(authentication);
		info.setRole(p.getRole());
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
		UserPojo p = convertUserForm(form);
		service.add(p);
		return new ModelAndView("redirect:/site/login");
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/session/signup", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = service.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	private static UserData convert(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}

	private UserPojo convertUserForm(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		if(f.getEmail().equals(email)){
			p.setRole("supervisor");	
		} else{
			p.setRole("operator");
		}
		p.setPassword(f.getPassword());
		return p;
	}

	private static Authentication convertUserPojo(UserPojo p) {
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(p.getEmail());
		principal.setId(p.getId());
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(p.getRole()));
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}
}
