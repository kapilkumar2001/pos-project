package com.increff.employee.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private LocalDateTime time;
	private String status;
	
	public int getId() {
		return id;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() { 
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
