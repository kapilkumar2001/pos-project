package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {

	private double quantity;
	private double sellingPrice;
	private String barcode;
	private int orderItemId;
}
