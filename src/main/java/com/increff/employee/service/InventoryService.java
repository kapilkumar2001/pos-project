package com.increff.employee.service;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class InventoryService {

	@Autowired
	InventoryDao dao;
	
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p, String barcode) throws ApiException {
		normalize(barcode);
		if(StringUtil.isEmpty(String.valueOf(p.getQuantity()))) {
			throw new ApiException("quantity cannot be empty");
		}
		else if(StringUtil.isEmpty(barcode)) {
			throw new ApiException("barcode cannot be empty");
		}
		InventoryPojo inv = get(barcode);
		if(inv==null)
		{
			dao.insert(p, barcode);
		}
		else
		{
			update(barcode, inv.getQuantity()+p.getQuantity());
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
		List<InventoryPojo> inventories =  dao.selectAll();
		for(InventoryPojo p: inventories) {
			p.setBarcode(dao.getProductById(p.getId()).getBarcode());
		}
		return inventories;
	}
	
	@Transactional(rollbackOn  = ApiException.class)
	public void update(String barcode, int quantity) throws ApiException {
		InventoryPojo ex = getCheck(barcode);
	    ex.setQuantity(quantity);
		dao.update(ex);
	}
	
	@Transactional
	public InventoryPojo getCheck(String barcode) throws ApiException {
		InventoryPojo p = dao.select(barcode);
		if (p == null) {
			throw new ApiException("barcode does not exit, barcode: " + barcode);
		}
		return p;
	}
	
	
	protected static void normalize(String barcode) {
		barcode = StringUtil.toLowerCase(barcode);
	}
	
}
