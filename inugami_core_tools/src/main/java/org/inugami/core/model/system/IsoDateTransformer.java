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
package org.inugami.core.model.system;

import java.text.SimpleDateFormat;
import java.util.Date;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;

/**
 * IsoDateTransformer
 * 
 * @author patrickguillerm
 * @since 4 oct. 2018
 */
public class IsoDateTransformer extends AbstractTransformer implements Transformer {
    
    @Override
    public void transform(final Object object) {
        if (object instanceof Date) {
            getContext().writeQuoted(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format((Date) object));
        }
        else {
            getContext().write("null");
        }
        
    }
    
}
