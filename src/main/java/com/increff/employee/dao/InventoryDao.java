package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;



@Repository
public class InventoryDao extends AbstractDao{

	private static String delete_id = "delete from InventoryPojo p where id=:id";
	private static String select_id = "select p from InventoryPojo p where id=:id";
	private static String select_all = "select p from InventoryPojo p";
	private static String select_product_id = "select p from ProductPojo p where barcode=:barcode";
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(InventoryPojo p, String barcode) {
		int id = getProduct(barcode).getId();
		p.setId(id);
		em.persist(p);
	}
		
	public InventoryPojo select(String barcode) {
		int id = getProduct(barcode).getId();
		TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}
	
	public void update(InventoryPojo p) {
	}
	
	public ProductPojo getProduct(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(select_product_id, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}
}
