package com.increff.pos.service;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

@Service
public class ProductService{

	@Autowired
	private ProductDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo productPojo) throws ApiException {
		if(productPojo.getBrandId()==0) {
			throw new ApiException("Brand & Category combination doesn't exist");
		}
		ProductPojo tmpPojo = dao.getProductByBarcode(productPojo.getBarcode());
		if(tmpPojo!=null) {
			throw new ApiException("Product with this barcode already exist, barcode: "+ productPojo.getBarcode());
		}
		if(productPojo.getMrp()<=0){
			throw new ApiException("MRP should be greater than 0");
		}

		dao.insert(productPojo);
	}
	
	@Transactional
	public ProductPojo getById(int id) throws ApiException {
		return dao.select(id);
	}

	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo productPojo = getById(id);
		System.out.println("product barcode: "+ productPojo.getBarcode());
		if (Objects.isNull(productPojo)) {
			throw new ApiException("ID does not exit, id: " + id);
		}
		System.out.println("returning pojo");
		return productPojo;
	}

	@Transactional
	public List<ProductPojo> getAll() throws ApiException{
		List<ProductPojo> products = dao.selectAll();
		return products;	
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, ProductPojo productPojo) throws ApiException {
		if(productPojo.getBrandId()==0) {
			throw new ApiException("Brand & Category combination doesn't exist");
		}
		
		ProductPojo tmpPojo = dao.getProductByBarcode(productPojo.getBarcode());
		if(Objects.nonNull(tmpPojo) && tmpPojo.getId()!=id) {
			throw new ApiException("Product with this barcode already exists, barcode: "+ productPojo.getBarcode());
		}
		if(productPojo.getMrp()<=0){
			throw new ApiException("MRP should be greater than 0");
		}

		ProductPojo newPojo = getCheck(id);
		newPojo.setBarcode(productPojo.getBarcode());
		newPojo.setBrand(productPojo.getBrand());
		newPojo.setCategory(productPojo.getCategory());
		newPojo.setBrandId(productPojo.getBrandId());
		newPojo.setMrp(productPojo.getMrp());
		newPojo.setName(productPojo.getName());
		dao.update(newPojo);
	}

	@Transactional
	public ProductPojo getProductByBarcode(String barcode) throws ApiException{
		if(StringUtil.isEmpty(barcode)){
			throw new ApiException("Barcode can not be empty");
		}
		ProductPojo productPojo = dao.getProductByBarcode(barcode);
		if(Objects.isNull(productPojo)) {
			throw new ApiException("Product with this barcode doesn't exist, barcode: "+ barcode);
		}
		return productPojo;
	}
	
    @Transactional
	public List<ProductPojo> getProductsByBrandId(int brandId) throws ApiException{
        return dao.getAllProductsByBrandId(brandId);
	}

	@Transactional
	public void checkSellingPrice(String barcode, double sellingPrice) throws ApiException{
		if(sellingPrice<0){
			throw new ApiException("Selling Price should be a positive number");
		}
		ProductPojo productPojo = dao.getProductByBarcode(barcode);
		if(productPojo.getMrp()<sellingPrice){
			throw new ApiException("Selling Price should be less than MRP, MRP for item " + barcode + ": " + productPojo.getMrp());
		}
	}
}
