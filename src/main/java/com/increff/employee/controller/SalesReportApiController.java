package com.increff.employee.controller;

import com.increff.employee.model.SalesReportData;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.SalesReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class SalesReportApiController {
    @Autowired
    private SalesReportService salesReportService;

    @ApiOperation(value = "Gets salesreport data")
    @RequestMapping(path = "/api/sales-report/{startDate}/{endDate}/{brand}/{category}", method = RequestMethod.GET)
    public List<SalesReportData> get(
        @RequestParam(value="startdate", defaultValue = "2023-01-24") String startDate,
        @RequestParam(value="enddate", defaultValue = "2023-01-26") String endDate,
        @RequestParam(value="brand", required = false, defaultValue = "") String brand,
        @RequestParam(value="category", required = false, defaultValue = "") String category)
        throws ApiException {
            return salesReportService.get(startDate, endDate, brand, category);
    }

}
