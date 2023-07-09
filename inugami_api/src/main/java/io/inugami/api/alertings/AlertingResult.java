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
package io.inugami.api.alertings;

import io.inugami.api.models.JsonBuilder;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.JsonSerializerSpi;
import io.inugami.api.spi.SpiLoader;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * AlertingResult
 *
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AlertingResult implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 7298930435253118179L;

    private           String       alerteName;
    private           String       level;
    private           String       url;
    private transient AlerteLevels levelType   = AlerteLevels.UNDEFINE;
    private           int          levelNumber = AlerteLevels.UNDEFINE.getLevel();
    private           String       message;
    private           String       subLabel;
    private           long         created     = System.currentTimeMillis();
    private           long         duration    = 60;
    private transient Object       data;
    private           String       channel     = "@all";
    private           boolean      multiAlerts;
    private           List<String> providers;


    public AlertingResult(final String alerteName) {
        super();
        this.alerteName = alerteName;
    }


    @Override
    public JsonObject cloneObj() {
        return this.toBuilder().build();
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String convertToJson() {
        //@formatter:off
        return new JsonBuilder()
                .openObject()
                .addField("alertName").valueQuot(alerteName).addSeparator()
                .addField("channel").valueQuot(channel).addSeparator()
                .addField("level").valueQuot(level).addSeparator()
                .addField("levelType").valueQuot(levelType.name()).addSeparator()
                .addField("levelNumber").write(levelNumber).addSeparator()
                .addField("message").valueQuot(message).addSeparator()
                .addField("subLabel").valueQuot(subLabel).addSeparator()
                .addField("url").valueQuot(url).addSeparator()
                .addField("created").write(created).addSeparator()
                .addField("duration").write(duration).addSeparator()
                .addField("data").write(convertData()).addSeparator()
                .addField("multiAlerts").write(multiAlerts)
                .closeObject()
                .toString();
        //@formatter:on
    }

    public String convertData() {
        String result = data == null ? JsonBuilder.VALUE_NULL : null;

        if (result == null) {

            if (data instanceof JsonObject) {
                result = ((JsonObject) data).convertToJson();
            } else {
                final JsonSerializerSpi jsonSerializer = SpiLoader.getInstance().loadSpiSingleService(JsonSerializerSpi.class);
                result = jsonSerializer.serialize(data);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return convertToJson();
    }


    public AlertingResult addProvider(final String provider) {
        if (providers == null) {
            providers = new ArrayList<>();
        }

        if ((provider != null) && !providers.contains(provider)) {
            providers.add(provider);
        }

        return this;
    }
}
