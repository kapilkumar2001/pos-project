package com.increff.employee.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.PosDaySalesPojo;

@Repository
public class PosDaySalesDao {
    
    @PersistenceContext
	private EntityManager em;
	
	public void insert(PosDaySalesPojo p){
		em.persist(p);
	}
    
}
