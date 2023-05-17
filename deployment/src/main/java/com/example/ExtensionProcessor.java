package com.example;

import io.quarkus.arc.deployment.ContextRegistrationPhaseBuildItem;
import io.quarkus.arc.deployment.ContextRegistrationPhaseBuildItem.ContextConfiguratorBuildItem;
import io.quarkus.arc.deployment.CustomScopeBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtensionProcessor {

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem("example");
    }

    @BuildStep
    ContextConfiguratorBuildItem registerContext(ContextRegistrationPhaseBuildItem contextRegistrationPhase) {
        log.info("### Registering context");
        return new ContextConfiguratorBuildItem(
                contextRegistrationPhase.getContext().configure(TenantScoped.class)
                        .contextClass(TenantScopedContext.class));
    }

    @BuildStep
    CustomScopeBuildItem customScope() {
        return new CustomScopeBuildItem(TenantScoped.class);
    }
}
