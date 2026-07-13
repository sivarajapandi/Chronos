package com.example.Chronos.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class printMessageHandler implements JobHandler{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getJobType(){
        return "printMessage";
    }


    @Override
    public void validate(String payload){
        try{
            JsonNode node = objectMapper.readTree(payload);

            if(!node.has("message")){
                throw new IllegalArgumentException("payload must contains message");
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid payload format", e);
        }
    }

    @Override
    public void execute(String payload){
        try{
            JsonNode node = objectMapper.readTree(payload);
            String message = node.get("message").asText();
            System.out.println("Executing printMessage job: "+message);
        }catch (Exception e){
            throw new RuntimeException("Failed to execute printMessage job", e);
        }

    }
}
