package org.inugami.webapp.rest;

import org.inugami.api.providers.Provider;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.core.context.ApplicationContext;
import org.inugami.core.context.Context;
import org.inugami.core.security.commons.roles.UserConnected;
import org.inugami.webapp.rest.models.DataProviderRestModel;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Path("provider")
public class DataProviderRest {
    @Inject
    private ApplicationContext context;

    @GET
    @UserConnected
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataProviderRestModel> getProviders(){

        final List<Plugin> pluginList = new ArrayList<>();
        org.inugami.core.context.Context.getInstance().getPlugins().ifPresent(plugins -> {
            pluginList.addAll(Context.getInstance().getPlugins().get());
        });


        List<DataProviderRestModel> modelList   = new ArrayList<>();
        for (Plugin plugin: pluginList) {
            for(Provider provider: plugin.getProviders().orElse(Collections.emptyList())){
                modelList.add(new DataProviderRestModel(provider.getType(),provider.getTimeout(),provider.getConfig()));
            }
        }

        return modelList ;//result.toString();
    }
}
