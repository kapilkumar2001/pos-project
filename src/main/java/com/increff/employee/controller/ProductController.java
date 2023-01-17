package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	
	@ApiOperation(value = "Adds product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductPojo productPojo = convert(form);
		productService.add(productPojo);
		
		// Adds inventory
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setQuantity(0);
		inventoryPojo.setBarcode(form.getBarcode());
		inventoryService.add(inventoryPojo);
	}
	
	@ApiOperation(value = "Deletes a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		productService.delete(id);
	}

	@ApiOperation(value = "Gets a product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductPojo productPojo = productService.get(id);
		return convert(productPojo);
	}

	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		List<ProductPojo> productPojoList = productService.getAll();
		List<ProductData> productDataList = new ArrayList<ProductData>();
		for (ProductPojo productPojo : productPojoList) {
			productDataList.add(convert(productPojo));
		}
		return productDataList;
	}

	@ApiOperation(value = "Updates an product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm productForm) throws ApiException {
		ProductPojo productPojo = convert(productForm);
		productService.update(id, productPojo);
	}
	
	private static ProductData convert(ProductPojo productPojo) {
		ProductData productData = new ProductData();
		productData.setId(productPojo.getId());
		productData.setName(productPojo.getName());
		productData.setBarcode(productPojo.getBarcode());
		productData.setMrp(productPojo.getMrp());
		productData.setBrand(productPojo.getBrand());
		productData.setCategory(productPojo.getCategory());
		productData.setBrandCategory(productPojo.getBrand_category());
		return productData;
	}

	private static ProductPojo convert(ProductForm productForm) {
		ProductPojo productPojo = new ProductPojo();
		productPojo.setBarcode(productForm.getBarcode());
		productPojo.setMrp(productForm.getMrp());
		productPojo.setBrand(productForm.getBrand());
		productPojo.setCategory(productForm.getCategory());
		productPojo.setName(productForm.getName());
		return productPojo;
	}
}
