package com.example.quarkus;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Scope;
import org.jboss.logging.Logger;


public class Processor {
    private static final String FEATURE = "example-extension";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void exampleBean(Capabilities capabilities, BuildProducer<AdditionalBeanBuildItem> beans) {
        if (capabilities.isPresent("com.example.core")) {
        beans.produce(AdditionalBeanBuildItem.builder()
                .addBeanClass(ExampleService.class)
                .build());
        } else{
            Logger.getLogger(Process.class.getName()).warn("Capability com.example.core not found");
        }
    }
}