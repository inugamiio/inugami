/* --------------------------------------------------------------------
 *  Inugami
 * --------------------------------------------------------------------
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.inugami.framework.api.listeners;

import io.inugami.framework.api.loggers.mdc.initializer.MdcInitializer;
import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.api.tools.RunSafeUtils;
import io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultApplicationLifecycleSPI implements ApplicationLifecycleSPI {
    private static final Map<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> LISTENERS =
            new ConcurrentHashMap<>();
    public static final  String                                                                 STARTING  = "STARTING";
    public static final  String                                                                 UP        = "UP";
    public static final  String                                                                 FAIL      = "FAIL";

    public static void register(ApplicationLifecycleSPI listener) {
        if (listener != null) {
            LISTENERS.put(listener.getClass(), listener);
        }
    }

    public static void unregister(Class<? extends ApplicationLifecycleSPI> listenerClass) {
        LISTENERS.remove(listenerClass);
    }

    @Override
    public void onApplicationStarting(final Object event) {
        MdcService.getInstance().lifecycle(STARTING);
        Loggers.BOOTSTRAP.info("application starting...");
        MdcService.getInstance().lifecycleRemove();
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onApplicationStarting(event);
        }

    }

    @Override
    public void onEnvironmentPrepared(final Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onEnvironmentPrepared(event);
        }
    }

    @Override
    public void onApplicationContextInitialized(final Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onApplicationContextInitialized(event);
        }
    }

    @Override
    public void onApplicationPrepared(final Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onApplicationPrepared(event);
        }
    }

    @Override
    public void onbWebServerInitialized(Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onbWebServerInitialized(event);
        }
    }

    @Override
    public void onContextRefreshed(Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onContextRefreshed(event);
        }
    }

    @Override
    public void onApplicationStarted(Object event) {
        MdcInitializer.initialize();
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onApplicationStarted(event);
        }

    }

    @Override
    public void onAvailabilityChange(Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onAvailabilityChange(event);
        }
    }

    @Override
    public void onApplicationReady(Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            entry.getValue().onApplicationReady(event);
        }
        MdcService.getInstance().lifecycle(UP);
        Loggers.BOOTSTRAP.info("application up");
        MdcService.getInstance().lifecycleRemove();
    }


    @Override
    public void onApplicationFail(final Object event) {
        for (Map.Entry<Class<? extends ApplicationLifecycleSPI>, ApplicationLifecycleSPI> entry : LISTENERS.entrySet()) {
            RunSafeUtils.runSafeVoid(() -> entry.getValue().onApplicationFail(event));
        }
        MdcService.getInstance().lifecycle(FAIL);
        Loggers.BOOTSTRAP.error("application fail");
        MdcService.getInstance().lifecycleRemove();
        RunSafeUtils.runSafeVoid(() -> Thread.sleep(1000));
    }
}
