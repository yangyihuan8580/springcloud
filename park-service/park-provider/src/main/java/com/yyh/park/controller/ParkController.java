package com.yyh.park.controller;

import com.alibaba.fastjson.JSON;
import com.yyh.cache.cache.service.CacheService;
import com.yyh.common.base.Result;
import com.yyh.common.web.ABaseController;
import com.yyh.common.web.IBaseController;
import com.yyh.common.web.IBaseService;
import com.yyh.config.ParkConfiguration;
import com.yyh.park.dto.ParkDTO;
import com.yyh.park.entity.Park;
import com.yyh.park.service.ParkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/park")
public class ParkController extends ABaseController<Park> implements IBaseController<Park> {

    private static final Logger logger = LoggerFactory.getLogger(ParkController.class);

    @Autowired
    private ParkConfiguration parkConfiguration;

    @Autowired
    private ParkService parkService;

    @Autowired
    private CacheService redisService;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;

    @RequestMapping(value = "{parkId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ParkDTO> get(@PathVariable("parkId") Long parkId) {
        ParkDTO parkDTO = new ParkDTO();
        Park park = redisService.get(String.valueOf(parkId));
        logger.info("redis:{}", JSON.toJSONString(park));
        if (park != null) {
            park = parkService.selectByPrimaryKey(parkId);
        }
        BeanUtils.copyProperties(park, parkDTO);
        parkDTO.setParkName(parkConfiguration.getParkName());
        Result success = Result.success(parkDTO);
        executor.execute(() -> {
                try {
                    logger.info("执行任务开始=========");
                    Thread.sleep(1000);
                    logger.info("执行任务结束=========");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        );

        return success;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<Park> add(@RequestBody Park park) {
        parkService.addSelective(park);
        redisService.set(String.valueOf(park.getId()), park, 10, TimeUnit.SECONDS);
        return Result.success(park);
    }


    @RequestMapping(value = "timeout")
    public Result<ParkDTO> timeout() throws InterruptedException {
        Thread.sleep(5000);
        return Result.SUCCESS;
    }

    @Override
    protected IBaseService<Park> getBaseService() {
        return parkService;
    }
}
