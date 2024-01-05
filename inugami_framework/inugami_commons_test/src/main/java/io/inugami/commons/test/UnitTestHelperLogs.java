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

import io.inugami.commons.test.api.LineMatcher;
import io.inugami.commons.test.dto.AssertLogContext;
import io.inugami.commons.test.logs.LogTestAppender;
import io.inugami.framework.api.monitoring.logs.DefaultLogListener;
import io.inugami.framework.interfaces.exceptions.UncheckedException;
import io.inugami.framework.interfaces.functionnals.VoidFunctionWithException;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.monitoring.MdcServiceSpiFactory;
import io.inugami.framework.interfaces.monitoring.logger.BasicLogEvent;
import io.inugami.framework.interfaces.monitoring.logger.LogListener;
import lombok.experimental.UtilityClass;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

import static io.inugami.commons.test.UnitTestHelperText.assertText;


@SuppressWarnings({"java:S112", "java:S5361"})
@UtilityClass
public class UnitTestHelperLogs {

    public static final String LINE         = "\n";
    public static final String WINDOWS_LINE = "\r";
    public static final String EMPTY        = "";
    public static final String TAB          = "    ";

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
            MdcServiceSpiFactory.getInstance().clear();
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

        String log = context.getLogRenderer() == null
                ? processLogsRendering(logs)
                : context.getLogRenderer().apply(logs);

        assertText(log, logsContent, matchers);
    }

    private static String processLogsRendering(final List<BasicLogEvent> logs) {
        JsonBuilder json = new JsonBuilder();
        json.openList().line();

        final Iterator<BasicLogEvent> iterator = Optional.ofNullable(logs)
                                                         .orElse(new ArrayList<>())
                                                         .iterator();

        while (iterator.hasNext()) {
            final BasicLogEvent log = iterator.next();
            json.write(processLogRendering(log));
            if (iterator.hasNext()) {
                json.addSeparator().line();
            }
        }

        json.line().closeList();
        return json.toString();
    }

    private String processLogRendering(final BasicLogEvent log) {
        JsonBuilder json = new JsonBuilder();
        json.write(TAB).openObject().line();

        json.write(TAB).write(TAB).addField("loggerName").valueQuot(log.getLoggerName()).addSeparator().line();
        json.write(TAB).write(TAB).addField("level").valueQuot(log.getLevel()).addSeparator().line();

        final Map<String, Serializable> mdc = Optional.ofNullable(log.getMdc()).orElse(new HashMap<>());
        if (mdc.isEmpty()) {
            json.write(TAB).write(TAB).addField("mdc").openObject().closeObject().line();
        } else {
            json.write(TAB).write(TAB).addField("mdc").openObject().line();

            final List<String> keys = new ArrayList<>(mdc.keySet());
            Collections.sort(keys);
            final Iterator<String> mdcIterator = keys.iterator();

            while (mdcIterator.hasNext()) {
                final String key = mdcIterator.next();
                json.write(TAB).write(TAB).write(TAB).addField(key).valueQuot(mdc.get(key));
                if (mdcIterator.hasNext()) {
                    json.addSeparator().line();
                }
            }
            json.line().write(TAB).write(TAB).closeObject().addSeparator().line();
        }

        json.write(TAB).write(TAB).addField("message");
        if (log.getMessage().contains(LINE)) {
            final Iterator<String> linesIterator = Arrays.asList(log.getMessage().split(LINE)).iterator();
            json.openList().line();
            while (linesIterator.hasNext()) {
                final String line = linesIterator.next();
                json.write(TAB).write(TAB).write(TAB).valueQuot(cleanLine(line));
                if (linesIterator.hasNext()) {
                    json.addSeparator().line();
                }
            }
            json.line()
                .write(TAB).write(TAB).closeList()
                .line();
        } else {
            json.valueQuot(cleanLine(log.getMessage()));
            json.line();
        }

        json.write(TAB).closeObject();
        return json.toString();
    }

    private static String cleanLine(final String message) {
        return message == null ? null : message.replaceAll("\"", "\\\\\"").replaceAll(WINDOWS_LINE, EMPTY);
    }


}
