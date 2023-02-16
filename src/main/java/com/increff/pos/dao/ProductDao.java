package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{

	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_all = "select p from ProductPojo p";
	private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
	private static String select_by_brandid = "select p from ProductPojo p where brand_id=:brand_id";
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void insert(ProductPojo p) {
		em.persist(p);
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

	public List<ProductPojo> getAllProductByBrandId(int brandId){
		TypedQuery<ProductPojo> query = getQuery(select_by_brandid, ProductPojo.class);
		query.setParameter("brand_id", brandId);
		return query.getResultList();
	}
}
