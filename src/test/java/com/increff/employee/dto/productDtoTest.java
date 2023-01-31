package com.increff.employee.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.BrandCategoryForm;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;

public class productDtoTest extends AbstractUnitTest{

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

    @Test
    public void testGet() throws ApiException{
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

        ProductData productData = productDto.get(1);

        assertEquals("testb1", productData.getBarcode());
        assertEquals("test brand 1", productData.getBrand());
        assertEquals("test category 1", productData.getCategory());
        assertEquals("test product 1", productData.getName());
        assertEquals(1, productData.getBrandCategory());
        assertEquals(55.55, productData.getMrp(), 0);
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 2");
        brandCategoryDto.add(brandCategoryForm);


        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 1");
        productDto.add(productForm);

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 2");
        productDto.add(productForm);

        
        List<ProductData> productDataList = productDto.getAll();

        assertEquals(2, productDataList.size());

        int i = 1;
        for(ProductData productData: productDataList){
            assertEquals("testb"+i, productData.getBarcode());
            assertEquals("test brand 1", productData.getBrand());
            assertEquals("test category 1", productData.getCategory());
            assertEquals("test product "+i, productData.getName());
            assertEquals(1, productData.getBrandCategory());
            assertEquals(55.55, productData.getMrp(), 0);
            i++;
        }
    }

    @Test
    public void testDelete() throws ApiException{
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

        productForm = new ProductForm();
        productForm.setBarcode("testb2");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(55.55);
        productForm.setName("test product 2");
        productDto.add(productForm);
        
        List<ProductData> productDataList = productDto.getAll();

        assertEquals(2, productDataList.size());

        int i = 1;
        for(ProductData productData: productDataList){
            assertEquals("testb"+i, productData.getBarcode());
            assertEquals("test brand 1", productData.getBrand());
            assertEquals("test category 1", productData.getCategory());
            assertEquals("test product "+i, productData.getName());
            assertEquals(1, productData.getBrandCategory());
            assertEquals(55.55, productData.getMrp(), 0);
            i++;
        }

        productDto.delete(1);

        productDataList = productDto.getAll();

        assertEquals(1, productDataList.size());

        i = 2;
        for(ProductData productData: productDataList){
            assertEquals("testb"+i, productData.getBarcode());
            assertEquals("test brand 1", productData.getBrand());
            assertEquals("test category 1", productData.getCategory());
            assertEquals("test product "+i, productData.getName());
            assertEquals(1, productData.getBrandCategory());
            assertEquals(55.55, productData.getMrp(), 0);
            i++;
        }
    }

    @Test
    public void testUpdate() throws ApiException{
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
    
        ProductData productData = productDto.get(1);

        assertEquals("testb1", productData.getBarcode());
        assertEquals("test brand 1", productData.getBrand());
        assertEquals("test category 1", productData.getCategory());
        assertEquals("test product 1", productData.getName());
        assertEquals(1, productData.getBrandCategory());
        assertEquals(55.55, productData.getMrp(), 0);
      
        productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp(65.56);
        productForm.setName("updated test product 1");
        productDto.update(1, productForm);

        productData = productDto.get(1);

        assertEquals("testb1", productData.getBarcode());
        assertEquals("test brand 1", productData.getBrand());
        assertEquals("test category 1", productData.getCategory());
        assertEquals("updated test product 1", productData.getName());
        assertEquals(1, productData.getBrandCategory());
        assertEquals(65.56, productData.getMrp(), 0);
    }
}
