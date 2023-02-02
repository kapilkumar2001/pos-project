package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.StringUtil;

@Service
public class OrderItemService {

    @Autowired
    OrderItemDao orderItemDao;
    
    @Transactional(rollbackOn = ApiException.class) 
	public void addItemstoOrder(int orderId, List<OrderItemPojo> orderItemPojoList) throws ApiException{
        for(OrderItemPojo orderItemPojo: orderItemPojoList)
		{
			if(StringUtil.isEmpty(orderItemPojo.getBarcode())) {
				throw new ApiException("Barcode can not be empty");
			}
			if(orderItemPojo.getQuantity()<0) {
				throw new ApiException("Quantity can not be less than 0");
			}
			if(orderItemPojo.getSellingPrice()<=0) {
				throw new ApiException("Selling Price can not be less than or equal to 0");
			}
			orderItemPojo.setOrderId(orderId);
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
	public List<OrderItemPojo> getOrderItemsbyOrderId(int orderId) throws ApiException{
		List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderId);
		return orderItemPojoList;
	}

    public OrderItemPojo getOrderItembyItemId(int id) throws ApiException{
		return orderItemDao.selectByOrderItemId(id);
	}

    @Transactional(rollbackOn  = ApiException.class)
	public void update(int orderId, List<OrderItemPojo> orderItemPojoList, List<Integer> orderItemIdstoRemove) throws ApiException {

		for(OrderItemPojo orderItemPojo: orderItemPojoList)
		{

			if(StringUtil.isEmpty(orderItemPojo.getBarcode())) {
				throw new ApiException("Barcode can not be empty");
			}
			if(orderItemPojo.getQuantity()<0) {
				throw new ApiException("Quantity can not be less than 0");
			}
			if(orderItemPojo.getSellingPrice()<=0) {
				throw new ApiException("Selling Price can not be less than or equal to 0");
			}


			if(orderItemPojo.getId()==0) {
				OrderItemPojo orderItemPojoIn = new OrderItemPojo();
				
				orderItemPojoIn.setBarcode(orderItemPojo.getBarcode());
				orderItemPojoIn.setOrderId(orderId);
				orderItemPojoIn.setQuantity((int) orderItemPojo.getQuantity());
				orderItemPojoIn.setSellingPrice(orderItemPojo.getSellingPrice());
				orderItemPojoIn.setProductId(orderItemPojo.getProductId());
				addOrderItem(orderItemPojoIn);	
			}
			else {
				OrderItemPojo orderItemPojoNew = orderItemDao.selectByOrderItemId(orderItemPojo.getId());
				
			    orderItemPojoNew.setBarcode(orderItemPojo.getBarcode());
			    orderItemPojoNew.setOrderId(orderId);
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

    protected static void normalize(OrderItemPojo orderItemPojo) {
		orderItemPojo.setBarcode(StringUtil.toLowerCase(orderItemPojo.getBarcode()));
	}
}
