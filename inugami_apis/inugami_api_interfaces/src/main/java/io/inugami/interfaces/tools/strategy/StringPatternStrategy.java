package io.inugami.interfaces.tools.strategy;

import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

@Getter
@ToString
public class StringPatternStrategy implements Strategy<String, String> {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final boolean acceptAll;
    private final Pattern pattern;
    private final String  result;
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public StringPatternStrategy(final String result) {
        this.acceptAll = true;
        this.pattern = null;
        this.result = result;
    }

    public StringPatternStrategy(final String pattern, final String result) {
        this.acceptAll = pattern == null;
        this.pattern = pattern == null ? null : Pattern.compile(pattern);
        this.result = result;
    }

    // =========================================================================
    // API
    // =========================================================================
    @Override
    public boolean accept(final String inputData) {
        return acceptAll ? acceptAll : pattern.matcher(inputData).matches();
    }

    @Override
    public String process(final String inputData) throws StrategyException {
        return result;
    }
}
