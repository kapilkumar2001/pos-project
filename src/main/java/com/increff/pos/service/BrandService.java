package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.util.StringUtil;

@Service
public class BrandService{

	@Autowired
	private BrandDao brandDao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo brandPojo) throws ApiException {
		normalize(brandPojo);
		if(StringUtil.isEmpty(brandPojo.getBrand())) {
			throw new ApiException("brand name cannot be empty");
		}
		else if(StringUtil.isEmpty(brandPojo.getCategory())) {
			throw new ApiException("category name cannot be empty");
		}
		BrandPojo p = brandDao.getBrand(brandPojo.getBrand(), brandPojo.getCategory());
		if(p!=null) {
			throw new ApiException("This brand-category combination already exists");
		}
		brandDao.insert(brandPojo);
	}
		
	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return brandDao.selectAll();
	}
	
	@Transactional
	public List<BrandPojo> getCategories(String brand) {
		return brandDao.selectCategories(brand);
	}

	@Transactional
	public List<BrandPojo> getBrands(String category) {
		return brandDao.selectBrands(category);
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, BrandPojo brandPojo) throws ApiException {
		normalize(brandPojo);
		
		if(StringUtil.isEmpty(brandPojo.getBrand())) {
			throw new ApiException("brand name cannot be empty");
		}
		else if(StringUtil.isEmpty(brandPojo.getCategory())) {
			throw new ApiException("category name cannot be empty");
		}
		BrandPojo p = brandDao.getBrand(brandPojo.getBrand(), brandPojo.getCategory());
		if(p!=null && (p.getId()!=id)) {
			throw new ApiException("Brand and Category already exists");
		}
		
		BrandPojo newPojo = getCheck(id);
		newPojo.setBrand(brandPojo.getBrand());
		newPojo.setCategory(brandPojo.getCategory());
		brandDao.update(newPojo);
	}
	
	@Transactional
	public BrandPojo getCheck(int id) throws ApiException {
		BrandPojo brandPojo = brandDao.select(id);
		if (brandPojo == null) {
			throw new ApiException("ID does not exit, id: " + id);
		}
		return brandPojo;
	}

	@Transactional
	public BrandPojo getBrand(String brand, String category) throws ApiException{
		BrandPojo brandPojo =  brandDao.getBrand(brand, category);
		if(brandPojo==null){
			throw  new ApiException("Brand-Category Combination doesn't exist");
		}
		return brandPojo;
	}
	
	protected static void normalize(BrandPojo brandPojo) {
		brandPojo.setBrand(StringUtil.toLowerCase(brandPojo.getBrand()));
		brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()));
	}
}
