package com.example;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
public class TenantContextInfo {
    
    @Getter
    @Setter
    private String tenant;
}
