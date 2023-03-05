package io.inugami.commons.test;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.regex.Pattern;

public class DefaultLineAssertion {

    public static LineAssertion skipLine(final int line){
        return new Skip(line);
    }
    @Builder
    @AllArgsConstructor
    public static class Skip implements LineAssertion{
        private final int lineNumber;


        @Override
        public boolean accept(final int lineNumber,final String value) {
            return lineNumber==this.lineNumber;
        }

        @Override
        public boolean isMatching(final String value, final String ref) {
            return true;
        }

        @Override
        public boolean isSkipped(final String value) {
            return true;
        }
    }

    @Builder
    @AllArgsConstructor
    public static class Regex implements LineAssertion{
        private final int     lineNumber;
        private final Pattern regex;

        public Regex(final int lineNumber, final String regex){
            this.regex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.lineNumber = lineNumber;
        }
        @Override
        public boolean accept(final int lineNumber,final String value) {
            return lineNumber==this.lineNumber;
        }

        @Override
        public boolean isMatching(final String value, final String ref) {
            return regex.matcher(value).matches();
        }

        @Override
        public boolean isSkipped(final String value) {
            return false;
        }
    }
}
