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
package io.inugami.commons.engine.js.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import io.inugami.api.models.data.basic.RawJsonObject;

/**
 * JsList
 * 
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
public class JsList<E> extends ArrayList<E> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 7740900495327036807L;
    
    protected int             length;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public E set(final int index, final E element) {
        final E result = super.set(index, element);
        length = size();
        return result;
        
    }
    
    @Override
    public boolean add(final E e) {
        final boolean result = super.add(e);
        length = size();
        return result;
    }
    
    @Override
    public void add(final int index, final E element) {
        super.add(index, element);
        length = size();
    }
    
    @Override
    public E remove(final int index) {
        final E result = super.remove(index);
        length = size();
        return result;
    }
    
    @Override
    public boolean remove(final Object o) {
        final boolean result = super.remove(o);
        length = size();
        return result;
    }
    
    @Override
    public void clear() {
        super.clear();
        length = size();
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        final boolean result = super.addAll(c);
        length = size();
        return result;
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        final boolean result = super.addAll(index, c);
        length = size();
        return result;
    }
    
    @Override
    protected void removeRange(final int fromIndex, final int toIndex) {
        super.removeRange(fromIndex, toIndex);
        length = size();
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        final boolean result = super.removeAll(c);
        length = size();
        return result;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        final boolean result = super.retainAll(c);
        length = size();
        return result;
    }
    
    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        final boolean result = super.removeIf(filter);
        length = size();
        return result;
    }
    
    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        super.replaceAll(operator);
        length = size();
    }
    
    public String join(final String charSeparator) {
        final List<String> values = new ArrayList<>();
        for (final E item : this) {
            if (item instanceof RawJsonObject) {
                values.add(((RawJsonObject) item).convertToJson());
            }
            else {
                values.add(String.valueOf(item));
            }
            
        }
        return String.join(charSeparator == null ? "," : charSeparator, values);
    }
}
