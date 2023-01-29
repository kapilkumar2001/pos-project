package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.util.StringUtil;

@Service
public class InventoryService {

	@Autowired
	InventoryDao inventoryDao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo inventoryPojo) throws ApiException {
		normalize(inventoryPojo.getBarcode());
		if(inventoryPojo.getQuantity()<0) {
			throw new ApiException("quantity cannot be less than 0");
		}
		else if(StringUtil.isEmpty(inventoryPojo.getBarcode())) {
			throw new ApiException("barcode cannot be empty");
		}

		InventoryPojo existingInventoryPojo = inventoryDao.select(inventoryPojo.getId());
		if(existingInventoryPojo==null)
		{
		   inventoryDao.insert(inventoryPojo);
		}
		else
		{
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
		return inventoryDao.selectAll();
	}

	@Transactional
	public void updateInventoryWhileCreatingOrder(int id, String barcode, int quantity) throws ApiException{
		if(quantity<0) {
			throw new ApiException("not enough quantity available, barcode: " + barcode);
		}
		update(id, barcode, quantity);
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, String barcode, int quantity) throws ApiException {
		if(quantity<0) {
			throw new ApiException("quantity cannot be less than 0");
		}
		else if(StringUtil.isEmpty(barcode)) {
			throw new ApiException("barcode cannot be empty");
		}

		InventoryPojo newInventoryPojo = getCheck(id ,barcode);
		newInventoryPojo.setBarcode(barcode);
		newInventoryPojo.setQuantity(quantity);
	    inventoryDao.update(newInventoryPojo);
	}
	
	@Transactional
	public InventoryPojo getCheck(int id, String barcode) throws ApiException {
		InventoryPojo inventoryPojo = inventoryDao.select(id);
		if (inventoryPojo == null) {
			throw new ApiException("barcode does not exit, barcode: " + barcode);
		}
		return inventoryPojo;
	}
	
	
	protected static void normalize(String barcode) {
		barcode = StringUtil.toLowerCase(barcode);
	}
	
}
