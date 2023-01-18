package com.increff.employee.model;

import java.util.List;

public class OrderData extends OrderForm{

	private int id;
	private String time;
	private List<OrderItemData> orders;

	public List<OrderItemData> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderItemData> orders) {
		this.orders = orders;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
