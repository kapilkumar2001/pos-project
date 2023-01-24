package com.increff.employee.controller;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.SalesReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class SalesReportApiController {
    @Autowired
    private SalesReportService salesReportService;

    @ApiOperation(value = "Gets salesreport data")
    @RequestMapping(path = "/api/sales-report/", method = RequestMethod.GET)
    public List<SalesReportData> get(
            @RequestParam(value="brand", required = false, defaultValue = "") String brand,
            @RequestParam(value="category", required = false, defaultValue = "") String category)
            throws ApiException {
                return salesReportService.get(brand, category);
    }

}
