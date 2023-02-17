package com.increff.pos.api;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;

@Service
public class BrandApi{

	@Autowired
	private BrandDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandPojo brandPojo) throws ApiException {
		BrandPojo p = dao.getBrandByBrandAndCategory(brandPojo.getBrand(), brandPojo.getCategory());
		if(Objects.nonNull(p)) {
			throw new ApiException("This brand-category combination already exists");
		}
		dao.insert(brandPojo);
	}
		
	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo getById(int id) throws ApiException {
		return dao.selectById(id);
	}

	@Transactional
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}
	
	@Transactional
	public List<BrandPojo> getByBrand(String brand) {
		return dao.selectByBrand(brand);
	}

	@Transactional
	public List<BrandPojo> getByCategory(String category) {
		return dao.selectByCategory(category);
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, BrandPojo brandPojo) throws ApiException {
		BrandPojo p = dao.getBrandByBrandAndCategory(brandPojo.getBrand(), brandPojo.getCategory());
		if(Objects.nonNull(p) && (p.getId()!=id)) {
			throw new ApiException("This brand-category combination already exists");
		}
		BrandPojo newPojo = getCheck(id);
		newPojo.setBrand(brandPojo.getBrand());
		newPojo.setCategory(brandPojo.getCategory());
		dao.update(newPojo);
	}
	
	@Transactional
	public BrandPojo getCheck(int id) throws ApiException {
		BrandPojo brandPojo = getById(id);
		if (Objects.isNull(brandPojo)) {
			throw new ApiException("ID does not exists, id: " + id);
		}
		return brandPojo;
	}

	@Transactional
	public BrandPojo getBrandByBrandAndCategory(String brand, String category) throws ApiException{
		BrandPojo brandPojo =  dao.getBrandByBrandAndCategory(brand, category);
		if(Objects.isNull(brandPojo)){
			throw  new ApiException("Brand-Category Combination doesn't exist");
		}
		return brandPojo;
	}
}
