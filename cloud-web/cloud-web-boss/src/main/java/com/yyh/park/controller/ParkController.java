package com.yyh.park.controller;

import com.yyh.common.base.BaseRequest;
import com.yyh.common.base.Result;
import com.yyh.config.BossConfiguration;
import com.yyh.park.dto.ParkDTO;
import com.yyh.park.feign.ParkFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "ParkController")
@RestController
@RequestMapping("/park")
@Validated
public class ParkController {

    private static final Logger logger = LoggerFactory.getLogger(ParkController.class);

    @Autowired
    private ParkFeignClient parkFeignClient;

    @Autowired
    private BossConfiguration bossConfiguration;

    @ApiOperation(value = "查询车场")
    @ApiImplicitParam(name = "parkId", value = "车场id", required = true, dataType = "Long", example = "0")
    @GetMapping(value = "{parkId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ParkDTO> selectParkById(@PathVariable("parkId") Long parkId) {
        return parkFeignClient.selectParkById(parkId);
    }

    @ApiOperation(value = "添加车场")
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ParkDTO> add(@Valid @RequestBody BaseRequest<ParkDTO> parkDTO) {
        return parkFeignClient.add(parkDTO.getData());
    }


    @GetMapping(value = "timeout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Object> test() throws InterruptedException {
        boolean needVerify = bossConfiguration.isNeedVerify();
        logger.info("needVerify:{}", needVerify);
        Thread.sleep(5000);
        return Result.SUCCESS;
    }
}
