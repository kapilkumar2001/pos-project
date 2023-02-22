package com.increff.pos.api;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
public class UserApi {

	@Autowired
	private UserDao dao;

	@Transactional
	public void add(UserPojo userPojo) throws ApiException {
		normalize(userPojo);
		UserPojo existing = dao.select(userPojo.getEmail());
		if (Objects.nonNull(existing)) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(userPojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	protected static void normalize(UserPojo userPojo) {
		userPojo.setEmail(userPojo.getEmail().toLowerCase().trim());
		userPojo.setRole(userPojo.getRole().toLowerCase().trim());
	}
}
