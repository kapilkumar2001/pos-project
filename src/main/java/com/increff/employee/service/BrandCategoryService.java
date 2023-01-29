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
	private BrandCategoryDao brandCategoryDao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandCategoryPojo brandCategoryPojo) throws ApiException {
		normalize(brandCategoryPojo);
		if(StringUtil.isEmpty(brandCategoryPojo.getBrand())) {
			throw new ApiException("brand name cannot be empty");
		}
		else if(StringUtil.isEmpty(brandCategoryPojo.getCategory())) {
			throw new ApiException("category name cannot be empty");
		}
		BrandCategoryPojo p = brandCategoryDao.getBrandCategory(brandCategoryPojo.getBrand(), brandCategoryPojo.getCategory());
		if(p!=null) {
			throw new ApiException("This brand-category combination already exists");
		}
		brandCategoryDao.insert(brandCategoryPojo);
	}
	
	@Transactional
	public void delete(int id) {
		brandCategoryDao.delete(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public BrandCategoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandCategoryPojo> getAll() {
		return brandCategoryDao.selectAll();
	}
	
	@Transactional
	public List<BrandCategoryPojo> getCategories(String brand) {
		return brandCategoryDao.selectCategories(brand);
	}

	@Transactional
	public List<BrandCategoryPojo> getBrands(String category) {
		return brandCategoryDao.selectBrands(category);
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, BrandCategoryPojo brandCategoryPojo) throws ApiException {
		normalize(brandCategoryPojo);
		
		if(StringUtil.isEmpty(brandCategoryPojo.getBrand())) {
			throw new ApiException("brand name cannot be empty");
		}
		else if(StringUtil.isEmpty(brandCategoryPojo.getCategory())) {
			throw new ApiException("category name cannot be empty");
		}
		BrandCategoryPojo p = brandCategoryDao.getBrandCategory(brandCategoryPojo.getBrand(), brandCategoryPojo.getCategory());
		if(p!=null && (p.getId()!=id)) {
			throw new ApiException("Brand and Category already exists");
		}
		
		BrandCategoryPojo newPojo = getCheck(id);
		newPojo.setBrand(brandCategoryPojo.getBrand());
		newPojo.setCategory(brandCategoryPojo.getCategory());
		brandCategoryDao.update(newPojo);
	}
	
	@Transactional
	public BrandCategoryPojo getCheck(int id) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = brandCategoryDao.select(id);
		if (brandCategoryPojo == null) {
			throw new ApiException("ID does not exit, id: " + id);
		}
		return brandCategoryPojo;
	}

	@Transactional
	public BrandCategoryPojo getBrandCategory(String brand, String category) throws ApiException{
		BrandCategoryPojo brandCategoryPojo =  brandCategoryDao.getBrandCategory(brand, category);
		if(brandCategoryPojo==null){
			throw  new ApiException("Brand-Category Combination doesn't exist");
		}
		return brandCategoryPojo;
	}
	
	protected static void normalize(BrandCategoryPojo brandCategoryPojo) {
		brandCategoryPojo.setBrand(StringUtil.toLowerCase(brandCategoryPojo.getBrand()));
		brandCategoryPojo.setCategory(StringUtil.toLowerCase(brandCategoryPojo.getCategory()));
	}
}
