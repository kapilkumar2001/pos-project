package com.increff.pos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.PosDaySalesApi;
import com.increff.pos.helper.PosDaySalesHelper;
import com.increff.pos.model.PosDaySalesData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.PosDaySalesPojo;
import com.increff.pos.util.StatusEnum;

@Component
public class PosDaySalesDto {
    
    @Autowired
    private OrderItemApi orderItemApi;
    @Autowired
    private OrderApi orderApi;
    @Autowired 
    private PosDaySalesApi api;

    @Transactional
    public void create() throws ApiException {
        PosDaySalesPojo posDaySalesPojo = new PosDaySalesPojo();
        LocalDateTime time = LocalDateTime.now();
        LocalDate date = time.toLocalDate();
        posDaySalesPojo.setDate(date);

        LocalDateTime dayEndTime = formatDateTime(time);
        LocalDateTime dayStartTime = formatDateTime(time.minusHours(23).minusMinutes(59).minusSeconds(59));
     
        List<OrderPojo> orderPojoList = orderApi.getOrderByTime(dayStartTime, dayEndTime);
        int invoicedOrdersCount = 0;
        int invoicedItemsCount = 0;
        int totalRevenue = 0;
        for(OrderPojo orderPojo: orderPojoList){
            if((orderPojo.getStatus()).equals(StatusEnum.invoiced)){
                invoicedOrdersCount+=1;
                List<OrderItemPojo> orderItemPojoList =  orderItemApi.getOrderItemsbyOrderId(orderPojo.getId());  
                invoicedItemsCount += orderItemPojoList.size();
                for(OrderItemPojo orderItemPojo: orderItemPojoList){
                    totalRevenue += orderItemPojo.getSellingPrice();
                }
            }
        }
        posDaySalesPojo.setInvoicedOrdersCount(invoicedOrdersCount);
        posDaySalesPojo.setInvoicedItemsCount(invoicedItemsCount);
        posDaySalesPojo.setTotalRevenue(totalRevenue);    
        api.create(posDaySalesPojo);
    }

    @Transactional
    public List<PosDaySalesData> getAllDaySale(){
        List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();
        List<PosDaySalesPojo> posDaySalesPojoList = api.getAll();
        for(PosDaySalesPojo posDaySalesPojo: posDaySalesPojoList){
            posDaySalesDataList.add(PosDaySalesHelper.convert(posDaySalesPojo));
        }    
        return posDaySalesDataList;
    }

    @Transactional
    public List<PosDaySalesData> getDaySale(String startDate, String endDate){
        List<PosDaySalesData> posDaySalesDataList = new ArrayList<>();

        LocalDateTime time = LocalDateTime.now();
        if(startDate.equals("")){
            startDate = time.minusMonths(1).toLocalDate().toString(); 
        }
        if(endDate.equals("")){
            endDate = time.toLocalDate().toString();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startdate = LocalDate.parse(startDate, formatter);
        LocalDate enddate = LocalDate.parse(endDate, formatter);

        List<PosDaySalesPojo> posDaySalesPojoList = api.getByDate(startdate, enddate);
        for(PosDaySalesPojo posDaySalesPojo: posDaySalesPojoList){
            posDaySalesDataList.add(PosDaySalesHelper.convert(posDaySalesPojo));
        }
        return posDaySalesDataList;
    }

    public LocalDateTime formatDateTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time.toString());
        String newFormat = dateTime.format(formatter); 
        time = LocalDateTime.parse(newFormat, formatter);
        return time;
    }
}
