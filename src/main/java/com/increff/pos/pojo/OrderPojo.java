package com.increff.pos.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderPojo {

	// TODO: getter setter lombok

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private String status;
	
	public int getId() {
		return id;
	}
	
	public LocalDateTime getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.created_at = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updated_at;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updated_at = updatedAt;
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
