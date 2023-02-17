package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData{

	private int id;
	private int productId;
	private String productName;
	private double amount;
	private int quantity;
	private double sellingPrice;
	private String barcode;
	private int orderItemId;
}
