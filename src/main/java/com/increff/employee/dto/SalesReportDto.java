package com.increff.employee.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandCategoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

@Component
public class SalesReportDto {
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @Transactional
    public List<SalesReportData> get(String startDate, String endDate, String brand, String category) throws ApiException{
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        List<BrandCategoryPojo> brandCategoryPojoList = new ArrayList<>();

        if((!brand.equals("")) && (!category.equals(""))){
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.getBrandCategory(brand, category);
            
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            salesReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();
            int quantity=0;
            double revenue = 0;
            List<ProductPojo> productPojoList = productService.getProductsByBrandCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList){
                int productId = productPojo.getId();
                String tmpStartDate = startDate + "00:00:00";
                String tmpEndDate = endDate + "23:59:59";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
                
                LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);
               
                List<OrderPojo> orderPojoList = orderService.getOrderByTime(startDateTime, endDateTime);

                for(OrderPojo orderPojo: orderPojoList){
                  

                    List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsbyOrderId(orderPojo.getId());

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
            brandCategoryPojoList = brandCategoryService.getCategories(brand);
        }

        else if(!category.equals("")) {
            brandCategoryPojoList = brandCategoryService.getBrands(category);
        }

        else{
            brandCategoryPojoList = brandCategoryService.getAll();
        }

        for(BrandCategoryPojo brandCategoryPojo: brandCategoryPojoList){
           
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            salesReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();
            int quantity=0;
            double revenue = 0;
            List<ProductPojo> productPojoList = productService.getProductsByBrandCategoryId(brandCategoryId);

            for(ProductPojo productPojo: productPojoList){
                int productId = productPojo.getId();
                String tmpStartDate = startDate + "00:00:00";
                String tmpEndDate = endDate + "23:59:59";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
                
                LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);
               
                List<OrderPojo> orderPojoList = orderService.getOrderByTime(startDateTime, endDateTime);

                for(OrderPojo orderPojo: orderPojoList){
                    List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsbyOrderId(orderPojo.getId());

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
