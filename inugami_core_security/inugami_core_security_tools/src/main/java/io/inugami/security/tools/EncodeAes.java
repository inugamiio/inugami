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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import io.inugami.api.exceptions.Asserts;
import io.inugami.commons.cli.CliHelper;
import io.inugami.commons.cli.DefaultOptions;
import io.inugami.commons.cli.MainCliAction;
import io.inugami.commons.security.EncryptionUtils;
import io.inugami.commons.writer.RapportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EncodeAes
 * 
 * @author patrick_guillerm
 * @since 18 déc. 2017
 */
public class EncodeAes implements MainCliAction {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(DefaultOptions defaultOpts, String[] args, RapportWriter arg2) throws Exception {
        final Options options = new Options();
        options.addOption(new Option("k", "key", true, "Secret key to use"));
        options.addOption(new Option("i", "input", true, "value to encode"));
        
        new ValueEncoder(options, defaultOpts, args).encode(this::encode, this::showHelp);
    }
    
    private void encode(CommandLine cmd) {
        final String secretKey = cmd.getOptionValue('k');
        if (secretKey != null && !secretKey.trim().isEmpty()) {
            System.setProperty("security.aes.secret.key", secretKey);
        }
        
        final String value = cmd.getOptionValue('i');
        Asserts.notEmpty("value is mandatory!", value);
        
        System.out.println(new EncryptionUtils().encodeAES(value));
        
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    private void showHelp() {
        final Logger log = LoggerFactory.getLogger("ROOT");
        CliHelper.drawDeco("HELP", "*", log);
        log.info("This main programm allow to encode value in AES encryption.");
        log.info("-k    -key    : for specify secret token");
        log.info("-i    -input  : value to encode");
        log.info("-h    -help   : for show help");
    }
}
