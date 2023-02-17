package com.increff.pos.helper;

import com.increff.pos.model.PosDaySalesData;
import com.increff.pos.pojo.PosDaySalesPojo;

public class PosDaySalesHelper {
    public static PosDaySalesData convert(PosDaySalesPojo posDaySalesPojo){
        PosDaySalesData posDaySalesData = new PosDaySalesData();
        posDaySalesData.setDate(posDaySalesPojo.getDate());
        posDaySalesData.setInvoicedItemsCount(posDaySalesPojo.getInvoicedItemsCount());
        posDaySalesData.setInvoicedOrdersCount(posDaySalesPojo.getInvoicedOrdersCount());
        posDaySalesData.setTotalRevenue(posDaySalesPojo.getTotalRevenue());
        return posDaySalesData;
    } 
}
