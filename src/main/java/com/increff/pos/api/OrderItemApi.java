package com.increff.pos.api;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;

@Service
public class OrderItemApi {

    @Autowired
    private OrderItemDao dao;
    
    @Transactional(rollbackOn = ApiException.class) 
	public void addItemstoOrder(int orderId, List<OrderItemPojo> orderItemPojoList) throws ApiException{
        for(OrderItemPojo orderItemPojo: orderItemPojoList)
		{
			orderItemPojo.setOrderId(orderId);
			addOrderItem(orderItemPojo);
		}
    }
    
    @Transactional(rollbackOn = ApiException.class) 
	public void addOrderItem(OrderItemPojo orderItemPojo) throws ApiException {
		dao.insert(orderItemPojo);	
	} 

    @Transactional(rollbackOn = ApiException.class) 
	public List<OrderItemPojo> getOrderItemsbyOrderId(int orderId) throws ApiException{
		List<OrderItemPojo> orderItemPojoList = dao.selectByOrderId(orderId);
		return orderItemPojoList;
	}

    public OrderItemPojo getOrderItembyItemId(int id) throws ApiException{
		return dao.selectByOrderItemId(id);
	}

    @Transactional(rollbackOn  = ApiException.class)
	public void update(int orderId, List<OrderItemPojo> orderItemPojoList, List<Integer> orderItemIdstoRemove) throws ApiException {
		for(OrderItemPojo orderItemPojo: orderItemPojoList){
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
				OrderItemPojo orderItemPojoNew = dao.selectByOrderItemId(orderItemPojo.getId());		
			    orderItemPojoNew.setBarcode(orderItemPojo.getBarcode());
			    orderItemPojoNew.setOrderId(orderId);
			    orderItemPojoNew.setQuantity((int) orderItemPojo.getQuantity());
				orderItemPojoNew.setSellingPrice(orderItemPojo.getSellingPrice());
			    orderItemPojoNew.setProductId(orderItemPojo.getProductId());	
				dao.update(orderItemPojoNew);
			}
		}
		// deleting the deleted order-items from OrderItemPojo 
		for(Integer orderItemId: orderItemIdstoRemove) {
			dao.delete(orderItemId);
		}	
	}
}
