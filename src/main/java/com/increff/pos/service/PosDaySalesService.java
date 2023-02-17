package com.increff.pos.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.PosDaySalesDao;
import com.increff.pos.pojo.PosDaySalesPojo;

@Service
public class PosDaySalesService {

    @Autowired 
    private PosDaySalesDao dao;
    
    @Transactional(rollbackOn = ApiException.class)
    public void create(PosDaySalesPojo posDaySalesPojo) throws ApiException{
        dao.insert(posDaySalesPojo);
    }

    @Transactional
    public List<PosDaySalesPojo> getAll(){
        return dao.selectAll();
    }

    @Transactional
    public List<PosDaySalesPojo> getByDate(LocalDate startDate, LocalDate endDate){
        return dao.selectByDate(startDate, endDate);
    }
    
}
