package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.increff.employee.controller.OrderApiController;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;



@Repository
public class InventoryDao extends AbstractDao{
	
	
	private static String select_id = "select p from InventoryPojo p where id=:id";
	private static String select_all = "select p from InventoryPojo p";
	
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(InventoryPojo p) {
		em.persist(p);
	}
		
	public InventoryPojo select(int id) {
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
	
}
