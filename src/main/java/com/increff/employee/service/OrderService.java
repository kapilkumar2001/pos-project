package com.increff.employee.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ProductDao productDao;
	
	
	@Transactional(rollbackOn = ApiException.class) 
	public void createOrder(List<OrderItemForm> form) throws ApiException{
		OrderPojo orderPojo = new OrderPojo();
		normalize(orderPojo);
		if(StringUtil.isEmpty(orderPojo.getTime())) {
			throw new ApiException("Something wrong! Please try again!");
		}
		orderDao.insert(orderPojo);
		
		for(OrderItemForm orderItemForm: form)
		{
			if(StringUtil.isEmpty(orderItemForm.getBarcode())) {
				throw new ApiException("Barcode can not be empty");
			}
			if(orderItemForm.getQuantity()<0) {
				throw new ApiException("Quantity can not be less than 0");
			}
			if(orderItemForm.getSellingPrice()<=0) {
				throw new ApiException("Selling Price can not be less than or equal to 0");
			}
			OrderItemPojo orderItemPojo = new OrderItemPojo();
			orderItemPojo.setBarcode(orderItemForm.getBarcode());
			orderItemPojo.setOrderId(orderPojo.getId());
			orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
			orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
			addOrderItem(orderItemPojo);	
		}
	}
	
	@Transactional(rollbackOn = ApiException.class) 
	public void addOrderItem(OrderItemPojo orderItemPojo) throws ApiException{
		normalize(orderItemPojo);
		
		if(StringUtil.isEmpty(orderItemPojo.getBarcode())) {
			throw new ApiException("Barcode can not be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(orderItemPojo.getOrderId()))) {
			throw new ApiException("something wrong while creating order. please try again!");
		}
		else if(StringUtil.isEmpty(String.valueOf(orderItemPojo.getQuantity()))) {
			throw new ApiException("quantity can not be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(orderItemPojo.getSellingPrice()))) {
			throw new ApiException("sellingprice can not be empty");
		}
			
		orderItemPojo.setProductId(productDao.getProductByBarcode(orderItemPojo.getBarcode()).getId());
		if(StringUtil.isEmpty(String.valueOf(orderItemPojo.getProductId()))) {
			throw new ApiException("product doesn't exist");
		}
		
		// reduce quantity in inventory
		InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId(), orderItemPojo.getBarcode());
		if(orderItemPojo.getQuantity()>inventoryPojo.getQuantity()) {
			throw new ApiException("not enough quantity available, barcode: " + orderItemPojo.getBarcode());
		}
		inventoryService.update(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
		
		orderItemDao.insert(orderItemPojo);
	}
	
	
	@Transactional(rollbackOn = ApiException.class) 
	public OrderPojo getOrder(int id) throws ApiException{
		OrderPojo orderPojo = getCheck(id);
		return orderPojo;
	}
	
	@Transactional(rollbackOn = ApiException.class) 
	public OrderData getOrderItems(int orderId) throws ApiException{
		List<OrderItemPojo> orderItemPojo = orderItemDao.selectByOrderId(orderId);
		OrderPojo orderPojo = getOrder(orderId);
		return convert(orderPojo, orderItemPojo);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<OrderData> getAllOrders() throws ApiException{
		List<OrderPojo> orders = orderDao.selectAll();
		List<OrderData> newList = new ArrayList<OrderData>();
		for(OrderPojo p: orders) {
			newList.add(getOrderItems(p.getId()));
		}
		return newList;
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int orderId, List<OrderItemForm> orderFormList) throws ApiException {
		OrderPojo orderPojo = getCheck(orderId);
		
		OrderData orderData = getOrderItems(orderId);
		List<OrderItemData> existingOrderItemDataList = orderData.getOrders();
		List<Integer> existingOrderItemIds = new ArrayList<>();
		for(OrderItemData orderItemData: existingOrderItemDataList) {
			existingOrderItemIds.add(orderItemData.getOrderItemId());
		}
		
		List<Integer> newOrderItemIds = new ArrayList<>();
		
		for(OrderItemForm orderItemForm: orderFormList)
		{
			newOrderItemIds.add(orderItemForm.getOrderItemId());
			
			if(StringUtil.isEmpty(orderItemForm.getBarcode())) {
				throw new ApiException("Barcode can not be empty");
			}
			if(orderItemForm.getQuantity()<0) {
				throw new ApiException("Quantity can not be less than 0");
			}
			if(orderItemForm.getSellingPrice()<=0) {
				throw new ApiException("Selling Price can not be less than or equal to 0");
			}
			
			System.out.println("orderitemid: " + orderItemForm.getOrderItemId());
			
			if(orderItemForm.getOrderItemId()==0) {
				
				System.out.println("orderItemPojo null");
				
				OrderItemPojo orderItemPojoIn = new OrderItemPojo();
				orderItemPojoIn.setBarcode(orderItemForm.getBarcode());
				orderItemPojoIn.setOrderId(orderPojo.getId());
				orderItemPojoIn.setQuantity((int) orderItemForm.getQuantity());
				orderItemPojoIn.setSellingPrice(orderItemForm.getSellingPrice());
				addOrderItem(orderItemPojoIn);	
			}
			else {
				
				OrderItemPojo orderItemPojo = orderItemDao.selectByOrderItemId(orderItemForm.getOrderItemId());
				OrderItemPojo orderItemPojoTemp = orderItemDao.selectByOrderItemId(orderItemForm.getOrderItemId());
				int prevQuantity =  orderItemPojoTemp.getQuantity();
				
				System.out.println("orderItemPojo not null, updating");
			
			    orderItemPojo.setBarcode(orderItemForm.getBarcode());
			    orderItemPojo.setOrderId(orderPojo.getId());
			    orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
				orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
			    
				normalize(orderItemPojo);
				
				ProductPojo productPojo = productDao.getProductByBarcode(orderItemPojo.getBarcode());
				if(StringUtil.isEmpty(String.valueOf(orderItemPojo.getProductId()))) {
					throw new ApiException("product doesn't exist, barcode:" + orderItemForm.getBarcode());
				}
				orderItemPojo.setProductId(productPojo.getId());
		
				// reduce quantity from inventory
				InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId(), orderItemPojo.getBarcode());
				
				System.out.println(orderItemPojo.getBarcode() + "  " + inventoryPojo.getQuantity() + "  " + prevQuantity + " " + orderItemPojo.getQuantity());
				if(orderItemPojo.getQuantity()>(inventoryPojo.getQuantity() + prevQuantity)) {
					throw new ApiException("not enough quantity available, barcode: " + orderItemPojo.getBarcode());
				}
				
				inventoryService.update(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  (inventoryPojo.getQuantity() + prevQuantity) - orderItemPojo.getQuantity());
				
				orderItemDao.update(orderItemPojo);
			}
		}
		
		for(Integer orderItemId: existingOrderItemIds) {
			System.out.println("existing orderItem, id:" + orderItemId);
			
		}
		
		for(Integer orderItemId: newOrderItemIds) {
			System.out.println("new orderItem, id:" + orderItemId);
			
		}
		
		
		existingOrderItemIds.removeAll(newOrderItemIds);
		
		for(Integer orderItemId: existingOrderItemIds) {
			System.out.println("deleting orderItem, id:" + orderItemId);
			orderItemDao.delete(orderItemId);
		}
		
	}
	
	@Transactional
	public OrderPojo getCheck(int id) throws ApiException{
		OrderPojo orderPojo = orderDao.select(id);
		if(orderPojo == null) {
			throw new ApiException("order with this id doesn't exist, id: " + id);
		}
		return orderPojo;
	}
	
	protected static void normalize(OrderPojo orderPojo) {
		orderPojo.setTime(StringUtil.toLowerCase(getTimestamp()));
		orderPojo.setStatus("created");
	}
	
	protected static void normalize(OrderItemPojo orderItemPojo) {
		orderItemPojo.setBarcode(StringUtil.toLowerCase(orderItemPojo.getBarcode()));
	}
	
	public static String getTimestamp() {
		String ts = Instant.now().toString();
		ts=ts.replace('T', ' ');
		ts=ts.replace('Z',' ');
		ts = ts.substring(0, 16);
		return ts;
	}
	
	public OrderData convert(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) {
		OrderData orderData = new OrderData();
		orderData.setTime(orderPojo.getTime());
		orderData.setId(orderPojo.getId());
		orderData.setStatus(orderPojo.getStatus());
		
		List<OrderItemData> orderItemDataList = new ArrayList<>();
		
		for(OrderItemPojo orderItemPojo : orderItemPojoList) {
			OrderItemData tmp = new OrderItemData();
			ProductPojo productPojo = productDao.select(orderItemPojo.getProductId()); 
			tmp.setBarcode(productPojo.getBarcode());
			tmp.setQuantity(orderItemPojo.getQuantity());
			tmp.setSellingPrice(orderItemPojo.getSellingPrice());
			tmp.setId(orderItemPojo.getOrderId());
			tmp.setProductId(orderItemPojo.getProductId());
			tmp.setProductName(productPojo.getName());	
			tmp.setOrderItemId(orderItemPojo.getId());
			orderItemDataList.add(tmp);
		}
		
		orderData.setOrders(orderItemDataList);
		return orderData;
	}
}
