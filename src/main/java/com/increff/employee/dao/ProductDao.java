package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{

	private static String delete_id = "delete from ProductPojo p where id=:id";
	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_all = "select p from ProductPojo p";
	private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
	private static String select_by_brandcategoryid = "select p from ProductPojo p where brand_category=:brand_category";
	
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
	
	// get product by barcode
	public ProductPojo getProductByBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public List<ProductPojo> getAllProductByBrandsCategoryId(int brand_category){
		TypedQuery<ProductPojo> query = getQuery(select_by_brandcategoryid, ProductPojo.class);
		query.setParameter("brand_category", brand_category);
		return query.getResultList();
	}
}
