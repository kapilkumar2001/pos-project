package com.increff.employee.service;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class InventoryService {

	@Autowired
	InventoryDao inventoryDao;
	@Autowired
	ProductDao productDao;
	
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p) throws ApiException {
		normalize(p.getBarcode());
		if(StringUtil.isEmpty(String.valueOf(p.getQuantity()))) {
			throw new ApiException("quantity cannot be empty");
		}
		else if(StringUtil.isEmpty(p.getBarcode())) {
			throw new ApiException("barcode cannot be empty");
		}
		if(productDao.getProductByBarcode(p.getBarcode())==null) {
			throw new ApiException("product with this barcode doesn't exist, barcode: "+ p.getBarcode());
		}
		int id = productDao.getProductByBarcode(p.getBarcode()).getId();
		p.setId(id);
		InventoryPojo inv = inventoryDao.select(id);
		if(inv==null)
		{
		   inventoryDao.insert(p);
		}
		else
		{
			update(p.getBarcode(), inv.getQuantity()+p.getQuantity());
		}
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(String barcode) throws ApiException {
		InventoryPojo p =  getCheck(barcode);
		p.setBarcode(barcode);
		return p;
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
		if(StringUtil.isEmpty(String.valueOf(quantity))) {
			throw new ApiException("quantity cannot be empty");
		}
		else if(StringUtil.isEmpty(barcode)) {
			throw new ApiException("barcode cannot be empty");
		}
		
		InventoryPojo ex = getCheck(barcode);
		ex.setBarcode(barcode);
	    ex.setQuantity(quantity);
	    inventoryDao.update(ex);
	}
	
	@Transactional
	public InventoryPojo getCheck(String barcode) throws ApiException {
		int id = productDao.getProductByBarcode(barcode).getId();
		InventoryPojo p = inventoryDao.select(id);
		if (p == null) {
			throw new ApiException("barcode does not exit, barcode: " + barcode);
		}
		return p;
	}
	
	
	protected static void normalize(String barcode) {
		barcode = StringUtil.toLowerCase(barcode);
	}
	
}
