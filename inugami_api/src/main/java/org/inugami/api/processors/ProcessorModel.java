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
package org.inugami.api.processors;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * PostProcessorModel
 * 
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
@XStreamAlias("processor")
public class ProcessorModel extends ClassBehavior {
    private static final long serialVersionUID = 8318093792064035460L;
    
    public ProcessorModel() {
        super();
    }
    
    public ProcessorModel(final String name, final String className, final Config... configs) {
        super(name, className, configs);
    }
    
}
