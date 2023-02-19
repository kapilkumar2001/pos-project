package com.increff.pos.api;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.StringUtil;

@Service
public class InventoryApi {

	@Autowired
	InventoryDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo existingInventoryPojo = dao.select(inventoryPojo.getId());
		if(Objects.isNull(existingInventoryPojo)) {
		   dao.insert(inventoryPojo);
		} else{
			if(existingInventoryPojo.getQuantity()+inventoryPojo.getQuantity()<0) {
				throw new ApiException("Quantity can not be decreased to less than 0, Available Quantity: " + existingInventoryPojo.getQuantity());
			}
			update(existingInventoryPojo.getId(), inventoryPojo.getBarcode(), existingInventoryPojo.getQuantity()+inventoryPojo.getQuantity());
		}
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int id, String barcode) throws ApiException {
		InventoryPojo inventoryPojo =  getCheck(id, barcode);
		inventoryPojo.setBarcode(barcode);
		return inventoryPojo;
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void updateInventoryWhileCreatingOrder(int id, String barcode, int quantity, int prevQuantity) throws ApiException{
		
		if(quantity<=0){
			throw new ApiException("Quantity should be greater than 0");
		}
		InventoryPojo inventoryPojo = get(id, barcode);
		int updatedQuantity = (inventoryPojo.getQuantity()+prevQuantity) - quantity;
		if(updatedQuantity<0) {
			throw new ApiException("Not enough quantity. Only " + inventoryPojo.getQuantity() + " items left for barcode: " + barcode);
		}

		InventoryPojo newInventoryPojo = getCheck(id ,barcode);
		newInventoryPojo.setBarcode(barcode);
		newInventoryPojo.setQuantity(updatedQuantity);
	    dao.update(newInventoryPojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public void increaseInventory(int id, String barcode, int quantity) throws ApiException{
		InventoryPojo inventoryPojo = get(id, barcode);
		int updatedQuantity = (inventoryPojo.getQuantity()+quantity);
		if(updatedQuantity<0) {
			throw new ApiException("Quantity can not be decreased to less than 0, Available Quantity: " + inventoryPojo.getQuantity());
		}
		update(id, barcode, updatedQuantity);
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, String barcode, int quantity) throws ApiException {
		if(quantity<0) {
			throw new ApiException("Quantity should be a positive number");
		}
		if(StringUtil.isEmpty(barcode)) {
			throw new ApiException("Barcode cannot be empty");
		}
		
		InventoryPojo newInventoryPojo = getCheck(id ,barcode);
		newInventoryPojo.setBarcode(barcode);
		newInventoryPojo.setQuantity(quantity);
	    dao.update(newInventoryPojo);
	}
	
	@Transactional
	public InventoryPojo getCheck(int id, String barcode) throws ApiException {
		InventoryPojo inventoryPojo = dao.select(id);
		if (Objects.isNull(inventoryPojo)) {
			throw new ApiException("Barcode does not exit, barcode: " + barcode);
		}
		return inventoryPojo;
	}
	
}
