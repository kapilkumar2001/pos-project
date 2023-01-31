package com.increff.employee.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.BrandCategoryData;
import com.increff.employee.model.BrandCategoryForm;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;

public class brandCategoryDtoTest extends AbstractUnitTest{

    @Autowired
    private BrandCategoryDto brandCategoryDto;

    @Test
	public void testAdd() throws ApiException {
		BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);
	}

    @Test
    public void testGet() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        BrandCategoryData brandCategoryData = brandCategoryDto.get(1);
        assertEquals("test brand 1", brandCategoryData.getBrand());
        assertEquals("test category 1", brandCategoryData.getCategory());
    }

    @Test
    public void testGetAll() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 2");
        brandCategoryDto.add(brandCategoryForm);

        List<BrandCategoryData> brandCategoryDataList = brandCategoryDto.getAll();

        assertEquals(2, brandCategoryDataList.size());

        int i = 1;

        for(BrandCategoryData brandCategoryData: brandCategoryDataList){
            assertEquals("test brand "+i, brandCategoryData.getBrand());
            assertEquals("test category "+i, brandCategoryData.getCategory());
            i++;
        }
    }

    @Test
    public void testDelete() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 2");
        brandCategoryDto.add(brandCategoryForm);

        List<BrandCategoryData> brandCategoryDataList = brandCategoryDto.getAll();

        assertEquals(2, brandCategoryDataList.size());

        int i = 1;

        for(BrandCategoryData brandCategoryData: brandCategoryDataList){
            assertEquals("test brand "+i, brandCategoryData.getBrand());
            assertEquals("test category "+i, brandCategoryData.getCategory());
            i++;
        }

        brandCategoryDto.delete(1);

        brandCategoryDataList = brandCategoryDto.getAll();

        assertEquals(1, brandCategoryDataList.size());

        i = 2;

        for(BrandCategoryData brandCategoryData: brandCategoryDataList){
            assertEquals("test brand "+i, brandCategoryData.getBrand());
            assertEquals("test category "+i, brandCategoryData.getCategory());
            i++;
        }
    }


    @Test
    public void testUpdate() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 2");
        brandCategoryDto.add(brandCategoryForm);

        List<BrandCategoryData> brandCategoryDataList = brandCategoryDto.getAll();

        assertEquals(2, brandCategoryDataList.size());

        int i = 1;

        for(BrandCategoryData brandCategoryData: brandCategoryDataList){
            assertEquals("test brand "+i, brandCategoryData.getBrand());
            assertEquals("test category "+i, brandCategoryData.getCategory());
            i++;
        }

        brandCategoryForm.setBrand("updated test brand 1");
        brandCategoryForm.setCategory("updated test category 1");
        brandCategoryDto.update(1, brandCategoryForm);

        BrandCategoryData brandCategoryData = brandCategoryDto.get(1);

        assertEquals("updated test brand 1", brandCategoryData.getBrand());
        assertEquals("updated test category 1", brandCategoryData.getCategory());
    }

    @Test
    public void testGetBrands() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 2");
        brandCategoryDto.add(brandCategoryForm);

        List<String> brands = brandCategoryDto.getBrands();

        List<String> expectedList = new ArrayList<>();

        expectedList.add("test brand 1");
        expectedList.add("test brand 2");

        assertEquals(expectedList, brands);
    }

    @Test
    public void testGetAllCategories() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 1");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 2");
        brandCategoryDto.add(brandCategoryForm);

        List<String> categories = brandCategoryDto.getAllCategories();

        List<String> expectedList = new ArrayList<>();

        expectedList.add("test category 1");
        expectedList.add("test category 2");

        assertEquals(expectedList, categories);
    }

    @Test
    public void testGetCategories() throws ApiException{
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 11");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 1");
        brandCategoryForm.setCategory("test category 12");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 21");
        brandCategoryDto.add(brandCategoryForm);

        brandCategoryForm.setBrand("test brand 2");
        brandCategoryForm.setCategory("test category 22");
        brandCategoryDto.add(brandCategoryForm);

        List<String> categories = brandCategoryDto.getCategories("test brand 1");

        List<String> expectedList = new ArrayList<>();

        expectedList.add("test category 11");
        expectedList.add("test category 12");

        assertEquals(expectedList, categories);
    }

}
