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
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.interfaces;
    requires io.inugami.monitoring.api;
    requires jakarta.servlet;
    requires lombok;
    requires org.slf4j;
    requires spring.webmvc;

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

    uses ExceptionHandlerMapper;
    uses ExceptionResolver;
    uses Interceptable;
    uses ResponseListener;
    uses io.inugami.framework.interfaces.exceptions.WarningTracker;
    uses io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI;
    uses io.inugami.framework.interfaces.monitoring.FilterInterceptorCachePurgeStrategy;
    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodResolver;
    uses io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker;
    uses io.inugami.framework.interfaces.monitoring.MdcCleanerSPI;
    uses io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor;

    provides io.inugami.framework.interfaces.exceptions.ExceptionHandlerMapper with io.inugami.monitoring.core.interceptors.exceptions.DefaultExceptionMapper;
    provides io.inugami.framework.interfaces.exceptions.WarningTracker with io.inugami.monitoring.core.spi.DefaultWarningTracker;
    provides io.inugami.framework.interfaces.listeners.ApplicationLifecycleSPI with io.inugami.monitoring.core.interceptors.FilterInterceptor;
    provides io.inugami.framework.interfaces.monitoring.Interceptable with io.inugami.monitoring.core.spi.H2Interceptable;
    provides io.inugami.framework.interfaces.monitoring.JavaRestMethodTracker with io.inugami.monitoring.core.spi.DefaultJavaRestMethodTracker;
    provides io.inugami.framework.interfaces.monitoring.MdcCleanerSPI with io.inugami.monitoring.core.interceptors.mdc.DefaultMdcCleaner;
    provides io.inugami.framework.interfaces.monitoring.Obfuscator with io.inugami.monitoring.core.obfuscators.AuthorizationObfuscator, io.inugami.monitoring.core.obfuscators.TokenObfuscator, io.inugami.monitoring.core.obfuscators.PasswordObfuscator;
    provides io.inugami.framework.interfaces.monitoring.ResponseListener with io.inugami.monitoring.core.interceptors.WarningResponseListener;
    provides io.inugami.framework.interfaces.monitoring.TrackingInformationSPI with io.inugami.monitoring.core.context.DefaultTrackingInformationSPI;
    provides io.inugami.framework.interfaces.monitoring.interceptors.MonitoringFilterInterceptor with io.inugami.monitoring.core.spi.IoLogInterceptor, io.inugami.monitoring.core.spi.ServiceCounterInterceptor, io.inugami.monitoring.core.spi.MdcInterceptor, io.inugami.monitoring.core.spi.CorsInterceptable;
    provides io.inugami.framework.interfaces.monitoring.IoLogContentDisplayResolverSPI with io.inugami.monitoring.core.spi.DefaultIoLogContentDisplayResolverSPI;
}