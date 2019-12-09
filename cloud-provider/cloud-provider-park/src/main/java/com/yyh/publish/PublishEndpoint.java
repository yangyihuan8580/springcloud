package com.yyh.publish;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;

import org.apache.juli.logging.Log;
import org.apache.tomcat.util.net.*;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "endpoints.publish")
public class PublishEndpoint implements ApplicationListener<ApplicationPreparedEvent> {

    private static final Map<String, Object> NO_CONTEXT_MESSAGE = Collections
            .unmodifiableMap(Collections.<String, String>singletonMap("message",
                    "No context to publish."));

    /**
     * 等待多时秒
     */
    @Getter
    @Setter
    private Integer waitSeconds;

    public PublishEndpoint() {
    }


    private ConfigurableApplicationContext context;

    public Map<String, Object> invoke() {
        if (this.context == null) {
            return NO_CONTEXT_MESSAGE;
        }
        try {
            if(null == waitSeconds) {
                waitSeconds = 10;
            }
            Map<String, Object> shutdownMessage = Collections
                    .unmodifiableMap(Collections.<String, Object>singletonMap("message", "Service will exit after "+waitSeconds+" seconds"));
            return shutdownMessage;
        }
        finally {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PublishEndpoint.this.context.stop();
                        Thread.sleep(waitSeconds * 1000);
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    PublishEndpoint.this.context.close();
                }
            });
            thread.setContextClassLoader(getClass().getClassLoader());
            thread.start();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent input) {
        if (this.context == null) {
            this.context = input.getApplicationContext();
        }
    }
}