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
package io.inugami.core.alertings.senders.email.sender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Email
 * 
 * @author patrick_guillerm
 * @since 16 mars 2018
 */
public class EmailModel implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -1001114973632946955L;
    
    private String            from;
    
    private String            fromName;
    
    private List<String>      to;
    
    private String            subject;
    
    private String            body;
    
    private Date              date;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public EmailModel() {
        super();
    }
    
    public EmailModel(final String from, final String to, final String subject) {
        this.from = from;
        this.to = new ArrayList<>();
        if (to != null) {
            this.to.add(to);
        }
        this.subject = subject;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((subject == null) ? 0 : subject.hashCode());
        result = (prime * result) + ((to == null) ? 0 : to.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        
        if (!result && (obj != null) && (obj instanceof EmailModel)) {
            final EmailModel other = (EmailModel) obj;
            //@formatter:off
            result=    subject==null?other.getSubject()==null : subject.equals(other.getSubject())
                   &&  (to==null)?other.getTo()==null : to.equals(other.getTo());
            //@formatter:on
        }
        
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Email [from=");
        builder.append(from);
        builder.append(", fromName=");
        builder.append(fromName);
        builder.append(", to=");
        builder.append(to);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", body=");
        builder.append(body);
        builder.append(", date=");
        builder.append(date);
        builder.append(", getClass()=");
        builder.append(getClass());
        builder.append(", hashCode()=");
        builder.append(hashCode());
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public String getFromName() {
        return fromName;
    }
    
    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }
    
    public List<String> getTo() {
        return to;
    }
    
    public void setTo(final List<String> to) {
        this.to = to;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(final String subject) {
        this.subject = subject;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(final String body) {
        this.body = body;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
    
}
