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
package org.inugami.security.tools;

import java.util.function.Consumer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.functionnals.VoidFunction;
import org.inugami.commons.cli.CliHelper;
import org.inugami.commons.cli.DefaultOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ValueEncoder
 * 
 * @author patrick_guillerm
 * @since 18 déc. 2017
 */
public class ValueEncoder {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private Options              options;
    
    private final DefaultOptions defaultOpts;
    
    private final String[]       args;
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public ValueEncoder(DefaultOptions defaultOpts, String[] args) {
        this(null, defaultOpts, args);
    }
    
    public ValueEncoder(Options options, DefaultOptions defaultOpts, String[] args) {
        super();
        this.options = options;
        this.defaultOpts = defaultOpts;
        this.args = args;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public void encode(Consumer<CommandLine> consumer, VoidFunction onHelp) {
        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(options, args, true);
        }
        catch (ParseException e) {
            throw new FatalException(e.getMessage(), e);
        }
        if (defaultOpts.isShowHelp() && onHelp != null) {
            onHelp.process();
        }
        else {
            consumer.accept(cmd);
        }
    }
    
    public void encodeDefault(final Consumer<String> consumer) {
        options = new Options();
        options.addOption(new Option("i", "input", true, "value to encode"));
        
        //@formatter:off
        encode((cmd)->{
            final String value = cmd.getOptionValue('i');
            Asserts.notEmpty("value is mandatory!", value);
            consumer.accept(value);
            
        },this::showHelp);
        //@formatter:on
    }
    
    // =========================================================================
    // SHOW HELP
    // =========================================================================
    private void showHelp() {
        final Logger log = LoggerFactory.getLogger("ROOT");
        CliHelper.drawDeco("HELP", "*", log);
        log.info("-i    -input  : value to encode");
        log.info("-h    -help   : for show help");
    }
    
}
