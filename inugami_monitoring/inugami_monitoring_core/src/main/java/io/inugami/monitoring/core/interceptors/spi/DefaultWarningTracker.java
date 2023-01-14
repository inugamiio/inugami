package io.inugami.monitoring.core.interceptors.spi;

import io.inugami.api.exceptions.Warning;
import io.inugami.api.exceptions.WarningTracker;
import io.inugami.api.monitoring.MdcService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultWarningTracker implements WarningTracker {
    @Override
    public void track(final List<Warning> warning) {
        final List<String> codes = new ArrayList<>(warning.stream().map(Warning::getWarningCode).collect(Collectors.toList()));
        Collections.sort(codes);

        final String warningCode = String.join("_", codes);
        final MdcService mdc      = MdcService.getInstance();

        mdc.warning(warningCode);

        for(Warning warn : warning){
            mdc.setMdc("warn_"+warn.getWarningCode(), warn.getMessage());
        }
    }
}
