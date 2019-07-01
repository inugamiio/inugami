package org.inugami.webapp.rest;

import org.inugami.api.providers.Provider;
import org.inugami.configuration.models.ProviderConfig;
import org.inugami.configuration.models.plugins.Plugin;
import org.inugami.configuration.models.plugins.PluginConfiguration;
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
    public List<String> getProviders(){

        final List<PluginConfiguration> pluginConfList = new ArrayList<>();
        org.inugami.core.context.Context.getInstance().getPlugins().ifPresent(plugins -> {
            for(Plugin plugin : Context.getInstance().getPlugins().get()){
                pluginConfList.add(plugin.getConfig());
            }
        });


        List<String> response   = new ArrayList<>();
        for (PluginConfiguration pluginconf: pluginConfList) {
            
            if(pluginconf.getProviders() != null){
                for(ProviderConfig providerConf: pluginconf.getProviders()){
                    response.add(providerConf.getName());
                }
            }
        }

        return response ;
    }
}
