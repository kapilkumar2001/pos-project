package com.increff.pos.api;

import com.increff.pos.model.OrderFopObject;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.PDFHandler;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoiceApi {
   
    @Transactional(rollbackOn = ApiException.class)
    public void generateInvoice(OrderPojo orderPojo, List<OrderItemData> orderItemDataList, double totalAmount) throws ApiException {

        OrderFopObject orderFopObject = new OrderFopObject();
        orderFopObject.setOrderId(orderPojo.getId());
        
        String pattern = "dd MMM yyyy hh:mm a";
        orderFopObject.setTime(orderPojo.getUpdatedAt().format(DateTimeFormatter.ofPattern(pattern)));
       
        DecimalFormat dec = new DecimalFormat("#.##");
        
        orderFopObject.setOrders(orderItemDataList);
        orderFopObject.setTotalAmount(Double.valueOf(dec.format(totalAmount)));


        PDFHandler handler = new PDFHandler();
        String templateFilePath ="src/main/resources/com/increff/pos/";

        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(orderFopObject);
            handler.createPDFFile(orderPojo.getId(),streamSource,templateFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
