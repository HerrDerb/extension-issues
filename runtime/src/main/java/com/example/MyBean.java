package com.example;

import jakarta.enterprise.event.Observes;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@TenantScoped
public class MyBean {

    @Setter
    private int value = -1;

    private List<Object> received = new ArrayList<>();

    void observe(@Observes Object any) {
        received.add(any);
    }

    public int getValue() {
        try {
            return value;
        } finally {
            value = -1;
        }
    }

    public List<Object> getReceived() {
        try {
            return received;
        } finally {
            received.clear();
        }
    }

}