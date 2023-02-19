package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.helper.BrandHelper;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandDto {

    @Autowired
    private BrandApi api;

    public void add(BrandForm brandForm) throws ApiException{
        BrandHelper.validate(brandForm);
        BrandPojo brandPojo = BrandHelper.convert(brandForm);
        BrandHelper.normalize(brandPojo);
        api.add(brandPojo);
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo brandPojo = api.getCheck(id);
        return BrandHelper.convert(brandPojo);
    }

    public List<BrandData> getAll(){
        List<BrandPojo> brandPojoList = api.getAll();
        List<BrandData> brandDataList = new ArrayList<BrandData>();
        for (BrandPojo brandPojo : brandPojoList) {
            brandDataList.add(BrandHelper.convert(brandPojo));
        }
        return brandDataList;
    }

    public void update(int id, BrandForm brandForm) throws ApiException{
        BrandHelper.validate(brandForm);
        BrandPojo brandPojo = BrandHelper.convert(brandForm);
        BrandHelper.normalize(brandPojo);
        api.update(id, brandPojo);
    }

    public List<String> getAllBrands(){
        List<BrandPojo> brandPojoList = api.getAll();
        List<String> brandsList = new ArrayList<String>();
        for (BrandPojo brandPojo : brandPojoList) {
            brandsList.add(brandPojo.getBrand());
        }
        return brandsList;
    }

    public List<String> getAllCategories(){
        List<BrandPojo> brandPojoList = api.getAll();
        List<String> categoryList = new ArrayList<String>();
        for (BrandPojo brandPojo : brandPojoList) {
            categoryList.add(brandPojo.getCategory());
        }
        return categoryList;
    }

    public List<String> getCategoriesByBrand(String brand){
        List<BrandPojo> brandPojoList = api.getByBrand(brand);
        List<String> categoriesList = new ArrayList<String>();
        for (BrandPojo p : brandPojoList) {
            categoriesList.add(p.getCategory());
        }
        return categoriesList;
    }
}
