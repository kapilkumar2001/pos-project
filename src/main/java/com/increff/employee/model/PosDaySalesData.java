package com.increff.employee.model;

import java.time.LocalDate;

public class PosDaySalesData {
    
    private LocalDate date;
    private int invoicedOrdersCount;
    private int invoicedItemsCount;
    private int totalRevenue;

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public int getInvoicedOrdersCount() {
        return invoicedOrdersCount;
    }
    public void setInvoicedOrdersCount(int invoicedOrdersCount) {
        this.invoicedOrdersCount = invoicedOrdersCount;
    }
    public int getInvoicedItemsCount() {
        return invoicedItemsCount;
    }
    public void setInvoicedItemsCount(int invoicedItemsCount) {
        this.invoicedItemsCount = invoicedItemsCount;
    }
    public int getTotalRevenue() {
        return totalRevenue;
    }
    public void setTotalRevenue(int totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
