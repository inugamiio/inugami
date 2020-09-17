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
package io.inugami.configuration.services.mapping.transformers;

import java.util.Optional;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.ObjectTransformer;
import flexjson.transformer.Transformer;

/**
 * OptionnalTransformer
 * 
 * @author patrick_guillerm
 * @since 20 janv. 2017
 */
public class OptionalTransformer extends AbstractTransformer implements Transformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        final Transformer objTransformer = new ObjectTransformer();
        getContext().writeOpenObject();
        if (object instanceof Optional<?>) {
            final Optional<?> opt = (Optional<?>) object;
            getContext().writeName("present");
            getContext().write(String.valueOf(opt.isPresent()));
            getContext().writeComma();
            
            getContext().writeName("data");
            if (opt.isPresent()) {
                objTransformer.transform(opt.get());
            }
            else {
                getContext().write("null");
            }
        }
        
        getContext().writeCloseObject();
    }
}
