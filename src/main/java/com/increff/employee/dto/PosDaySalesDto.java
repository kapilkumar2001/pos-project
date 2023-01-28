package com.increff.employee.dto;

import java.time.LocalDate;
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
import com.increff.employee.service.PosDaySalesService;

@Component
public class PosDaySalesDto {
    
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired 
    private PosDaySalesService posDaySalesService;

    @Transactional
    @Scheduled(cron = "59 59 23 * * *")
    public void create() throws ApiException {
        PosDaySalesPojo posDaySalesPojo = new PosDaySalesPojo();
        LocalDateTime time = LocalDateTime.now();
        LocalDate date = time.toLocalDate();
        
        posDaySalesPojo.setDate(date);
        LocalDateTime dayEndTime = time;
        LocalDateTime dayStartTime = time.minusHours(23).minusMinutes(59).minusSeconds(59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(dayStartTime.toString());
        String newFormat1 = dateTime.format(formatter);
    
        dateTime = LocalDateTime.parse(dayEndTime.toString());
        String newFormat2 = dateTime.format(formatter);
        
        dayStartTime = LocalDateTime.parse(newFormat1, formatter);
        dayEndTime = LocalDateTime.parse(newFormat2, formatter);
     
        List<OrderPojo> orderPojoList = orderService.getOrderByTime(dayStartTime, dayEndTime);
        int invoicedOrdersCount = 0;
        int invoicedItemsCount = 0;
        int totalRevenue = 0;
        for(OrderPojo orderPojo: orderPojoList){
            if((orderPojo.getStatus()).equals("invoiced")){
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
        
        posDaySalesService.create(posDaySalesPojo);
    }
}
