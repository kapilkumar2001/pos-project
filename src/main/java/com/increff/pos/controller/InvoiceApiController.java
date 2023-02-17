package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.InvoiceDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class InvoiceApiController {

    @Autowired
    private InvoiceDto dto;

    @ApiOperation(value="generates invoice")
    @RequestMapping(path="api/invoice/{id}", method= RequestMethod.POST)
    public void generateInvoice(@PathVariable int id) throws ApiException {
        dto.generateInvoice(id);
    }

    @ApiOperation(value="gets invoice")
    @RequestMapping(path="api/invoice/{id}", method=RequestMethod.GET)
    public ResponseEntity<byte[]> getInvoice(@PathVariable int id) throws IOException, ApiException  {
        return dto.getInvoice(id);
    }

}
