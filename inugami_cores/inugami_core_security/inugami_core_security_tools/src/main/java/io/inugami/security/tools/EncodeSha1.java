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

import io.inugami.commons.cli.DefaultOptions;
import io.inugami.commons.cli.MainCliAction;
import io.inugami.commons.security.EncryptionUtils;
import io.inugami.commons.writer.RapportWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * EncodeBase64
 *
 * @author patrick_guillerm
 * @since 18 déc. 2017
 */
@Slf4j
public class EncodeSha1 implements MainCliAction {

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void process(DefaultOptions defaultOpts, String[] args, RapportWriter arg2) throws Exception {
        new ValueEncoder(defaultOpts, args).encodeDefault(this::encode);
    }

    public void encode(final String value) {
        log.info(new EncryptionUtils().encodeSha1(value));
    }
}
