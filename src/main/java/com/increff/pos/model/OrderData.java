package com.increff.pos.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData{

	private int id;
	private String createdAt;
	private String updatedAt;
	private String status;
	private List<OrderItemData> orders;
}
