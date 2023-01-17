package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandCategoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private BrandCategoryDao brandCategoryDao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo productPojo) throws ApiException {
		normalize(productPojo);
		if(StringUtil.isEmpty(productPojo.getName())) {
			throw new ApiException("product name cannot be empty");
		}
		else if(StringUtil.isEmpty(productPojo.getBarcode())) {
			throw new ApiException("barcode cannot be empty");
		}
		else if(productPojo.getMrp()<=0) {
			throw new ApiException("mrp cannot be less than or equal to 0");
		}
		else if(StringUtil.isEmpty(String.valueOf(productPojo.getBrand()))) {
			throw new ApiException("brand cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(productPojo.getCategory()))) {
			throw new ApiException("category cannot be empty");
		}
		
		ProductPojo tmpPojo = productDao.getProductByBarcode(productPojo.getBarcode());
		if(tmpPojo!=null) {
			throw new ApiException("Product with this barcode already exist, barcode: "+ productPojo.getBarcode());
		}
		
		int id = brandCategoryDao.getBrandCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
		if(id==0) {
			throw new ApiException("brand & category doesn't exist");
		}
		productPojo.setBrand_category(id);
		productDao.insert(productPojo);
	}
	
	@Transactional
	public void delete(int id) {
		productDao.delete(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		ProductPojo p = getCheck(id);
		BrandCategoryPojo b = brandCategoryDao.select(p.getBrand_category());
		p.setBrand(b.getBrand());
		p.setCategory(b.getCategory());
		return p;
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> getAll() throws ApiException{
		List<ProductPojo> products = productDao.selectAll();
	    for(ProductPojo p: products)  {
	    	BrandCategoryPojo b = brandCategoryDao.select(p.getBrand_category());
			p.setBrand(b.getBrand());
			p.setCategory(b.getCategory());
		}
		return products;	
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, ProductPojo productPojo) throws ApiException {
		normalize(productPojo);
		if(StringUtil.isEmpty(productPojo.getName())) {
			throw new ApiException("product name cannot be empty");
		}
		else if(StringUtil.isEmpty(productPojo.getBarcode())) {
			throw new ApiException("barcode cannot be empty");
		}
		else if(productPojo.getMrp()<=0) {
			throw new ApiException("mrp cannot be less than or equal to 0");
		}
		else if(StringUtil.isEmpty(String.valueOf(productPojo.getBrand()))) {
			throw new ApiException("brand cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(productPojo.getCategory()))) {
			throw new ApiException("category cannot be empty");
		}
		
		ProductPojo tmpPojo = productDao.getProductByBarcode(productPojo.getBarcode());
		if(tmpPojo!=null && tmpPojo.getId()!=id) {
			throw new ApiException("Product with this barcode already exist, barcode: "+ productPojo.getBarcode());
		}
		
		int brandCategoryId = brandCategoryDao.getBrandCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
		if(brandCategoryId==0) {
			throw new ApiException("brand & category combination doesn't exist");
		}
		
		ProductPojo newPojo = getCheck(id);
		newPojo.setBarcode(productPojo.getBarcode());
		newPojo.setBrand(productPojo.getBrand());
		newPojo.setCategory(productPojo.getCategory());
		newPojo.setBrand_category(brandCategoryId);
		newPojo.setMrp(productPojo.getMrp());
		newPojo.setName(productPojo.getName());
		productDao.update(newPojo);
	}
	
	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = productDao.select(id);
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
