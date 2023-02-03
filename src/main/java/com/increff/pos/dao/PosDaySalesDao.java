package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.PosDaySalesPojo;

@Repository
public class PosDaySalesDao extends AbstractDao{

	private static String select_all = "select p from PosDaySalesPojo p";
    
    @PersistenceContext
	private EntityManager em;
	
	public void insert(PosDaySalesPojo p){
		em.persist(p);
	}

	public List<PosDaySalesPojo> selectAll() {
		TypedQuery<PosDaySalesPojo> query = getQuery(select_all, PosDaySalesPojo.class);
		return query.getResultList();
	}
    
}
