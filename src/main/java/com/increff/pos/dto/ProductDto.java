package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm form) throws ApiException {
        ProductPojo productPojo = convert(form);
        productService.add(productPojo);

        // Adds inventory
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryPojo.setBarcode(form.getBarcode());
        inventoryService.add(inventoryPojo);
    }

    public void delete(int id) {
        productService.delete(id);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(productPojo.getBrand_category());
        productPojo.setBrand(brandCategoryPojo.getBrand());
        productPojo.setCategory(brandCategoryPojo.getCategory());
        return convert(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<ProductData>();
        for (ProductPojo productPojo : productPojoList) {
            BrandCategoryPojo b = brandCategoryService.get(productPojo.getBrand_category());
            productPojo.setBrand(b.getBrand());
            productPojo.setCategory(b.getCategory());

            productDataList.add(convert(productPojo));
        }
        return productDataList;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        ProductPojo productPojo = convert(productForm);

        int brandCategoryId = brandCategoryService.getBrandCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
        productPojo.setBrand_category(brandCategoryId);

        productService.update(id, productPojo);
    }

    private ProductData convert(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setName(productPojo.getName());
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(productPojo.getBrand());
        productData.setCategory(productPojo.getCategory());
        productData.setBrandCategory(productPojo.getBrand_category());
        return productData;
    }

    private ProductPojo convert(ProductForm productForm) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        double mrp;
        try{
            mrp = Double.parseDouble(productForm.getMrp());
        } catch(NumberFormatException e){
            throw new ApiException("Mrp should be a positive number");
        }
        productPojo.setMrp(mrp);
        productPojo.setBrand(productForm.getBrand());
        productPojo.setCategory(productForm.getCategory());
        productPojo.setName(productForm.getName());

        int id = brandCategoryService.getBrandCategory(productPojo.getBrand(), productPojo.getCategory()).getId();
        productPojo.setBrand_category(id);
        return productPojo;
    }
}
