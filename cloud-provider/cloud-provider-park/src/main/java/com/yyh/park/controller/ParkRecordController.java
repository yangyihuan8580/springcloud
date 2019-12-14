package com.yyh.park.controller;


import com.yyh.common.base.BaseRequest;
import com.yyh.common.base.Result;
import com.yyh.elastic.park.repository.ParkRecordRepository;
import com.yyh.park.entity.ParkRecord;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/parkRecord")
public class ParkRecordController {

    @Autowired
    private ParkRecordRepository parkRecordRepository;

    @RequestMapping(value = "query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<ParkRecord>> query(@RequestBody BaseRequest<ParkRecord> req) {
        @Valid ParkRecord data = req.getData();

        QueryBuilder queryBuilder = new TermQueryBuilder("plateNumber", data.getPlateNumber());
//        QueryBuilder queryBuilder = new CommonTermsQueryBuilder("plateNumber", data.getPlateNumber());
//        QueryBuilder queryBuilder = new QueryStringQueryBuilder(data.getPlateNumber()).field("plateNumber");
        Iterable<ParkRecord> search = parkRecordRepository.search(queryBuilder);
        return Result.success(search);
    }


    @RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<ParkRecord> save(@RequestBody BaseRequest<ParkRecord> req) {
        @Valid ParkRecord data = req.getData();
        parkRecordRepository.save(data);
        log.info("插入es成功=============================");
        return Result.success(data);
    }
}
