package com.increff.employee.service;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService{

	@Autowired
	private ProductDao productDao;
	
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
		if(productPojo.getBrand_category()==0) {
			throw new ApiException("brand & category combination doesn't exist");
		}
		
		ProductPojo tmpPojo = productDao.getProductByBarcode(productPojo.getBarcode());
		if(tmpPojo!=null) {
			throw new ApiException("Product with this barcode already exist, barcode: "+ productPojo.getBarcode());
		}

		productDao.insert(productPojo);
	}
	
	@Transactional
	public void delete(int id) {
		productDao.delete(id);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		ProductPojo p = getCheck(id);
		return p;
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<ProductPojo> getAll() throws ApiException{
		List<ProductPojo> products = productDao.selectAll();
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
		if(productPojo.getBrand_category()==0) {
			throw new ApiException("brand & category combination doesn't exist");
		}
		
		ProductPojo tmpPojo = productDao.getProductByBarcode(productPojo.getBarcode());
		if(tmpPojo!=null && tmpPojo.getId()!=id) {
			throw new ApiException("Product with this barcode already exist, barcode: "+ productPojo.getBarcode());
		}

		ProductPojo newPojo = getCheck(id);
		newPojo.setBarcode(productPojo.getBarcode());
		newPojo.setBrand(productPojo.getBrand());
		newPojo.setCategory(productPojo.getCategory());
		newPojo.setBrand_category(productPojo.getBrand_category());
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

	@Transactional
	public ProductPojo getProductByBarcode(String barcode) throws ApiException{
		ProductPojo productPojo = productDao.getProductByBarcode(barcode);
		if(productPojo==null) {
			throw new ApiException("product with this barcode doesn't exist, barcode: "+ barcode);
		}
		return productPojo;
	}
	
    @Transactional
	public List<ProductPojo> getProductsByBrandCategoryId(int brandCategoryId) throws ApiException{
        return productDao.getAllProductByBrandsCategoryId(brandCategoryId);
	}

	protected static void normalize(ProductPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
		p.setName(StringUtil.toLowerCase(p.getName()));
	}
}
