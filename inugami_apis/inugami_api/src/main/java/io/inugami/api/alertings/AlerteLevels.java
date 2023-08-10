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
package io.inugami.api.alertings;

import lombok.Getter;

import java.util.regex.Pattern;

/**
 * AlerteLevels
 *
 * @author patrick_guillerm
 * @since 21 déc. 2017
 */
@SuppressWarnings({"java:S135"})
@Getter
public enum AlerteLevels {
    //@formatter:off
    FATAL   (1000000, ".*fatal.*"  ,"#980101"),
    ERROR   ( 100000, ".*error.*"  ,"#d40000"),
    WARN    (  10000, ".*warn.*"   ,"#fb8b00"),
    INFO    (   1000, ".*info.*"   ,"#339900"),
    DEBUG   (    100, ".*debug.*"  ,"#013397"),
    TRACE   (     10 , ".*trace.*" ,"#666666"),
    OFF     (      0 , "^OFF$"     ,"#cccccf"),
    UNDEFINE(      1,"#333333");
    //@formatter:on
    private static final AlerteLevels[] values = values();

    private int level;

    private Pattern regex;

    private String color;

    private AlerteLevels(final int level, final String color) {
        this.level = level;
        regex = null;
        this.color = color;
    }

    private AlerteLevels(final int level, final String regex, final String color) {
        this.level = level;
        this.regex = Pattern.compile(regex);
        this.color = color;
    }

    public static synchronized AlerteLevels getAlerteLevel(final String level) {
        AlerteLevels result = UNDEFINE;
        if (level != null) {
            final String localLevel = level.toLowerCase().trim();

            for (final AlerteLevels item : values) {
                if (UNDEFINE == item) {
                    break;
                } else if (item.regex.matcher(localLevel).matches()) {
                    result = item;
                    break;
                }
            }
        }
        return result;
    }

    public static AlerteLevels getLevel(final Double levelValue) {
        if (levelValue == null) {
            return AlerteLevels.UNDEFINE;
        }

        for (AlerteLevels value : values) {
            if (levelValue.intValue() >= value.getLevel()) {
                return value;
            }
        }
        return AlerteLevels.UNDEFINE;
    }
}
