package com.increff.pos.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;


@Component
public class OrderDto {

    @Autowired
    private OrderService orderService; 
	@Autowired
	private OrderItemService orderItemService;
    @Autowired
	private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void createOrder(List<OrderItemForm> form) throws ApiException{
        OrderPojo orderPojo = new OrderPojo();
		
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        
        for(OrderItemForm orderItemForm: form)
		{
			OrderItemPojo orderItemPojo = new OrderItemPojo();
			orderItemPojo.setBarcode(orderItemForm.getBarcode());
			orderItemPojo.setOrderId(orderPojo.getId());

            // setting product id
            ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
            orderItemPojo.setProductId(productPojo.getId());
			
			orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
			orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
				
            // reduce quantity in inventory
            inventoryService.updateInventoryWhileCreatingOrder(orderItemPojo.getProductId(), orderItemPojo.getBarcode(), orderItemPojo.getQuantity(), 0);

			productService.checkSellingPrice(orderItemForm.getBarcode(), orderItemForm.getSellingPrice());

            orderItemPojoList.add(orderItemPojo);
        }

        orderService.createOrder(orderPojo);
		orderItemService.addItemstoOrder(orderPojo.getId(), orderItemPojoList);
    }

    public OrderPojo getOrder(int id) throws ApiException{
		OrderPojo orderPojo = orderService.getOrder(id);
		return orderPojo;
	}

    public OrderData getOrderItems(int orderId) throws ApiException{
		List<OrderItemPojo> orderItemPojo = orderItemService.getOrderItemsbyOrderId(orderId);
		OrderPojo orderPojo = getOrder(orderId);
		return convert(orderPojo, orderItemPojo);
	}

    public List<OrderData> getAllOrders() throws ApiException{
        List<OrderPojo> orderPojoList = orderService.getAllOrders();
		List<OrderData> orderDataList = new ArrayList<OrderData>();
		for(OrderPojo orderPojo: orderPojoList) {
			orderDataList.add(getOrderItems(orderPojo.getId()));
		}
		return orderDataList;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int orderId, List<OrderItemForm> orderItemFormList) throws ApiException{

		OrderPojo orderPojo = orderService.getOrder(orderId);
		orderService.update(orderPojo);
	
		List <OrderItemPojo> orderItemPojoList = new ArrayList<>();

		List<OrderItemPojo> existingOrderItemPojoList = orderItemService.getOrderItemsbyOrderId(orderId);
		List<Integer> existingOrderItemIds = new ArrayList<>();
		for(OrderItemPojo orderItemPojo: existingOrderItemPojoList) {
			existingOrderItemIds.add(orderItemPojo.getId());
		}
		List<Integer> newOrderItemIds = new ArrayList<>();

		for(OrderItemForm orderItemForm: orderItemFormList)
		{
			newOrderItemIds.add(orderItemForm.getOrderItemId());
			
			OrderItemPojo orderItemPojo = new OrderItemPojo();
			orderItemPojo.setBarcode(orderItemForm.getBarcode());
			orderItemPojo.setOrderId(orderId);
			orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
			orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
			orderItemPojo.setId(orderItemForm.getOrderItemId());

			// setting product id
			ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
			orderItemPojo.setProductId(productPojo.getId());

			// reduce quantity in inventory
			if(orderItemForm.getOrderItemId()!=0){
				OrderItemPojo orderItemPojoTemp = orderItemService.getOrderItembyItemId(orderItemForm.getOrderItemId());
				int prevQuantity =  orderItemPojoTemp.getQuantity();
				inventoryService.updateInventoryWhileCreatingOrder(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  orderItemPojo.getQuantity(), prevQuantity);
			}
			else{ 
				inventoryService.updateInventoryWhileCreatingOrder(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  orderItemPojo.getQuantity(), 0);
			}

			productService.checkSellingPrice(orderItemForm.getBarcode(), orderItemForm.getSellingPrice());
			orderItemPojoList.add(orderItemPojo);
		}

		// increase quantity in inventory for deleted items
		existingOrderItemIds.removeAll(newOrderItemIds);

		for(Integer orderItemId: existingOrderItemIds) {
			OrderItemPojo orderItemPojo = orderItemService.getOrderItembyItemId(orderItemId);
			String barcode = productService.get(orderItemPojo.getProductId()).getBarcode();
			inventoryService.increaseInventory(orderItemPojo.getProductId(), barcode, orderItemPojo.getQuantity());
		}	

		List<Integer> orderItemIdstoRemove = existingOrderItemIds;
		orderItemService.update(orderId, orderItemPojoList, orderItemIdstoRemove);
    }

	@Transactional(rollbackOn = ApiException.class)
	public void cancelOrder(int orderId) throws ApiException{
		OrderPojo orderPojo = orderService.getOrder(orderId);
		orderPojo.setStatus("cancelled");
		orderService.update(orderPojo);

		List<OrderItemPojo> existingOrderItemPojoList = orderItemService.getOrderItemsbyOrderId(orderId);
		
		for(OrderItemPojo orderItemPojo: existingOrderItemPojoList) {
			String barcode = productService.get(orderItemPojo.getProductId()).getBarcode();
			inventoryService.increaseInventory(orderItemPojo.getProductId(), barcode, orderItemPojo.getQuantity());
		}
	}

    public OrderData convert(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) throws ApiException {
		OrderData orderData = new OrderData();

		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String dateTime = orderPojo.getCreatedAt().format(format);
        orderData.setCreatedAt(dateTime);
		dateTime = orderPojo.getUpdatedAt().format(format);
		orderData.setUpdatedAt(dateTime);

		orderData.setId(orderPojo.getId());
		orderData.setStatus(orderPojo.getStatus());
		
		List<OrderItemData> orderItemDataList = new ArrayList<>();
		
		for(OrderItemPojo orderItemPojo : orderItemPojoList) {
			OrderItemData tmp = new OrderItemData();
			ProductPojo productPojo = productService.get(orderItemPojo.getProductId()); 
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
