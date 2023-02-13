package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;

public class orderDtoTest extends AbstractUnitTest{
    
    @Autowired 
    private OrderDto orderDto;

    @Autowired
    private BrandCategoryDto brandCategoryDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Test
    public void testCreateOrder() throws ApiException{

        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 1");
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity(54);
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 2");
        productDto.add(productForm);

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity(54);
        inventoryDto.add(inventoryForm);


        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb1");
        orderItemForm.setQuantity(1);
        orderItemForm.setSellingPrice(1);
        orderItemFormList.add(orderItemForm);

        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb2");
        orderItemForm.setQuantity(2);
        orderItemForm.setSellingPrice(2);
        orderItemFormList.add(orderItemForm);

        orderDto.createOrder(orderItemFormList);
    }

    @Test
    public void testGetOrder() throws ApiException{
        
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 1");
        productDto.add(productForm);

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity(54);
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 2");
        productDto.add(productForm);

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity(54);
        inventoryDto.add(inventoryForm);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb1");
        orderItemForm.setQuantity(1);
        orderItemForm.setSellingPrice(1);
        orderItemFormList.add(orderItemForm);

        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb2");
        orderItemForm.setQuantity(2);
        orderItemForm.setSellingPrice(2);
        orderItemFormList.add(orderItemForm);


        orderDto.createOrder(orderItemFormList);

        OrderData orderData = orderDto.getOrderItems(1);
        
        assertEquals(1, orderData.getId());
        assertEquals("created", orderData.getStatus());
        List<OrderItemData> orderItemDataList = orderData.getOrders();

        int i=1;
        for(OrderItemData orderItemData: orderItemDataList){
            assertEquals("testb"+i,orderItemData.getBarcode());
            assertEquals(i, orderItemData.getSellingPrice(), 0);
            assertEquals(i, orderItemData.getQuantity(), 0);
            assertEquals(i, orderItemData.getOrderItemId());
            i++;
        }
    }

    
}
