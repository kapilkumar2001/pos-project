package com.increff.pos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.model.PosDaySalesData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.PosDaySalesPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.PosDaySalesService;

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

    @Transactional
    public List<PosDaySalesData> getAllDaySale(){
        List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();

        List<PosDaySalesPojo> posDaySalesPojoList = posDaySalesService.getAll();

        for(PosDaySalesPojo posDaySalesPojo: posDaySalesPojoList){
            PosDaySalesData posDaySalesData = new PosDaySalesData();

            posDaySalesData.setDate(posDaySalesPojo.getDate());
            posDaySalesData.setInvoicedItemsCount(posDaySalesPojo.getInvoicedItemsCount());
            posDaySalesData.setInvoicedOrdersCount(posDaySalesPojo.getInvoicedOrdersCount());
            posDaySalesData.setTotalRevenue(posDaySalesPojo.getTotalRevenue());

            posDaySalesDataList.add(posDaySalesData);
        }    
        return posDaySalesDataList;
    }

    @Transactional
    public List<PosDaySalesData> getDaySale(String startDate, String endDate){
        System.out.println("in getDateSale");
        List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startdate = LocalDate.parse(startDate, formatter);
        LocalDate enddate = LocalDate.parse(endDate, formatter);

        List<PosDaySalesPojo> posDaySalesPojoList = posDaySalesService.getByDate(startdate, enddate);

        for(PosDaySalesPojo posDaySalesPojo: posDaySalesPojoList){
            PosDaySalesData posDaySalesData = new PosDaySalesData();
            posDaySalesData.setDate(posDaySalesPojo.getDate());
            posDaySalesData.setInvoicedItemsCount(posDaySalesPojo.getInvoicedItemsCount());
            posDaySalesData.setInvoicedOrdersCount(posDaySalesPojo.getInvoicedOrdersCount());
            posDaySalesData.setTotalRevenue(posDaySalesPojo.getTotalRevenue());

            posDaySalesDataList.add(posDaySalesData);
        }
        return posDaySalesDataList;
    }
}
