package com.increff.pos.dto;

import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductService service;
    @Autowired
    private BrandService brandService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm productForm) throws ApiException {
        ProductHelper.validate(productForm);
        ProductPojo productPojo = ProductHelper.convert(productForm);
        int brandId = brandService.getBrandByBrandAndCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
        productPojo.setBrandId(brandId);
        ProductHelper.normalize(productPojo);

        service.add(productPojo);
        addInventory(productPojo);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = service.getCheck(id);
        BrandPojo brandPojo = brandService.getById(productPojo.getBrandId());
        productPojo.setBrand(brandPojo.getBrand());
        productPojo.setCategory(brandPojo.getCategory());
        return ProductHelper.convert(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = service.getAll();
        List<ProductData> productDataList = new ArrayList<ProductData>();
        for (ProductPojo productPojo : productPojoList) {
            BrandPojo b = brandService.getById(productPojo.getBrandId());
            productPojo.setBrand(b.getBrand());
            productPojo.setCategory(b.getCategory());
            productDataList.add(ProductHelper.convert(productPojo));
        }
        return productDataList;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        ProductHelper.validate(productForm);
        ProductPojo productPojo = ProductHelper.convert(productForm);
        int brandId = brandService.getBrandByBrandAndCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
        productPojo.setBrandId(brandId);
        ProductHelper.normalize(productPojo);

        service.update(id, productPojo);
    }

    public void addInventory(ProductPojo productPojo) throws ApiException{
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryPojo.setBarcode(productPojo.getBarcode());
        inventoryService.add(inventoryPojo);
    }
}
