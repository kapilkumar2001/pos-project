package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.InventoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api
@RestController
@RequestMapping("/api/inventory")//todo check this
public class InventoryApiController {

    @Autowired
	private InventoryDto dto;
	
	@ApiOperation(value = "Gets inventory by barcode")
	@RequestMapping(path = "{barcode}", method = RequestMethod.GET)
//	@GetMapping(path = "{barcode}")
	public InventoryData get(@PathVariable String barcode) throws ApiException{
		return dto.get(barcode);
	}

	//todo check path variables and url prams differnce
	
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
}
