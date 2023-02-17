package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private OrderDto dto;
	
	@ApiOperation(value="creates an order")
	@RequestMapping(path="api/order", method=RequestMethod.POST)
	public void createOrder(@RequestBody List<OrderItemForm> form) throws ApiException{
		dto.createOrder(form);
	}
	
	@ApiOperation(value="gets an order")
	@RequestMapping(path="api/order/{id}", method=RequestMethod.GET)
	public OrderData getOrder(@PathVariable int id) throws ApiException{
		return dto.getOrderItems(id);
	}
	
	@ApiOperation(value="get a list of all orders")
	@RequestMapping(path="api/order", method=RequestMethod.GET)
	public List<OrderData> getAllOrders() throws ApiException{
		return dto.getAllOrders();
	}
	
	@ApiOperation(value = "Updates an order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody List<OrderItemForm> form) throws ApiException {
		dto.update(id, form);
	}

	@ApiOperation(value = "Cancel an order")
	@RequestMapping(path = "/api/order/cancel/{id}", method = RequestMethod.PUT)
	public void cancel(@PathVariable int id) throws ApiException {
		dto.cancelOrder(id);
	}
}
