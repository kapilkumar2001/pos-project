package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.InventoryPojo;



@Repository
public class InventoryDao extends AbstractDao{
	
	
	private static String SELECT_BY_ID = "select p from InventoryPojo p where id=:id";
	private static String SELECT_ALL = "select p from InventoryPojo p";
	
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(InventoryPojo p) {
		em.persist(p);
	}
		
	public InventoryPojo select(int id) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_BY_ID, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		return query.getResultList();
	}
	
	public void update(InventoryPojo p) {
	}
	
}
