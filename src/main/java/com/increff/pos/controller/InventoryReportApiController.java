package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.InventoryReportDto;
import com.increff.pos.model.InventoryReportData;

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
    private InventoryReportDto dto;//todo ove this to inventory controller

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/api/inventory-report/", method = RequestMethod.GET)
    public List<InventoryReportData> get() throws ApiException {
        return dto.get();
    }

}
