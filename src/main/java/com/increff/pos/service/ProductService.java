package com.increff.pos.service;
import java.text.DecimalFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService{

	@Autowired
	private ProductDao productDao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo productPojo) throws ApiException {
		normalize(productPojo);
		if(StringUtil.isEmpty(String.valueOf(productPojo.getBrand()))) {
			throw new ApiException("Brand cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(productPojo.getCategory()))) {
			throw new ApiException("Category cannot be empty");
		}
		else if(StringUtil.isEmpty(productPojo.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		else if(StringUtil.isEmpty(productPojo.getName())) {
			throw new ApiException("Product Name cannot be empty");
		}
		else if(productPojo.getMrp()<=0) {
			throw new ApiException("MRP should be greater than 0");
		}
		else if(productPojo.getBrand_category()==0) {
			throw new ApiException("Brand & Category combination doesn't exist");
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
		if(StringUtil.isEmpty(String.valueOf(productPojo.getBrand()))) {
			throw new ApiException("Brand cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(productPojo.getCategory()))) {
			throw new ApiException("Category cannot be empty");
		}
		if(productPojo.getBrand_category()==0) {
			throw new ApiException("Brand & Category combination doesn't exist");
		}
		else if(StringUtil.isEmpty(productPojo.getName())) {
			throw new ApiException("Product Name cannot be empty");
		}
		else if(StringUtil.isEmpty(productPojo.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		
		ProductPojo tmpPojo = productDao.getProductByBarcode(productPojo.getBarcode());
		if(tmpPojo!=null && tmpPojo.getId()!=id) {
			throw new ApiException("Product with this barcode already exist, barcode: "+ productPojo.getBarcode());
		}
		if(productPojo.getMrp()<=0){
			throw new ApiException("MRP should be greater than 0");
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
		if(StringUtil.isEmpty(barcode)){
			throw new ApiException("Barcode can not be empty");
		}
		ProductPojo productPojo = productDao.getProductByBarcode(barcode);
		if(productPojo==null) {
			throw new ApiException("Product with this barcode doesn't exist, barcode: "+ barcode);
		}
		return productPojo;
	}
	
    @Transactional
	public List<ProductPojo> getProductsByBrandCategoryId(int brandCategoryId) throws ApiException{
        return productDao.getAllProductByBrandsCategoryId(brandCategoryId);
	}

	public void checkSellingPrice(String barcode, double sellingPrice) throws ApiException{
		if(sellingPrice<0){
			throw new ApiException("Selling Price should be a positive number");
		}
		ProductPojo productPojo = productDao.getProductByBarcode(barcode);
		if(productPojo.getMrp()<sellingPrice){
			throw new ApiException("Selling Price should be less than MRP, MRP for item " + barcode + ": " + productPojo.getMrp());
		}
	}

	protected static void normalize(ProductPojo productPojo) {
		DecimalFormat dec = new DecimalFormat("#.##");
		productPojo.setMrp(Double.valueOf(dec.format(productPojo.getMrp())));
		productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()));
		productPojo.setName(StringUtil.toLowerCase(productPojo.getName()));
	}
}
