package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;

@Component
public class InventoryReportDto {
    
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi api;

    @Transactional
    public List<InventoryReportData> get() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();

        List<BrandPojo> brandPojoList = brandApi.getAll(); 

        for(BrandPojo brandPojo: brandPojoList){

            InventoryReportData inventoryReportData = new InventoryReportData();
            inventoryReportData.setBrand(brandPojo.getBrand());
            inventoryReportData.setCategory(brandPojo.getCategory());

            int brandId = brandPojo.getId();

            int quantity=0;
            List<ProductPojo> productPojoList = productApi.getProductsByBrandId(brandId);

            for(ProductPojo productPojo: productPojoList) {
                InventoryPojo inventoryPojo = api.get(productPojo.getId(), productPojo.getBarcode());
                quantity = quantity + inventoryPojo.getQuantity();
            }

            inventoryReportData.setQuantity(quantity);
            inventoryReportDataList.add(inventoryReportData);
        }

        return inventoryReportDataList;
    }
}
