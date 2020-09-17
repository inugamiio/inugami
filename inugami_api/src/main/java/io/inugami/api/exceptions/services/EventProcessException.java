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
package io.inugami.api.exceptions.services;

import java.util.Optional;

import io.inugami.api.exceptions.MessagesFormatter;
import io.inugami.api.exceptions.TechnicalException;
import io.inugami.api.models.events.GenericEvent;
import io.inugami.api.models.events.TargetConfig;

/**
 * ProcessorException
 * 
 * @author patrick_guillerm
 * @since 6 oct. 2016
 */
public class EventProcessException extends TechnicalException {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /** The Constant serialVersionUID. */
    private static final long  serialVersionUID = -3327417822907598222L;
    
    public static final int    ERR_CODE         = 500;
    
    private final GenericEvent event;
    
    private final TargetConfig target;
    
    // =========================================================================
    // PROTECTED CONSTRUCTORS
    // =========================================================================
    protected EventProcessException(final GenericEvent event, final TargetConfig target, final int code,
                                    final String message, final Throwable cause) {
        super(code, message, cause);
        this.event = event;
        this.target = target;
    }
    
    // =========================================================================
    // PUBLIC CONSTRUCTORS
    // =========================================================================
    
    public EventProcessException() {
        this(null, null, ERR_CODE, null, null);
    }
    
    public EventProcessException(final String message, final Throwable cause) {
        this(null, null, ERR_CODE, message, cause);
    }
    
    public EventProcessException(final GenericEvent event, final TargetConfig target, final String message,
                                 final Throwable cause) {
        this(event, target, ERR_CODE, message, cause);
    }
    
    public EventProcessException(final String message, final Object... values) {
        this(null, null, ERR_CODE, MessagesFormatter.format(message, values), null);
    }
    
    public EventProcessException(final Throwable cause, final String message, final Object... values) {
        this(null, null, ERR_CODE, MessagesFormatter.format(message, values), cause);
    }
    
    public EventProcessException(final String message) {
        this(null, null, ERR_CODE, message, null);
    }
    
    public EventProcessException(final Throwable cause) {
        this(null, null, ERR_CODE, null, cause);
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public Optional<GenericEvent> getEvent() {
        Optional<GenericEvent> result = Optional.empty();
        if (event != null) {
            result = Optional.of(event);
        }
        return result;
    }
    
    public Optional<TargetConfig> getTarget() {
        Optional<TargetConfig> result = Optional.empty();
        if (target != null) {
            result = Optional.of(target);
        }
        return result;
    }
    
}
