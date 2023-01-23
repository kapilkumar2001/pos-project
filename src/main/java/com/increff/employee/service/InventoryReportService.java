package com.increff.employee.service;

import com.increff.employee.dao.BrandCategoryDao;
import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryReportService {

    @Autowired
    private BrandCategoryDao brandCategoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;

    @Transactional
    public List<InventoryReportData> get() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();

        List<BrandCategoryPojo> brandCategoryPojoList = brandCategoryDao.selectAll();

        for(BrandCategoryPojo brandCategoryPojo: brandCategoryPojoList){
            // System.out.println("brandcategorypojo:  " + brandCategoryPojo.getId() + " " + brandCategoryPojo.getBrand() + " " + brandCategoryPojo.getCategory());

            InventoryReportData inventoryReportData = new InventoryReportData();
            inventoryReportData.setBrand(brandCategoryPojo.getBrand());
            inventoryReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();

            int quantity=0;
            List<ProductPojo> productPojoList = productDao.getAllProductByBrandsCategoryId(brandCategoryId);
            if(productPojoList.size()!=0)
            // System.out.println("size - >  " +  productPojoList.get(0).getBarcode());
            
            
            for(ProductPojo productPojo: productPojoList){
                // System.out.print("productpojo:  ");
                // System.out.print("productpojo:  " + productPojo.getId() + " " + productPojo.getBrand_category());
                int productId = productPojo.getId();
                InventoryPojo inventoryPojo = inventoryDao.select(productId);
                quantity = quantity + inventoryPojo.getQuantity();
                System.out.println(" " +quantity);
            }
            // System.out.println("productpojo end ");

            inventoryReportData.setQuantity(quantity);

            inventoryReportDataList.add(inventoryReportData);
        }

        return inventoryReportDataList;
    }
}
