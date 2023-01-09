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
		dao.insert(p, barcode);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(String barcode) throws ApiException {
		return getCheck(barcode);
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
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
