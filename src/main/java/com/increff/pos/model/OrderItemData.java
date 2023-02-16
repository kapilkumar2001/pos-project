package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm{

	private int id;
	private int productId;
	private String productName;
	private double amount;
}
