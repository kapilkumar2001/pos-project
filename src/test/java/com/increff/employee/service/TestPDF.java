package com.increff.employee.service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.OrderFopObject;
import com.increff.employee.util.PDFHandler;
import com.increff.employee.model.OrderItemData;


public class TestPDF {
    public static void main(String args[]){
        OrderFopObject orderFopObject = new OrderFopObject();
        
        orderFopObject.setOrderId(0);
        orderFopObject.setTime("2023-01-17 06:03");
        
        DecimalFormat dec = new DecimalFormat("#.##");
        double totalAmount = 0;
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(int i=0;i<6;i++) {
            OrderItemData orderItemData = new OrderItemData();
            orderItemData.setBarcode("abcdex"+i);
            orderItemData.setQuantity((2+i));
            double mrp = ((2.1723)*(i+1)) + i;
            orderItemData.setSellingPrice(mrp);
            orderItemData.setId(0);
            orderItemData.setProductId((i+1)*5);
            orderItemData.setProductName("GoodDay Biscuit " + (i+1)*(5));
            orderItemData.setOrderItemId(i+1);
            double amount = (2+i)*(mrp);
            
            orderItemData.setAmount(Double.valueOf(dec.format(amount)));
            totalAmount += amount;
            orderItemDataList.add(orderItemData);
        }
        orderFopObject.setOrders(orderItemDataList);
        orderFopObject.setTotalAmount(Double.valueOf(dec.format(totalAmount)));
      
        PDFHandler handler = new PDFHandler();
        String templateFilePath ="src/main/resources/com/increff/employee/";

        System.out.println("generating pdf for order id:0");
        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(orderFopObject);
            handler.createPDFFile(0,streamSource,templateFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



