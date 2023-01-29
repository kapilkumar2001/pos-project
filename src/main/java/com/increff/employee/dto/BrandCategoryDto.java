package com.increff.employee.dto;

import com.increff.employee.model.BrandCategoryData;
import com.increff.employee.model.BrandCategoryForm;
import com.increff.employee.pojo.BrandCategoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class BrandCategoryDto {

    @Autowired
    private BrandCategoryService brandCategoryService;

    public void add(BrandCategoryForm brandCategoryForm) throws ApiException{
        BrandCategoryPojo brandCategoryPojo = convert(brandCategoryForm);
        brandCategoryService.add(brandCategoryPojo);
    }

    public void delete(int id) {
        brandCategoryService.delete(id);
    }

    public BrandCategoryData get(int id) throws ApiException {
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(id);
        return convert(brandCategoryPojo);
    }

    public List<BrandCategoryData> getAll(){
        List<BrandCategoryPojo> brandCategoryPojoList = brandCategoryService.getAll();
        List<BrandCategoryData> brandCategoryDataList = new ArrayList<BrandCategoryData>();
        for (BrandCategoryPojo brandCategoryPojo : brandCategoryPojoList) {
            brandCategoryDataList.add(convert(brandCategoryPojo));
        }
        return brandCategoryDataList;
    }

    public void update(int id, BrandCategoryForm brandCategoryForm) throws ApiException{
        BrandCategoryPojo brandCategoryPojo = convert(brandCategoryForm);
        brandCategoryService.update(id, brandCategoryPojo);
    }

    public List<String> getBrands(){
        List<BrandCategoryPojo> brandCategoryPojoList = brandCategoryService.getAll();
        List<String> brandsList = new ArrayList<String>();
        for (BrandCategoryPojo brandCategoryPojo : brandCategoryPojoList) {
            brandsList.add(brandCategoryPojo.getBrand());
        }
        return brandsList;
    }

    public List<String> getAllCategories(){
        List<BrandCategoryPojo> brandCategoryPojoList = brandCategoryService.getAll();
        List<String> categoryList = new ArrayList<String>();
        for (BrandCategoryPojo brandCategoryPojo : brandCategoryPojoList) {
            categoryList.add(brandCategoryPojo.getCategory());
        }
        return categoryList;
    }

    public List<String> getCategories(String brand){
        List<BrandCategoryPojo> brandCategoryPojoList = brandCategoryService.getCategories(brand);
        List<String> categoriesList = new ArrayList<String>();
        for (BrandCategoryPojo p : brandCategoryPojoList) {
            categoriesList.add(p.getCategory());
        }
        return categoriesList;
    }

    private static BrandCategoryData convert(BrandCategoryPojo brandCategoryPojo) {
        BrandCategoryData brandCategoryData = new BrandCategoryData();
        brandCategoryData.setId(brandCategoryPojo.getId());
        brandCategoryData.setBrand(brandCategoryPojo.getBrand());
        brandCategoryData.setCategory(brandCategoryPojo.getCategory());
        return brandCategoryData;
    }

    private static BrandCategoryPojo convert(BrandCategoryForm brandCategoryForm) {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setBrand(brandCategoryForm.getBrand());
        brandCategoryPojo.setCategory(brandCategoryForm.getCategory());
        return brandCategoryPojo;
    }

}
