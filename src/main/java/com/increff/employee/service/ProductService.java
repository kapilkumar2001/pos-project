package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService{

	@Autowired
	ProductDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getName())) {
			throw new ApiException("product name cannot be empty");
		}
		else if(StringUtil.isEmpty(p.getBarcode())) {
			throw new ApiException("barcode cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(p.getMrp()))) {
			throw new ApiException("mrp cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(p.getBrand()))) {
			throw new ApiException("brand cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(p.getCategory()))) {
			throw new ApiException("category cannot be empty");
		}
		int id = dao.getBrandCategory(p).getId();
		if(id==0) {
			throw new ApiException("brand category doesn't exist");
		}
		p.setBrand_category(id);
		dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		ProductPojo p = getCheck(id);
		BrandCategoryPojo b = dao.getBrandCategoryById(p.getBrand_category());
		p.setBrand(b.getBrand());
		p.setCategory(b.getCategory());
		return p;
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> getAll() throws ApiException{
		List<ProductPojo> products = dao.selectAll();
	    for(ProductPojo p: products)  {
	    	BrandCategoryPojo b = dao.getBrandCategoryById(p.getBrand_category());
			p.setBrand(b.getBrand());
			p.setCategory(b.getCategory());
		}
		return products;
		
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheck(id);
		ex.setBarcode(p.getBarcode());
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		ex.setBrand_category(dao.getBrandCategory(p).getId());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		dao.update(ex);
	}
	
	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("ID does not exit, id: " + id);
		}
		return p;
	}
	
	protected static void normalize(ProductPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
		p.setName(StringUtil.toLowerCase(p.getName()));
	}
}
