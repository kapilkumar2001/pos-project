package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao{

	private static String select_id = "select p from OrderPojo p where id=:id";
	private static String select_all = "select p from OrderPojo p";
	
	
	@PersistenceContext
	private EntityManager em;
	
	public void insert(OrderPojo p){
		em.persist(p);
	}
	
	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderPojo> selectAll(){
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}
	
}
