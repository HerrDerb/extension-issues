package com.example.quarkus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.QuarkusUnitTest;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class ExampleTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest();

    @Inject
    Instance<ExampleService> exampleService;

    @Test
    void test() {
      var exampleServiceCount =  exampleService.stream().count();
      assertEquals(1, exampleServiceCount, "Expected exactly one ExampleService instance");
    }
}