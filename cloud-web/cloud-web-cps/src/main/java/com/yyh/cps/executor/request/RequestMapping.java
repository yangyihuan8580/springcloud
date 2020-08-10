package com.yyh.cps.executor.request;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.yyh.cps.executor.TcpMessageService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component
public class RequestMapping {

    private static final Logger logger = LoggerFactory.getLogger(RequestMapping.class);
    private static Map<String, Class<?>> requestMapping = null;

    static {
        Map<String, Class<?>> mapping = Maps.newConcurrentMap();
        Reflections reflections = new Reflections("com.yyh.cps.executor.request");
        Set<Class<? extends TcpMessageService>> annotated = reflections.getSubTypesOf(TcpMessageService.class);
        Iterator<Class<? extends TcpMessageService>> iterator = annotated.iterator();
        while (iterator.hasNext()) {
            Class<?> next = iterator.next();
            Request requestCode = next.getAnnotation(Request.class);
            if (requestCode != null) {
                if (mapping.containsKey(requestCode.code())) {
                    logger.error("HTTP接口初始化失败！", new IllegalArgumentException(requestCode.code() + "设置重复 "
                            + next.getName() + " <=conflict=> " + mapping.get(requestCode.code()).getName()));
                    System.exit(0);
                }
                mapping.put(requestCode.code(), next);
            }
        }
        requestMapping = ImmutableMap.copyOf(mapping);
        logger.info(requestMapping.size() + " subClasses found for BaseRequest.class");
        logger.info("key sets are " + JSON.toJSONString(requestMapping.keySet()));
    }

    public static Class<?> getClassByCode(String code) {
        return requestMapping.get(code);
    }

    public static boolean containsKey(String key) {
        return requestMapping.containsKey(key);
    }
}
