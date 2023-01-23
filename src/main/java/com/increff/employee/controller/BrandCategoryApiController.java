package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.dto.BrandCategoryDto;
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
	BrandCategoryDto brandCategoryDto;
	
	@ApiOperation(value = "Adds brand and category")
	@RequestMapping(path = "/api/brandcategory", method = RequestMethod.POST)
	public void add(@RequestBody BrandCategoryForm form) throws ApiException {
		brandCategoryDto.add(form);
	}
	
	@ApiOperation(value = "Deletes an brand category")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		brandCategoryDto.delete(id);
	}

	@ApiOperation(value = "Gets an brandcategory by ID")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.GET)
	public BrandCategoryData get(@PathVariable int id) throws ApiException {
		BrandCategoryData brandCategoryData = brandCategoryDto.get(id);
		return brandCategoryData;
	}

	@ApiOperation(value = "Gets list of all brandcategories")
	@RequestMapping(path = "/api/brandcategory", method = RequestMethod.GET)
	public List<BrandCategoryData> getAll() {
		List<BrandCategoryData> brandCategoryDataList = brandCategoryDto.getAll();
		return brandCategoryDataList;
	}

	@ApiOperation(value = "Updates an brandcategory")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
		brandCategoryDto.update(id, brandCategoryForm);
	}
	
	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brandcategory/get-brands", method = RequestMethod.GET)
	public List<String> getAllBrands() {
		List<String> brandsList = brandCategoryDto.getBrands();
		return brandsList;
	}

	@ApiOperation(value = "Gets list of all categories by brands")
	@RequestMapping(path = "/api/brandcategory/get-categories/{brand}", method = RequestMethod.GET)
	public List<String> getCategories(@PathVariable String brand) {
		List<String> categoriesList = brandCategoryDto.getCategories(brand);
		return categoriesList;
	}

}
