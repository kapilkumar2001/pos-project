package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.dto.ProductDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {

	@Autowired
	private ProductDto productDto;
	
	@ApiOperation(value = "Adds product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm productForm) throws ApiException {
		productDto.add(productForm);
	}

	@ApiOperation(value = "Gets a product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		return productDto.get(id);
	}

	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		return productDto.getAll();
	}

	@ApiOperation(value = "Updates an product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm productForm) throws ApiException {
		productDto.update(id, productForm);
	}
	

}
