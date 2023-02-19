package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String barcode;
	private int brand_id;
	private String name;
	private double mrp;
	@Transient
	private String brand;
	@Transient
	private String category;
	
	
	public int getBrandId() {
		return brand_id;
	}
	public void setBrandId(int brand_id) {
		this.brand_id = brand_id;
	}

}
