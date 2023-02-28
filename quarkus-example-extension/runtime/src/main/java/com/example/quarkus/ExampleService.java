package com.example.quarkus;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExampleService {

    public String hello(){
        return "world";
    }
}