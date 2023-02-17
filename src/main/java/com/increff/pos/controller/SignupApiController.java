package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class SignupApiController {

	@Autowired
	private UserService service;

	@Value("${signup.email}")
	private String email;

	// signup 
	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView addUser(HttpServletRequest req, UserForm form) throws ApiException {
		UserPojo p = convert(form);
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

	private UserPojo convert(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		if(f.getEmail().equals(email)){
			p.setRole("supervisor");	
		}
		else{
			p.setRole("operator");
		}
		p.setPassword(f.getPassword());
		return p;
	}

}
