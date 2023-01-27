package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
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
        InventoryPojo p = convert(form);
        inventoryService.add(p);
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
        inventoryService.update(productPojo.getId(), barcode, inventoryForm.getQuantity());
    }

    private InventoryData convert(InventoryPojo p) {
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        d.setBarcode(p.getBarcode());
        d.setId(p.getId());
        return d;
    }

    private InventoryPojo convert(InventoryForm f) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(f.getQuantity());
        inventoryPojo.setBarcode(f.getBarcode());

        int id = productService.getProductByBarcode(inventoryPojo.getBarcode()).getId();
        inventoryPojo.setId(id);
        return inventoryPojo;
    }
}
