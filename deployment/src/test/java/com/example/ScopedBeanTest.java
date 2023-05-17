package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.arc.Arc;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class ScopedBeanTest {

    private final String TENANT_A = "tenant-a";
    private final String TENANT_B = "tenant-b";

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest().withEmptyApplication();

    @Inject
    Event<Object> event;

    @Inject
    MyBean myBean;

    @Inject
    TenantContextInfo tenantContextInfo;

    @BeforeEach
    void setup() {
        Arc.container().requestContext().terminate();
    }

    @Test
    void tenantScopedcontext_whenSameTenant_shouldHaveSetValue() {
        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_A);
        myBean.setValue(1);
        Arc.container().requestContext().terminate();

        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_A);
        var value = myBean.getValue();
        assertEquals(1, value);
    }

    @Test
    void tenantScopedcontext_whenOtherTenant_shouldHaveInitialValue() {
        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_A);
        myBean.setValue(1);
        Arc.container().requestContext().terminate();

        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_B);
        var value = myBean.getValue();
        assertEquals(-1, value);
    }

    @Test
    void tenantScopedObserve_whenFireForTenantA_shouldNotReceiveForTenantA() {
        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_A);
        var object = new Object();

        event.fire(object);

        var objects = myBean.getReceived();
        assertEquals(List.of(object), objects);
    }

    @Test
    void tenantScopedObserve_whenFireForTenantA_shouldNotReceiveForTenantB() {
        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_A);
        var object = new Object();

        event.fire(object);
        Arc.container().requestContext().terminate();

        Arc.container().requestContext().activate();
        tenantContextInfo.setTenant(TENANT_B);
        var objects = myBean.getReceived();
        assertEquals(List.of(), objects);
    }

}