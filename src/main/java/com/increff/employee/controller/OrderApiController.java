package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderService orderService;
	
	@ApiOperation(value="creates an order")
	@RequestMapping(path="api/order", method=RequestMethod.POST)
	public void createOrder(@RequestBody List<OrderItemForm> form) throws ApiException{
		orderService.createOrder(form);
	}
	
	@ApiOperation(value="gets an order")
	@RequestMapping(path="api/order/{id}", method=RequestMethod.GET)
	public OrderData getOrder(@PathVariable int id) throws ApiException{
		return orderService.getOrderItems(id);
	}
	
	@ApiOperation(value="get a list of all orders")
	@RequestMapping(path="api/order", method=RequestMethod.GET)
	public List<OrderData> getAllOrders() throws ApiException{
		List<OrderData> orders = orderService.getAllOrders();
		return orders;
	}
	
	@ApiOperation(value = "Updates an order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody List<OrderItemForm> form) throws ApiException {
		orderService.update(id, form);
	}
}
