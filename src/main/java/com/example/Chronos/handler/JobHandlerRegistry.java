package com.example.Chronos.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobHandlerRegistry {

    //while initiating, spring checks for the components (handlers) and injects them into the constructor of this class, so we can use them to populate the map
    private final Map<String,JobHandler> handlerMap = new HashMap<>();

    public JobHandlerRegistry(List<JobHandler> handlers){

        for(JobHandler handler:handlers){
            handlerMap.put(handler.getJobType(),handler);
            System.out.println(
                    "Registered handler: " + handler.getJobType());
        }
    }

    public JobHandler getHandler(String type){
        JobHandler handler=handlerMap.get(type);

        if(handler==null){
            throw new IllegalArgumentException("No handler found for job type: "+type);
        }
        return handler;
    }
}
