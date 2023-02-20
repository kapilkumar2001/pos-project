package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.api.AbstractUnitTest;
import com.increff.pos.api.ApiException;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;


public class brandDtoTest extends AbstractUnitTest{

    @Autowired
    private BrandDto dto;
    @Autowired
    private BrandDao dao;

    @Test
	public void testAdd() throws ApiException {
		BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        dto.add(brandForm);

        BrandPojo brandPojo = dao.selectById(1);
        assertEquals("test brand 1", brandPojo.getBrand());
        assertEquals("test category 1", brandPojo.getCategory());
	}

    @Test
    public void testGet() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        dto.add(brandForm);

        BrandData brandData = dto.get(1);
        assertEquals("test brand 1", brandData.getBrand());
        assertEquals("test category 1", brandData.getCategory());
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        dto.add(brandForm);

        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
        dto.add(brandForm);

        List<BrandData> brandDataList = dto.getAll();

        assertEquals(2, brandDataList.size());

        int i = 1;

        for(BrandData brandData: brandDataList){
            assertEquals("test brand "+i, brandData.getBrand());
            assertEquals("test category "+i, brandData.getCategory());
            i++;
        }
    }

    @Test
    public void testUpdate() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        dto.add(brandForm);

        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
        dto.add(brandForm);

        List<BrandData> brandDataList = dto.getAll();

        assertEquals(2, brandDataList.size());

        int i = 1;

        for(BrandData brandData: brandDataList){
            assertEquals("test brand "+i, brandData.getBrand());
            assertEquals("test category "+i, brandData.getCategory());
            i++;
        }

        brandForm.setBrand("updated test brand 1");
        brandForm.setCategory("updated test category 1");
        dto.update(1, brandForm);

        BrandData brandData = dto.get(1);

        assertEquals("updated test brand 1", brandData.getBrand());
        assertEquals("updated test category 1", brandData.getCategory());
    }

    @Test
    public void testGetBrands() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        dto.add(brandForm);

        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
        dto.add(brandForm);

        List<String> brands = dto.getAllBrands();

        List<String> expectedList = new ArrayList<>();

        expectedList.add("test brand 1");
        expectedList.add("test brand 2");

        assertEquals(expectedList, brands);
    }

    @Test
    public void testGetAllCategories() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 1");
        dto.add(brandForm);

        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 2");
        dto.add(brandForm);

        List<String> categories = dto.getAllCategories();

        List<String> expectedList = new ArrayList<>();

        expectedList.add("test category 1");
        expectedList.add("test category 2");

        assertEquals(expectedList, categories);
    }

    @Test
    public void testGetCategories() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 11");
        dto.add(brandForm);

        brandForm.setBrand("test brand 1");
        brandForm.setCategory("test category 12");
        dto.add(brandForm);

        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 21");
        dto.add(brandForm);

        brandForm.setBrand("test brand 2");
        brandForm.setCategory("test category 22");
        dto.add(brandForm);

        List<String> categories = dto.getCategoriesByBrand("test brand 1");

        List<String> expectedList = new ArrayList<>();

        expectedList.add("test category 11");
        expectedList.add("test category 12");

        assertEquals(expectedList, categories);
    }

}
