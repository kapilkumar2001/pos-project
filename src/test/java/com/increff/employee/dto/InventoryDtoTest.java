package com.increff.employee.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.BrandCategoryForm;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    private BrandCategoryDto brandCategoryDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Test
    public void testAdd() throws ApiException{
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

        InventoryData inventoryData = inventoryDto.get("testb1");

        assertEquals(0, inventoryData.getQuantity()); 
    }
}
