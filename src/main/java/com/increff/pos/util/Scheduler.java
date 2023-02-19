package com.increff.pos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.PosDaySalesDto;

@Component
public class Scheduler {

    @Autowired
    private PosDaySalesDto posDaySalesDto;

    @Scheduled(cron = "59 59 23 * * *")
    public void scheduler() throws ApiException {
        posDaySalesDto.create();
    }
}
