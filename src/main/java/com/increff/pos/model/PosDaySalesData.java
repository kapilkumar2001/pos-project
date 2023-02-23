package com.increff.pos.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosDaySalesData {
    
    private String date;
    private int invoicedOrdersCount;
    private int invoicedItemsCount;
    private int totalRevenue;
}
