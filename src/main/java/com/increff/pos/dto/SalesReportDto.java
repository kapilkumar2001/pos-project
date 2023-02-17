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
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.StatusEnum;

@Component
public class SalesReportDto {
    DecimalFormat dec = new DecimalFormat("#.##");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    @Transactional
    public List<SalesReportData> get(String startDate, String endDate, String brand, String category) throws ApiException{
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        List<BrandPojo> brandPojoList = new ArrayList<>();
 
        LocalDateTime time = LocalDateTime.now();
        if(startDate.equals("")){
            startDate = time.minusMonths(1).toLocalDate().toString(); 
        }
        if(endDate.equals("")){
            endDate = time.toLocalDate().toString();
        }
        String tmpStartDate = startDate + "00:00:00";
        String tmpEndDate = endDate + "23:59:59";
        LocalDateTime startDateTime = LocalDateTime.parse(tmpStartDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(tmpEndDate, formatter);

        if((!brand.equals("")) && (!category.equals(""))){
            brandPojoList.add(brandService.getBrandByBrandAndCategory(brand, category));
        } else if(!brand.equals("")){
            brandPojoList = brandService.getByBrand(brand);
        } else if(!category.equals("")) {
            brandPojoList = brandService.getByCategory(category);
        } else{
            brandPojoList = brandService.getAll();
        }

        for(BrandPojo brandPojo: brandPojoList){
            SalesReportData salesReportData = getSalesReportData(brandPojo, startDateTime, endDateTime);
            salesReportDataList.add(salesReportData);
        }
        return salesReportDataList;
    }


    public SalesReportData getSalesReportData(BrandPojo brandPojo, LocalDateTime startDateTime, LocalDateTime endDateTime) throws ApiException{
        SalesReportData salesReportData = new SalesReportData();
        salesReportData.setBrand(brandPojo.getBrand());
        salesReportData.setCategory(brandPojo.getCategory());

        int quantity=0;
        double revenue = 0;
        List<Integer> productIds = new ArrayList<Integer>();

        List<ProductPojo> productPojoList = productService.getProductsByBrandId(brandPojo.getId());
        for(ProductPojo productPojo: productPojoList){
            productIds.add(productPojo.getId());
        }

        List<OrderPojo> orderPojoList = orderService.getOrderByTime(startDateTime, endDateTime);

        for(OrderPojo orderPojo: orderPojoList){
            if(orderPojo.getStatus().equals(StatusEnum.invoiced)){
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
        return salesReportData;
    }
}
