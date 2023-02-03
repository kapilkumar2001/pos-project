package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.PosDaySalesDto;
import com.increff.pos.model.PosDaySalesData;
import com.increff.pos.service.ApiException;

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

	@ApiOperation(value = "Gets day sales data by date range")
    @RequestMapping(path = "/api/posdaysales-report/", method = RequestMethod.GET)
    public List<PosDaySalesData> get(
        @RequestParam(value="startdate", defaultValue = "2023-01-24") String startDate,
        @RequestParam(value="enddate", defaultValue = "2023-02-30") String endDate)
        throws ApiException {
            return posDaySalesDto.getDaySale(startDate, endDate);
    }
}
