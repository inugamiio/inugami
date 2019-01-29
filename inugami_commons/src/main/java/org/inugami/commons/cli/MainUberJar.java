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
package org.inugami.commons.cli;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.inugami.api.exceptions.Asserts;
import org.inugami.commons.files.FilesUtils;
import org.inugami.commons.spi.SpiLoader;
import org.inugami.commons.writer.RapportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MainUberJar
 * 
 * @author patrick_guillerm
 * @since 28 juin 2017
 */
public class MainUberJar {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Logger LOGGER = LoggerFactory.getLogger("ROOT");
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static void main(final String[] args) {
        try {
            process(args);
        }
        catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
    }
    
    // =========================================================================
    // MAIN
    // =========================================================================
    @SuppressWarnings("squid:S1147")
    private static void process(final String[] args) throws Exception {
        CliHelper.drawDeco("CLI PROCESSING", "#", LOGGER);
        drawArguments(args);
        
        final DefaultOptions defaultOptions = new DefaultOptions(args);
        
        //@formatter:off
        final List<MainCliAction> services = new SpiLoader().loadSpiService(MainCliAction.class);
        final Optional<MainCliAction> serviceOptional = services.stream()
                                                                .filter(name -> matchSpiService(name,defaultOptions.getAction()))
                                                                .findFirst();
        
        Asserts.isTrue("Can't found implementation for action :" + defaultOptions.getAction(), serviceOptional.isPresent());
        //@formatter:on
        
        final MainCliAction service = serviceOptional.get();
        
        final File rapportFile = buildRapportFile(defaultOptions, service.getClass().getSimpleName());
        
        final RapportWriter writer = new RapportWriter(rapportFile);
        LOGGER.info("repport file : {}", FilesUtils.getCanonicalPath(rapportFile));
        
        CliHelper.newLine(LOGGER);
        CliHelper.drawDeco(String.format("Process : %s", service.getClass()), "=", LOGGER);
        
        try {
            service.process(defaultOptions, defaultOptions.getOtherOptions(), writer);
            
        }
        catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        finally {
            writer.flush();
            writer.close();
        }
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    private static File buildRapportFile(final DefaultOptions defaultOptions, final String simpleName) {
        final File tmpFile = FilesUtils.buildFile(FilesUtils.getTmpDir(), simpleName + ".log");
        return defaultOptions.getReportFile().orElse(tmpFile);
    }
    
    private static boolean matchSpiService(final MainCliAction instance, final String name) {
        return name.equals(instance.getClass().getName()) || name.equals(instance.getClass().getSimpleName());
    }
    
    private static void drawArguments(final String... args) {
        final String arguments = String.join(" ", args);
        LOGGER.info("arguments : {}", arguments);
    }
}
