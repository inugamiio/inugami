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
package org.inugami.commons.test.arquillian;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.inugami.api.models.Tuple;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * ShrinkWrapBuilder
 * 
 * @author patrick_guillerm
 * @since 19 mars 2018
 */
public final class ShrinkWrapBuilder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final List<File>                  libs                    = new ArrayList<>();
    
    private final List<Class<?>>              classes                 = new ArrayList<>();
    
    private final List<Tuple<String, String>> resources               = new ArrayList<>();
    
    private final List<String>                simpleManifestResources = new ArrayList<>();
    
    private final List<Tuple<Asset, String>>  manifestResources       = new ArrayList<>();
    
    private String                            applicationName         = null;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ShrinkWrapBuilder() {
        
    }
    
    public Archive<?> buildArchive() {
        //@formatter:off
         final File[] dependencies = libs.toArray(new File[] {});
        
        
         final WebArchive result = ShrinkWrap.create(WebArchive.class, applicationName+".war");
         
         result.setWebXML("web.xml");
         result.addAsLibraries(dependencies);
         result.addClasses(classes.toArray(new Class<?>[] {}));
         
         simpleManifestResources.forEach(result::addAsManifestResource);
         manifestResources.forEach(item->result.addAsResource(item.getKey(),item.getValue()));
         resources.forEach(item-> result.addAsResource(item.getKey(),item.getValue()));
         return result;
    }
    
    // =========================================================================
    // INIT
    // =========================================================================
    public ShrinkWrapBuilder initBaseApplication() {
        addTransitiveDependency("org.jboss.weld.servlet:weld-servlet:2.3.5.Final");
        addAsResource("META-INF/logback-test.xml", "logback.xml");
        addAsManifestResource("arquillian.xml");
        addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return this;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public ShrinkWrapBuilder addDependency(final String gav) {
        final File[] files = Maven.resolver().resolve(gav).withoutTransitivity().as(File.class);
        if (files != null) {
            for (final File file : files) {
                libs.add(file);
            }
        }
        return this;
    }
    
    public ShrinkWrapBuilder addTransitiveDependency(final String gav) {
        final File[] files = Maven.resolver().resolve(gav).withTransitivity().as(File.class);
        if (files != null) {
            for (final File file : files) {
                libs.add(file);
            }
        }
        return this;
    }
    
    public ShrinkWrapBuilder setArchiveName(final Class<?> clazz) {
        if (clazz != null) {
            applicationName = clazz.getSimpleName();
        }
        return this;
    }
    
    public ShrinkWrapBuilder addAsResource(final String resourceName, final String target) {
        resources.add(new Tuple<>(resourceName, target));
        return this;
    }
    
    public ShrinkWrapBuilder addAsManifestResource(final String target) {
        if (target != null) {
            simpleManifestResources.add(target);
        }
        return this;
    }
    
    public ShrinkWrapBuilder addAsManifestResource(final Asset asset, final String target) {
        if (target != null) {
            manifestResources.add(new Tuple<>(asset, target));
        }
        return this;
    }
    
    public ShrinkWrapBuilder addClasses(final Class<?>... classes) {
        for (final Class<?> item : classes) {
            if (item != null) {
                this.classes.add(item);
            }
        }
        return this;
    }
}
