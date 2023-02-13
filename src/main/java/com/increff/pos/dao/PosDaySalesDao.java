package com.increff.pos.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.PosDaySalesPojo;

@Repository
public class PosDaySalesDao extends AbstractDao{

	private static String select_all = "select p from PosDaySalesPojo p";
	private static String select_date = "select p from PosDaySalesPojo p where date>=:startDate and date<=:endDate";
    
    @PersistenceContext
	private EntityManager em;
	
	public void insert(PosDaySalesPojo p){
		em.persist(p);
	}

	public List<PosDaySalesPojo> selectAll() {
		TypedQuery<PosDaySalesPojo> query = getQuery(select_all, PosDaySalesPojo.class);
		return query.getResultList();
	}

	public List<PosDaySalesPojo> selectByDate(LocalDate startDate, LocalDate endDate){
		TypedQuery<PosDaySalesPojo> query = getQuery(select_date, PosDaySalesPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
    
}
