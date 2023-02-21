package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Component
@Transactional
public class InventoryDto {

    @Autowired
    private InventoryApi api;
    @Autowired
    private BrandApi brandApi;
    @Autowired
    private ProductApi productApi;
    

    public void add(InventoryForm inventoryForm) throws ApiException {
        InventoryHelper.validate(inventoryForm);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        int id = productApi.getProductByBarcode(inventoryPojo.getBarcode()).getId();
        inventoryPojo.setId(id);
        InventoryHelper.normalize(inventoryForm.getBarcode());
        api.add(inventoryPojo);
    }

    public InventoryData get(String barcode) throws ApiException{
        int id = productApi.getProductByBarcode(barcode).getId();
        InventoryPojo inventoryPojo = api.get(id, barcode);
        InventoryData inventoryData =  InventoryHelper.convert(inventoryPojo);
        ProductPojo productPojo =  productApi.getCheck(inventoryPojo.getId());
        inventoryData.setProductName(productPojo.getName());
        return inventoryData;
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = api.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            ProductPojo productPojo =  productApi.getCheck(inventoryPojo.getId());
            inventoryPojo.setBarcode(productPojo.getBarcode());
            InventoryData inventoryData = InventoryHelper.convert(inventoryPojo);
            inventoryData.setProductName(productPojo.getName());
            inventoryDataList.add(inventoryData);
        }
        return inventoryDataList;
    }

    public void update(String barcode, InventoryForm inventoryForm) throws ApiException {
        InventoryHelper.validate(inventoryForm);
        InventoryHelper.normalize(barcode);
        ProductPojo productPojo =  productApi.getProductByBarcode(barcode);
        int quantity;
        try{
            quantity = Integer.parseInt(inventoryForm.getQuantity());
        } catch(NumberFormatException e){
            throw new ApiException("Quantity should be a integer value");
        }
        api.update(productPojo.getId(), barcode,  quantity);
    }

    @Transactional
    public List<InventoryReportData> getReportData() throws ApiException {
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
