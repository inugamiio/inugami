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
package io.inugami.framework.configuration.services.functions;

import java.util.Arrays;

/**
 * FunctionData
 *
 * @author patrick_guillerm
 * @since 17 août 2017
 */
public class FunctionData {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int start;

    private final int end;

    private final String functionName;

    private final String[] parameters;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public FunctionData(final String functionName, final String[] parameters, final int start, final int end) {
        super();
        this.start = start;
        this.end = end;
        this.functionName = functionName;
        this.parameters = parameters;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("FunctionData [functionName=");
        builder.append(functionName);
        builder.append(", parameters=");
        builder.append(Arrays.toString(parameters));
        builder.append(", start=").append(start);
        builder.append(", end=").append(end);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getFunctionName() {
        return functionName;
    }

    public String[] getParameters() {
        return parameters;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

}
