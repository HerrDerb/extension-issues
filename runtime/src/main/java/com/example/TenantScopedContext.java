package com.example;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ContextInstanceHandle;
import io.quarkus.arc.InjectableBean;
import io.quarkus.arc.InjectableContext;
import io.quarkus.arc.impl.ContextInstanceHandleImpl;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantScopedContext implements InjectableContext {

    private final Map<Contextual<?>, Map<String,  ContextInstanceHandle<?>>> contextualMap = new ConcurrentHashMap<>();

    @Override
    public void destroy(Contextual<?> contextual) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return TenantScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        String tenant = Arc.container().select(TenantContextInfo.class).get().getTenant();
        if (creationalContext == null) {
            return null;
        }
        var tenantMap = contextualMap.computeIfAbsent(contextual, t -> new HashMap<>());
        tenantMap.computeIfAbsent(tenant, c -> {
            log.info("### Create new InstanceHandle for {}", contextual.getClass().getName());
            T createdInstance = contextual.create(creationalContext);
            ContextInstanceHandle<T> contextInstanceHandle = new ContextInstanceHandleImpl<T>(
                    (InjectableBean<T>) contextual, createdInstance, creationalContext);
            return contextInstanceHandle;

        });
        return get(contextual);
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        String tenant = Arc.container().select(TenantContextInfo.class).get().getTenant();
        log.info("### Get InstanceHandle for {}", contextual.getClass().getName());
        if (contextualMap.containsKey(contextual) && contextualMap.get(contextual).containsKey(tenant)) {
            return (T) contextualMap.get(contextual).get(tenant).get();
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return Arc.container().requestContext().isActive() && Arc.container().select(TenantContextInfo.class).get().getTenant() != null;
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContextState getState() {
        throw new UnsupportedOperationException();
    }

}
