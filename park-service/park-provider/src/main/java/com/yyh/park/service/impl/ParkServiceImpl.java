package com.yyh.park.service.impl;

import com.yyh.common.util.SnowFlakeStrategy;
import com.yyh.park.dto.ParkDTO;
import com.yyh.park.entity.Park;
import com.yyh.park.service.ParkService;
import org.springframework.stereotype.Service;

@Service(value = "parkService")
public class ParkServiceImpl implements ParkService {


    @Override
    public ParkDTO add(ParkDTO parkDTO) {
        parkDTO.setParkId(SnowFlakeStrategy.getPrimaryId());
        return parkDTO;
    }

    @Override
    public Park get(Long parkId) {
        Park park = new Park();
        park.setParkId(parkId);
        return park;
    }

}
