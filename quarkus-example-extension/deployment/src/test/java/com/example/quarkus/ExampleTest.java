package com.example.quarkus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class ExampleTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest();

    @Inject
    ExampleService exampleService;

    @Test
    void test() {
        // should not fail
        var world = exampleService.hello();
        assertEquals("world", world);
    }
}