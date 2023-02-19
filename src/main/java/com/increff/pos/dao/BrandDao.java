package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String SELECT_BY_ID = "select p from BrandPojo p where id=:id";
	private static String SELECT_BY_BRAND = "select p from BrandPojo p where brand=:brand";
	private static String SELECT_BY_CATEGORY = "select p from BrandPojo p where category=:category";
	private static String SELECT_ALL = "select p from BrandPojo p";
	private static String SELECT_BY_BRAND_AND_CATEGORY = "select p from BrandPojo p where brand=:brand AND category=:category";
	
	@Transactional
	public void insert(BrandPojo p) {
		em().persist(p);
	}
	
	public BrandPojo selectById(int id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_ID, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}
	
	public List<BrandPojo> selectByBrand(String brand) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND, BrandPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}

	public List<BrandPojo> selectByCategory(String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_CATEGORY, BrandPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public void update(BrandPojo p) {
	}
	
	public BrandPojo getBrandByBrandAndCategory(String brand, String category){
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_AND_CATEGORY, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}
}
