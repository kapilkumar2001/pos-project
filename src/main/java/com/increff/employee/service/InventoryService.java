package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.util.StringUtil;

@Service
public class InventoryService {

	@Autowired
	InventoryDao inventoryDao;
	@Autowired
	ProductDao productDao;
	
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo inventoryPojo) throws ApiException {
		normalize(inventoryPojo.getBarcode());
		if(inventoryPojo.getQuantity()<0) {
			throw new ApiException("quantity cannot be less than 0");
		}
		else if(StringUtil.isEmpty(inventoryPojo.getBarcode())) {
			throw new ApiException("barcode cannot be empty");
		}
		if(productDao.getProductByBarcode(inventoryPojo.getBarcode())==null) {
			throw new ApiException("product with this barcode doesn't exist, barcode: "+ inventoryPojo.getBarcode());
		}
		int id = productDao.getProductByBarcode(inventoryPojo.getBarcode()).getId();
		inventoryPojo.setId(id);
		InventoryPojo existingInventoryPojo = inventoryDao.select(id);
		if(existingInventoryPojo==null)
		{
		   inventoryDao.insert(inventoryPojo);
		}
		else
		{
			update(inventoryPojo.getBarcode(), existingInventoryPojo.getQuantity()+inventoryPojo.getQuantity());
		}
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(String barcode) throws ApiException {
		InventoryPojo inventoryPojo =  getCheck(barcode);
		inventoryPojo.setBarcode(barcode);
		return inventoryPojo;
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		List<InventoryPojo> inventories =  inventoryDao.selectAll();
		for(InventoryPojo p: inventories) {
			p.setBarcode(productDao.select(p.getId()).getBarcode());
		}
		return inventories;
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(String barcode, int quantity) throws ApiException {
		if(quantity<0) {
			throw new ApiException("quantity cannot be less than 0");
		}
		else if(StringUtil.isEmpty(barcode)) {
			throw new ApiException("barcode cannot be empty");
		}
		if(productDao.getProductByBarcode(barcode)==null) {
			throw new ApiException("product with this barcode doesn't exist, barcode: "+ barcode);
		}
		
		InventoryPojo newInventoryPojo = getCheck(barcode);
		newInventoryPojo.setBarcode(barcode);
		newInventoryPojo.setQuantity(quantity);
	    inventoryDao.update(newInventoryPojo);
	}
	
	@Transactional
	public InventoryPojo getCheck(String barcode) throws ApiException {
		int id = productDao.getProductByBarcode(barcode).getId();
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
