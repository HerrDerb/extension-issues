package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.arc.Arc;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class ScopedBeanTest {

    @RegisterExtension
    static final QuarkusUnitTest config =
            new QuarkusUnitTest().withEmptyApplication();

            @Inject
            private MyBean myBean;
        
            @Test
            void test_Atomic(){
             assertTrue(myBean.get());
            }
        
            @Test
            void test_other(){
                assertTrue(myBean.getWrappedBoolean());
            }
    
   @ApplicationScoped
    public static  class MyBean {

        @Inject
        AtomicBoolean booleanBean;
        @Inject
        OtherBean otherBean;

        public boolean get() {
            return booleanBean.get();
        }

        public boolean getWrappedBoolean() {
            return otherBean.isValue();
        }

        @MyContextScoped
        AtomicBoolean scopedBean() {
            return new AtomicBoolean();
        }

        @MyContextScoped
        OtherBean otherBean() {
            return new OtherBean();
        }

    }

}