package com.yyh.park.controller;


import com.yyh.common.base.BaseRequest;
import com.yyh.common.base.Result;
import com.yyh.elastic.park.repository.ParkRecordRepository;
import com.yyh.park.entity.ParkRecord;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/parkRecord")
public class ParkRecordController {

    @Autowired
    private ParkRecordRepository parkRecordRepository;

    /**
     * es的示例  https://www.cnblogs.com/sbj-dawn/archive/2018/04/20/8891419.html
     *          https://www.cnblogs.com/sbj-dawn/p/8891526.html
     * @param req
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<ParkRecord>> query(@RequestBody BaseRequest<ParkRecord> req) {
        @Valid ParkRecord data = req.getData();


        QueryBuilder queryBuilder = null;
        /** 匹配全部文档 */
//        queryBuilder = QueryBuilders.matchAllQuery();
        /** 单个匹配，前缀匹配 */
//        queryBuilder = QueryBuilders.matchQuery("plateNumber", data.getPlateNumber());
        /** 单个匹配，前缀匹配 */
//        queryBuilder = QueryBuilders.multiMatchQuery(data.getPlateNumber(), "plateNumber", "carColor");
        /** 模糊匹配 */
        queryBuilder = QueryBuilders.wildcardQuery("plateNumber",new StringBuilder().append("*").append(data.getPlateNumber()).append("*").toString());//搜索名字中含有jack文档（name中只要包含jack即可）

        /** 复合查询
         * must 相当于 &&
         * should 相当于 ||
         */
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        QueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("carColor",new StringBuilder().append("*").append(data.getCarColor()).append("*").toString());//搜索名字中含有jack文档（name中只要包含jack即可）
//        boolQueryBuilder.must(queryBuilder);
//        boolQueryBuilder.must(queryBuilder1);

        /** 范围查询 */
//        queryBuilder = QueryBuilders.rangeQuery("id")
//                .from(123)
//                .to(500)
//                .includeLower(true)     // 包含上界
//                .includeUpper(true);    // 包含下界

        /** query String 搜索  多个值用空格分开 */
        Map<String,Float> fields = new HashMap<>(2);
        fields.put("plateNumber", AbstractQueryBuilder.DEFAULT_BOOST);
        fields.put("carColor", AbstractQueryBuilder.DEFAULT_BOOST);
        queryBuilder = QueryBuilders
                .queryStringQuery(new StringBuilder()
                        .append("*")
                        .append(data.getPlateNumber())
                        .append(" ")
                        .append(data.getCarColor())
                        .toString())
                .defaultOperator(Operator.OR)
                .fields(fields)
                .fuzzyMaxExpansions(50);
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
