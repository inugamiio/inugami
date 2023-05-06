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
package io.inugami.api.monitoring.models;

import flexjson.JSON;
import io.inugami.api.dao.Identifiable;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.mapping.DateTransformer;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.graphite.number.GraphiteNumber;
import io.inugami.api.models.data.graphite.number.GraphiteNumberTransformer;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * GenericMonitoringModel
 *
 * @author patrick_guillerm
 * @since 27 déc. 2018
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
public class GenericMonitoringModel implements JsonObject, Identifiable<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4735410638475899702L;

    private final String uid;

    private final String asset;

    private final String environment;

    private final String instanceName;

    private final String instanceNumber;

    private final String counterType;

    private final String device;

    private final String callType;

    private final String service;

    private final String subService;

    private final String valueType;

    private final String timeUnit;

    @JSON(transformer = DateTransformer.class)
    private final Date date;

    private final long time;

    private final String errorCode;

    private final String errorType;

    @JSON(transformer = GraphiteNumberTransformer.class)
    private final GraphiteNumber value;

    @EqualsAndHashCode.Include
    private final String path;

    private final String data;

    private final long timestamp;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public static class GenericMonitoringModelBuilder {
        private void buildPath() {
            try {
                final MessageDigest md = MessageDigest.getInstance("SHA-512");
                final byte[] bytes = md.digest(String.join(".", this.asset,
                                                           this.environment,
                                                           this.instanceName,
                                                           this.instanceNumber,
                                                           this.counterType,
                                                           this.callType,
                                                           this.service,
                                                           this.subService,
                                                           this.device,
                                                           this.errorCode,
                                                           this.errorType,
                                                           this.valueType,
                                                           this.timeUnit)
                                                     .getBytes(StandardCharsets.UTF_8));
                this.path = Hex.encodeHexString(bytes);
            } catch (final NoSuchAlgorithmException e) {
                Loggers.DEBUG.error(e.getMessage(), e);
            }
        }

        public GenericMonitoringModel build() {
            buildPath();
            return new GenericMonitoringModel(this.uid,
                                              this.asset,
                                              this.environment,
                                              this.instanceName,
                                              this.instanceNumber,
                                              this.counterType,
                                              this.device,
                                              this.callType,
                                              this.service,
                                              this.subService,
                                              this.valueType,
                                              this.timeUnit,
                                              this.date,
                                              this.time,
                                              this.errorCode,
                                              this.errorType,
                                              this.value,
                                              this.path,
                                              this.data,
                                              this.timestamp);
        }
    }


    // uid = hashPath(path);

    // =========================================================================
    // OVERRIDES
    // =========================================================================

    public String getNonTemporalHash() {
        //@formatter:off
        return String.join(".", asset,
                           environment,
                           instanceName,
                           instanceNumber,
                           counterType,
                           callType,
                           service,
                           subService,
                           device,
                           errorCode,
                           errorType,
                           valueType,
                           timeUnit
        );
        //@formatter:on
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(final String uid) {
        // nothing to do
    }

    @Override
    public boolean isUidSet() {
        return true;
    }

}
