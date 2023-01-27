package com.increff.employee.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderPojo;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Transactional(rollbackOn = ApiException.class) 
	public void createOrder(OrderPojo orderPojo) throws ApiException{
		normalize(orderPojo);
		orderDao.insert(orderPojo);
	}
	
	@Transactional(rollbackOn = ApiException.class) 
	public OrderPojo getOrder(int id) throws ApiException{
		OrderPojo orderPojo = orderDao.select(id);
		if(orderPojo == null) {
			throw new ApiException("order with this id doesn't exist, id: " + id);
		}
		return orderPojo;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<OrderPojo> getAllOrders() throws ApiException{
		List<OrderPojo> orders = orderDao.selectAll();
		return orders;
	}

    @Transactional
	public List<OrderPojo> getOrderByTime(LocalDateTime startTime, LocalDateTime endTime){
		return orderDao.selectByTime(startTime, endTime);
	}
	
	@Transactional
	public void update(OrderPojo orderPojo) throws ApiException{
		orderDao.update(orderPojo);
	}

	protected static void normalize(OrderPojo orderPojo) {
		orderPojo.setTime(LocalDateTime.now());
		orderPojo.setStatus("created");
	}

	
}
