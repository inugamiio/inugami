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
package io.inugami.framework.interfaces.models.tools;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Chrono
 *
 * @author patrick_guillerm
 * @since 26 mai 2017
 */
@SuppressWarnings({"java:S6355", "java:S1123", "java:S1133"})
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
public final class Chrono implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1317921298017444143L;

    private static final long UNDEF = -1;

    @EqualsAndHashCode.Include
    private long startTime;

    private long startTimeMs;

    private long stopTime;

    @EqualsAndHashCode.Include
    private long stopTimeMs;

    private boolean running = false;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Chrono() {
        this(UNDEF, UNDEF);
    }

    protected Chrono(final long startTime, final long stopTime) {
        super();
        this.startTime = startTime;
        this.stopTime = stopTime;
    }


    // =========================================================================
    // METHODS
    // =========================================================================
    public static Chrono startChrono() {
        return new Chrono().start();
    }

    public Chrono start() {
        startTime = System.nanoTime();
        startTimeMs = System.currentTimeMillis();
        running = true;
        return this;
    }

    public Chrono stop() {
        stopTime = System.nanoTime();
        stopTimeMs = System.currentTimeMillis();
        running = false;
        return this;
    }

    public Chrono snapshot() {
        if (running) {
            stopTime = System.nanoTime();
        }
        return this;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    public Chrono cloneObj() {
        return new Chrono(startTime, startTimeMs, stopTime, stopTimeMs, running);
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getDuration() {
        return getDurationNano() / 1000000;
    }

    public long getDurationNano() {
        return stopTime == -1 ? -1 : stopTime - startTime;
    }
}
