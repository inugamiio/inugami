package io.inugami.commons.spring.exception;

import org.springframework.http.ResponseEntity;
import org.zalando.problem.ThrowableProblem;

import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandlerService {

    ResponseEntity<ThrowableProblem> manageException(final Throwable throwable);

    void manageException(final Throwable throwable, final HttpServletResponse response);

}
