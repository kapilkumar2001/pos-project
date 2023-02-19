package com.increff.pos.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;


@Repository
public class OrderItemDao extends AbstractDao{

	private static String SELECT_BY_ORDERID = "select p from OrderItemPojo p where orderId=:orderId";
	private static String SELECT_BY_ID = "select p from OrderItemPojo p where id=:id";
	private static String DELETE_BY_ID = "delete from OrderItemPojo p where id=:id";
	private static String SELECT_ALL = "select p from OrderItemPojo p";
	private static String SELECT_BY_PRODUCTID = "select p from OrderItemPojo p where productId=:productId";
		
	public void insert(OrderItemPojo p){
		em().persist(p);
	}

	public List<OrderItemPojo> selectAll(){
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
		return query.getResultList();
	}

	public List<OrderItemPojo> selectByProductId(int productId) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_PRODUCTID, OrderItemPojo.class);
		query.setParameter("productId", productId);
		return query.getResultList();
	}
	
	public OrderItemPojo selectByOrderItemId(int id) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ID, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderItemPojo> selectByOrderId(int orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDERID, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
	
	public int delete(int id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public void update(OrderItemPojo p) {
	}
}