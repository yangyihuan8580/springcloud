package com.yyh.park.service;

import com.yyh.park.dto.ParkDTO;
import com.yyh.park.entity.Park;

public interface ParkService {

    ParkDTO add(ParkDTO parkDTO);

    Park get(Long parkId);
}
