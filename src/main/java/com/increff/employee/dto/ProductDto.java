package com.increff.employee.dto;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductForm form) throws ApiException {
        ProductPojo productPojo = convert(form);
        productService.add(productPojo);

        // Adds inventory
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(0);
        inventoryPojo.setBarcode(form.getBarcode());
        inventoryService.add(inventoryPojo);
    }

    public void delete(int id) {
        productService.delete(id);
    }

    public ProductData get(int id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        return convert(productPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productPojoList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<ProductData>();
        for (ProductPojo productPojo : productPojoList) {
            productDataList.add(convert(productPojo));
        }
        return productDataList;
    }

    public void update(int id, ProductForm productForm) throws ApiException {
        ProductPojo productPojo = convert(productForm);
        productService.update(id, productPojo);
    }

    private static ProductData convert(ProductPojo productPojo) {
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

    private static ProductPojo convert(ProductForm productForm) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrand(productForm.getBrand());
        productPojo.setCategory(productForm.getCategory());
        productPojo.setName(productForm.getName());
        return productPojo;
    }
}
