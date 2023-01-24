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
            
            InventoryReportData inventoryReportData = new InventoryReportData();
            inventoryReportData.setBrand(brandCategoryPojo.getBrand());
            inventoryReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();

            int quantity=0;
            List<ProductPojo> productPojoList = productDao.getAllProductByBrandsCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList) {
                int productId = productPojo.getId();
                InventoryPojo inventoryPojo = inventoryDao.select(productId);
                quantity = quantity + inventoryPojo.getQuantity();
            }

            inventoryReportData.setQuantity(quantity);

            inventoryReportDataList.add(inventoryReportData);
        }

        return inventoryReportDataList;
    }
}
