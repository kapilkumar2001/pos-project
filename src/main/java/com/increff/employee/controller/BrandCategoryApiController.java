package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BrandCategoryData;
import com.increff.employee.model.BrandCategoryForm;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandCategoryApiController {

	@Autowired
	private BrandCategoryService service;
	
	@ApiOperation(value = "Adds brand and category")
	@RequestMapping(path = "/api/brandcategory", method = RequestMethod.POST)
	public void add(@RequestBody BrandCategoryForm form) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = convert(form);
		service.add(brandCategoryPojo);
	}
	
	@ApiOperation(value = "Deletes an brand category")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an brandcategory by ID")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.GET)
	public BrandCategoryData get(@PathVariable int id) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = service.get(id);
		return convert(brandCategoryPojo);
	}

	@ApiOperation(value = "Gets list of all brandcategories")
	@RequestMapping(path = "/api/brandcategory", method = RequestMethod.GET)
	public List<BrandCategoryData> getAll() {
		List<BrandCategoryPojo> list = service.getAll();
		List<BrandCategoryData> newList = new ArrayList<BrandCategoryData>();
		for (BrandCategoryPojo brandCategoryPojo : list) {
			newList.add(convert(brandCategoryPojo));
		}
		return newList;
	}

	@ApiOperation(value = "Updates an brandcategory")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandCategoryForm f) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = convert(f);
		service.update(id, brandCategoryPojo);
	}
	
	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brandcategory/get-brands", method = RequestMethod.GET)
	public List<String> getAllBrands() {
		List<BrandCategoryPojo> list = service.getAll();
		List<String> newList = new ArrayList<String>();
		for (BrandCategoryPojo brandCategoryPojo : list) {
			newList.add(brandCategoryPojo.getBrand());
		}
		return newList;
	}

	@ApiOperation(value = "Gets list of all categories by brands")
	@RequestMapping(path = "/api/brandcategory/get-categories/{brand}", method = RequestMethod.GET)
	public List<String> getCategories(@PathVariable String brand) {
		List<BrandCategoryPojo> list = service.getCategories(brand);
		List<String> newList = new ArrayList<String>();
		for (BrandCategoryPojo p : list) {
			newList.add(p.getCategory());
		}
		return newList;
	}
	
	private static BrandCategoryData convert(BrandCategoryPojo brandCategoryPojo) {
		BrandCategoryData brandCategoryData = new BrandCategoryData();
		brandCategoryData.setId(brandCategoryPojo.getId());
		brandCategoryData.setBrand(brandCategoryPojo.getBrand());
		brandCategoryData.setCategory(brandCategoryPojo.getCategory());
		return brandCategoryData;
	}

	private static BrandCategoryPojo convert(BrandCategoryForm brandCategoryForm) {
		BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
		brandCategoryPojo.setBrand(brandCategoryForm.getBrand());
		brandCategoryPojo.setCategory(brandCategoryForm.getCategory());
		return brandCategoryPojo;
	}
	
}
