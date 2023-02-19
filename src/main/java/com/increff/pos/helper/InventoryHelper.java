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
    }

    public static InventoryData convert(InventoryPojo p) throws ApiException{
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        d.setBarcode(p.getBarcode());
        d.setId(p.getId());
        return d;
    }

    public static InventoryPojo convert(InventoryForm f) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        int quantity;
        try{
            quantity = Integer.parseInt(f.getQuantity());
        } catch(NumberFormatException e){
            throw new ApiException("Quantity should be a integer value");
        }
        inventoryPojo.setQuantity(quantity);
        inventoryPojo.setBarcode(f.getBarcode());
        return inventoryPojo;
    }

    public static void normalize(String barcode) {
		barcode = StringUtil.toLowerCase(barcode);
	}
}
