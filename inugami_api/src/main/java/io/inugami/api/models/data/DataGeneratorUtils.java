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
package io.inugami.api.models.data;

import java.util.Date;
import java.util.Random;

/**
 * DataGeneratorUtils
 *
 * @author patrick_guillerm
 * @since 26 janv. 2018
 */
public final class DataGeneratorUtils {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public static final String[] LOREM = {"lorem", "ac", "adipiscing", "aliquam", "aliquet", "sagittis", "amet",
            "consectetur", "ante", "arcu", "cras", "nullam", "at", "auctor", "augue",
            "bibendum", "morbi", "blandit", "commodo", "condimentum", "duis", "sed",
            "congue", "risus", "consequat", "convallis", "donec", "cursus", "in",
            "dapibus", "diam", "et", "dictumst", "nulla", "vestibulum", "dignissim",
            "dis", "dolor", "suspendisse", "dui", "efficitur", "tellus", "egestas",
            "eget", "phasellus", "eleifend", "proin", "elementum", "elit", "nunc",
            "enim", "ut", "erat", "pellentesque", "eros", "est", "eu", "euismod", "ex",
            "facilisi", "vivamus", "facilisis", "faucibus", "felis", "praesent",
            "fermentum", "feugiat", "finibus", "fringilla", "gravida", "habitasse",
            "hac", "iaculis", "id", "imperdiet", "interdum", "ipsum", "justo",
            "integer", "lacinia", "lacus", "laoreet", "leo", "libero", "ligula",
            "lobortis", "lorem", "mauris", "magna", "magnis", "malesuada", "massa",
            "sit", "etiam", "nam", "maximus", "metus", "mi", "quisque", "molestie",
            "mollis", "montes", "nascetur", "mus", "natoque", "nec", "neque", "nibh",
            "quis", "nisi", "nisl", "fusce", "non", "pharetra", "vehicula", "odio",
            "orci", "ornare", "parturient", "penatibus", "placerat", "platea", "porta",
            "porttitor", "posuere", "pretium", "pulvinar", "purus", "quam",
            "ridiculus", "sapien", "scelerisque", "sem", "vitae", "sociis", "sodales",
            "cum", "suscipit", "tempor", "tempus", "tincidunt", "tortor", "tristique",
            "turpis", "ullamcorper", "ultrices", "urna", "varius", "vel", "velit",
            "venenatis", "viverra", "volutpat", "vulputate"};

    private static final int LENGTH = LOREM.length;

    private static final Random RANDOM = new Random((new Date()).getTime());

    //@formatter:off
    public static final  String[] CATEGORY        = {"lorem", "cursus", "ipsum", "justo", "vehicula", "tempus", "ridiculus", "tortor"};
    private static final int      LENGTH_CATEGORY = CATEGORY.length;
    //@formatter:on

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private DataGeneratorUtils() {
        super();
    }

    // =========================================================================
    // GENERATE WORD
    // =========================================================================
    public static String getWord() {
        return LOREM[getRandomBetween(0, LENGTH - 1)];
    }

    public static String getCategory() {
        return CATEGORY[getRandomBetween(0, LENGTH_CATEGORY - 1)];
    }

    public static String getLabel() {
        return getPhrase(5, 10, true);
    }

    public static String getPhrase() {
        return getPhrase(5, 10, false);
    }

    public static String getSection() {
        return getSection(getRandomBetween(3, 10), 5, 30);
    }

    public static String getSection(final int nbLine, final int nbWordMin, final int nbWordMax) {
        final StringBuilder result = new StringBuilder();

        for (int line = 0; line < nbLine; line++) {
            result.append(getPhrase(nbWordMin, nbWordMax, false));
            result.append(' ');
        }
        return result.toString();
    }

    public static String getPhrase(final int nbWordMin, final int nbWordMax, final boolean isLabel) {
        final int nbWord = getRandomBetween(nbWordMin, nbWordMax);

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < nbWord; i++) {
            final String word = getWord();
            if (i == 0) {
                result.append(word.substring(0, 1).toUpperCase());
                result.append(word.substring(1));
            } else {
                result.append(" ");
                result.append(word);
            }
        }
        if (!isLabel) {
            result.append(".");
        }
        return result.toString();
    }

    // =========================================================================
    // getDouble
    // =========================================================================
    public static double getDouble(final double min, final double max) {
        final double diff = max - min;
        return min + (Math.random() * diff);
    }

    // =========================================================================
    // getRandomBetween
    // =========================================================================
    public static int getRandomBetween(final int start, final int end) {
        final int diff = end - start;
        return start + new Double(Math.random() * diff).intValue();
    }

}
