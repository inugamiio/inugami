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
package io.inugami.commons.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import io.inugami.api.exceptions.Asserts;

/**
 * DefaultOptions
 * 
 * @author patrick_guillerm
 * @since 28 juin 2017
 */
public class DefaultOptions {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static String[] ARGS = { "-a", "-action", "-h", "-help", "-v", "-verbose", "-d", "-debug", "-r",
                                           "-report", "-dryRun" };
    
    private final String          action;
    
    private final boolean         showHelp;
    
    private final boolean         verbose;
    
    private final boolean         debug;
    
    private final boolean         dryRun;
    
    private final File            reportFile;
    
    private final String          defaultHelp;
    
    private final String[]        otherOptions;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DefaultOptions(final String... args) {
        super();
        final Options options = new Options();
        options.addOption(new Option("a", "action", true, "CLI action to execute"));
        options.addOption(new Option("h", "help", true, "CLI action to execute"));
        options.addOption(new Option("v", "verbose", true, "enable verbose"));
        options.addOption(new Option("d", "debug", true, "enable debug mode"));
        options.addOption(new Option("r", "report", true, "Report file path"));
        options.addOption(new Option("dryRun", true, "enable debug mode"));
        
        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, true);
        }
        catch (final ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        
        action = cmd.getOptionValue('a');
        Asserts.notEmpty("action is mandatory!", action);
        
        showHelp = Boolean.parseBoolean(cmd.getOptionValue('h'));
        verbose = Boolean.parseBoolean(cmd.getOptionValue('v'));
        debug = Boolean.parseBoolean(cmd.getOptionValue('d'));
        dryRun = Boolean.parseBoolean(cmd.getOptionValue("dryRun"));
        
        final String reportFilePath = cmd.getOptionValue('r');
        reportFile = reportFilePath == null ? null : new File(reportFilePath);
        
        defaultHelp = buildDefaultHelp(options);
        
        otherOptions = buildOtherProperties(args);
        
    }
    
    private String[] buildOtherProperties(final String... args) {
        final List<String> result = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            final String value = args[i];
            if (isDefualtArg(value)) {
                i++;
                
            }
            else {
                result.add(value);
            }
            
        }
        final String[] type = {};
        return result.toArray(type);
    }
    
    private boolean isDefualtArg(final String value) {
        boolean result = false;
        for (final String arg : ARGS) {
            if (arg.equals(value)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    public final String buildDefaultHelp(final Options options) {
        final StringBuilder result = new StringBuilder();
        options.getOptions().stream().map(this::buildOptionHelp).forEach(result::append);
        return result.toString();
    }
    
    public String buildOptionHelp(final Option option) {
        final StringBuilder result = new StringBuilder();
        result.append('\n');
        if (option.getOpt() != null) {
            result.append('-').append(option.getOpt()).append('\t');
        }
        
        if (option.getOpt() != null) {
            result.append('-').append(option.getLongOpt()).append('\t');
        }
        result.append('\t').append(':').append(option.getDescription());
        return result.toString();
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DefaultOptions [action=");
        builder.append(action);
        builder.append(", showHelp=");
        builder.append(showHelp);
        builder.append(", verbose=");
        builder.append(verbose);
        builder.append(", debug=");
        builder.append(debug);
        builder.append(", dryRun=");
        builder.append(dryRun);
        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getAction() {
        return action;
    }
    
    public boolean isShowHelp() {
        return showHelp;
    }
    
    public boolean isVerbose() {
        return verbose;
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public boolean isDryRun() {
        return dryRun;
    }
    
    public String getDefaultHelp() {
        return defaultHelp;
    }
    
    public Optional<File> getReportFile() {
        return Optional.ofNullable(reportFile);
    }
    
    public String[] getOtherOptions() {
        return otherOptions;
    }
    
}
