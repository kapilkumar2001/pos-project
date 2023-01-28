package com.increff.employee.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PosDaySalesPojo {
    
    @Id
    private LocalDateTime date;
    private int invoiced_orders_count;
    private int invoiced_items_count;
    private int total_revenue;

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public int getInvoicedOrdersCount() {
        return invoiced_orders_count;
    }
    public void setInvoicedOrdersCount(int invoicedOrdersCount) {
        this.invoiced_orders_count = invoicedOrdersCount;
    }
    public int getInvoicedItemsCount() {
        return invoiced_items_count;
    }
    public void setInvoicedItemsCount(int invoicedItemsCount) {
        this.invoiced_items_count = invoicedItemsCount;
    }
    public int getTotalRevenue() {
        return total_revenue;
    }
    public void setTotalRevenue(int totalRevenue) {
        this.total_revenue = totalRevenue;
    }

}
