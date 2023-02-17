package com.increff.pos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.dto.PosDaySalesDto;
import com.increff.pos.service.ApiException;

@Component
public class Scheduler {

    @Autowired
    private PosDaySalesDto posDaySalesDto;

    @Scheduled(cron = "00 36 13 * * *")
    public void scheduler() throws ApiException {
        posDaySalesDto.create();
    }
}
