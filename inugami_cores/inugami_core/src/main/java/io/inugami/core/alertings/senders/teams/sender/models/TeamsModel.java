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
package io.inugami.core.alertings.senders.teams.sender.models;

import flexjson.JSONSerializer;
import io.inugami.api.models.data.basic.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * https://docs.microsoft.com/en-us/microsoftteams/platform/concepts/connectors/connectors-using#creating-messages-through-office-365-connectors
 */
@SuppressWarnings({"java:S1948"})
public class TeamsModel implements JsonObject {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8053741922922261041L;

    private String text;

    private String title;

    private String summary;

    private String themeColor;

    private List<Section> sections;

    private List<PotentialAction> potentialAction;

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TeamsModel [text=");
        builder.append(text);
        builder.append(", title=");
        builder.append(title);
        builder.append(", summary=");
        builder.append(summary);
        builder.append(", themeColor=");
        builder.append(themeColor);
        builder.append(", sections=");
        builder.append(sections);
        builder.append(", potentialAction=");
        builder.append(potentialAction);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String convertToJson() {
        //@formatter:off
        return new JSONSerializer().exclude("*.class")
                                   .transform(new PotentialActionTransformer(), PotentialAction.class)
                                   .deepSerialize(this);
        //@formatter:on
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(final String themeColor) {
        this.themeColor = themeColor;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(final List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(final Section section) {
        if (sections == null) {
            sections = new ArrayList<>();
        }
        if (section != null) {
            sections.add(section);
        }
    }

    public List<PotentialAction> getPotentialAction() {
        return potentialAction;
    }

    public void setPotentialAction(final List<PotentialAction> potentialAction) {
        this.potentialAction = potentialAction;
    }

    public void addPotentialAction(final PotentialAction action) {
        if (potentialAction == null) {
            potentialAction = new ArrayList<>();
        }
        if (action != null) {
            potentialAction.add(action);
        }
    }

}
