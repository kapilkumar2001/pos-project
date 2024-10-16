package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.api.ApiException;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;

public class productDtoTest extends AbstractUnitTest{

    @Autowired
    private BrandDto brandDto;
    
    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private BrandDao brandDao;


    @Test 
    public void testAdd() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        List<BrandPojo> brandPojos = brandDao.selectAll();

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);

        List<ProductPojo> productPojos = productDao.selectAll();

        assertEquals("testb1", productPojos.get(0).getBarcode());
        assertEquals("test brand 1", productPojos.get(0).getBrand());
        assertEquals("test category 1", productPojos.get(0).getCategory());
        assertEquals("test product 1", productPojos.get(0).getName());
        assertEquals(brandPojos.get(0).getId(), productPojos.get(0).getBrandId());
        assertEquals(55.55, productPojos.get(0).getMrp(), 0);
        InventoryData inventoryData = inventoryDto.get("testb1");
        assertEquals(0, inventoryData.getQuantity()); 
    }

    @Test
    public void testGet() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        List<BrandPojo> brandPojos = brandDao.selectAll();

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");

        productDto.add(productForm);

        List<ProductPojo> productPojos = productDao.selectAll();
        ProductData productData = productDto.get(productPojos.get(0).getId());

        assertEquals("testb1", productData.getBarcode());
        assertEquals("test brand 1", productData.getBrand());
        assertEquals("test category 1", productData.getCategory());
        assertEquals("test product 1", productData.getName());
        assertEquals(brandPojos.get(0).getId(), productData.getBrandId());
        assertEquals(55.55, productData.getMrp(), 0);
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        brandForm = new BrandForm();
        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
        brandDto.add(brandForm);

        List<BrandPojo> brandPojos = brandDao.selectAll();

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
        productForm.setMrp("55.55");
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
            assertEquals(brandPojos.get(0).getId(), productData.getBrandId());
            assertEquals(55.55, productData.getMrp(), 0);
            i++;
        }
    }

    @Test
    public void testUpdate() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        List<BrandPojo> brandPojos = brandDao.selectAll();

        ProductForm productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("55.55");
        productForm.setName("test product 1");
        productDto.add(productForm);
    
        List<ProductPojo> productPojos = productDao.selectAll();
        ProductData productData = productDto.get(productPojos.get(0).getId());

        assertEquals("testb1", productData.getBarcode());
        assertEquals("test brand 1", productData.getBrand());
        assertEquals("test category 1", productData.getCategory());
        assertEquals("test product 1", productData.getName());
        assertEquals(brandPojos.get(0).getId(), productData.getBrandId());
        assertEquals(55.55, productData.getMrp(), 0);
      
        productForm = new ProductForm();
        productForm.setBarcode("testb1");
        productForm.setBrand("test brand 1");
        productForm.setCategory("test category 1");
        productForm.setMrp("65.56");
        productForm.setName("updated test product 1");
        productDto.update(productPojos.get(0).getId(), productForm);

        productData = productDto.get(productPojos.get(0).getId());

        assertEquals("testb1", productData.getBarcode());
        assertEquals("test brand 1", productData.getBrand());
        assertEquals("test category 1", productData.getCategory());
        assertEquals("updated test product 1", productData.getName());
        assertEquals(brandPojos.get(0).getId(), productData.getBrandId());
        assertEquals(65.56, productData.getMrp(), 0);
    }

    @Test
    public void testGetByBarcode() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        brandDto.add(brandForm);

        brandForm = new BrandForm();
        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
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
        productForm.setMrp("55.55");
        productForm.setName("test product 2");
        productDto.add(productForm);

        ProductData productData = productDto.getByBarcode("testb1");
        assertEquals("test product 1", productData.getName());
    }
}
