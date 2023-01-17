package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;


@Repository
public class OrderItemDao extends AbstractDao{

	private static String select_id = "select p from OrderItemPojo p where orderId=:orderId";
	
	@PersistenceContext
	private EntityManager em;
	
	
	public void insert(OrderItemPojo p){
		em.persist(p);
	}
	
	public List<OrderItemPojo> select(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
	
}