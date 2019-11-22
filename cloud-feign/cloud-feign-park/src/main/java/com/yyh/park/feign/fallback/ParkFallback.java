package com.yyh.park.feign.fallback;

import com.yyh.common.base.Result;
import com.yyh.park.dto.ParkDTO;
import com.yyh.park.feign.ParkFeignClient;
import org.springframework.stereotype.Component;

@Component
public class ParkFallback implements ParkFeignClient {

    @Override
    public Result<ParkDTO> add(ParkDTO parkDTO) {
        return Result.ERROR;
    }

    @Override
    public Result<ParkDTO> selectParkById(Long parkId) {
        return Result.ERROR;
    }
}
