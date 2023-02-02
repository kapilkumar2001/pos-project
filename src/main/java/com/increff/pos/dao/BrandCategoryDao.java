package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandCategoryPojo;

@Repository
public class BrandCategoryDao extends AbstractDao {

	private static String delete_id = "delete from BrandCategoryPojo p where id=:id";
	private static String select_id = "select p from BrandCategoryPojo p where id=:id";
	private static String select_categories = "select p from BrandCategoryPojo p where brand=:brand";
	private static String select_brands = "select p from BrandCategoryPojo p where category=:category";
	private static String select_all = "select p from BrandCategoryPojo p";
	private static String select_brand_category = "select p from BrandCategoryPojo p where brand=:brand AND category=:category";

	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(BrandCategoryPojo p) {
		em.persist(p);
	}
	
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public BrandCategoryPojo select(int id) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_id, BrandCategoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<BrandCategoryPojo> selectAll() {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_all, BrandCategoryPojo.class);
		return query.getResultList();
	}
	
	public List<BrandCategoryPojo> selectCategories(String brand) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_categories, BrandCategoryPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}

	public List<BrandCategoryPojo> selectBrands(String category) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brands, BrandCategoryPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public void update(BrandCategoryPojo p) {
	}
	
	// get brandCategoryPojo by brand and category
	public BrandCategoryPojo getBrandCategory(String brand, String category){
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brand_category, BrandCategoryPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}


}
