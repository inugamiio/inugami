package io.inugami.monitoring.springboot.api;


import io.inugami.framework.interfaces.exceptions.ErrorCode;
import org.zalando.problem.ProblemBuilder;

public interface ProblemAdditionalFieldBuilder {

    void addInformation(ProblemBuilder builder, final Throwable exception, final ErrorCode errorCode);
}
