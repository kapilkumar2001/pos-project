package com.increff.pos.dto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InvoiceApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StatusEnum;

@Component
public class InvoiceDto {
    @Autowired
    InvoiceApi api;
    @Autowired
    OrderApi orderApi;
    @Autowired
    OrderItemApi orderItemApi;
    @Autowired
    ProductApi productApi;


    @Transactional
    public void generateInvoice(int orderId) throws ApiException {
        OrderPojo orderPojo =  orderApi.getOrder(orderId);
        
        if(orderPojo.getStatus().equals(StatusEnum.invoiced)){
            return;
        }

        // update order status to from created to invoiced
        orderPojo.setStatus(StatusEnum.invoiced);
        orderApi.update(orderPojo);

        List<OrderItemPojo> orderItemPojoList =  orderItemApi.getOrderItemsbyOrderId(orderId);

        DecimalFormat dec = new DecimalFormat("#.##");
        double totalAmount = 0;
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderItemData orderItemData = new OrderItemData();
            ProductPojo productPojo = productApi.getCheck(orderItemPojo.getProductId());
            orderItemData.setBarcode(productPojo.getBarcode());
            orderItemData.setQuantity(orderItemPojo.getQuantity());
            orderItemData.setSellingPrice(Double.valueOf(dec.format(orderItemPojo.getSellingPrice())));
            orderItemData.setId(orderItemPojo.getOrderId());
            orderItemData.setProductId(orderItemPojo.getProductId());
            orderItemData.setProductName(productPojo.getName());
            orderItemData.setOrderItemId(orderItemPojo.getId());
            double amount = orderItemPojo.getQuantity()*orderItemPojo.getSellingPrice();
            orderItemData.setAmount(Double.valueOf(dec.format(amount)));
            totalAmount += amount;
            orderItemDataList.add(orderItemData);
        }

        api.generateInvoice(orderPojo, orderItemDataList, totalAmount);
    }

    @Transactional
    public ResponseEntity<byte[]> getInvoice(int orderId) throws ApiException, IOException {
        String sourcePath = "src/main/invoices/invoice-" + orderId + ".pdf";
        byte[] contents = loadFile(sourcePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "invoice-" + orderId + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    public byte[] readFully(InputStream stream) throws IOException{
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1){
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public byte[] loadFile(String sourcePath) throws IOException{
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(new File(sourcePath));
            return readFully(inputStream);
        } 
        finally{
            if (Objects.nonNull(inputStream)){
                inputStream.close();
            }
        }
    }
}
