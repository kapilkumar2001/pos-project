package com.increff.pos.dto;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.SalesReportData;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;

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

        DecimalFormat dec = new DecimalFormat("#.##");

        // Setting up default values of start and enddate if null
        LocalDateTime time = LocalDateTime.now();
        if(startDate.equals("")){
            startDate = time.minusMonths(1).toLocalDate().toString(); 
        }
        if(endDate.equals("")){
            endDate = time.toLocalDate().toString();
        }
        String tmpStartDate = startDate + "00:00:00";
        String tmpEndDate = endDate + "23:59:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);


        if((!brand.equals("")) && (!category.equals(""))){
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.getBrandCategory(brand, category);
            
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandCategoryPojo.getBrand());
            salesReportData.setCategory(brandCategoryPojo.getCategory());

            int brandCategoryId = brandCategoryPojo.getId();
            int quantity=0;
            double revenue = 0;

            List<ProductPojo> productPojoList = productService.getProductsByBrandCategoryId(brandCategoryId);
            List<Integer> productIds = new ArrayList<Integer>();
            for(ProductPojo productPojo: productPojoList){
                productIds.add(productPojo.getId());
            }

            List<OrderPojo> orderPojoList = orderService.getOrderByTime(startDateTime, endDateTime);

            for(OrderPojo orderPojo: orderPojoList){
                if(orderPojo.getStatus().equals("invoiced")){
                    List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsbyOrderId(orderPojo.getId());
                    for(OrderItemPojo orderItemPojo: orderItemPojoList){
                        if(productIds.contains(orderItemPojo.getProductId())){
                            quantity = quantity + orderItemPojo.getQuantity();
                            revenue = revenue + (orderItemPojo.getSellingPrice()*orderItemPojo.getQuantity());
                        }
                    }   
                }
            }

            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(Double.valueOf(dec.format(revenue)));

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

            int quantity=0;
            double revenue = 0;
            List<Integer> productIds = new ArrayList<Integer>();

            List<ProductPojo> productPojoList = productService.getProductsByBrandCategoryId(brandCategoryPojo.getId());
            for(ProductPojo productPojo: productPojoList){
                productIds.add(productPojo.getId());
            }

            List<OrderPojo> orderPojoList = orderService.getOrderByTime(startDateTime, endDateTime);

            for(OrderPojo orderPojo: orderPojoList){
                if(orderPojo.getStatus().equals("invoiced")){
                    List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsbyOrderId(orderPojo.getId());
                    for(OrderItemPojo orderItemPojo: orderItemPojoList){
                        if(productIds.contains(orderItemPojo.getProductId())){
                            quantity = quantity + orderItemPojo.getQuantity();
                            revenue = revenue + (orderItemPojo.getSellingPrice()*orderItemPojo.getQuantity());
                        }
                    }   
                }
            }
            salesReportData.setQuantity(quantity);
            salesReportData.setRevenue(Double.valueOf(dec.format(revenue)));
            salesReportDataList.add(salesReportData);
        }
        return salesReportDataList;
    }
}
