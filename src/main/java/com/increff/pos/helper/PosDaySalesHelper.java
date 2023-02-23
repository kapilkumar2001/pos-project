package com.increff.pos.helper;

import java.time.format.DateTimeFormatter;

import com.increff.pos.model.PosDaySalesData;
import com.increff.pos.pojo.PosDaySalesPojo;

public class PosDaySalesHelper {
    public static PosDaySalesData convert(PosDaySalesPojo posDaySalesPojo){
        PosDaySalesData posDaySalesData = new PosDaySalesData();
        String pattern = "dd MMM yyyy";
        posDaySalesData.setDate(posDaySalesPojo.getDate().format(DateTimeFormatter.ofPattern(pattern)));
        posDaySalesData.setInvoicedItemsCount(posDaySalesPojo.getInvoicedItemsCount());
        posDaySalesData.setInvoicedOrdersCount(posDaySalesPojo.getInvoicedOrdersCount());
        posDaySalesData.setTotalRevenue(posDaySalesPojo.getTotalRevenue());
        return posDaySalesData;
    } 
}
