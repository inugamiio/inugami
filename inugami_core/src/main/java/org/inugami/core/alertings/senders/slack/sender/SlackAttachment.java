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
package org.inugami.core.alertings.senders.slack.sender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import flexjson.JSON;

/**
 * SlackAttachment
 * 
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
public class SlackAttachment implements Serializable {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -5605422165406020249L;
    
    private String            fallback;
    
    private String            color;
    
    private String            pretext;
    
    @JSON(name = "author_name")
    private String            authorName;
    
    @JSON(name = "author_link")
    private String            authorLink;
    
    @JSON(name = "author_icon")
    private String            authorIcon;
    
    private String            title;
    
    @JSON(name = "title_link")
    private String            titleLink;
    
    private String            text;
    
    @JSON(name = "image_url")
    private String            imageUrl;
    
    @JSON(name = "thumb_url")
    private String            thumbUrl;
    
    private String            footer;
    
    @JSON(name = "footer_icon")
    private String            footerIcon;
    
    private long              ts;
    
    private List<SlackField>  fields;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SlackAttachment() {
    }
    
    public SlackAttachment(final String color, final String title) {
        super();
        this.color = color;
        this.title = title;
    }
    
    public SlackAttachment(final String color, final String title, final String priority) {
        super();
        this.color = color;
        this.title = title;
        
    }
    
    private SlackAttachment(final String fallback, final String color, final String pretext, final String authorName,
                            final String authorLink, final String authorIcon, final String title,
                            final String titleLink, final String text, final String imageUrl, final String thumbUrl,
                            final String footer, final String footerIcon, final long ts,
                            final List<SlackField> fields) {
        super();
        this.fallback = fallback;
        this.color = color;
        this.pretext = pretext;
        this.authorName = authorName;
        this.authorLink = authorLink;
        this.authorIcon = authorIcon;
        this.title = title;
        this.titleLink = titleLink;
        this.text = text;
        this.imageUrl = imageUrl;
        this.thumbUrl = thumbUrl;
        this.footer = footer;
        this.footerIcon = footerIcon;
        this.ts = ts;
        this.fields = fields;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public SlackAttachment cloneObject() {
        final List<SlackField> newFields = new ArrayList<>();
        if (fields != null) {
            for (final SlackField item : fields) {
                newFields.add(item.cloneObject());
            }
        }
        return new SlackAttachment(fallback, color, pretext, authorName, authorLink, authorIcon, title, titleLink, text,
                                   imageUrl, thumbUrl, footer, footerIcon, ts, newFields);
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SlackAttachment [fallback=");
        builder.append(fallback);
        builder.append(", color=");
        builder.append(color);
        builder.append(", pretext=");
        builder.append(pretext);
        builder.append(", authorName=");
        builder.append(authorName);
        builder.append(", authorLink=");
        builder.append(authorLink);
        builder.append(", authorIcon=");
        builder.append(authorIcon);
        builder.append(", title=");
        builder.append(title);
        builder.append(", titleLink=");
        builder.append(titleLink);
        builder.append(", text=");
        builder.append(text);
        builder.append(", imageUrl=");
        builder.append(imageUrl);
        builder.append(", thumbUrl=");
        builder.append(thumbUrl);
        builder.append(", footer=");
        builder.append(footer);
        builder.append(", footerIcon=");
        builder.append(footerIcon);
        builder.append(", ts=");
        builder.append(ts);
        builder.append(", fields=");
        builder.append(fields);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    
    public String getFallback() {
        return fallback;
    }
    
    public void setFallback(final String fallback) {
        this.fallback = fallback;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    public String getPretext() {
        return pretext;
    }
    
    public void setPretext(final String pretext) {
        this.pretext = pretext;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }
    
    public String getAuthorLink() {
        return authorLink;
    }
    
    public void setAuthorLink(final String authorLink) {
        this.authorLink = authorLink;
    }
    
    public String getAuthorIcon() {
        return authorIcon;
    }
    
    public void setAuthorIcon(final String authorIcon) {
        this.authorIcon = authorIcon;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getTitleLink() {
        return titleLink;
    }
    
    public void setTitleLink(final String titleLink) {
        this.titleLink = titleLink;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getThumbUrl() {
        return thumbUrl;
    }
    
    public void setThumbUrl(final String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    
    public String getFooter() {
        return footer;
    }
    
    public void setFooter(final String footer) {
        this.footer = footer;
    }
    
    public String getFooterIcon() {
        return footerIcon;
    }
    
    public void setFooterIcon(final String footerIcon) {
        this.footerIcon = footerIcon;
    }
    
    public long getTs() {
        return ts;
    }
    
    public void setTs(final long ts) {
        this.ts = ts;
    }
    
    public List<SlackField> getFields() {
        return fields;
    }
    
    public void setFields(final List<SlackField> fields) {
        this.fields = fields;
    }
    
    public void addField(final SlackField slackField) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        if (slackField != null) {
            fields.add(slackField);
        }
    }
    
}
