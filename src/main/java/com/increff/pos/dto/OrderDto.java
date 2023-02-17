package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.OrderHelper;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StatusEnum;


@Component
public class OrderDto {

    @Autowired
    private OrderApi orderApi; 
	@Autowired
	private OrderItemApi orderItemApi;
    @Autowired
	private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    @Transactional(rollbackOn = ApiException.class)
    public void createOrder(List<OrderItemForm> orderItemFormList) throws ApiException{
		OrderHelper.validateOrderItems(orderItemFormList);
		List<OrderItemPojo> orderItemPojoList = convertToOrderItemPojo(orderItemFormList);
		OrderPojo orderPojo = new OrderPojo();
		OrderHelper.normalize(orderPojo);
        orderApi.createOrder(orderPojo);
		OrderHelper.normalize(orderItemPojoList);
		orderItemApi.addItemstoOrder(orderPojo.getId(), orderItemPojoList);
    }

    public OrderPojo getOrder(int id) throws ApiException{
		OrderPojo orderPojo = orderApi.getOrder(id);
		return orderPojo;
	}

    public OrderData getOrderItems(int orderId) throws ApiException{
		OrderPojo orderPojo = getOrder(orderId);
		List<OrderItemPojo> orderItemPojoList = orderItemApi.getOrderItemsbyOrderId(orderId);
		OrderData orderData = OrderHelper.convert(orderPojo);
		List<OrderItemData> orderItemDataList = convertToOrderItemData(orderItemPojoList);
		orderData.setOrders(orderItemDataList);
		return orderData;
	}

    public List<OrderData> getAllOrders() throws ApiException{
        List<OrderPojo> orderPojoList = orderApi.getAllOrders();
		List<OrderData> orderDataList = new ArrayList<OrderData>();
		for(OrderPojo orderPojo: orderPojoList) {
			orderDataList.add(getOrderItems(orderPojo.getId()));
		}
		return orderDataList;
    }

	@Transactional(rollbackOn = ApiException.class)
	public void cancelOrder(int orderId) throws ApiException{
		OrderPojo orderPojo = orderApi.getOrder(orderId);
		orderPojo.setStatus(StatusEnum.cancelled);
		orderApi.update(orderPojo);

		List<OrderItemPojo> existingOrderItemPojoList = orderItemApi.getOrderItemsbyOrderId(orderId);
		
		for(OrderItemPojo orderItemPojo: existingOrderItemPojoList) {
			String barcode = productApi.getCheck(orderItemPojo.getProductId()).getBarcode();
			inventoryApi.increaseInventory(orderItemPojo.getProductId(), barcode, orderItemPojo.getQuantity());
		}
	}

    @Transactional(rollbackOn = ApiException.class)
    public void update(int orderId, List<OrderItemForm> orderItemFormList) throws ApiException{
		OrderHelper.validateOrderItems(orderItemFormList);
		
		OrderPojo orderPojo = orderApi.getOrder(orderId);
		orderApi.update(orderPojo);
	
		List<OrderItemPojo> existingOrderItemPojoList = orderItemApi.getOrderItemsbyOrderId(orderId);
		List<Integer> existingOrderItemIds = new ArrayList<>();
		for(OrderItemPojo orderItemPojo: existingOrderItemPojoList) {
			existingOrderItemIds.add(orderItemPojo.getId());
		}

		List<Integer> newOrderItemIds = new ArrayList<>();
		List <OrderItemPojo> orderItemPojoList = new ArrayList<>();
		for(OrderItemForm orderItemForm: orderItemFormList)
		{
			newOrderItemIds.add(orderItemForm.getOrderItemId());

			OrderItemPojo orderItemPojo = OrderHelper.convert(orderItemForm);
			orderItemPojo.setOrderId(orderId);
			orderItemPojo.setId(orderItemForm.getOrderItemId());
			ProductPojo productPojo = productApi.getProductByBarcode(orderItemForm.getBarcode());
			orderItemPojo.setProductId(productPojo.getId());
			// reduce quantity in inventory
			if(orderItemForm.getOrderItemId()!=0){
				OrderItemPojo orderItemPojoTemp = orderItemApi.getOrderItembyItemId(orderItemForm.getOrderItemId());
				int prevQuantity =  orderItemPojoTemp.getQuantity();
				inventoryApi.updateInventoryWhileCreatingOrder(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  orderItemPojo.getQuantity(), prevQuantity);
			} else{ 
				inventoryApi.updateInventoryWhileCreatingOrder(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  orderItemPojo.getQuantity(), 0);
			}
			productApi.checkSellingPrice(orderItemForm.getBarcode(), orderItemForm.getSellingPrice());
			orderItemPojoList.add(orderItemPojo);
		}

		// increase quantity in inventory for deleted items
		existingOrderItemIds.removeAll(newOrderItemIds);
		for(Integer orderItemId: existingOrderItemIds) {
			OrderItemPojo orderItemPojo = orderItemApi.getOrderItembyItemId(orderItemId);
			String barcode = productApi.getCheck(orderItemPojo.getProductId()).getBarcode();
			inventoryApi.increaseInventory(orderItemPojo.getProductId(), barcode, orderItemPojo.getQuantity());
		}	
		List<Integer> orderItemIdstoRemove = existingOrderItemIds;

		OrderHelper.normalize(orderItemPojoList);
		orderItemApi.update(orderId, orderItemPojoList, orderItemIdstoRemove);
    }

	public List<OrderItemPojo> convertToOrderItemPojo(List<OrderItemForm> orderItemFormList) throws ApiException{
		List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
		for(OrderItemForm orderItemForm: orderItemFormList){
			OrderItemPojo orderItemPojo = OrderHelper.convert(orderItemForm);
			ProductPojo productPojo = productApi.getProductByBarcode(orderItemForm.getBarcode());
			orderItemPojo.setProductId(productPojo.getId());
			// reduce quantity in inventory
		    inventoryApi.updateInventoryWhileCreatingOrder(orderItemPojo.getProductId(), orderItemPojo.getBarcode(), orderItemPojo.getQuantity(), 0);
			productApi.checkSellingPrice(orderItemForm.getBarcode(), orderItemForm.getSellingPrice());
			orderItemPojoList.add(orderItemPojo);
		}    
		return orderItemPojoList;
	}

	public List<OrderItemData> convertToOrderItemData(List<OrderItemPojo> orderItemPojoList) throws ApiException{
		List<OrderItemData> orderItemDataList = new ArrayList<>();
		for(OrderItemPojo orderItemPojo: orderItemPojoList){
			OrderItemData orderItemData = OrderHelper.convert(orderItemPojo);
			ProductPojo productPojo = productApi.getCheck(orderItemPojo.getProductId());
			orderItemData.setBarcode(productPojo.getBarcode());
			orderItemData.setProductName(productPojo.getName());
			orderItemDataList.add(orderItemData);
		}
		return orderItemDataList;
	}

}
