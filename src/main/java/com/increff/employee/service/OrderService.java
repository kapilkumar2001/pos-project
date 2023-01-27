package com.increff.employee.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.StringUtil;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;	
	
	@Transactional(rollbackOn = ApiException.class) 
	public void createOrder(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) throws ApiException{
		normalize(orderPojo);
		orderDao.insert(orderPojo);
		
		for(OrderItemPojo orderItemPojo: orderItemPojoList)
		{
			orderItemPojo.setOrderId(orderPojo.getId());
			addOrderItem(orderItemPojo);
		}
	}

    @Transactional(rollbackOn = ApiException.class) 
	public void addOrderItem(OrderItemPojo orderItemPojo) throws ApiException {
		if(StringUtil.isEmpty(String.valueOf(orderItemPojo.getOrderId()))) {
			throw new ApiException("something went wrong. please try again!");
		}
		normalize(orderItemPojo);
		orderItemDao.insert(orderItemPojo);	
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
	public List<OrderItemPojo> getOrderItems(int orderId) throws ApiException{
		List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderId);
		return orderItemPojoList;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<OrderPojo> getAllOrders() throws ApiException{
		List<OrderPojo> orders = orderDao.selectAll();
		return orders;
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int orderId, List<OrderItemPojo> orderItemPojoList, List<Integer> orderItemIdstoRemove) throws ApiException {

		OrderPojo orderPojo = getOrder(orderId);
		
		for(OrderItemPojo orderItemPojo: orderItemPojoList)
		{
			if(orderItemPojo.getId()==0) {
				OrderItemPojo orderItemPojoIn = new OrderItemPojo();
				
				orderItemPojoIn.setBarcode(orderItemPojo.getBarcode());
				orderItemPojoIn.setOrderId(orderPojo.getId());
				orderItemPojoIn.setQuantity((int) orderItemPojo.getQuantity());
				orderItemPojoIn.setSellingPrice(orderItemPojo.getSellingPrice());
				orderItemPojoIn.setProductId(orderItemPojo.getProductId());
				addOrderItem(orderItemPojoIn);	
			}
			else {
				OrderItemPojo orderItemPojoNew = orderItemDao.selectByOrderItemId(orderItemPojo.getId());
				
			    orderItemPojoNew.setBarcode(orderItemPojo.getBarcode());
			    orderItemPojoNew.setOrderId(orderPojo.getId());
			    orderItemPojoNew.setQuantity((int) orderItemPojo.getQuantity());
				orderItemPojoNew.setSellingPrice(orderItemPojo.getSellingPrice());
			    orderItemPojoNew.setProductId(orderItemPojo.getProductId());
			
				normalize(orderItemPojoNew);	
				orderItemDao.update(orderItemPojoNew);
			}
		}
		
		// deleting the deleted order-items from OrderItemPojo 
		for(Integer orderItemId: orderItemIdstoRemove) {
			System.out.println("deleting orderItemId: "+ orderItemId);
			orderItemDao.delete(orderItemId);
		}	
	}

	public OrderItemPojo getOrderItembyItemId(int id) throws ApiException{
		return orderItemDao.selectByOrderItemId(id);
	}
	
	
	protected static void normalize(OrderPojo orderPojo) {
		orderPojo.setTime(LocalDateTime.now());
		orderPojo.setStatus("created");
	}
	
	protected static void normalize(OrderItemPojo orderItemPojo) {
		orderItemPojo.setBarcode(StringUtil.toLowerCase(orderItemPojo.getBarcode()));
	}
	
}
