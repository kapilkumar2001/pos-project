package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.StatusEnum;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.api.ApiException;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;

public class orderDtoTest extends AbstractUnitTest{
    
    @Autowired 
    private OrderDto orderDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Test
    public void testCreateOrder() throws ApiException{

        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);
        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity("54");
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

        List<OrderPojo> orderPojos = orderDao.selectAll();
        assertEquals(StatusEnum.created, orderPojos.get(0).getStatus());
        List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderPojos.get(0).getId());

        int i=1;
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            assertEquals("testb"+i,orderItemPojo.getBarcode());
            assertEquals(i, orderItemPojo.getSellingPrice(), 0);
            assertEquals(i, orderItemPojo.getQuantity(), 0);
            i++;
        }
    }

    @Test
    public void testGetOrder() throws ApiException{
        
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity("54");
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

        List<OrderPojo> orderPojos = orderDao.selectAll();
        OrderData orderData = orderDto.getOrderItems(orderPojos.get(0).getId());
        assertEquals("created", orderData.getStatus());
        List<OrderItemData> orderItemDataList = orderData.getOrders();

        int i=1;
        for(OrderItemData orderItemData: orderItemDataList){
            assertEquals("testb"+i,orderItemData.getBarcode());
            assertEquals(i, orderItemData.getSellingPrice(), 0);
            assertEquals(i, orderItemData.getQuantity(), 0);
            i++;
        }
    }


    @Test
    public void testUpdateOrder() throws ApiException{
        
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity("20");
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb3");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("65.23");
        productForm.setName("test product 3");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb3");
        inventoryForm.setQuantity("33");
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

        List<OrderPojo> orderPojos = orderDao.selectAll();
        OrderData orderData = orderDto.getOrderItems(orderPojos.get(0).getId());
        assertEquals(orderPojos.get(0).getId(), orderData.getId());
        assertEquals("created", orderData.getStatus());
        List<OrderItemData> orderItemDataList = orderData.getOrders();

        int i=1;
        for(OrderItemData orderItemData: orderItemDataList){
            assertEquals("testb"+i,orderItemData.getBarcode());
            assertEquals(i, orderItemData.getSellingPrice(), 0);
            assertEquals(i, orderItemData.getQuantity(), 0);
            i++;
        }

        orderItemFormList = new ArrayList<>();
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb2");
        orderItemForm.setQuantity(4);
        orderItemForm.setSellingPrice(8);
        orderItemForm.setOrderItemId(orderItemDataList.get(1).getOrderItemId());
        orderItemFormList.add(orderItemForm);

        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb3");
        orderItemForm.setQuantity(6);
        orderItemForm.setSellingPrice(12);
        orderItemForm.setOrderItemId(0);
        orderItemFormList.add(orderItemForm);

        orderPojos = orderDao.selectAll();
        orderDto.update(orderPojos.get(0).getId(), orderItemFormList);

        orderData = orderDto.getOrderItems(orderPojos.get(0).getId());
        assertEquals("created", orderData.getStatus());
        orderItemDataList = orderData.getOrders();

        i=2;
        for(OrderItemData orderItemData: orderItemDataList){
            assertEquals("testb"+i,orderItemData.getBarcode());
            assertEquals(i*4, orderItemData.getSellingPrice(), 0);
            assertEquals(i*2, orderItemData.getQuantity(), 0);
            i++;
        }

        assertEquals(54, inventoryDto.get("testb1").getQuantity());
        assertEquals(16, inventoryDto.get("testb2").getQuantity());
        assertEquals(27, inventoryDto.get("testb3").getQuantity());
    }
    
    @Test
    public void testCancelOrder() throws ApiException{
        
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity("20");
        inventoryDto.add(inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb3");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("65.23");
        productForm.setName("test product 3");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb3");
        inventoryForm.setQuantity("33");
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

        List<OrderPojo> orderPojos = orderDao.selectAll();
        OrderData orderData = orderDto.getOrderItems(orderPojos.get(0).getId());
        assertEquals(orderPojos.get(0).getId(), orderData.getId());
        assertEquals("created", orderData.getStatus());
        List<OrderItemData> orderItemDataList = orderData.getOrders();

        int i=1;
        for(OrderItemData orderItemData: orderItemDataList){
            assertEquals("testb"+i,orderItemData.getBarcode());
            assertEquals(i, orderItemData.getSellingPrice(), 0);
            assertEquals(i, orderItemData.getQuantity(), 0);
            i++;
        }

        orderDto.cancelOrder(orderPojos.get(0).getId());
        orderData = orderDto.getOrderItems(orderPojos.get(0).getId());
        assertEquals(orderPojos.get(0).getId(), orderData.getId());
        assertEquals("cancelled", orderData.getStatus());

        assertEquals(54, inventoryDto.get("testb1").getQuantity());
        assertEquals(20, inventoryDto.get("testb2").getQuantity());
        assertEquals(33, inventoryDto.get("testb3").getQuantity());
    }

    @Test
    public void testGetAllOrders() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);
        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("515.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("155.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity("20");
        inventoryDto.add(inventoryForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb3");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("265.23");
        productForm.setName("test product 3");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb3");
        inventoryForm.setQuantity("33");
        inventoryDto.add(inventoryForm);

        brandForm = new BrandForm();
        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
        brandDto.add(brandForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb11");
        productForm.setBrand("test brand 2");
        productForm.setCategory("test category 2");
        productForm.setMrp("155.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb11");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb22");
        productForm.setBrand("test brand 2");
        productForm.setCategory("test category 2");
        productForm.setMrp("515.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb22");
        inventoryForm.setQuantity("20");
        inventoryDto.add(inventoryForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb33");
        productForm.setBrand("test brand 2");
        productForm.setCategory("test category 2");
        productForm.setMrp("651.23");
        productForm.setName("test product 3");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb33");
        inventoryForm.setQuantity("33");
        inventoryDto.add(inventoryForm);

        brandForm = new BrandForm();
        brandForm.setBrand("test brand 3");
        brandForm.setCategory("test category 3");
        brandDto.add(brandForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb111");
        productForm.setBrand("test brand 3");
        productForm.setCategory("test category 3");
        productForm.setMrp("155.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb111");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb222");
        productForm.setBrand("test brand 3");
        productForm.setCategory("test category 3");
        productForm.setMrp("515.55");
        productForm.setName("test product 2");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb222");
        inventoryForm.setQuantity("20");
        inventoryDto.add(inventoryForm);
        productForm = new ProductForm();
        productForm.setBarcode("testb333");
        productForm.setBrand("test brand 3");
        productForm.setCategory("test category 3");
        productForm.setMrp("651.23");
        productForm.setName("test product 3");
        productDto.add(productForm);
        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb333");
        inventoryForm.setQuantity("33");
        inventoryDto.add(inventoryForm);

        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb1");
        orderItemForm.setQuantity(4);
        orderItemForm.setSellingPrice(89.99);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb22");
        orderItemForm.setQuantity(5);
        orderItemForm.setSellingPrice(240.33);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb33");
        orderItemForm.setQuantity(7);
        orderItemForm.setSellingPrice(250.33);
        orderItemFormList.add(orderItemForm);
        orderDto.createOrder(orderItemFormList);
        
        orderItemFormList = new ArrayList<>();
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb11");
        orderItemForm.setQuantity(4);
        orderItemForm.setSellingPrice(99.99);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb222");
        orderItemForm.setQuantity(3);
        orderItemForm.setSellingPrice(20.33);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb33");
        orderItemForm.setQuantity(7);
        orderItemForm.setSellingPrice(250.33);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb3");
        orderItemForm.setQuantity(7);
        orderItemForm.setSellingPrice(250.33);
        orderItemFormList.add(orderItemForm);
        orderDto.createOrder(orderItemFormList);

        orderItemFormList = new ArrayList<>();
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb111");
        orderItemForm.setQuantity(1);
        orderItemForm.setSellingPrice(99.99);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb222");
        orderItemForm.setQuantity(2);
        orderItemForm.setSellingPrice(20.33);
        orderItemFormList.add(orderItemForm);
        orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode("testb333");
        orderItemForm.setQuantity(7);
        orderItemForm.setSellingPrice(250.33);
        orderItemFormList.add(orderItemForm);
        orderDto.createOrder(orderItemFormList);

        assertEquals(3, orderDto.getAllOrders().size());
    }
}
