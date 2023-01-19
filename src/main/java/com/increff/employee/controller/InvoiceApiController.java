package com.increff.employee.controller;

import com.increff.employee.service.ApiException;
import com.increff.employee.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class InvoiceApiController {

    @Autowired
    private InvoiceService invoiceService;

    @ApiOperation(value="generates invoice")
    @RequestMapping(path="api/invoice/{id}", method= RequestMethod.POST)
    public void generateInvoice(@PathVariable int id) throws ApiException {
        invoiceService.generateInvoice(id);
    }

}
