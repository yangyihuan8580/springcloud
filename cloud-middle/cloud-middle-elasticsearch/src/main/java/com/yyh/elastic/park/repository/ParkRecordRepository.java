package com.yyh.elastic.park.repository;

import com.yyh.park.entity.ParkRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ParkRecordRepository extends ElasticsearchRepository<ParkRecord, Long> {
}
