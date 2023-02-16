package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    public void add(BrandForm brandForm) throws ApiException{
        BrandPojo brandPojo = convert(brandForm);
        brandService.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo brandPojo = brandService.get(id);
        return convert(brandPojo);
    }

    public List<BrandData> getAll(){
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<BrandData>();
        for (BrandPojo brandPojo : brandPojoList) {
            brandDataList.add(convert(brandPojo));
        }
        return brandDataList;
    }

    public void update(int id, BrandForm brandForm) throws ApiException{
        BrandPojo brandPojo = convert(brandForm);
        brandService.update(id, brandPojo);
    }

    public List<String> getBrands(){
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<String> brandsList = new ArrayList<String>();
        for (BrandPojo brandPojo : brandPojoList) {
            brandsList.add(brandPojo.getBrand());
        }
        return brandsList;
    }

    public List<String> getAllCategories(){
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<String> categoryList = new ArrayList<String>();
        for (BrandPojo brandPojo : brandPojoList) {
            categoryList.add(brandPojo.getCategory());
        }
        return categoryList;
    }

    public List<String> getCategories(String brand){
        List<BrandPojo> brandPojoList = brandService.getCategories(brand);
        List<String> categoriesList = new ArrayList<String>();
        for (BrandPojo p : brandPojoList) {
            categoriesList.add(p.getCategory());
        }
        return categoriesList;
    }

    private static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setId(brandPojo.getId());
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        return brandData;
    }

    private static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brandForm.getBrand());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }

}
