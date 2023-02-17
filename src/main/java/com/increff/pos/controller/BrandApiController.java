package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.BrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandApiController {
	@Autowired
	private BrandDto dto;
	
	@ApiOperation(value = "Adds brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		dto.add(form);
	}
	
	@ApiOperation(value = "Gets an brand details by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "Gets list of all brands and categories")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Updates an brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm brandForm) throws ApiException {
		dto.update(id, brandForm);
	}
	
	@ApiOperation(value = "Gets list of all brands names")
	@RequestMapping(path = "/api/brand/get-brands", method = RequestMethod.GET)
	public List<String> getAllBrands() {
		return dto.getAllBrands();
	}

	@ApiOperation(value = "Gets list of all categories names by brand")
	@RequestMapping(path = "/api/brand/get-categories/{brand}", method = RequestMethod.GET)
	public List<String> getCategories(@PathVariable String brand) {
		return dto.getCategoriesByBrand(brand);
	}

	@ApiOperation(value = "Gets list of all categories")
	@RequestMapping(path = "/api/brand/get-categories", method = RequestMethod.GET)
	public List<String> getAllCategories() {
		return dto.getAllCategories();
	}
}
