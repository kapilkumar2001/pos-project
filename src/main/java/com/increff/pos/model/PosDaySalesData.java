package com.increff.pos.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosDaySalesData {
    
    private LocalDate date;
    private int invoicedOrdersCount;
    private int invoicedItemsCount;
    private int totalRevenue;
}
