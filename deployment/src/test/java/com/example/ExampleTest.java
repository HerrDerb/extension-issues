package com.example;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.security.TestIdentityAssociation;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.TestSecurityIdentityAugmentor;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExampleTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest();

    @Inject
    SecurityIdentity securityIdentity;

    @Test
    @ActivateRequestContext
    @TestSecurity(user = "testUser", roles = "testRole")
    @Order(1)
    void whenWithTestSecurityAnnotation_shouldHaveSecurityIdentity() {
        assertEquals(securityIdentity.getPrincipal().getName(),"testUser");
        assertTrue( securityIdentity.hasRole("testRole"));
    }

    @Test
    @ActivateRequestContext
    @Order(2)
    void whenProgrammatically_shouldHaveSecurityIdentity() {
        withUserRole(Set.of("testRole"));

        assertEquals(securityIdentity.getPrincipal().getName(),"testUser");
        assertTrue( securityIdentity.hasRole("testRole"));
    }

    private void withUserRole(Set<String> userRoles) {
        QuarkusSecurityIdentity identity = QuarkusSecurityIdentity.builder()
                .setPrincipal(new QuarkusPrincipal("testUser"))
                .addRoles(userRoles).build();
        Instance<TestSecurityIdentityAugmentor> producer = CDI.current().select(TestSecurityIdentityAugmentor.class);
        if (producer.isResolvable()) {
        CDI.current().select(TestIdentityAssociation.class).get().setTestIdentity(producer.get().augment(identity, new Annotation[]{}));
        }

        CDI.current().select(TestIdentityAssociation.class).get().setTestIdentity(identity);
    }
}
