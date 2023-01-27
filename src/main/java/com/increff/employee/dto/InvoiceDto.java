package com.increff.employee.dto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InvoiceService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

@Component
public class InvoiceDto {
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProductService productService;


    @Transactional
    public void generateInvoice(int orderId) throws ApiException {
        OrderPojo orderPojo =  orderService.getOrder(orderId);
        
        if(orderPojo == null) {
            throw new ApiException("order with this id doesn't exist, orderId: " + orderId);
        }

        if(orderPojo.getStatus()=="invoiced"){
            return;
        }

        // update order status to from created to invoiced
        orderPojo.setStatus("invoiced");
        orderService.update(orderPojo);

        List<OrderItemPojo> orderItemPojoList =  orderItemService.getOrderItemsbyOrderId(orderId);

        DecimalFormat dec = new DecimalFormat("#.##");
        double totalAmount = 0;
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderItemData orderItemData = new OrderItemData();
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
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

        invoiceService.generateInvoice(orderPojo, orderItemDataList, totalAmount);
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
            if (inputStream != null){
                inputStream.close();
            }
        }
    }
}
