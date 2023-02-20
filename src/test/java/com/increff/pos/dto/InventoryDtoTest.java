package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.api.ApiException;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Test
    public void testAdd() throws ApiException{
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

        InventoryData inventoryData = inventoryDto.get("testb1");
        assertEquals(0, inventoryData.getQuantity()); 

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        inventoryDto.add(inventoryForm);
        inventoryData = inventoryDto.get("testb1");
        assertEquals(54, inventoryData.getQuantity()); 

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("46");
        inventoryDto.add(inventoryForm);
        inventoryData = inventoryDto.get("testb1");
        assertEquals(100, inventoryData.getQuantity()); 
    }

    @Test
    public void testGet() throws ApiException{
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

        InventoryData inventoryData = inventoryDto.get("testb1");
        assertEquals(0, inventoryData.getQuantity());
    }

    @Test
    public void testGetAll() throws ApiException{
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

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("65.55");
        productForm.setName("test product 2");
        productDto.add(productForm);

        List<InventoryData> inventoryDataList = inventoryDto.getAll();

        int i=1;
        for(InventoryData inventoryData: inventoryDataList){
            assertEquals("testb"+i, inventoryData.getBarcode());
            assertEquals(i, inventoryData.getId());
            assertEquals("test product "+i, inventoryData.getProductName());
            assertEquals(0, inventoryData.getQuantity());
            i++;
        }
    }

    @Test
    public void update() throws ApiException{
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

        inventoryDto.update("testb1", inventoryForm);

        InventoryData inventoryData = inventoryDto.get("testb1");
        assertEquals(54, inventoryData.getQuantity());

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("46");

        inventoryDto.update("testb1", inventoryForm);

        inventoryData = inventoryDto.get("testb1");
        assertEquals(46, inventoryData.getQuantity());
    }
}
