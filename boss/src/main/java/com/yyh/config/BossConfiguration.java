package com.yyh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Data
public class BossConfiguration {

    @Value("${is_need_verify}")
    private boolean needVerify;

}
