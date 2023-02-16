package com.increff.pos.dto;

import com.increff.pos.helper.InventoryHelper;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService service;
    @Autowired
    private ProductService productService;

    public void add(InventoryForm inventoryForm) throws ApiException {
        InventoryHelper.validate(inventoryForm);
        InventoryPojo inventoryPojo = InventoryHelper.convert(inventoryForm);
        int id = productService.getProductByBarcode(inventoryPojo.getBarcode()).getId();
        inventoryPojo.setId(id);
        InventoryHelper.normalize(inventoryForm.getBarcode());
        service.add(inventoryPojo);
    }

    public InventoryData get(String barcode) throws ApiException{
        int id = productService.getProductByBarcode(barcode).getId();
        InventoryPojo inventoryPojo = service.get(id, barcode);
        InventoryData inventoryData =  InventoryHelper.convert(inventoryPojo);
        ProductPojo productPojo =  productService.getCheck(inventoryPojo.getId());
        inventoryData.setProductName(productPojo.getName());
        return inventoryData;
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = service.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : inventoryPojoList) {
            ProductPojo productPojo =  productService.getCheck(inventoryPojo.getId());
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
        ProductPojo productPojo =  productService.getProductByBarcode(barcode);
        int quantity;
        try{
            quantity = Integer.parseInt(inventoryForm.getQuantity());
        } catch(NumberFormatException e){
            throw new ApiException("Quantity should be a integer value");
        }
        service.update(productPojo.getId(), barcode,  quantity);
    }
}
