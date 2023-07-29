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
package io.inugami.logs.obfuscator.api;

import io.inugami.logs.obfuscator.tools.ObfuscatorUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Deprecated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String PASSWORD  = ObfuscatorUtils.PASSWORD;
    public static final String MASK      = ObfuscatorUtils.MASK;
    public static final String MASK_JSON = ObfuscatorUtils.MASK_JSON;
    public static final String QUOT      = ObfuscatorUtils.QUOT;
    public static final String OPEN_TAG  = ObfuscatorUtils.OPEN_TAG;
    public static final String CLOSE_TAG = ObfuscatorUtils.CLOSE_TAG;

}
