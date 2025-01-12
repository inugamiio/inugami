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
package io.inugami.framework.configuration.services.validators;


import io.inugami.framework.configuration.exceptions.ConfigurationException;
import io.inugami.framework.configuration.models.app.*;
import io.inugami.framework.configuration.models.plugins.PropertyModel;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.MessagesFormatter;
import io.inugami.framework.interfaces.models.Config;

import java.util.ArrayList;
import java.util.List;

import static io.inugami.framework.configuration.services.validators.ValidatorProcessor.*;


/**
 * ApplicationConfigValidator
 *
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
public class ApplicationConfigValidator implements Validator {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final ApplicationConfig appConfig;

    private final String path;

    private final ValidatorProcessor validator = new ValidatorProcessor();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ApplicationConfigValidator(final ApplicationConfig appConfig, final String path) {
        this.appConfig = appConfig;
        this.path = path;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void validate() throws ConfigurationException {
        final List<Condition> conditions = new ArrayList<>();

        Asserts.assertNotNull("Application configuration is mandatory!", appConfig);
        conditions.addAll(validateRootApplicationConfiguration(appConfig));
        conditions.addAll(validateProperties(appConfig));
        conditions.addAll(validateSecurityConfigs(appConfig));

        //@formatter:off
        validator.validate(path,
                           conditions,
                           (condition, path) -> MessagesFormatter.format(condition.getMessage() + " {0}", path));
        //@formatter:on
    }

    // =========================================================================
    // VALIDATORS
    // =========================================================================

    private List<Condition> validateRootApplicationConfiguration(final ApplicationConfig config) {
        final List<Condition> result = new ArrayList<>();
        //@formatter:off
        result.add(condition("Application name mustn't be empty", ifPresentIsEmpty(config.getApplicationName())));
        //@formatter:on
        return result;
    }

    private List<Condition> validateProperties(final ApplicationConfig conf) {
        return validate(conf.getProperties(), (data, result) -> {
            for (final PropertyModel item : data) {
                result.add(condition("Property key is mandatory", isEmpty(item.getKey())));
            }
        });
    }


    private List<Condition> validateSecurityConfigs(final ApplicationConfig conf) {
        return validate(conf.getSecurity(), (data, result) -> {
            for (final SecurityConfiguration item : data) {
                result.addAll(validateSecurityItemConfig(item));
            }
        });
    }

    private List<Condition> validateSecurityItemConfig(final SecurityConfiguration data) {
        final List<Condition> result = new ArrayList<>();
        result.addAll(validateConfigTags(data.getConfigs()));
        result.add(condition("Security name is mandatory", isEmpty(data.getName())));

        if (!"technical".equals(data.getName())) {
            result.add(condition("Only technical provider can create users!", isNotNull(data.getUsers())));
        }

        result.addAll(validateSecurityRoles(data.getRoles()));
        result.addAll(validateSecurityUsers(data.getUsers()));

        return result;
    }

    private List<Condition> validateSecurityRoles(final List<RoleMappeurConfig> roles) {
        return validate(roles, (data, result) -> {
            for (final RoleMappeurConfig item : data) {
                result.add(condition("Role name is mandatory", isEmpty(item.getName())));
                result.add(condition("Level must be higher than 0", item.getLevel() < 0));
                result.addAll(validateRoleMatchers(item.getMatchers()));
            }
        });
    }

    private List<Condition> validateRoleMatchers(final List<MatcherConfig> matchers) {
        return validate(matchers, (data, result) -> {
            for (final MatcherConfig item : data) {
                result.add(condition("Role matcher expression is mandatory", isEmpty(item.getExpr())));
            }
        });
    }

    private List<Condition> validateSecurityUsers(final List<UserConfig> users) {
        return validate(users, (data, result) -> {
            for (final UserConfig item : data) {
                result.addAll(validateSecurityUserItemConfig(item));
            }
        });
    }

    private List<Condition> validateSecurityUserItemConfig(final UserConfig user) {
        return validate(user, (data, result) -> {
            result.add(condition("User login is mandatory", isEmpty(data.getLogin())));
            result.add(condition("User login is mandatory", isEmpty(data.getPassword())));
            if (data.getToken() != null) {
                result.add(condition("User token mustn't be empty", isEmpty(data.getToken())));
            }
            result.add(condition("User roles is mandatory!", isNull(data.getUserRoles())));
        });
    }

    private List<Condition> validateConfigTags(final List<Config> configs) {
        return validate(configs, (data, result) -> {
            for (final Config config : data) {
                result.add(condition("config key is mandatory", config.getKey() == null));
            }
        });
    }
}
