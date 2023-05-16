package com.example;

import io.quarkus.arc.ContextInstanceHandle;
import io.quarkus.arc.InjectableBean;
import io.quarkus.arc.InjectableContext;
import io.quarkus.arc.impl.ContextInstanceHandleImpl;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyContext implements InjectableContext {

    private final Map<Contextual<?>, ContextInstanceHandle<?>> contextualMap = new ConcurrentHashMap<>();

    @Override
    public void destroy(Contextual<?> contextual) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return MyContextScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        if (creationalContext == null) {
            return null;
        }
        contextualMap.computeIfAbsent(contextual, c -> {
            log.info("### Create new InstanceHandle for {}", contextual.getClass().getName());
            T createdInstance = contextual.create(creationalContext);
            ContextInstanceHandle<T> contextInstanceHandle = new ContextInstanceHandleImpl<T>(
                    (InjectableBean<T>) contextual, createdInstance, creationalContext);
            return contextInstanceHandle;

        });
        T bean = get(contextual);
        if( bean instanceof AtomicBoolean){
            ((AtomicBoolean)bean).set(true);
        }
        if( bean instanceof OtherBean){
            ((OtherBean)bean).setValue(true);
        }
        return bean;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        log.info("### Get InstanceHandle for {}", contextual.getClass().getName());
        if (contextualMap.containsKey(contextual)) {
            return (T) contextualMap.get(contextual).get();
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
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
