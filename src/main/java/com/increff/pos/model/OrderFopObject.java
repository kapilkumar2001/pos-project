package com.increff.pos.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@XmlRootElement(name="OrderFopObject")
@XmlSeeAlso({OrderItemData.class})
public class OrderFopObject {

    private int orderId;
    private String time;
    private List<OrderItemData> orders;
    private double totalAmount;

    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "orderItem")
    public List<OrderItemData> getOrders() {
        return orders;
    }

}
