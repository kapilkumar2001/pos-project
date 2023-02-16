package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData{

	private int id;
	private String productName;
	private String barcode;
	private int quantity;
}
