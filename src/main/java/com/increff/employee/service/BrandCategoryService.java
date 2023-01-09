package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandCategoryDao;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.util.StringUtil;

@Service
public class BrandCategoryService{

	@Autowired
	BrandCategoryDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandCategoryPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("brand name cannot be empty");
		}
		else if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("category name cannot be empty");
		}
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public BrandCategoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandCategoryPojo> getAll() {
		return dao.selectAll();
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, BrandCategoryPojo p) throws ApiException {
		normalize(p);
		BrandCategoryPojo ex = getCheck(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		dao.update(ex);
	}
	
	@Transactional
	public BrandCategoryPojo getCheck(int id) throws ApiException {
		BrandCategoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("ID does not exit, id: " + id);
		}
		return p;
	}
	
	protected static void normalize(BrandCategoryPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
