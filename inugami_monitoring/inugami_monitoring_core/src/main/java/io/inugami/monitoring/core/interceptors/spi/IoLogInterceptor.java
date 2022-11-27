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
package io.inugami.monitoring.core.interceptors.spi;
import java.util.List;
import java.util.Map;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.JsonBuilder;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.data.ResponseData;
import io.inugami.api.monitoring.data.ResquestData;
import io.inugami.api.monitoring.exceptions.ErrorResult;
import io.inugami.api.monitoring.interceptors.MonitoringFilterInterceptor;
import io.inugami.api.monitoring.models.GenericMonitoringModel;
import io.inugami.api.monitoring.warn.WarnCode;
import io.inugami.api.monitoring.warn.WarnContext;
import io.inugami.api.processors.ConfigHandler;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import org.slf4j.Logger;

/**
 * LogInterceptor
 * 
 * @author patrickguillerm
 * @since Jan 7, 2019
 */
public class IoLogInterceptor implements MonitoringFilterInterceptor {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER        = Loggers.IOLOG;
    
    private static final String EMPTY_CONTENT = "empty";
    
    private static final String EMPTY         = "";
    
    private final String        inputDecorator;
    
    private final String        outputDecorator;
    
    private final boolean       enableDecorator;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public IoLogInterceptor() {
        enableDecorator = true;
        inputDecorator = "[IN] ";
        outputDecorator = "[OUT] ";
    }
    
    public IoLogInterceptor(final ConfigHandler<String, String> configuration) {
        enableDecorator = configuration.grabBoolean("enableDecorator", true);
        inputDecorator = configuration.grabOrDefault("inputDecorator", "[IN] ");
        outputDecorator = configuration.grabOrDefault("outputDecorator", "[OUT] ");
    }
    
    @Override
    public MonitoringFilterInterceptor buildInstance(final ConfigHandler<String, String> configuration) {
        return new IoLogInterceptor(configuration);
    }
    
    @Override
    public String getName() {
        return "iolog";
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public List<GenericMonitoringModel> onBegin(final ResquestData request) {
        final JsonBuilder msg = buildIologIn(request );
        MdcService.getInstance().lifecycleIn();
        LOGGER.info((enableDecorator ? inputDecorator : EMPTY) +  ObfuscatorTools.applyObfuscators(msg.toString()));
        MdcService.getInstance().lifecycleRemove();
        return null;
    }

    private JsonBuilder buildIologIn(final ResquestData request) {
        final JsonBuilder result = new JsonBuilder();
        result.openList().write(request.getMethod()).closeList();
        result.writeSpace().write(request.getContextPath()).write(request.getUri()).line();
        result.write("headers :").line();
        for ( Map.Entry<String, String>  entry : request.getHearder().entrySet()) {
            result.tab().write(entry.getKey()).write(":").writeSpace().write(entry.getValue()).line();
        }

        result.write("payload :").line();
        result.write(request.getContent()==null?JsonBuilder.VALUE_NULL:request.getContent());
        return result;
    }

    @Override
    public List<GenericMonitoringModel> onDone(final ResquestData request, final ResponseData httpResponse,
                                               final ErrorResult error) {
        final JsonBuilder msg = buildIologIn(request);

        msg.addLine();
        msg.write("response:").line();
        msg.tab().write("status:").write(httpResponse.getCode()).line();
        msg.tab().write("datetime:").write(httpResponse.getDatetime()).line();
        msg.tab().write("duration:").write(httpResponse.getDuration()).line();
        msg.tab().write("contentType:").write(httpResponse.getContentType()).line();
        msg.tab().write("headers:").line();
        for ( Map.Entry<String, String>  entry : httpResponse.getHearder().entrySet()) {
            msg.tab().tab().write(entry.getKey()).write(":").writeSpace().write(entry.getValue()).line();
        }




        final Map<String, List<WarnCode>> warns = WarnContext.getInstance().getWarns();
        if(!warns.isEmpty()){
            msg.tab().write("warning:").line();
            for ( Map.Entry<String, List<WarnCode>>  entry : warns.entrySet()) {
                msg.tab().tab().write(entry.getKey()).write(":").line();
                for(WarnCode warn : entry.getValue()){
                    msg.tab().tab().tab().write(warn.getMessage());
                    if(warn.getMessageDetail()!=null){
                        msg.write(":").write(warn.getMessageDetail());
                    }
                    msg.line();
                }
            }
        }
        msg.tab().write("payload:").write(httpResponse.getContent());


        //@formatter:on
        MdcService.getInstance().lifecycleOut();
        if (error == null) {
            LOGGER.info((enableDecorator ? outputDecorator : EMPTY) + msg);
        }
        else {
            if(error.isExploitationError()){

                Loggers.XLLOG.error("[HTTP-{}:{}] {} : {}",error.getHttpCode(), error.getErrorCode(), error.getMessage(), error.getCause());
            }
            LOGGER.error((enableDecorator ? outputDecorator : EMPTY) + msg);
        }
        MdcService.getInstance().lifecycleRemove();
        return null;
    }
    

}
