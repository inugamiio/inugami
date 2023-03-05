package io.inugami.logs.obfuscator.encoder;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Slf4j
public class JoeTest {

    public static class TestValueMasker implements ValueMasker {
        private static final Pattern REGEX = Pattern.compile("(password\\s*)([=:])(?:[^\\s])");
        public static final String PASSWORD = "password";
        public static final String REPLACEMENT = "$1$2****";

        @Override
        public Object mask(JsonStreamContext context, Object value) {
            return shouldObfuscate(value)? obfuscate((String)value):null;
        }

        protected boolean shouldObfuscate(Object value){
            return value instanceof String &&  ((String)value).contains(PASSWORD);
        }
        protected Object obfuscate(final String value) {
            return REGEX.matcher(value).replaceAll(REPLACEMENT);
        }
    }


    private static class JsonStreamContext{

    }

    private static interface ValueMasker{
        Object mask(JsonStreamContext context, Object value) ;
    }
}
