package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;


@Repository
public class OrderItemDao extends AbstractDao{

	private static String select_order_id = "select p from OrderItemPojo p where orderId=:orderId";
	private static String select_id = "select p from OrderItemPojo p where id=:id";
	private static String delete_id = "delete from OrderItemPojo p where id=:id";
	private static String select_all = "select p from OrderItemPojo p";
	private static String select_product_id = "select p from OrderItemPojo p where productId=:productId";
	
	@PersistenceContext
	private EntityManager em;
	
	
	public void insert(OrderItemPojo p){
		em.persist(p);
	}

	public List<OrderItemPojo> selectAll(){
		TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
		return query.getResultList();
	}

	public List<OrderItemPojo> selectByProductId(int productId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_product_id, OrderItemPojo.class);
		query.setParameter("productId", productId);
		return query.getResultList();
	}
	
	public OrderItemPojo selectByOrderItemId(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderItemPojo> selectByOrderId(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_order_id, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
	
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public void update(OrderItemPojo p) {
	}
}