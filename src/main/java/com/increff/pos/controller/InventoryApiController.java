package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.InventoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryApiController {

    @Autowired
	private InventoryDto dto;
	
	@ApiOperation(value = "Adds inventory")
	@RequestMapping(path = "", method= RequestMethod.POST)
	public void add(@RequestBody InventoryForm inventoryForm) throws ApiException{
		dto.add(inventoryForm);
	}

	@ApiOperation(value = "Gets inventory by barcode")
	@RequestMapping(path = "{barcode}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable String barcode) throws ApiException{
		return dto.get(barcode);
	}

	@ApiOperation(value = "Gets list of all inventories")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		return dto.getAll();
	}
	
	@ApiOperation(value = "Updates an inventory")
	@RequestMapping(path = "{barcode}", method = RequestMethod.PUT)
	public void update(@PathVariable String barcode, @RequestBody InventoryForm inventoryForm) throws ApiException {
		dto.update(barcode, inventoryForm);
	}

	@ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/report/", method = RequestMethod.GET)
    public List<InventoryReportData> getReport() throws ApiException {
        return dto.getReportData();
    }
}
