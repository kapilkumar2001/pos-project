package com.increff.pos.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.increff.pos.pojo.OrderPojo;


@Repository
public class OrderDao extends AbstractDao{

	private static String SELECT_BY_ID = "select p from OrderPojo p where id=:id";
	private static String SELECT_ALL = "select p from OrderPojo p";
	private static String SELECT_BY_TIME = "select p from OrderPojo p where updated_at>=:startTime and updated_at<=:endTime";
	private static String DELETE_BY_ID = "delete from OrderPojo p where id=:id";

	@PersistenceContext
	private EntityManager em;
	
	public void insert(OrderPojo p){
		em.persist(p);
	}
	
	public OrderPojo select(int id) {
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_ID, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderPojo> selectAll(){
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
		return query.getResultList();
	}

	public List<OrderPojo> selectByTime(LocalDateTime startTime, LocalDateTime endTime){
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_TIME, OrderPojo.class);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		return query.getResultList();
	}

	public int delete(int id) {
		Query query = em.createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public void update(OrderPojo p) {
	}
}
