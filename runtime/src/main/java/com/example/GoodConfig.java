package com.example;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithParentName;
import java.util.Map;

@ConfigMapping(prefix = "my.props")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface GoodConfig {

    // Use an additional wrapper makes the whole thing working.
    
    /**
     * @return
     */
    @WithParentName
    Map<String, NestedProperties> nested();

    public interface Wrapper{

        /**
         * @return
         */
        NestedProperties sub();
    }

    public interface NestedProperties {

        /**
         * @return
         */
        int id();
    }

    
}
