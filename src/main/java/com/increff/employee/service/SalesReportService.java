package com.increff.employee.service;

import com.increff.employee.dao.*;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesReportService {

    @Autowired
    private BrandCategoryDao brandCategoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;

    @Transactional
    public List<SalesReportData> get(String startDate, String endDate, String brand, String category) throws ApiException{
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
                String tmpStartDate = startDate + "00:00:00";
                String tmpEndDate = endDate + "23:59:59";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
                
                LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);
               
                List<OrderPojo> orderPojoList = orderDao.selectByTime(startDateTime, endDateTime);

                for(OrderPojo orderPojo: orderPojoList){
                    System.out.println(orderPojo.getTime());

                    List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderPojo.getId());

                    for(OrderItemPojo orderItemPojo: orderItemPojoList){
                        if(orderItemPojo.getProductId()==productId){
                            quantity = quantity + orderItemPojo.getQuantity();
                            revenue = revenue + orderItemPojo.getSellingPrice();
                        }
                    }   
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

        else{
            brandCategoryPojoList = brandCategoryDao.selectAll();
        }

        for(BrandCategoryPojo brandCategoryPojo: brandCategoryPojoList){
           
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            salesReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();
            int quantity=0;
            double revenue = 0;
            List<ProductPojo> productPojoList = productDao.getAllProductByBrandsCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList){
                int productId = productPojo.getId();
                String tmpStartDate = startDate + "00:00:00";
                String tmpEndDate = endDate + "23:59:59";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
                
                LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);
               
                List<OrderPojo> orderPojoList = orderDao.selectByTime(startDateTime, endDateTime);

                for(OrderPojo orderPojo: orderPojoList){
                    System.out.println(orderPojo.getTime());

                    List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderPojo.getId());

                    for(OrderItemPojo orderItemPojo: orderItemPojoList){
                        if(orderItemPojo.getProductId()==productId){
                            quantity = quantity + orderItemPojo.getQuantity();
                            revenue = revenue + orderItemPojo.getSellingPrice();
                        }
                    }   
                }
            }
            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(revenue);
           
            salesReportDataList.add(salesReportData);
        }

        return salesReportDataList;
    }
}
