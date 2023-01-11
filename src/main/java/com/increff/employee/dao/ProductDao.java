package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{

	private static String delete_id = "delete from ProductPojo p where id=:id";
	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_all = "select p from ProductPojo p";
	private static String select_brandcategory_by_brand_category = "select p from BrandCategoryPojo p where brand=:brand AND category=:category";
	private static String select_brandcategory_by_id = "select p from BrandCategoryPojo p where id=:id";
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(ProductPojo p) {
		em.persist(p);
	}
	
	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
		return query.getResultList();
	}
	
	public void update(ProductPojo p) {
	}
	
	// get brandcategory by productpojo
	public BrandCategoryPojo getBrandCategory(ProductPojo p){
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brandcategory_by_brand_category, BrandCategoryPojo.class);
		query.setParameter("brand", p.getBrand());
		query.setParameter("category", p.getCategory());
		return getSingle(query);
	}
	
	// get brandcategory by id
	public BrandCategoryPojo getBrandCategoryById(int id) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brandcategory_by_id, BrandCategoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
}
