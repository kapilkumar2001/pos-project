package com.increff.pos.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.increff.pos.util.StatusEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	@Enumerated(EnumType.STRING)
	private StatusEnum status = StatusEnum.created;
	
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
}
