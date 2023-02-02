package com.increff.pos.controller;

import com.increff.pos.dto.InvoiceDto;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class InvoiceApiController {

    @Autowired
    private InvoiceDto invoiceDto;

    @ApiOperation(value="generates invoice")
    @RequestMapping(path="api/invoice/{id}", method= RequestMethod.POST)
    public void generateInvoice(@PathVariable int id) throws ApiException {
        invoiceDto.generateInvoice(id);
    }

    @ApiOperation(value="gets invoice")
    @RequestMapping(path="api/invoice/{id}", method=RequestMethod.GET)
    public ResponseEntity<byte[]> getInvoice(@PathVariable int id) throws IOException  {

        String sourcePath = "src/main/invoices/invoice-" + id + ".pdf";
        byte[] contents = invoiceDto.loadFile(sourcePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        //  actual filename of pdf
        String filename = "increff-invoice-" + id + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

}
