package com.yyh.park.feign;

import com.yyh.common.base.Result;
import com.yyh.park.dto.ParkDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "park-provider")
public interface ParkFeignClient {

    @RequestMapping(value = "/park", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<ParkDTO> add(ParkDTO parkDTO);

    @RequestMapping(value = "/park/{parkId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result<ParkDTO> selectParkById(@PathVariable("parkId") Long parkId);
}
