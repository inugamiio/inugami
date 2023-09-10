package io.inugami.api.listeners;

import io.inugami.api.processors.ConfigHandler;

public interface ApplicationLifecycleSPI {
    default void onApplicationStarting(Object event){
    }

    default void onEnvironmentPrepared(Object event){
    }

    default void onConfigurationReady(final ConfigHandler<String, String> configuration){
    }

    default void onApplicationContextInitialized(Object event){
    }

    default void onApplicationPrepared(Object event){
    }

    default void onbWebServerInitialized(Object event){
    }


    default void onContextRefreshed(Object event){
    }
    default void onApplicationStarted(Object event){
    }
    default void onAvailabilityChange(Object event){
    }
    default void onApplicationReady(Object event){
    }

    default void onApplicationFail(Object event){
    }
}
