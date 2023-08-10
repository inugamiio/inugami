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
package io.inugami.monitoring.config.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.functionnals.ApplyIfNull;
import io.inugami.api.functionnals.PostProcessing;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.processors.ConfigHandler;
import lombok.Getter;
import lombok.Setter;

import static io.inugami.monitoring.config.tools.ApplyStrategy.applyStrategy;
import static io.inugami.monitoring.config.tools.ApplyStrategy.applyStrategyBoolean;

/**
 * MonitoringConfig
 *
 * @author patrickguillerm
 * @since Jan 14, 2019
 */
@Setter
@Getter
@XStreamAlias("monitoring")
public class MonitoringConfig implements PostProcessing<ConfigHandler<String, String>>, JsonObject, ApplyIfNull {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8578418466667776898L;

    @XStreamAsAttribute
    private Boolean enable;

    @XStreamAsAttribute
    private String env;

    @XStreamAsAttribute
    private String asset;

    @XStreamAsAttribute
    private String hostname;

    @XStreamAsAttribute
    private String instanceName;

    @XStreamAsAttribute
    private String instanceNumber;

    @XStreamAsAttribute
    private String maxSensorsTasksThreads;

    @XStreamAsAttribute
    private String applicationVersion;

    private HeaderInformationsConfig header;

    private PropertiesConfig properties;

    private MonitoringSendersConfig senders;

    private SensorsConfig sensors;

    private InterceptorsConfig interceptors;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    @Override
    public void postProcessing(final ConfigHandler<String, String> context) throws TechnicalException {

        if (header == null) {
            header = new HeaderInformationsConfig();
        }

        senders = senders == null ? new MonitoringSendersConfig() : senders;
        sensors = sensors == null ? new SensorsConfig() : sensors;

        //@formatter:off
        enable                 = applyStrategyBoolean(JvmKeyValues.MONITORING_ENABLE, enable, true);
        env                    = applyStrategy(JvmKeyValues.ENVIRONMENT,env,"prod");
        asset                  = applyStrategy(JvmKeyValues.ASSET,asset,"inugami");
        hostname               = applyStrategy(JvmKeyValues.APPLICATION_HOST_NAME,hostname);
        instanceName           = applyStrategy(JvmKeyValues.INSTANCE,instanceName);
        instanceNumber         = applyStrategy(JvmKeyValues.INSTANCE_NUMBER,instanceNumber); 
        maxSensorsTasksThreads = applyIfNull(maxSensorsTasksThreads , () -> "20");
        //@formatter:on

        header.postProcessing(context);

    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        return convertToJson();
    }

}
