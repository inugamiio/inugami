package io.inugami.framework.interfaces.exceptions;


import org.zalando.problem.ProblemBuilder;

public interface ProblemAdditionalFieldBuilder {

    void addInformation(ProblemBuilder builder, final Throwable exception, final ErrorCode errorCode);
}
