package com.yyh.park.controller;

import com.alibaba.fastjson.JSON;
import com.yyh.common.base.Result;
import com.yyh.park.dto.ParkDTO;
import com.yyh.park.entity.Park;
import com.yyh.park.service.ParkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RefreshScope
@RequestMapping("/park")
public class ParkController {

    private static final Logger logger = LoggerFactory.getLogger(ParkController.class);

    @Value("${parkName}")
    private String parkName;

    @Autowired
    private ParkService parkService;

    @RequestMapping(value = "{parkId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ParkDTO> get(@PathVariable("parkId") Long parkId) {
        ParkDTO parkDTO = new ParkDTO();
        Park park = parkService.get(parkId);
        BeanUtils.copyProperties(park, parkDTO);
        parkDTO.setParkName(parkName);
        Result success = Result.success(parkDTO);
        logger.info("return:{}", JSON.toJSONString(success));
        return success;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ParkDTO> add(@RequestBody ParkDTO parkDTO) {
        parkDTO = parkService.add(parkDTO);
        return Result.success(parkDTO);
    }

    @RequestMapping(value = "timeout")
    public Result<ParkDTO> timeout() throws InterruptedException {
        Thread.sleep(5000);
        return Result.SUCCESS;
    }

}
