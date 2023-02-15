package com.increff.pos.dto;

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
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    public void add(InventoryForm form) throws ApiException {
        InventoryPojo inventoryPojo = convert(form);
        inventoryService.add(inventoryPojo);
    }

    public InventoryData get(String barcode) throws ApiException{
        int id = productService.getProductByBarcode(barcode).getId();
        InventoryPojo inventoryPojo = inventoryService.get(id, barcode);
        return convert(inventoryPojo);
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<InventoryData>();
        for (InventoryPojo p : inventoryPojoList) {
            p.setBarcode(productService.get(p.getId()).getBarcode());
            inventoryDataList.add(convert(p));
        }
        return inventoryDataList;
    }

    public void update(String barcode, InventoryForm inventoryForm) throws ApiException {
        ProductPojo productPojo =  productService.getProductByBarcode(barcode);
        int quantity;
        try{
            quantity = Integer.parseInt(inventoryForm.getQuantity());
        } catch(NumberFormatException e){
            throw new ApiException("Quantity should be a integer value");
        }
        inventoryService.update(productPojo.getId(), barcode,  quantity);
    }

    private InventoryData convert(InventoryPojo p) throws ApiException{
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        d.setBarcode(p.getBarcode());
        d.setId(p.getId());
        ProductPojo productPojo =  productService.get(p.getId());
        d.setProductName(productPojo.getName());
        return d;
    }

    private InventoryPojo convert(InventoryForm f) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        int quantity;
        try{
            quantity = Integer.parseInt(f.getQuantity());
        } catch(NumberFormatException e){
            throw new ApiException("Quantity should be a integer value");
        }
        inventoryPojo.setQuantity(quantity);
        inventoryPojo.setBarcode(f.getBarcode());

        int id = productService.getProductByBarcode(inventoryPojo.getBarcode()).getId();
        inventoryPojo.setId(id);
        return inventoryPojo;
    }
}
