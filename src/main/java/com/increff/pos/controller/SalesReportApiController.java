package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.SalesReportDto;
import com.increff.pos.model.SalesReportData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class SalesReportApiController {
    @Autowired
    private SalesReportDto dto;

    @ApiOperation(value = "Gets salesreport data")
    @RequestMapping(path = "/api/sales-report/", method = RequestMethod.GET)
    public List<SalesReportData> get(
        @RequestParam(value="startdate", defaultValue = "", required = false) String startDate,
        @RequestParam(value="enddate", defaultValue = "", required = false) String endDate,
        @RequestParam(value="brand", required = false, defaultValue = "") String brand,
        @RequestParam(value="category", required = false, defaultValue = "") String category)
        throws ApiException {
            return dto.get(startDate, endDate, brand, category);
    }

}
