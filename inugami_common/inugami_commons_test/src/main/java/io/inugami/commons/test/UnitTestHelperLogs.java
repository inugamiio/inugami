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
package io.inugami.commons.test;

import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.functionnals.VoidFunctionWithException;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.logs.BasicLogEvent;
import io.inugami.api.monitoring.logs.DefaultLogListener;
import io.inugami.api.monitoring.logs.LogListener;
import io.inugami.commons.test.api.LineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.commons.test.logs.LogTestAppender;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static io.inugami.commons.test.UnitTestHelperText.assertText;

@SuppressWarnings({"java:S112"})
@UtilityClass
public class UnitTestHelperLogs {

    static void assertLogs(final VoidFunctionWithException process,
                           final Class<?> objClass,
                           final String logs,
                           final LineMatcher... matchers) {
        assertLogs(AssertLogContext.builder()
                                   .process(process)
                                   .addClass(objClass)
                                   .lineMatchers(Arrays.asList(matchers))
                                   .logs(logs)
                                   .build());
    }

    static void assertLogsIntegration(final VoidFunctionWithException process,
                                      final Class<?> objClass,
                                      final String logs,
                                      final LineMatcher... matchers) {
        assertLogs(AssertLogContext.builder()
                                   .process(process)
                                   .addClass(objClass)
                                   .integrationTestEnabled()
                                   .lineMatchers(Arrays.asList(matchers))
                                   .logs(logs)
                                   .build());
    }


    static void assertLogs(final VoidFunctionWithException process,
                           final String pattern,
                           final String logs,
                           final LineMatcher... matchers) {
        assertLogs(AssertLogContext.builder()
                                   .process(process)
                                   .addPattern(pattern)
                                   .lineMatchers(Arrays.asList(matchers))
                                   .logs(logs)
                                   .build());
    }

    static void assertLogsIntegration(final VoidFunctionWithException process,
                                      final String pattern,
                                      final String logs,
                                      final LineMatcher... matchers) {
        assertLogs(AssertLogContext.builder()
                                   .process(process)
                                   .addPattern(pattern)
                                   .integrationTestEnabled()
                                   .lineMatchers(Arrays.asList(matchers))
                                   .logs(logs)
                                   .build());
    }

    static void assertLogs(final AssertLogContext context) {
        validateContext(context);
        cleanMdc(context);

        final List<BasicLogEvent> logs      = new ArrayList<>();
        final List<LogListener>   listeners = createListeners(context, logs::add);
        listeners.forEach(LogTestAppender::register);

        try {
            context.getProcess().process();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            listeners.forEach(LogTestAppender::removeListener);
            cleanMdc(context);
            assertLogsContent(logs, context);
        }
    }

    private static void cleanMdc(final AssertLogContext context) {
        if (context.getCleanMdc() == null || context.getCleanMdc().booleanValue()) {
            MdcService.getInstance().clear();
        }
    }

    private static void validateContext(final AssertLogContext context) {
        if ((context.getClasses() == null || context.getClasses().isEmpty()) &&
            (context.getPatterns() == null || context.getPatterns().isEmpty())) {
            throw new UncheckedException("class or pattern required to intercept logs");
        }

        if (context.getLogs() == null && context.getPath() == null) {
            throw new UncheckedException("reference logs or reference logs path is required");
        }

        if (context.getProcess() == null) {
            throw new UncheckedException("process is required");
        }
    }

    private static List<LogListener> createListeners(final AssertLogContext context,
                                                     final Consumer<BasicLogEvent> appender) {
        final List<LogListener> result = new ArrayList<>();

        for (final Class<?> objClass : Optional.ofNullable(context.getClasses()).orElse(new ArrayList<>())) {
            result.add(new DefaultLogListener(objClass, appender));
        }
        for (final String pattern : Optional.ofNullable(context.getPatterns()).orElse(new ArrayList<>())) {
            result.add(new DefaultLogListener(pattern, appender));
        }
        return result;
    }


    private static void assertLogsContent(final List<BasicLogEvent> logs,
                                          final AssertLogContext context) {

        String logsContent = context.getLogs();
        if (context.getPath() != null) {
            logsContent = context.getIntegrationTest() != null && context.getIntegrationTest().booleanValue()
                    ? UnitTestHelperFile.readFileIntegration(context.getPath())
                    : UnitTestHelperFile.readFileRelative(context.getPath());
        }
        final LineMatcher[] matchers = Optional.ofNullable(context.getLineMatchers())
                                               .orElse(new ArrayList<>())
                                               .toArray(new LineMatcher[]{});
        assertText(logs, logsContent, matchers);
    }

}
