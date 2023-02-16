package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class InventoryPojo {
	@Id
	private int id;
	private int quantity;
	@Transient
	private String barcode;
}
