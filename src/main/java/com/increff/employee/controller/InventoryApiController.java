package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api
@RestController
public class InventoryApiController {

	@Autowired
	private InventoryService service;
	
	
	@ApiOperation(value = "Adds inventory")
	@RequestMapping(path = "/api/inventory", method= RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException{
		InventoryPojo  p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Gets inventory by barcode")
	@RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable String barcode) throws ApiException{
		InventoryPojo p = service.get(barcode);
		return convert(p);
	}
	
	@ApiOperation(value = "Gets list of all inventories")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	@ApiOperation(value = "Updates an inventory")
	@RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.PUT)
	public void update(@PathVariable String barcode, @RequestBody InventoryForm f) throws ApiException {
		service.update(barcode, f.getQuantity());
	}

	private static InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setQuantity(p.getQuantity());
		d.setBarcode(p.getBarcode());
		d.setId(p.getId());
		return d;
	}
	
	private static InventoryPojo convert(InventoryForm f) {
		InventoryPojo p = new InventoryPojo();
		p.setQuantity(f.getQuantity());
		p.setBarcode(f.getBarcode());
		return p;
	}
	
}
