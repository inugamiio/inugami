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
package io.inugami.core.alertings.senders.teams.icons;

import org.apache.commons.codec.binary.Base64;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.commons.files.FilesUtils;

/**
 * TeamIconREsolver
 * 
 * @author patrick_guillerm
 * @since 21 août 2018
 */
public interface TeamIconResolver {
    TeamIcon resolve(String level);
    
    default String readImage(final String name) {
        byte[] data = null;
        try {
            data = new FilesUtils().readFromClassLoader(name);
        }
        catch (final TechnicalException e) {
            throw new FatalException(e.getMessage(), e);
        }
        
        final StringBuilder result = new StringBuilder();
        final String fileType = name.substring(name.lastIndexOf(".")).replaceAll("[.]", "");
        result.append("data:image/").append(fileType).append(";base64,");
        result.append(new String(Base64.encodeBase64(data)));
        return result.toString();
    }
}
