package com.increff.pos.helper;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.StringUtil;

public class BrandHelper {

    public static void validate(BrandForm brandForm) throws ApiException{
        if(StringUtil.isEmpty(brandForm.getBrand())) {
			throw new ApiException("brand name cannot be empty");
		}
		else if(StringUtil.isEmpty(brandForm.getCategory())) {
			throw new ApiException("category name cannot be empty");
		}
    }

    public static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setId(brandPojo.getId());
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        return brandData;
    }

    public static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brandForm.getBrand());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }

    public static void normalize(BrandPojo brandPojo) {
		brandPojo.setBrand(StringUtil.toLowerCase(brandPojo.getBrand()));
		brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()));
	}

}
