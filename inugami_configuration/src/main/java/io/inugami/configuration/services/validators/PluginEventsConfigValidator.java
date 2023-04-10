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
package io.inugami.configuration.services.validators;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.events.*;
import io.inugami.api.processors.ClassBehavior;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.configuration.exceptions.ConfigurationException;
import io.inugami.configuration.models.EventConfig;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static io.inugami.configuration.services.validators.ValidatorProcessor.*;

/**
 * PluginEventsConfigValidator.
 *
 * @author patrick_guillerm
 * @since 29 déc. 2016
 */
public class PluginEventsConfigValidator implements Validator {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final EventConfig      eventConfig;
    private final String           configFile;
    private final JavaScriptEngine jsEngine;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public PluginEventsConfigValidator(final EventConfig eventConfig, final String configFile) {
        this.eventConfig = eventConfig;
        this.configFile = configFile;
        jsEngine = JavaScriptEngine.getInstance();
    }
    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public void validate() throws ConfigurationException {
        final List<Condition> conditions = new ArrayList<>();

        //@formatter:off
        conditions.add(condition("Can't validate null event config!", eventConfig == null));
        conditions.add(condition("error configuration event file hasn't path!", configFile == null));
        Asserts.assertNotNull("error in initialization of event configuration, no name found!", eventConfig.getName());
        //@formatter:on

        if (eventConfig.getEvents() != null) {
            eventConfig.getEvents().forEach(event -> conditions.addAll(buildConditionsForEvent(event)));
        }

        if (eventConfig.getSimpleEvents() != null) {
            eventConfig.getSimpleEvents().forEach(event -> conditions.addAll(buildConditionsForSimpleEvent(event)));
        }

        //@formatter:off
        new ValidatorProcessor().validate(configFile,
                                          conditions,
                                          (condition, path) -> format(condition.getMessage() + " {0}", configFile));
        //@formatter:on
    }

    // =========================================================================
    // EVENTS / SIMPLE EVENT
    // =========================================================================
    private List<Condition> buildConditionForGenericEvent(final GenericEvent event) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(condition("event must have name!", event.getName() == null));

        final String eventName = event.getName() == null ? "" : " " + event.getName();

        if (event.getFrom().isPresent()) {
            conditions.add(condition(eventName + " from attribute mustn't be empty!", isEmpty(event.getFrom().get())));
        }

        if (event.getUntil().isPresent()) {
            conditions.add(condition(eventName + " until attribute mustn't be empty!",
                                     isEmpty(event.getUntil().get())));
        }

        if (event.getProvider().isPresent()) {
            conditions.add(condition(eventName + " provider attribute mustn't be empty!",
                                     isEmpty(event.getProvider().get())));
        }

        if (event.getProcessors().isPresent()) {
            event.getProcessors().get().forEach(item -> conditions.addAll(buildConditionForClassBehavior(item)));
        }
        return conditions;
    }


    private List<Condition> buildConditionsForEvent(final Event event) {
        final List<Condition> conditions = buildConditionForGenericEvent(event);

        final String eventName = event.getName() == null ? "" : event.getName();
        conditions.add(condition(eventName + " targets are mandatory for event!", event.getTargets() == null));
        conditions.addAll(validateAlerting(event.getAlertings().orElse(null)));

        boolean allTargetsHavesProvider = true;
        for (final TargetConfig target : event.getTargets()) {
            conditions.addAll(buildConditionForGenericEvent(target));
            conditions.add(condition(eventName + " query is mandatory for target!", isEmpty(target.getQuery())));
            conditions.addAll(validateAlerting(target.getAlertings().orElse(null)));
            if (!target.getProvider().isPresent()) {
                allTargetsHavesProvider = false;
            }
        }

        if (!allTargetsHavesProvider) {
            //@formatter:off
            conditions.add(condition(
                    eventName + " all targets doen't define specific provider, you must define generic provider on event!",
                    !event.getProvider().isPresent()));
            //@formatter:on
        }
        return conditions;
    }


    private List<Condition> buildConditionsForSimpleEvent(final SimpleEvent event) {
        final List<Condition> conditions = buildConditionForGenericEvent(event);
        final String          eventName  = event.getName() == null ? "" : event.getName();

        conditions.add(condition(eventName + " provider is mandatory for simple event!",
                                 !event.getProvider().isPresent()));
        conditions.addAll(validateAlerting(event.getAlertings().orElse(null)));
        return conditions;
    }


    private List<Condition> buildConditionForClassBehavior(final ClassBehavior behavior) {
        final List<Condition> conditions = new ArrayList<>();
        Asserts.assertNotNull(behavior);

        conditions.add(condition("behavior name mustn't be null!", isEmpty(behavior.getName())));
        //@formatter:off
        final String name = behavior.getName() == null ? "" : String.format("(behavior name = \"%s\")", behavior.getName());
        conditions.add(condition("You can't redefine behavior className in event config file ! " + name, !isEmpty(behavior.getClassName())));
        conditions.add(condition("You can't redefine behavior configurtion in event config file ! " + name, behavior.getConfigs() != null));
        //@formatter:on
        return conditions;
    }

    private List<Condition> validateAlerting(final List<AlertingModel> alertings) {
        return validate(alertings, (data, result) -> {
            for (final AlertingModel item : alertings) {
                result.add(condition("Alerte name is mandatory!", isEmpty(item.getName())));
                final String name = item.getName() == null ? "" : String.format("(%s)", item.getName());

                if (item.grabFunction().isPresent()) {
                    result.add(condition("You can't have JavaScript function and condition on same alerte!" + name,
                                         isNotNull(item.getCondition())));
                } else {
                    result.add(condition("Alerte provider is mandatory!" + name, isEmpty(item.getProvider())));
                    result.add(condition("Alerte condition is mandatory!" + name, isEmpty(item.getCondition())));

                    if (item.getCondition() != null) {
                        result.addAll(validateAlerteCondition(item.getCondition()));
                    }
                }

            }
        });
    }

    protected List<Condition> validateAlerteCondition(final String condition) {
        final List<Condition> result = new ArrayList<>();
        try {
            jsEngine.checkScriptInnerFunction(condition);
        } catch (final ScriptException e) {
            result.add(condition("Alerting script error :" + e.getMessage().replaceAll("\\{", "").replaceAll("\\}", ""),
                                 true));
        }
        return result;
    }
}
