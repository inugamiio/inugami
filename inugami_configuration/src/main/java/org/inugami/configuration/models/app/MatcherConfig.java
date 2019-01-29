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
package org.inugami.configuration.models.app;

import java.io.Serializable;

import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.api.functionnals.PostProcessing;
import org.inugami.api.models.JsonBuilder;
import org.inugami.api.processors.ConfigHandler;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * MatcherConfig
 * 
 * @author patrick_guillerm
 * @since 15 déc. 2017
 */
@XStreamAlias("matcher")
public class MatcherConfig implements Serializable, PostProcessing<ConfigHandler<String, String>> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1051920675890974241L;
    
    @XStreamAsAttribute
    private String            expr;
    
    @XStreamAsAttribute
    private ExpressionType    type             = ExpressionType.EXACT;
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public MatcherConfig() {
        super();
    }
    
    public MatcherConfig(String expr, ExpressionType type) {
        super();
        this.expr = expr;
        this.type = type;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public void postProcessing(ConfigHandler<String, String> ctx) throws TechnicalException {
        Asserts.notEmpty("Matcher expression mustn't be empty!", expr);
        expr = ctx.applyProperties(expr);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result && obj != null && obj instanceof MatcherConfig) {
            result = hash().equals(((MatcherConfig) obj).hash());
        }
        return result;
    }
    
    private String hash() {
        //@formatter:off
        return new JsonBuilder()
                     .openObject()
                     .addField("expr").valueQuot(expr).addSeparator()
                     .addField("type").valueQuot(type)
                     .closeObject()
                     .toString();
        //@formatter:on
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MatcherConfig [");
        builder.append(", expr=");
        builder.append(expr);
        builder.append(", type=");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getExpr() {
        return expr;
    }
    
    public void setExpr(String expr) {
        this.expr = expr;
    }
    
    public ExpressionType getType() {
        return type;
    }
    
    public void setType(ExpressionType type) {
        this.type = type;
    }
    
}
