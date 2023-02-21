package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.api.ApiException;
import com.increff.pos.dao.ProductDao;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    private BrandDto brandDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private InventoryDto dto;
    @Autowired
    private ProductDao productDao;

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

        InventoryData inventoryData = dto.get("testb1");
        assertEquals(0, inventoryData.getQuantity()); 

        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("54");
        dto.add(inventoryForm);
        inventoryData = dto.get("testb1");
        assertEquals(54, inventoryData.getQuantity()); 

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("46");
        dto.add(inventoryForm);
        inventoryData = dto.get("testb1");
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

        InventoryData inventoryData = dto.get("testb1");
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

        List<ProductPojo> productPojos = productDao.selectAll();
        List<InventoryData> inventoryDataList = dto.getAll();

        int i=1;
        for(InventoryData inventoryData: inventoryDataList){
            assertEquals("testb"+i, inventoryData.getBarcode());
            assertEquals(productPojos.get(i-1).getId(), inventoryData.getId());
            assertEquals("test product "+i, inventoryData.getProductName());
            assertEquals(0, inventoryData.getQuantity());
            i++;
        }
    }

    @Test
    public void testUpdate() throws ApiException{
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

        dto.update("testb1", inventoryForm);

        InventoryData inventoryData = dto.get("testb1");
        assertEquals(54, inventoryData.getQuantity());

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb1");
        inventoryForm.setQuantity("46");

        dto.update("testb1", inventoryForm);

        inventoryData = dto.get("testb1");
        assertEquals(46, inventoryData.getQuantity());
    }

    @Test
    public void testGetReport() throws ApiException{
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
        inventoryForm.setQuantity("10");
        dto.update("testb1", inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("65.55");
        productForm.setName("test product 2");
        productDto.add(productForm);

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb2");
        inventoryForm.setQuantity("20");
        dto.update("testb2", inventoryForm);


        brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 2");
        brandDto.add(brandForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb3");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 2");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb3");
        inventoryForm.setQuantity("40");
        dto.update("testb3", inventoryForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb4");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 2");
        productForm.setMrp("65.55");
        productForm.setName("test product 2");
        productDto.add(productForm);

        inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("testb4");
        inventoryForm.setQuantity("30");
        dto.update("testb4", inventoryForm);

        List<InventoryReportData> inventoryReportDatas = dto.getReportData();
        
        assertEquals("test brand 1", inventoryReportDatas.get(0).getBrand()); 
        assertEquals("test category 1", inventoryReportDatas.get(0).getCategory());
        assertEquals(30, inventoryReportDatas.get(0).getQuantity());
        assertEquals("test brand 1", inventoryReportDatas.get(1).getBrand());
        assertEquals("test category 2", inventoryReportDatas.get(1).getCategory());
        assertEquals(70, inventoryReportDatas.get(1).getQuantity());
    }
}
