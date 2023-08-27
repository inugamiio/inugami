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
package io.inugami.core.providers.kibana.models;

import flexjson.JSON;

import java.util.List;

/**
 * Hits
 *
 * @author patrick_guillerm
 * @since 24 oct. 2016
 */
@SuppressWarnings({"java:S1700"})
public class Hits {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private long total;

    @JSON(name = "max_score")
    private Double maxScore;

    private List<Hit> hits;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Hits() {
        super();
    }

    public Hits(final long total, final Double maxScore, final List<Hit> hits) {
        super();
        this.total = total;
        this.maxScore = maxScore;
        this.hits = hits;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(final Double maxScore) {
        this.maxScore = maxScore;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(final List<Hit> hits) {
        this.hits = hits;
    }
}
