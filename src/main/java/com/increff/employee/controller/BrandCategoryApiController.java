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
		BrandCategoryPojo p = convert(form);
		service.add(p);
	}
	
	@ApiOperation(value = "Deletes an brand category")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an brandcategory by ID")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.GET)
	public BrandCategoryData get(@PathVariable int id) throws ApiException {
		BrandCategoryPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all brandcategories")
	@RequestMapping(path = "/api/brandcategory", method = RequestMethod.GET)
	public List<BrandCategoryData> getAll() {
		List<BrandCategoryPojo> list = service.getAll();
		List<BrandCategoryData> list2 = new ArrayList<BrandCategoryData>();
		for (BrandCategoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an brandcategory")
	@RequestMapping(path = "/api/brandcategory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandCategoryForm f) throws ApiException {
		BrandCategoryPojo p = convert(f);
		service.update(id, p);
	}
	
	private static BrandCategoryData convert(BrandCategoryPojo p) {
		BrandCategoryData d = new BrandCategoryData();
		d.setId(p.getId());
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		return d;
	}

	private static BrandCategoryPojo convert(BrandCategoryForm f) {
		BrandCategoryPojo p = new BrandCategoryPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}
	
}
