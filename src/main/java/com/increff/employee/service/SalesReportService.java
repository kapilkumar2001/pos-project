package com.increff.employee.service;

import com.increff.employee.dao.*;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesReportService {

    @Autowired
    private BrandCategoryDao brandCategoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional
    public List<SalesReportData> get(String brand, String category) throws ApiException{
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        List<BrandCategoryPojo> brandCategoryPojoList = new ArrayList<>();

        if((!brand.equals("")) && (!category.equals(""))){

            BrandCategoryPojo brandCategoryPojo = brandCategoryDao.getBrandCategory(brand, category);
            if(brandCategoryPojo==null){
                throw  new ApiException("Brand-Category Combination doesn't exist");
            }
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            salesReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();
            int quantity=0;
            double revenue = 0;
            List<ProductPojo> productPojoList = productDao.getAllProductByBrandsCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList){
                int productId = productPojo.getId();
                List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByProductId(productId);
                for(OrderItemPojo orderItemPojo: orderItemPojoList){
                    quantity = quantity + orderItemPojo.getQuantity();
                    revenue = revenue + orderItemPojo.getSellingPrice();
                }
            }
            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(revenue);

            salesReportDataList.add(salesReportData);

            return salesReportDataList;
        }

        else if(!brand.equals("")){
            brandCategoryPojoList = brandCategoryDao.selectCategories(brand);
        }

        else if(!category.equals("")) {
            brandCategoryPojoList = brandCategoryDao.selectBrands(category);
        }

        for(BrandCategoryPojo brandCategoryPojo: brandCategoryPojoList){
            System.out.println("for loop 2");
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            salesReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();
            int quantity=0;
            double revenue = 0;
            List<ProductPojo> productPojoList = productDao.getAllProductByBrandsCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList){
                int productId = productPojo.getId();
                List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByProductId(productId);
                for(OrderItemPojo orderItemPojo: orderItemPojoList){
                    quantity = quantity + orderItemPojo.getQuantity();
                    revenue = revenue + orderItemPojo.getSellingPrice();
                }
            }
            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(revenue);

            salesReportDataList.add(salesReportData);
        }

        return salesReportDataList;
    }
}
