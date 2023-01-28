package com.increff.employee.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.PosDaySalesPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
// import com.increff.employee.service.PosDaySalesService;

@Component
public class PosDaySalesDto {
    
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    // @Autowired 
    // private PosDaySalesService posDaySalesService;

    @Transactional
    // @Scheduled(fixedRate = 20000)
    public void create() throws ApiException {
        PosDaySalesPojo posDaySalesPojo = new PosDaySalesPojo();
        LocalDateTime time = LocalDateTime.now();
        
     
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
        // System.out.println("schedulr7");
        // LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
        // System.out.println(startDateTime);
        // System.out.println("schedulr8");
        // LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);
        // System.out.println(endDateTime);
   
        // System.out.println(time);
        posDaySalesPojo.setDate(time);
        LocalDateTime dayEndTime = time;
        LocalDateTime dayStartTime = time.minusHours(23).minusMinutes(59).minusSeconds(59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(dayStartTime.toString());
        String newFormat1 = dateTime.format(formatter);
        // System.out.println("newformat1: " + newFormat1);
    
        dateTime = LocalDateTime.parse(dayEndTime.toString());
        String newFormat2 = dateTime.format(formatter);
        // System.out.println("newformat2: " + newFormat2);

        dayStartTime = LocalDateTime.parse(newFormat1, formatter);
        System.out.println("newformat1: " + newFormat1);
        dayEndTime = LocalDateTime.parse(newFormat2, formatter);
        System.out.println("newformat2: " + newFormat2);

        // String tmpStartDate = time + "00:00:00";
        // String tmpEndDate = date + "23:59:59";

        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
                
        // LocalDateTime startDateTime = LocalDateTime.parse(dayStartTime.toString(), formatter);
        // LocalDateTime endDateTime = LocalDateTime.parse(dayEndTime.toString(), formatter);

        // dayStartTime = LocalDateTime.parse(.toString());
        // // String  = dayStartTime.format("yyyy-MM-ddHH:mm:ss");
        // dayEndTime = LocalDateTime.parse(.toString());

        // System.out.println("start" + startDateTime);
        // System.out.println("end" + endDateTime);
     
        List<OrderPojo> orderPojoList = orderService.getOrderByTime(dayStartTime, dayEndTime);
        System.out.println("schedulr9");
        int invoicedOrdersCount = 0;
        int invoicedItemsCount = 0;
        int totalRevenue = 0;
        for(OrderPojo orderPojo: orderPojoList){
            if(orderPojo.getStatus()=="invoiced"){
                invoicedOrdersCount+=1;
                List<OrderItemPojo> orderItemPojoList =  orderItemService.getOrderItemsbyOrderId(orderPojo.getId());  
                invoicedItemsCount += orderItemPojoList.size();

                for(OrderItemPojo orderItemPojo: orderItemPojoList){
                    totalRevenue += orderItemPojo.getSellingPrice();
                }
            }
        }

        posDaySalesPojo.setInvoicedOrdersCount(invoicedOrdersCount);
        posDaySalesPojo.setInvoicedItemsCount(invoicedItemsCount);
        posDaySalesPojo.setTotalRevenue(totalRevenue);

        System.out.println(invoicedOrdersCount + " " + invoicedItemsCount + " " + totalRevenue);
        
        // posDaySalesService.create(posDaySalesPojo);
    }
}
