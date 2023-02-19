package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.ProductHelper;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductApi api;
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private InventoryApi inventoryApi;

    public void add(ProductForm productForm) throws ApiException {
        ProductHelper.validate(productForm);
        ProductPojo productPojo = ProductHelper.convert(productForm);
        int brandId = brandApi.getBrandByBrandAndCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
        productPojo.setBrandId(brandId);
        ProductHelper.normalize(productPojo);

        api.add(productPojo);
        addInventory(productPojo);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = api.getCheck(id);
        BrandPojo brandPojo = brandApi.getById(productPojo.getBrandId());
        productPojo.setBrand(brandPojo.getBrand());
        productPojo.setCategory(brandPojo.getCategory());
        return ProductHelper.convert(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = api.getAll();
        List<ProductData> productDataList = new ArrayList<ProductData>();
        for (ProductPojo productPojo : productPojoList) {
            BrandPojo b = brandApi.getById(productPojo.getBrandId());
            productPojo.setBrand(b.getBrand());
            productPojo.setCategory(b.getCategory());
            productDataList.add(ProductHelper.convert(productPojo));
        }
        return productDataList;
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        if(StringUtil.isEmpty(barcode)){
			throw new ApiException("Barcode can not be empty");
		}
        barcode = StringUtil.toLowerCase(barcode);
        ProductPojo productPojo = api.getProductByBarcode(barcode);
        BrandPojo brandPojo = brandApi.getById(productPojo.getBrandId());
        productPojo.setBrand(brandPojo.getBrand());
        productPojo.setCategory(brandPojo.getCategory());
        return ProductHelper.convert(productPojo);
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        ProductHelper.validate(productForm);
        ProductPojo productPojo = ProductHelper.convert(productForm);
        int brandId = brandApi.getBrandByBrandAndCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
        productPojo.setBrandId(brandId);
        ProductHelper.normalize(productPojo);

        api.update(id, productPojo);
    }

    public void addInventory(ProductPojo productPojo) throws ApiException{
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryPojo.setBarcode(productPojo.getBarcode());
        inventoryApi.add(inventoryPojo);
    }
}
