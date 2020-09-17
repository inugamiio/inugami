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
package io.inugami.commons.transformer;

import java.util.Iterator;
import java.util.Map.Entry;

import io.inugami.api.models.JsonBuilder;

import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * OptionalTransformer
 * 
 * @author patrick_guillerm
 * @since 7 févr. 2018
 */
public class NashornTransformer extends AbstractTransformer {
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void transform(final Object object) {
        if (object == null) {
            getContext().write(JsonBuilder.VALUE_NULL);
        }
        else {
            if (object instanceof ScriptObjectMirror) {
                processTransform(object);
            }
            else {
                getContext().transform(object);
            }
        }
    }
    
    private void processTransform(final Object object) {
        final ScriptObjectMirror scriptObject = (ScriptObjectMirror) object;
        
        if (scriptObject.isArray()) {
            processList(scriptObject);
        }
        else {
            processObject(scriptObject);
        }
        
    }
    
    private void processList(final ScriptObjectMirror scriptObject) {
        final JSONContext json = getContext();
        json.writeOpenArray();
        
        boolean enableComma = false;
        for (final Entry<String, Object> item : scriptObject.entrySet()) {
            if (enableComma) {
                json.writeComma();
            }
            
            final Object value = item.getValue();
            if (value == null) {
                json.write(JsonBuilder.VALUE_NULL);
            }
            else {
                json.transform(value);
            }
            enableComma = true;
        }
        
        json.writeCloseArray();
        
    }
    
    private void processObject(final ScriptObjectMirror scriptObject) {
        final JSONContext json = getContext();
        json.writeOpenObject();
        final Iterator<Entry<String, Object>> iterator = scriptObject.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<String, Object> item = iterator.next();
            json.writeName(item.getKey());
            
            final Object value = item.getValue();
            if (value == null) {
                json.write(JsonBuilder.VALUE_NULL);
            }
            else if (isScriptObjectArray(value)) {
                processList((ScriptObjectMirror) value);
            }
            else {
                json.transform(item.getValue());
            }
            
            if (iterator.hasNext()) {
                json.writeComma();
            }
        }
        json.writeCloseObject();
    }
    
    private boolean isScriptObjectArray(final Object value) {
        return (value != null) && (value instanceof ScriptObjectMirror) && ((ScriptObjectMirror) value).isArray();
    }
}
