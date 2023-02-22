package com.increff.pos.helper;

import com.increff.pos.api.ApiException;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.StringUtil;

public class InventoryHelper {

    public static void validate(InventoryForm inventoryForm) throws ApiException{
        if(StringUtil.isEmpty(inventoryForm.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
        try{
            Integer.parseInt(inventoryForm.getQuantity());
        } catch(NumberFormatException e){
            throw new ApiException("Quantity should be a integer value");
        }
    }

    public static InventoryData convert(InventoryPojo inventoryPojo) throws ApiException{
        InventoryData inventoryData = new InventoryData();
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setBarcode(inventoryPojo.getBarcode());
        inventoryData.setId(inventoryPojo.getId());
        return inventoryData;
    }

    public static InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(Integer.parseInt(inventoryForm.getQuantity()));
        inventoryPojo.setBarcode(inventoryForm.getBarcode());
        return inventoryPojo;
    }

    public static String normalize(String barcode) {
		return StringUtil.toLowerCase(barcode);
	}
}
