package com.increff.pos.controller;

import com.increff.pos.dto.SalesReportDto;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class SalesReportApiController {
    @Autowired
    private SalesReportDto salesReportDto;

    @ApiOperation(value = "Gets salesreport data")
    @RequestMapping(path = "/api/sales-report/", method = RequestMethod.GET)
    public List<SalesReportData> get(
        @RequestParam(value="startdate", defaultValue = "2023-01-24") String startDate,
        @RequestParam(value="enddate", defaultValue = "2023-02-30") String endDate,
        @RequestParam(value="brand", required = false, defaultValue = "") String brand,
        @RequestParam(value="category", required = false, defaultValue = "") String category)
        throws ApiException {
            return salesReportDto.get(startDate, endDate, brand, category);
    }

}
