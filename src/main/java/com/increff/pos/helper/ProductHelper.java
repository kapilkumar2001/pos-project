package com.increff.pos.helper;

import java.text.DecimalFormat;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.StringUtil;

public class ProductHelper {
    

    public static void validate(ProductForm productForm) throws ApiException{
        if(StringUtil.isEmpty(String.valueOf(productForm.getBrand()))) {
			throw new ApiException("Brand cannot be empty");
		}
		else if(StringUtil.isEmpty(String.valueOf(productForm.getCategory()))) {
			throw new ApiException("Category cannot be empty");
		}
		else if(StringUtil.isEmpty(productForm.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		else if(StringUtil.isEmpty(productForm.getName())) {
			throw new ApiException("Product Name cannot be empty");
		}
		else if(StringUtil.isEmpty(productForm.getMrp())) {
			throw new ApiException("MRP cannot be empty");
		}
    }

    public static ProductData convert(ProductPojo productPojo) {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setName(productPojo.getName());
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(productPojo.getBrand());
        productData.setCategory(productPojo.getCategory());
        productData.setBrandId(productPojo.getBrandId());
        return productData;
    }

    public static ProductPojo convert(ProductForm productForm) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        double mrp;
        try{
            mrp = Double.parseDouble(productForm.getMrp());
        } catch(NumberFormatException e){
            throw new ApiException("Mrp should be a positive number");
        }
        productPojo.setMrp(mrp);
        productPojo.setBrand(productForm.getBrand());
        productPojo.setCategory(productForm.getCategory());
        productPojo.setName(productForm.getName());
        return productPojo;
    }
    
    public static void normalize(ProductPojo productPojo) {
		DecimalFormat dec = new DecimalFormat("#.##");
		productPojo.setMrp(Double.valueOf(dec.format(productPojo.getMrp())));
		productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()));
		productPojo.setName(StringUtil.toLowerCase(productPojo.getName()));
	}
}
