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
package io.inugami.commons.engine.extractor;

import io.inugami.api.exceptions.Asserts;
import io.inugami.commons.engine.extractor.srategies.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExtractCommand
 *
 * @author patrick_guillerm
 * @since 22 mai 2018
 */
@SuppressWarnings({"java:S6395", "java:S6353"})
public class ExtractCommand {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Pattern REGEX = Pattern.compile("(?:\\[)([0-9]+)(?:\\])");

    private final String fieldName;

    private final boolean isIteration;

    private final int iterationIndex;

    //@formatter:off
    private final ExtractCommandStrategy[] strategies = {
            new LengthExtractCommandStrategy(),
            new ListItemExtractCommandStrategy(),
            new BindingExtractCommandStrategy(),
            new MapExtractCommandStrategy(),
            new PojoExtractCommandStrategy()
    };
    //@formatter:on

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ExtractCommand(final String fieldName, final boolean isIteration, final int iterationIndex) {
        super();
        this.fieldName = fieldName;
        this.isIteration = isIteration;
        this.iterationIndex = iterationIndex;
    }

    public ExtractCommand(final String cmd) {
        Asserts.assertNotEmpty("command expression mustn't be null!", cmd);
        final Matcher matcher = REGEX.matcher(cmd);
        isIteration = matcher.matches();
        if (isIteration) {
            fieldName = null;
            iterationIndex = Integer.parseInt(matcher.group(1));
        } else {
            fieldName = cmd.trim();
            iterationIndex = -1;
        }
    }

    // javax.script.Bindings
    // =========================================================================
    // ACTION
    // =========================================================================
    public Object extract(final Object value) {
        Object result = null;
        if (value != null) {
            final ExtractCommandStrategy strategy = selectStrategy(value);
            if (strategy != null) {
                result = strategy.process(value, this);
            }
        }
        return result;
    }

    private ExtractCommandStrategy selectStrategy(final Object value) {
        ExtractCommandStrategy result = null;
        for (final ExtractCommandStrategy item : strategies) {
            if (item.accept(value, this)) {
                result = item;
                break;
            }
        }
        return result;
    }

    // =========================================================================
    // OVERRIDE
    // =========================================================================
    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + ((fieldName == null) ? 0 : fieldName.hashCode());
        result = (prime * result) + iterationIndex;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = this == obj;
        if (!result && (obj != null) && (obj instanceof ExtractCommand)) {
            final ExtractCommand other = (ExtractCommand) obj;
            //@formatter:off
            result = (fieldName == null ? other.getFieldName() == null : fieldName.equals(other.getFieldName()))
                    && (iterationIndex == other.getIterationIndex());
            //@formatter:on
        }

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ExtractCommand [fieldName=");
        builder.append(fieldName);
        builder.append(", isIteration=");
        builder.append(isIteration);
        builder.append(", iterationIndex=");
        builder.append(iterationIndex);
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public String getFieldName() {
        return fieldName;
    }

    public boolean isIteration() {
        return isIteration;
    }

    public int getIterationIndex() {
        return iterationIndex;
    }

}
