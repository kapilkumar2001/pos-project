package com.increff.employee.service;

import com.increff.employee.model.OrderFopObject;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.PDFHandler;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoiceService {
   
    @Transactional(rollbackOn = ApiException.class)
    public void generateInvoice(OrderPojo orderPojo, List<OrderItemData> orderItemDataList, double totalAmount) throws ApiException {

        OrderFopObject orderFopObject = new OrderFopObject();
        orderFopObject.setOrderId(orderPojo.getId());
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String dateTime = orderPojo.getUpdatedAt().format(format);
        orderFopObject.setTime(dateTime);
       
        DecimalFormat dec = new DecimalFormat("#.##");
        
        orderFopObject.setOrders(orderItemDataList);
        orderFopObject.setTotalAmount(Double.valueOf(dec.format(totalAmount)));


        PDFHandler handler = new PDFHandler();
        String templateFilePath ="src/main/resources/com/increff/employee/";

        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(orderFopObject);
            handler.createPDFFile(orderPojo.getId(),streamSource,templateFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
