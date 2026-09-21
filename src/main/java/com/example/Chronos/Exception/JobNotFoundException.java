package com.example.Chronos.Exception;

import java.util.UUID;

public class JobNotFoundException extends RuntimeException{

    //constructor
    public JobNotFoundException(UUID id){
        super("No Job found with id: " + id);
    }


}
