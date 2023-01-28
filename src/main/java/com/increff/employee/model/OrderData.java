package com.increff.employee.model;

import java.util.List;

public class OrderData{

	private int id;
	private String createdAt;
	private String updatedAt;
	private String status;
	private List<OrderItemData> orders;

	public String getStatus() {
		return status;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	public List<OrderItemData> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderItemData> orders) {
		this.orders = orders;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
