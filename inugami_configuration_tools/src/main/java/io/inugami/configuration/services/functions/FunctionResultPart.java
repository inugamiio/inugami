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
package io.inugami.configuration.services.functions;

/**
 * FunctionResultPart
 * 
 * @author patrick_guillerm
 * @since 20 sept. 2017
 */
public class FunctionResultPart {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private int    start;
    
    private int    end;
    
    private String content;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FunctionResultPart(final int start, final int end, final String content) {
        this.start = start;
        this.end = end;
        this.content = content;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("FunctionResultPart [start=");
        builder.append(start);
        builder.append(", end=");
        builder.append(end);
        builder.append(", content=");
        builder.append(content);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public int getStart() {
        return start;
    }
    
    public void setStart(final int start) {
        this.start = start;
    }
    
    public int getEnd() {
        return end;
    }
    
    public void setEnd(final int end) {
        this.end = end;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
}
