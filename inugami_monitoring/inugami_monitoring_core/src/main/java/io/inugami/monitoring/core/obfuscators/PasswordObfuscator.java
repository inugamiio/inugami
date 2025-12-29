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
package io.inugami.monitoring.core.obfuscators;

import io.inugami.framework.interfaces.monitoring.Obfuscator;
import io.inugami.monitoring.api.obfuscators.ObfuscatorTools;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * PasswordObfuscator
 *
 * @author patrickguillerm
 * @since Jan 8, 2019
 */
public class PasswordObfuscator implements Obfuscator {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final Pattern EMPTY   = ObfuscatorTools.buildJsonFieldPattern("(password|PASSWORD)", "\"\\s*\"");
    private static final Pattern DEFAULT = ObfuscatorTools.buildJsonFieldPattern("(password|PASSWORD)");

    // =================================================================================================================
    // METHODS
    // =================================================================================================================
    @Override
    public boolean accept(@Nullable String data) {
        return data!=null && (data.contains("password")||data.contains("PASSWORD"));
    }

    @Override
    public String clean(@NonNull String data) {
        String result = replaceAll(DEFAULT, data, "\"password\":\"xxxxx\"");
        return replaceAll(EMPTY, result, "\"password\":\"empty\"");
    }
}
