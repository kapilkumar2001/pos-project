package com.increff.pos.helper;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.increff.pos.api.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.StatusEnum;
import com.increff.pos.util.StringUtil;

public class OrderHelper {

    public static void validateOrderItems(List<OrderItemForm> orderItemFormList) throws ApiException{
        for(OrderItemForm orderItemForm: orderItemFormList)
		{
			if(StringUtil.isEmpty(orderItemForm.getBarcode())) {
				throw new ApiException("Barcode can not be empty");
			}
			if(orderItemForm.getQuantity()<=0) {
				throw new ApiException("Quantity should be greater than 0");
			}
			if(orderItemForm.getSellingPrice()<0) {
				throw new ApiException("Selling Price should be a positive number");
			}
		}
    }
    
    public static OrderData convert(OrderPojo orderPojo) throws ApiException {
		OrderData orderData = new OrderData();

		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
        String dateTime = orderPojo.getCreatedAt().format(format);
        orderData.setCreatedAt(dateTime);
		dateTime = orderPojo.getUpdatedAt().format(format);
		orderData.setUpdatedAt(dateTime);
		orderData.setId(orderPojo.getId());
		orderData.setStatus(orderPojo.getStatus().toString());
		return orderData;
	}

	public static OrderItemData convert(OrderItemPojo orderItemPojo) throws ApiException{
		OrderItemData orderItemData = new OrderItemData(); 
		orderItemData.setQuantity(orderItemPojo.getQuantity());
		orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
		orderItemData.setId(orderItemPojo.getOrderId());
		orderItemData.setProductId(orderItemPojo.getProductId());	
		orderItemData.setOrderItemId(orderItemPojo.getId());
		return orderItemData;
	}

    public static OrderItemPojo convert(OrderItemForm orderItemForm) throws ApiException {
		OrderItemPojo orderItemPojo = new OrderItemPojo();
		orderItemPojo.setBarcode(orderItemForm.getBarcode());
		orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
		orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
        return orderItemPojo;
    }

    public static void normalize(OrderPojo orderPojo) {
		orderPojo.setCreatedAt(LocalDateTime.now());
		orderPojo.setUpdatedAt(LocalDateTime.now());
		orderPojo.setStatus(StatusEnum.created);
	}

    public static void normalize(List<OrderItemPojo> orderItemPojoList) {
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            DecimalFormat dec = new DecimalFormat("#.##");
            orderItemPojo.setSellingPrice(Double.valueOf(dec.format(orderItemPojo.getSellingPrice())));
            orderItemPojo.setBarcode(StringUtil.toLowerCase(orderItemPojo.getBarcode()));
        }
	}
}
