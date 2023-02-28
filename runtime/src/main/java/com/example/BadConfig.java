package com.example;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;
import java.util.Map;

@ConfigMapping(prefix = "my.props")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface BadConfig {

    /**
     * @return
     */
    @WithParentName
    Map<String, NestedProperties> nested();

    public interface NestedProperties {

        /**
         * @return
         */
        @WithName("sub.id")
        int id();
    }

}
