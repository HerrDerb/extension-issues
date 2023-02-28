package com.example;

import io.quarkus.runtime.StartupEvent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;

public class MyBean {

    @Inject
    BadConfig badconfig;
    @Inject
    GoodConfig goodConfig;


    void init(@Observes StartupEvent ev){
        int size = badconfig.nested().size();
        LoggerFactory.getLogger(getClass()).info("Config count: {}", size);
        if(size == 0){
            throw new RuntimeException("Empty config map");
        }
    }
    
}
