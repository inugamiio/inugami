package org.inugami.webapp.rest;

import javax.validation.constraints.Past;
import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.services.mapping.PluginMapping;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;
import org.inugami.core.security.commons.roles.UserConnected;
import org.inugami.core.security.commons.services.SecurityTokenService;
import org.inugami.core.services.sse.SseService;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Path("provider")
public class ProviderRest {
    @Inject
    private ApplicationContext context;

    @GET
    @UserConnected
    @Produces(MediaType.APPLICATION_JSON)
    public String getProviders(){

        final List<Plugin> result = new ArrayList<>();
        org.inugami.core.context.Context.getInstance().getPlugins().ifPresent(plugins -> {
            result.addAll(Context.getInstance().getPlugins().get());
        });
        return "{}";
    }
}
