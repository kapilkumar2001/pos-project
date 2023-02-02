package com.increff.pos.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import java.util.List;

@XmlRootElement(name="OrderFopObject")
@XmlSeeAlso({OrderItemData.class})
public class OrderFopObject {
    private int orderId;
    private String time;
    private List<OrderItemData> orders;
    private double totalAmount;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "orderItem")
    public List<OrderItemData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItemData> orders) {
        this.orders = orders;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
