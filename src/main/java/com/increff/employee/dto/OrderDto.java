package com.increff.employee.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.StringUtil;


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

    @Transactional
    public void createOrder(List<OrderItemForm> form) throws ApiException{
        OrderPojo orderPojo = new OrderPojo();
		
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        
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

            // setting product id
            ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
            orderItemPojo.setProductId(productPojo.getId());
				
            // reduce quantity in inventory
            InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId(), orderItemPojo.getBarcode());
            if(orderItemPojo.getQuantity()>inventoryPojo.getQuantity()) {
                throw new ApiException("not enough quantity available, barcode: " + orderItemPojo.getBarcode());
            }
            inventoryService.update(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  inventoryPojo.getQuantity() - orderItemPojo.getQuantity());

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

    @Transactional
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

                InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId(), orderItemPojo.getBarcode());
				if(orderItemPojo.getQuantity()>(inventoryPojo.getQuantity() + prevQuantity)) {
					throw new ApiException("not enough quantity available, barcode: " + orderItemPojo.getBarcode());
				}
				inventoryService.update(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  (inventoryPojo.getQuantity() + prevQuantity) - orderItemPojo.getQuantity());
            }
            else{ 
                InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId(), orderItemPojo.getBarcode());
                if(orderItemPojo.getQuantity()>inventoryPojo.getQuantity()) {
                    throw new ApiException("not enough quantity available, barcode: " + orderItemPojo.getBarcode());
                }
                inventoryService.update(orderItemPojo.getProductId(), orderItemPojo.getBarcode(),  inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
            }
            
            orderItemPojoList.add(orderItemPojo);
        }

		// increase quantity in inventory for deleted items
		existingOrderItemIds.removeAll(newOrderItemIds);

		for(Integer orderItemId: existingOrderItemIds) {
			OrderItemPojo orderItemPojo = orderItemService.getOrderItembyItemId(orderItemId);
			String barcode = productService.get(orderItemPojo.getProductId()).getBarcode();
			InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId(), barcode);
			inventoryService.update(orderItemPojo.getProductId(), barcode,  inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
		}	

		List<Integer> orderItemIdstoRemove = existingOrderItemIds;
        orderItemService.update(orderId, orderItemPojoList, orderItemIdstoRemove);
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
