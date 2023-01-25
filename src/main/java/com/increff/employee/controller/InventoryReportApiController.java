package com.increff.employee.controller;

import com.increff.employee.model.InventoryReportData;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class InventoryReportApiController {

    @Autowired
    private InventoryReportService inventoryReportService;

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/api/inventory-report/", method = RequestMethod.GET)
    public List<InventoryReportData> get() throws ApiException {
        return inventoryReportService.get();
    }

}
