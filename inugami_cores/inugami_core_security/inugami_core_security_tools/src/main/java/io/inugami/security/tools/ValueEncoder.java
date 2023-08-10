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
package io.inugami.security.tools;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.functionnals.VoidFunction;
import io.inugami.commons.cli.CliHelper;
import io.inugami.commons.cli.DefaultOptions;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

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
    private Options options;

    private final DefaultOptions defaultOpts;

    private final String[] args;

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public ValueEncoder(final DefaultOptions defaultOpts, final String[] args) {
        this(null, defaultOpts, args);
    }

    public ValueEncoder(final Options options, final DefaultOptions defaultOpts, final String[] args) {
        super();
        this.options = options;
        this.defaultOpts = defaultOpts;
        this.args = args;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void encode(final Consumer<CommandLine> consumer, final VoidFunction onHelp) {
        CommandLine cmd = null;
        try {
            cmd = new DefaultParser().parse(options, args, true);
        } catch (final ParseException e) {
            throw new FatalException(e.getMessage(), e);
        }
        if (defaultOpts.isShowHelp() && onHelp != null) {
            onHelp.process();
        } else {
            consumer.accept(cmd);
        }
    }

    public void encodeDefault(final Consumer<String> consumer) {
        options = new Options();
        options.addOption(new Option("i", "input", true, "value to encode"));
        
        encode(cmd -> {
            final String value = cmd.getOptionValue('i');
            Asserts.assertNotEmpty("value is mandatory!", value);
            consumer.accept(value);

        }, this::showHelp);

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
