package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

@Component
public class InventoryReportDto {
    
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public List<InventoryReportData> get() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();

        List<BrandCategoryPojo> brandCategoryPojoList = brandCategoryService.getAll(); 

        for(BrandCategoryPojo brandCategoryPojo: brandCategoryPojoList){

            InventoryReportData inventoryReportData = new InventoryReportData();
            inventoryReportData.setBrand(brandCategoryPojo.getBrand());
            inventoryReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();

            int quantity=0;
            List<ProductPojo> productPojoList = productService.getProductsByBrandCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList) {
                InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId(), productPojo.getBarcode());
                quantity = quantity + inventoryPojo.getQuantity();
            }

            inventoryReportData.setQuantity(quantity);
            inventoryReportDataList.add(inventoryReportData);
        }

        return inventoryReportDataList;
    }
}
