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
package io.inugami.core.alertings.senders.teams;

import io.inugami.api.alertings.AlertingResult;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.RenderingException;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.spi.SpiLoader;
import io.inugami.core.alertings.messages.FormatAlertMessage;
import io.inugami.core.alertings.senders.teams.icons.TeamIcon;
import io.inugami.core.alertings.senders.teams.icons.TeamIconResolver;
import io.inugami.core.alertings.senders.teams.sender.models.Facts;
import io.inugami.core.alertings.senders.teams.sender.models.PotentialActionOpenUri;
import io.inugami.core.alertings.senders.teams.sender.models.Section;
import io.inugami.core.alertings.senders.teams.sender.models.TeamsModel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.*;

@Default
@Named
@ApplicationScoped
@SuppressWarnings({"java:S1871"})
public class DefaultTeamsAlertRenderer implements TeamsAlertRenderer {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String YYYY_MM_DD_HH_SS = "yyyy-MM-dd HH:ss";
    public static final  String SERVICE          = "service";

    private final List<TeamIconResolver> iconResolvers = SpiLoader.getInstance().loadSpiService(TeamIconResolver.class);

    @Override
    public boolean accept(final AlertingResult alert) {
        return true;
    }

    // =========================================================================
    // RENDERING
    // =========================================================================
    @Override
    public TeamsModel rendering(final AlertingResult alert) throws RenderingException {
        Asserts.assertNotNull(alert);
        if (Loggers.ALERTING.isDebugEnabled()) {
            Loggers.ALERTING.debug("process alerte for teams : {}", alert.convertToJson());
        }

        final TeamsModel result = new TeamsModel();
        result.setText(alert.getAlerteName());
        final String title = FormatAlertMessage.formatMessage(alert.getMessage(), alert);
        result.setThemeColor(alert.getLevelType().getColor());

        final Section section = new Section();
        section.setActivityTitle(title);
        section.setMarkdown(true);
        if (alert.getSubLabel() != null) {
            section.setActivitySubtitle(alert.getSubLabel());
        }

        section.addFact(buildService(alert.getData()));
        section.addFact(buildAlerteCreationDate(alert.getCreated()));
        section.addFact(buildAlerteWillDone(alert.getCreated(), alert.getDuration()));
        section.addFact(buildServerInfo(alert.getData()));
        section.addFact(buildCurrentValue(alert.getData()));
        section.addFact(buildNominalValue(alert.getData()));
        final String icon = resolveIcon(alert.getLevel());
        if (icon != null) {
            section.setActivityImage(icon);
        }
        result.addSection(section);

        if (alert.getUrl() != null) {
            result.addPotentialAction(new PotentialActionOpenUri("detail", alert.getUrl()));
        }

        return result;
    }

    private String resolveIcon(final String level) {
        TeamIcon icon = null;
        for (final TeamIconResolver resolver : iconResolvers) {
            final TeamIcon solution = resolver.resolve(level);
            if (solution == null) {
                continue;
            }
            if (icon == null || (solution.getPriority() > icon.getPriority())) {
                icon = solution;
            }

        }
        return icon == null ? null : icon.getIcon();
    }

    // =========================================================================
    // BUILDER
    // =========================================================================
    private Facts buildAlerteWillDone(final long created, final long duration) {
        final String value = new SimpleDateFormat(YYYY_MM_DD_HH_SS).format(new Date(created + duration));
        return new Facts("will-done", value);
    }

    private Facts buildAlerteCreationDate(final long created) {
        final String value = new SimpleDateFormat(YYYY_MM_DD_HH_SS).format(new Date(created));
        return new Facts("creation", value);
    }

    private Facts buildServerInfo(final Object data) {
        String server = null;

        final Map<String, Object> buffer = extractValues(data, "instanceName", "prodFerme");
        server = convertToString(buffer.get("instanceName"));
        if (server == null) {
            server = convertToString(buffer.get("prodFerme"));
        }

        return server == null ? null : new Facts("server", server);
    }

    private Facts buildCurrentValue(final Object data) {
        return buildValue(data, "currentValue", "current-value");
    }

    private Facts buildNominalValue(final Object data) {
        return buildValue(data, "nominal", "nominal");
    }

    private Facts buildValue(final Object data, final String field, final String name) {
        String value = null;
        String unit  = null;

        final Map<String, Object> buffer = extractValues(data, field, "unit");
        value = convertToString(buffer.get(field));
        unit = convertToString(buffer.get("unit"));

        final StringBuilder result = new StringBuilder();
        if (value != null) {
            result.append(value);
            if (unit != null) {
                result.append(unit);
            }
        }

        return result.toString().isEmpty() ? null : new Facts(name, result.toString());
    }

    private Facts buildService(final Object data) {

        final Map<String, Object> buffer = extractValues(data, SERVICE);
        final String              result = convertToString(buffer.get(SERVICE));

        return result == null ? null : new Facts(SERVICE, result);
    }

    private Map<String, Object> extractValues(final Object data, final String... keys) {
        final Map<String, Object> buffer = new HashMap<>();

        if (data == null) {
            return buffer;
        }


        if (data instanceof Map<?, ?>) {
            final Map<?, ?> values = (Map<?, ?>) data;
            for (final String key : keys) {
                Optional.ofNullable(values.get(key)).ifPresent(item -> buffer.put(key, item));
            }

        }

        return buffer;
    }

    private String convertToString(final Object object) {
        return object == null ? null : String.valueOf(object);
    }
}
