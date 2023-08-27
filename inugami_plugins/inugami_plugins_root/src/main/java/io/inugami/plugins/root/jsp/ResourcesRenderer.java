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
package io.inugami.plugins.root.jsp;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.models.Gav;
import io.inugami.api.models.JsonBuilder;
import io.inugami.commons.messages.MessagesServices;
import io.inugami.configuration.models.plugins.Plugin;
import io.inugami.configuration.models.plugins.Resource;
import io.inugami.configuration.models.plugins.ResourceCss;
import io.inugami.configuration.models.plugins.ResourceJavaScript;
import io.inugami.configuration.models.plugins.front.PluginFrontConfig;
import io.inugami.core.context.Context;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * ResourcesRenderer
 *
 * @author patrick_guillerm
 * @since 18 janv. 2017
 */
@SuppressWarnings({"java:S3655", "java:S6355", "java:S1123", "java:S1133"})
@UtilityClass
public class ResourcesRenderer {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    private static final char[] ROUTER = initRouter();

    private static final char[] MODULE = initModule();

    private static final String APPLICATION_VERSION = JvmKeyValues.APPLICATION_VERSION.get();

    private static final String APPLICATION_TITLE = JvmKeyValues.APPLICATION_TITLE.or("Inugami");

    // =========================================================================
    // RENDERER ROUTERS
    // =========================================================================
    public static char[] getRouter() {
        return cloneArray(ROUTER);
    }

    public static char[] getModule() {
        return cloneArray(MODULE);
    }

    private static char[] cloneArray(final char[] values) {
        final int    size   = values.length;
        final char[] result = new char[size];
        System.arraycopy(values, 0, result, 0, size);
        return result;

    }

    // =========================================================================
    // RENDERER ROUTERS
    // =========================================================================
    public static String renderPluginsCss(final String contextPath) {
        final StringBuilder result = new StringBuilder();

        processOnPlugins(plugin -> plugin.getResources()
                                         .stream()
                                         .filter(item -> isCssResource(item))
                                         .map(css -> renderCssLink(contextPath, css.getFullPath()))
                                         .forEach(result::append));

        processOnFrontPlugins(plugin -> {
            final String commonsCss = plugin.getFrontConfig().get().getCommonsCss();
            if (commonsCss != null) {
                result.append(renderCssLink(contextPath, commonsCss));
            }
        });

        return result.toString();
    }

    private static String renderCssLink(final String contextPath, final String cssPath) {
        final StringBuilder result = new StringBuilder();
        result.append("<link   href=");
        result.append('"');
        result.append(contextPath);
        result.append(cssPath);
        result.append('"');
        result.append(" rel=\"stylesheet\" />");
        result.append('\n');
        return result.toString();
    }

    public static String renderPluginsJavaScript(final String contextPath) {
        final StringBuilder result = new StringBuilder();
        //@formatter:off
        processOnPlugins(plugin -> plugin.getResources()
                  .stream()
                  .filter(item -> isJavaScriptResource(item))
                  .map(js -> renderJavaScriptLink(js, contextPath))
                  .forEach(jsLink -> result.append("\n").append(jsLink)));
        return result.toString();
    }

    private static String renderJavaScriptLink(final Resource js, final String contextPath) {
        final StringBuilder result = new StringBuilder();
        result.append("\t<script ");
        result.append("src=");
        result.append('"');
        result.append(contextPath);
        result.append(js.getFullPath());
        result.append('"');
        result.append("></script>");
        return result.toString();
    }

    private static boolean isCssResource(final Resource resource) {
        return resource instanceof ResourceCss;
    }

    private static boolean isJavaScriptResource(final Resource resource) {
        return resource instanceof ResourceJavaScript;
    }

    // =========================================================================
    // RENDERER ROUTERS
    // =========================================================================
    private static char[] initRouter() {
        final StringBuilder result = new StringBuilder();

        // ---------------------------------------------------
        // IMPORTS
        // ---------------------------------------------------
        //@formatter:off
        result.append(importation("RouterModule", "@angular/router"));
        result.append(importation("HomeView", "./view/home.view.ts"));
        //@formatter:on

        // ---------------------------------------------------
        // ROUTERS
        // ---------------------------------------------------
        result.append("const routes = [").append('\n');
        result.append("{ path: '', component: HomeView , pathMatch:'full'}").append('\n');
        result.append("];").append('\n');
        result.append("export const routing = RouterModule.forRoot(routes);");

        return result.toString().toCharArray();
    }

    // =========================================================================
    // RENDERER MODULE
    // =========================================================================
    private static char[] initModule() {
        final StringBuilder result = new StringBuilder();

        // ---------------------------------------------------
        // IMPORTS
        // ---------------------------------------------------
        //@formatter:off
        result.append(importation("CommonModule", "@angular/common"));
        result.append(importation("BrowserModule", "@angular/platform-browser"));
        result.append(importation("ReactiveFormsModule", "@angular/forms"));
        result.append(importation("NgModule", "@angular/core"));
        result.append(importation("FormsModule", "@angular/forms"));
        result.append(importation("APP_BASE_HREF", "@angular/common"));
        result.append(importation("AppRootModule", "./app.root.module.ts"));
        result.append(importation("AppRootModuleRoutes", "./app.root.module.ts"));
        result.append(importation("routing", "./app.routes.ts"));
        result.append(importation("AppComponent", "./app.component.ts"));
        //@formatter:on

        //@formatter:off
        processOnFrontPlugins(plugin -> {
            final PluginFrontConfig frontConfig = plugin.getFrontConfig().get();
            result.append(importation(frontConfig.getModule().getClassName(), frontConfig.getModule().getFile()));

            if (frontConfig.hasRouterModuleName()) {
                result.append(importation(frontConfig.getRouterModuleName(), frontConfig.getModule().getFile()));
            }
        });
        //@formatter:on

        // ---------------------------------------------------
        // MODULE
        // ---------------------------------------------------
        result.append("@NgModule({");
        result.append("imports: [CommonModule, BrowserModule, ReactiveFormsModule,FormsModule");
        processOnFrontPlugins(plugin -> {
            final PluginFrontConfig frontConfig = plugin.getFrontConfig().get();
            result.append(',');
            result.append(frontConfig.getModule().getClassName());
            if (frontConfig.hasRouterModuleName()) {
                result.append(',');
                result.append(frontConfig.getRouterModuleName());
            }
        });
        result.append(",routing");
        result.append(",AppRootModule");
        result.append(",AppRootModuleRoutes");
        result.append("],\n");

        result.append("declarations: [AppComponent],\n");
        result.append("entryComponents: [],\n");
        result.append("providers: [\n");
        result.append("     {provide: APP_BASE_HREF, useValue:CONTEXT_PATH}\n");
        result.append("],\n");
        result.append("bootstrap: [ AppComponent ]\n");
        result.append("})\n");
        result.append("export class AppModule {}");
        return result.toString().toCharArray();
    }

    // =========================================================================
    // PLUGINS GAVS
    // =========================================================================
    public static String renderPluginsGavs() {
        final List<Plugin> pluginsWithFront = new ArrayList<>();

        Context.getInstance().getPlugins().ifPresent(plugins -> plugins.stream()
                                                                       .filter(plugin -> plugin.getFrontConfig()
                                                                                               .isPresent())
                                                                       .forEach(pluginsWithFront::add));

        final JsonBuilder js = new JsonBuilder();
        js.openObject();
        //--------------------------------------------
        js.addField("frontPlugins").openObject();

        for (int i = 0; i < pluginsWithFront.size(); i++) {
            final Plugin plugin = pluginsWithFront.get(i);
            if (plugin.getFrontConfig().isPresent()) {
                if (i != 0) {
                    js.addSeparator();
                }
                js.addField(plugin.getFrontConfig().get().getPluginBaseName());
                js.write(renderGavJson(plugin.getGav()));
            }

        }
        js.closeObject();
        js.addSeparator();
        //--------------------------------------------
        js.addField("plugins").openObject();
        Context.getInstance().getPlugins().ifPresent(plugins -> {
            for (int i = 0; i < plugins.size(); i++) {
                if (i != 0) {
                    js.addSeparator();
                }
                js.addField(buildSimpleGav(plugins.get(i)));
                js.write(renderGavJson(plugins.get(i).getGav()));
            }

        });

        js.closeObject();
        js.closeObject();
        return js.toString();
    }


    private static String buildSimpleGav(final Plugin plugin) {
        final StringBuilder result = new StringBuilder();
        result.append(plugin.getGav().getGroupId());
        result.append(':');
        result.append(plugin.getGav().getArtifactId());
        return result.toString();
    }

    private static String renderGavJson(final Gav gav) {
        final StringBuilder json = new StringBuilder();
        json.append('{');
        json.append(writeParam("groupId", gav.getGroupId())).append(',');
        json.append(writeParam("artifactId", gav.getArtifactId())).append(',');
        json.append(writeParam("version", gav.getVersion())).append(',');
        json.append(writeParam("qualifier", gav.getQualifier()));
        json.append('}');
        return json.toString();
    }

    private static String writeParam(final String key, final String value) {
        final StringBuilder json = new StringBuilder();
        json.append('"');
        json.append(key);
        json.append('"');
        json.append(':');
        json.append('"');
        json.append(value);
        json.append('"');

        return json.toString();
    }


    // =========================================================================
    // PLUGINS I18N
    // =========================================================================
    public static String renderPluginsI18N() {
        return MessagesServices.getJson();
    }


    // =========================================================================
    // PLUGINS SYSTEM MAP
    // =========================================================================
    public static String renderPluginsSystemMap() {
        final JsonBuilder json = new JsonBuilder();
        processOnFrontPlugins(plugin -> {
            final PluginFrontConfig frontConfig = plugin.getFrontConfig().get();
            if (frontConfig.getSystemMap() != null) {
                //@formatter:off
                frontConfig.getSystemMap()
                           .entrySet()
                           .stream()
                           .map(ResourcesRenderer::renderSystemMapEntry)
                           .forEach(json::write);
                //@formatter:on

            }
        });
        return json.toString();
    }

    private static String renderSystemMapEntry(final Entry<String, String> entry) {
        Asserts.assertNotNull(entry.getKey());
        Asserts.assertNotEmpty(String.format("System Map namespace must define location (%s)", entry.getKey()),
                               entry.getValue());
        final JsonBuilder json = new JsonBuilder();
        json.line();
        json.addSeparator();
        json.addField(entry.getKey());
        if (entry.getValue().contains("\"")) {
            json.write(entry.getValue());
        } else {
            json.valueQuot(entry.getValue());
        }
        return json.toString();
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private static String importation(final String className, final String file) {
        final StringBuilder result = new StringBuilder();
        result.append("import {").append(className).append('}');
        result.append('\t').append("from").append('\t');
        result.append('\'').append(file).append('\'');
        result.append('\n');
        return result.toString();
    }


    private static void processOnFrontPlugins(final Consumer<Plugin> function) {
        Context.getInstance()
               .getPlugins()
               .orElseGet(Collections::emptyList)
               .stream()
               .filter(plugin -> plugin.getFrontConfig().isPresent())
               .forEach(function::accept);
    }

    private static void processOnPlugins(final Consumer<Plugin> function) {
        Context.getInstance()
               .getPlugins()
               .orElseGet(Collections::emptyList)
               .stream()
               .forEach(function);
    }

    public static String renderApplicationVersion() {
        return APPLICATION_VERSION;
    }

    public static String getApplicationTitle() {
        return APPLICATION_TITLE;
    }

}
