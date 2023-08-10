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
package io.inugami.core.providers.gitlab.models;

import flexjson.JSON;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GitlabMergeRequest implements Serializable {

    private static final long serialVersionUID = -3023531764346308334L;
    
    @JSON(name = "created_at")
    private String      creationDate;
    @JSON(name = "project_id")
    private int         projectId;
    private String      projectName;
    @EqualsAndHashCode.Include
    private int         id;
    private String      title;
    private String      state;
    private int         upvotes;
    private int         downvotes;
    private MergeAuthor author;

}
