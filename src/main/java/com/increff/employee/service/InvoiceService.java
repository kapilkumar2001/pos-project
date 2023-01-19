package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.OrderFopObject;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.PDFHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackOn = ApiException.class)
    public void generateInvoice(int orderId) throws ApiException {

        OrderPojo orderPojo = orderDao.select(orderId);

        if(orderPojo == null) {
            throw new ApiException("order with this id doesn't exist, orderId: " + orderId);
        }

        if(orderPojo.getStatus()=="invoiced"){
            return;
        }

        // update order status to from created to invoiced
        orderPojo.setStatus("invoiced");
        orderDao.update(orderPojo);

        List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderId);

        OrderFopObject orderFopObject = new OrderFopObject();
        orderFopObject.setOrderId(orderPojo.getId());
        orderFopObject.setTime(orderPojo.getTime());

        double totalAmount = 0;
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderItemData orderItemData = new OrderItemData();
            ProductPojo productPojo = productDao.select(orderItemPojo.getProductId());
            orderItemData.setBarcode(productPojo.getBarcode());
            orderItemData.setQuantity(orderItemPojo.getQuantity());
            orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
            orderItemData.setId(orderItemPojo.getOrderId());
            orderItemData.setProductId(orderItemPojo.getProductId());
            orderItemData.setProductName(productPojo.getName());
            orderItemData.setOrderItemId(orderItemPojo.getId());
            double amount = orderItemPojo.getQuantity()*orderItemPojo.getSellingPrice();
            orderItemData.setAmount(amount);
            totalAmount += amount;
            orderItemDataList.add(orderItemData);
        }
        orderFopObject.setOrders(orderItemDataList);
        orderFopObject.setTotalAmount(totalAmount);


        PDFHandler handler = new PDFHandler();
        String templateFilePath ="src/main/resources/com/increff/employee/";

        System.out.println("generating pdf for order id:" + orderId);
        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(orderFopObject);
            handler.createPDFFile(orderId,streamSource,templateFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
