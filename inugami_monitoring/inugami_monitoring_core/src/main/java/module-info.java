import io.inugami.framework.interfaces.exceptions.ExceptionHandlerMapper;
import io.inugami.framework.interfaces.exceptions.ExceptionResolver;
import io.inugami.framework.interfaces.monitoring.Interceptable;
import io.inugami.framework.interfaces.monitoring.ResponseListener;

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
module io.inugami.monitoring.core {
    requires io.inugami.framework.commons;
    requires io.inugami.monitoring.api;
    requires io.inugami.framework.api;
    requires io.inugami.framework.interfaces;
    requires lombok;
    requires jakarta.servlet;
    requires org.slf4j;

    exports io.inugami.monitoring.core.context;
    exports io.inugami.monitoring.core.context.sensors;
    exports io.inugami.monitoring.core.interceptable;
    exports io.inugami.monitoring.core.interceptors;
    exports io.inugami.monitoring.core.interceptors.exceptions;
    exports io.inugami.monitoring.core.interceptors.mdc;
    exports io.inugami.monitoring.core.obfuscators;
    exports io.inugami.monitoring.core.sensors;
    exports io.inugami.monitoring.core.sensors.aggregators;
    exports io.inugami.monitoring.core.spi;

    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
    uses ExceptionResolver;
    uses ExceptionHandlerMapper;
    uses io.inugami.framework.interfaces.monitoring.FilterInterceptorCachePurgeStrategy;
    uses io.inugami.framework.interfaces.monitoring.MdcCleanerSPI;
    uses Interceptable;
    uses ResponseListener;
    uses io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;
    uses io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
    uses io.inugami.framework.interfaces.exceptions.WarningTracker;

    provides io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI with io.inugami.monitoring.core.interceptors.FilterInterceptor;
    provides io.inugami.framework.interfaces.monitoring.Interceptable with io.inugami.monitoring.core.spi.H2Interceptable;
    provides io.inugami.framework.interfaces.exceptions.WarningTracker with io.inugami.monitoring.core.spi.DefaultWarningTracker;
    provides io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor with io.inugami.monitoring.core.spi.IoLogInterceptor, io.inugami.monitoring.core.spi.ServiceCounterInterceptor, io.inugami.monitoring.core.spi.MdcInterceptor, io.inugami.monitoring.core.spi.CorsInterceptable;
}