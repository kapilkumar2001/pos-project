package com.increff.pos.model;

public class ProductData extends ProductForm{

	private int id;
	private int brand_category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBrandCategory() {
		return brand_category;
	}

	public void setBrandCategory(int brand_category) {
		this.brand_category = brand_category;
	}
	
}
