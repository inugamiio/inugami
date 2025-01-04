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
package io.inugami.framework.configuration.services;

import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.models.Gav;
import io.inugami.api.models.events.AlertingModel;
import io.inugami.api.models.events.Event;
import io.inugami.api.models.events.SimpleEvent;
import io.inugami.api.models.events.TargetConfig;
import io.inugami.configuration.models.EventConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PluginConfigurationTest
 *
 * @author patrick_guillerm
 * @since 26 déc. 2016
 */
class EventFileConfigurationLoaderTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String RESOURCES_PATH = initResourcesPath();

    private static final Logger LOGGER = LoggerFactory.getLogger(EventFileConfigurationLoaderTest.class);

    // =========================================================================
    // INIT
    // =========================================================================
    private static String initResourcesPath() {
        final File   file       = new File(".");
        final String currentDir = file.getAbsoluteFile().getParentFile().getAbsolutePath();

        return currentDir + "/src/test/resources";
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    void testLoadFromFile() throws TechnicalException {
        try {
            process();
        } catch (final TechnicalException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

    }

    private void process() throws TechnicalException {
        final PluginConfigurationLoader loader = new PluginConfigurationLoader();

        LOGGER.info("load events-test-configuration.xml");
        //@formatter:off
        final File file = new File(RESOURCES_PATH + "/events-test-configuration.xml");
        final Optional<EventConfig> configOpt;
        try {
            configOpt = loader.loadEventConfigFromFile(new Gav(),file);
        } catch (final TechnicalException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        //@formatter:on

        assertTrue(configOpt.isPresent());
        final EventConfig config = configOpt.get();
        LOGGER.info("validate data");
        assertEquals("events-test-configuration", config.getName());
        assertThat(config.getScheduler()).isNotNull();
        assertEquals("0 0/5 * * * ?", config.getScheduler());
        assertEquals(file.getAbsolutePath(), config.getConfigFile());
        assertEquals(Boolean.FALSE, config.getEnable());

        validateSimplesEvents(config.getSimpleEvents());
        validateEvents(config.getEvents());
    }

    private void validateSimplesEvents(final List<SimpleEvent> events) {
        assertThat(events).isNotNull();
        assertEquals(4, events.size());

        SimpleEvent qualityEvent = null;
        SimpleEvent apiEvent     = null;
        SimpleEvent view10Event  = null;
        SimpleEvent view30Event  = null;

        for (final SimpleEvent event : events) {
            switch (event.getName()) {
                case "foobar-quality":
                    qualityEvent = event;
                    break;
                case "foobar-pourcentage":
                    apiEvent = event;
                    break;
                case "foobar-views-10mn":
                    view10Event = event;
                    break;
                case "foobar-views-30mn":
                    view30Event = event;
                    break;

                default:
                    throw new FatalException("unkown element {0}", event.getName());
            }
        }
        assertThat(qualityEvent).isNotNull();
        assertThat(apiEvent).isNotNull();
        assertThat(view10Event).isNotNull();
        assertThat(view30Event).isNotNull();

        // ----------------------------------------------------------------
        assertEquals("graphite.bigdata", qualityEvent.getProvider().get());
        assertEquals("foo.bar.Mapper", qualityEvent.getMapper().get());
        assertThat(qualityEvent.getProcessors()).isNotNull();
        assertEquals("0 0/2 * * * ?", qualityEvent.getScheduler());
        assertTrue(qualityEvent.getAlertings().isPresent());
        assertEquals(1, qualityEvent.getAlertings().get().size());
        final AlertingModel qualityAlert = qualityEvent.getAlertings().get().get(0);
        assertEquals("prd-prod-001", qualityAlert.getName());
        assertEquals("{{myAlertingProvider}}", qualityAlert.getProvider());
        assertEquals("value > 5", qualityAlert.getCondition());
        assertEquals("Attention augmentation d'erreur sur le service JOE", qualityAlert.getMessage());
        assertEquals("warn", qualityAlert.getLevel());

        assertEquals(2, qualityEvent.getProcessors().get().size());
        assertEquals("foo", qualityEvent.getProcessors().get().get(0).getName());
        assertEquals("bar", qualityEvent.getProcessors().get().get(1).getName());
        //@formatter:off
        assertEquals("scale(summarize(avg(org.foobar.joe.*.count), '24h', 'avg', true),100)",qualityEvent.getQuery());
        //@formatter:on
        assertFalse(qualityEvent.getFrom().isPresent());
        assertFalse(qualityEvent.getUntil().isPresent());

        // ----------------------------------------------------------------
        assertEquals("graphite.bigdata", apiEvent.getProvider().get());
        //@formatter:off
        assertEquals("scale(summarize(avg(org.foo.bar.joe.percent), '24h', 'avg', true),100)",apiEvent.getQuery());
        //@formatter:on
        assertFalse(apiEvent.getProcessors().isPresent());
        assertFalse(apiEvent.getFrom().isPresent());
        assertFalse(apiEvent.getUntil().isPresent());

        // ----------------------------------------------------------------
        assertEquals("graphite.bigdata", view10Event.getProvider().get());
        assertEquals("-10min", view10Event.getFrom().get());
        //@formatter:off
        assertEquals("sumSeries(summarize(org.foo.bar.view,\"10min\",\"avg\",true))",view10Event.getQuery());
        //@formatter:on
        assertFalse(view10Event.getProcessors().isPresent());
        assertFalse(view10Event.getUntil().isPresent());

        // ----------------------------------------------------------------
        assertEquals("graphite.bigdata", view30Event.getProvider().get());
        assertEquals("-30min", view30Event.getFrom().get());
        //@formatter:off
        assertEquals("sumSeries(summarize(org.foo.bar.view,\"10min\",\"avg\",true))",view30Event.getQuery());
        //@formatter:on
        assertFalse(view30Event.getProcessors().isPresent());
        assertFalse(view30Event.getUntil().isPresent());
    }

    private void validateEvents(final List<Event> events) {
        assertNotNull(events);
        assertEquals(2, events.size());

        Event apiPercent = null;
        Event paiement   = null;
        if ("foobar-api-pourcentrage".equals(events.get(0).getName())) {
            apiPercent = events.get(0);
            paiement = events.get(1);
        } else {
            apiPercent = events.get(1);
            paiement = events.get(0);
        }
        assertEquals("foobar-api-pourcentrage", apiPercent.getName());
        assertEquals("foobar-paiement", paiement.getName());

        validateApiPercent(apiPercent);
        validatePaiement(paiement);

    }

    private void validateApiPercent(final Event event) {
        assertEquals("graphite.bigdata", event.getProvider().get());
        assertEquals("foo.bar.MapperOnEvent", event.getMapper().get());
        assertFalse(event.getFrom().isPresent());
        assertFalse(event.getUntil().isPresent());
        assertEquals("0 0/3 * * * ?", event.getScheduler());

        assertTrue(event.getProcessors().isPresent());
        assertEquals(1, event.getProcessors().get().size());
        assertEquals("foo", event.getProcessors().get().get(0).getName());

        assertNotNull(event.getTargets());
        assertEquals(3, event.getTargets().size());

        TargetConfig foobarSys = null;
        TargetConfig gravida   = null;
        TargetConfig sapien    = null;

        //@formatter:off
        for (final TargetConfig target : event.getTargets()){
            switch (target.getName()) {
                case "foobar-sys" : foobarSys = target; break;
                case "gravida-pourcentrage"  : gravida = target; break;
                case "sapien-pourcentrage"      : sapien = target; break;
                default: throw new FatalException("unkown element : {0}",target.getName());
            }
        }
        // @formatter:on
        assertNotNull(foobarSys);
        assertNotNull(gravida);
        assertNotNull(sapien);

        assertFalse(foobarSys.getProvider().isPresent());
        assertFalse(foobarSys.getUntil().isPresent());
        assertFalse(foobarSys.getFrom().isPresent());
        assertEquals("foo.bar.MapperOnTarget", foobarSys.getMapper().get());

        assertTrue(foobarSys.getProcessors().isPresent());
        assertEquals(1, foobarSys.getProcessors().get().size());
        assertEquals("bar", foobarSys.getProcessors().get().get(0).getName());
        //@formatter:off
        assertEquals("summarize(asPercent(sumSeries(org.foo.bar.jmx.joe.sessions),sumSeries(org.foo.bar.jmx.*.session)), \"24h\", \"avg\",true)",
                     foobarSys.getQuery());
        //@formatter:on

        assertFalse(gravida.getProvider().isPresent());
        assertFalse(gravida.getUntil().isPresent());
        assertFalse(gravida.getFrom().isPresent());
        assertFalse(gravida.getProcessors().isPresent());
        //@formatter:off
        assertEquals("summarize(asPercent(sumSeries(org.foo.bar.jmx.gravida.sessions),sumSeries(org.foo.bar.jmx.*.session)), \"24h\", \"avg\",true)",
                     gravida.getQuery());
        //@formatter:on

        assertFalse(sapien.getProvider().isPresent());
        assertFalse(sapien.getUntil().isPresent());
        assertFalse(sapien.getFrom().isPresent());

        assertTrue(sapien.getProcessors().isPresent());
        assertEquals(1, sapien.getProcessors().get().size());
        assertEquals("joe", sapien.getProcessors().get().get(0).getName());
        //@formatter:off
        assertEquals("summarize(asPercent(sumSeries(org.foo.bar.jmx.sapien.sessions),sumSeries(org.foo.bar.jmx.*.session)), \"24h\", \"avg\",true)",
                     sapien.getQuery());
        //@formatter:on

        assertTrue(event.getAlertings().isPresent());
        assertEquals(1, event.getAlertings().get().size());
        final AlertingModel alerte = event.getAlertings().get().get(0);
        assertEquals("prd-prod-002", alerte.getName());
        assertEquals("{{myAlertingProvider}}", alerte.getProvider());
        assertEquals("Oups", alerte.getMessage());
        assertEquals("error", alerte.getLevel());
        //@formatter:off
        final String condition=
        "var level=null;\n" + 
        "            var message=null;\n" + 
        "            \n" + 
        "            if(value>0.8){\n" + 
        "                level=\"error\";\n" + 
        "                message=\"Erreur le seuil sur les sessions client est trop élevé\";\n" + 
        "            }else if(value>0.6){\n" + 
        "                level=\"warning\";\n" + 
        "                message=\"Attention le seuil sur les sessions client est haut\";\n" + 
        "            }\n" + 
        "            \n" + 
        "            return {\n" + 
        "                \"level\"   :level,\n" + 
        "                \"message\" :message\n" + 
        "            }"; 
        //@formatter:on

        assertEquals(condition, alerte.getCondition().trim());

    }

    private void validatePaiement(final Event event) {
        assertFalse(event.getFrom().isPresent());
        assertFalse(event.getUntil().isPresent());

        assertNotNull(event.getTargets());
        assertEquals(2, event.getTargets().size());

        TargetConfig targetCurrent = null;
        TargetConfig lastYear      = null;

        if ("current-paiement-cumul".equals(event.getTargets().get(0).getName())) {
            targetCurrent = event.getTargets().get(0);
            lastYear = event.getTargets().get(1);
        } else {
            targetCurrent = event.getTargets().get(1);
            lastYear = event.getTargets().get(0);
        }

        // targetCurrent -----------------------------------------------------
        assertEquals("current-paiement-cumul", targetCurrent.getName());
        assertFalse(targetCurrent.getUntil().isPresent());
        assertFalse(targetCurrent.getFrom().isPresent());
        assertFalse(targetCurrent.getProcessors().isPresent());
        assertEquals("graphite.bigdata", targetCurrent.getProvider().get());
        //@formatter:off
        assertEquals("summarize(sumSeries(org.foo.bar.paiement.*.count),\"24h\", true)",
                     targetCurrent.getQuery());
        //@formatter:on

        // lastYear ----------------------------------------------------------
        assertEquals("lastyear-paiement-cumul", lastYear.getName());
        assertFalse(lastYear.getUntil().isPresent());
        assertFalse(lastYear.getFrom().isPresent());
        assertFalse(lastYear.getProcessors().isPresent());
        assertEquals("jdbc.provider", lastYear.getProvider().get());
        //@formatter:off
        assertEquals("select max(dateTime), sum(OBJ) from FOOBAR",
                     lastYear.getQuery());
        //@formatter:on

    }

}
