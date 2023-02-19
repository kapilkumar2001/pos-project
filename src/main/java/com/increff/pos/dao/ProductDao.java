package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao{

	private static String SELECT_BY_ID = "select p from ProductPojo p where id=:id";
	private static String SELECT_ALL = "select p from ProductPojo p";
	private static String SELECT_BY_BARCODE = "select p from ProductPojo p where barcode=:barcode";
	private static String SELECT_BY_BRANDID = "select p from ProductPojo p where brand_id=:brand_id";
	
	@Transactional
	public void insert(ProductPojo p) {
		em().persist(p);
	}
	
	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
		return query.getResultList();
	}
	
	public void update(ProductPojo p) {
	}
	
	public ProductPojo getProductByBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BARCODE, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public List<ProductPojo> getAllProductsByBrandId(int brandId){
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BRANDID, ProductPojo.class);
		query.setParameter("brand_id", brandId);
		return query.getResultList();
	}
}
