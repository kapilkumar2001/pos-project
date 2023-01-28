package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.PosDaySalesDto;
import com.increff.employee.model.PosDaySalesData;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class PosDaySalesController {

    @Autowired
    private PosDaySalesDto posDaySalesDto;
    
    @ApiOperation(value = "Gets list of every day sales")
	@RequestMapping(path = "/api/posdaysales-report", method = RequestMethod.GET)
	public List<PosDaySalesData> getAll() throws ApiException {
		return posDaySalesDto.getAllDaySale();
	}
}
