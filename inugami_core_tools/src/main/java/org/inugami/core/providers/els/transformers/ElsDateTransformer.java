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
package org.inugami.core.providers.els.transformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;

/**
 * DataTransformer
 * 
 * @author patrick_guillerm
 * @since 13 sept. 2018
 */
public class ElsDateTransformer extends AbstractTransformer implements Transformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        if (object != null) {
            getContext().write("\"");
            getContext().write(new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSZ").format((Date) object));
            getContext().write("\"");
        }
        else {
            getContext().write("null");
        }
        
    }
    
}
