package com.increff.pos.api;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.increff.pos.model.OrderFopObject;
import com.increff.pos.util.PDFHandler;
import com.increff.pos.model.OrderItemData;


public class TestPDF {
    public static void main(String args[]){
        OrderFopObject orderFopObject = new OrderFopObject();
        
        orderFopObject.setOrderId(0);
        LocalDateTime dateTime = LocalDateTime.now();
        String pattern = "dd MMM yyyy hh:mm a";

        orderFopObject.setTime(dateTime.format(DateTimeFormatter.ofPattern(pattern)));
        
        DecimalFormat dec = new DecimalFormat("#.##");
        double totalAmount = 0;
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(int i=0;i<6;i++) {
            OrderItemData orderItemData = new OrderItemData();
            orderItemData.setBarcode("abcdex"+i);
            orderItemData.setQuantity((2+i));
            double mrp = ((2.17)*(i+1)) + i;
            orderItemData.setSellingPrice(Double.valueOf(dec.format(mrp)));
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
        String templateFilePath ="src/main/resources/com/increff/pos/";

        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(orderFopObject);
            handler.createPDFFile(0,streamSource,templateFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



