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
package io.inugami.core.alertings.senders.slack.sender;

import flexjson.JSON;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SlackAttachment
 *
 * @author patrick_guillerm
 * @since 20 mars 2018
 */
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SlackAttachment implements Serializable {

    private static final long serialVersionUID = -5605422165406020249L;

    private String           fallback;
    private String           color;
    private String           pretext;
    @JSON(name = "author_name")
    private String           authorName;
    @JSON(name = "author_link")
    private String           authorLink;
    @JSON(name = "author_icon")
    private String           authorIcon;
    private String           title;
    @JSON(name = "title_link")
    private String           titleLink;
    private String           text;
    @JSON(name = "image_url")
    private String           imageUrl;
    @JSON(name = "thumb_url")
    private String           thumbUrl;
    private String           footer;
    @JSON(name = "footer_icon")
    private String           footerIcon;
    private long             ts;
    private List<SlackField> fields;


    public SlackAttachment cloneObject() {
        return this.toBuilder().build();
    }


    public static class SlackAttachmentBuilder {
        public SlackAttachmentBuilder addField(SlackField field) {
            if (this.fields == null) {
                this.fields = new ArrayList<>();
            }
            if (field != null) {
                this.fields.add(field);
            }
            return this;
        }
    }

}
