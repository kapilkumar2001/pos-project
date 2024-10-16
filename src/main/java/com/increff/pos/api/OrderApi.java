package com.increff.pos.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;

@Service
public class OrderApi {

	@Autowired
	private OrderDao dao;
	
	@Transactional(rollbackOn = ApiException.class) 
	public void createOrder(OrderPojo orderPojo) throws ApiException{
		dao.insert(orderPojo);
	}
	
	@Transactional(rollbackOn = ApiException.class) 
	public OrderPojo getOrder(int id) throws ApiException{
		OrderPojo orderPojo = dao.select(id);
		if(Objects.isNull(orderPojo)) {
			throw new ApiException("order with this id doesn't exist, id: " + id);
		}
		return orderPojo;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<OrderPojo> getAllOrders() throws ApiException{
		List<OrderPojo> orders = dao.selectAll();
		return orders;
	}

    @Transactional
	public List<OrderPojo> getOrderByTime(LocalDateTime startTime, LocalDateTime endTime){
		return dao.selectByTime(startTime, endTime);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public void update(OrderPojo orderPojo) throws ApiException{
		
		orderPojo.setUpdatedAt(LocalDateTime.now());
		dao.update(orderPojo);
	}
}
