package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.PosDaySalesDto;
import com.increff.pos.model.PosDaySalesData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class PosDaySalesController {

    @Autowired
    private PosDaySalesDto dto;
    
	@ApiOperation(value = "Gets day sales data by date range")
    @RequestMapping(path = "/api/posdaysales-report/", method = RequestMethod.GET)
    public List<PosDaySalesData> get(
        @RequestParam(value="startdate", defaultValue = "", required = false) String startDate,
        @RequestParam(value="enddate", defaultValue = "", required = false) String endDate)
        throws ApiException {
            return dto.getDaySale(startDate, endDate);
    }
}
