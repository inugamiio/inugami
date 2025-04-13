package io.inugami.monitoring.springboot.api;

import io.inugami.api.exceptions.ErrorCode;
import org.zalando.problem.ProblemBuilder;

public interface ProblemAdditionalFieldBuilder {

    void addInformation(ProblemBuilder builder, final Throwable exception, final ErrorCode errorCode);
}
