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
package io.inugami.core.services.sse;

import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.Tuple;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.processors.fifo.FifoProcessorGlobaleProcessor;
import io.inugami.api.processors.fifo.FifoProcessorService;
import io.inugami.api.providers.concurrent.LifecycleBootstrap;
import io.inugami.commons.threads.RunAndCloseService;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.OutboundSseEvent.Builder;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SseServiceSender
 *
 * @author patrick_guillerm
 * @since 22 sept. 2017
 */
class SseServiceSender implements LifecycleBootstrap, FifoProcessorGlobaleProcessor<SendSseEvent> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final List<Tuple<Matcher, String>> MATCHERS = initCleanMatchers();

    private final static String ADMIN_EVENT = "administration";

    private final FifoProcessorService<SendSseEvent, SendSseEvent> fifo;

    private final SseBroadcaster broadcaster;

    private final Sse sse;

    // =========================================================================
    // INIT
    // =========================================================================
    private static List<Tuple<Matcher, String>> initCleanMatchers() {
        final List<Tuple<Matcher, String>> result = new ArrayList<>();
        result.add(buildMatcher("\\[\\s*,\\s*[{]", "[{"));
        return result;
    }

    private static Tuple<Matcher, String> buildMatcher(final String regex, final String replacement) {
        final Matcher matcher = Pattern.compile("\\[\\s*,\\s*[{]").matcher("");
        return new Tuple<Matcher, String>(matcher, replacement);
    }

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public SseServiceSender(final SseBroadcaster broadcaster, final Sse sse) {
        super();
        this.broadcaster = broadcaster;
        this.sse = sse;
        fifo = new FifoProcessorService<>(this, 1000, "sse-sender");
    }

    // =========================================================================
    // RUN
    // =========================================================================
    @Override
    public void start() {
        fifo.start();
    }

    @Override
    public void shutdown() {
        fifo.shutdown();
    }

    // =========================================================================
    // INPUT
    // =========================================================================
    void addEvent(final SendSseEvent event) {
        if (event != null) {
            fifo.add(event);
        }
    }

    @Override
    public void process(final BlockingQueue<SendSseEvent> queue) {
        final List<SendSseEvent> data = extractData(queue);

        final Map<String, List<SendSseEvent>> multiEvent = new LinkedHashMap<>();
        for (final SendSseEvent event : data) {
            final String channelName = event.getChannel() == null ? "sse" : event.getChannel();

            List<SendSseEvent> channel = multiEvent.get(channelName);
            if (channel == null) {
                channel = new ArrayList<>();
                multiEvent.put(channelName, channel);
            }
            channel.add(event);
        }

        for (final Map.Entry<String, List<SendSseEvent>> entry : multiEvent.entrySet()) {
            final MultiSseEvent event = new MultiSseEvent(entry.getKey(), entry.getValue());
            sendEvent(event);
        }

    }

    private synchronized List<SendSseEvent> extractData(final BlockingQueue<SendSseEvent> queue) {
        final List<SendSseEvent> data    = new ArrayList<>();
        final int                nbItems = queue.drainTo(data);
        if (Loggers.SSE.isDebugEnabled()) {
            Loggers.SSE.debug("sse event to process : {}", nbItems);
        }
        return data;
    }

    private synchronized void sendEvent(final MultiSseEvent event) {
        final String  json    = event.convertToJson();
        final Builder builder = sse.newEventBuilder();
        builder.name(event.getChannel());
        builder.data(String.class, json);
        builder.mediaType(MediaType.APPLICATION_JSON_TYPE);
        final OutboundSseEvent sseEvent = builder.build();

        final Callable<Void> task = buildTask(event, sseEvent, json);
        new RunAndCloseService<Void>("SSE_SENDER", 1000L, 1, task).run();

    }

    private Callable<Void> buildTask(final MultiSseEvent event, final OutboundSseEvent sseEvent, final String json) {
        return () -> {
            final boolean notAdmin = !ADMIN_EVENT.equals(event.getChannel());
            final Chrono  chrono   = Chrono.startChrono();

            if (notAdmin) {
                Loggers.SSE.info("> prepare send  data ....");
            }

            broadcaster.broadcast(sseEvent);
            chrono.stop();

            if (notAdmin) {
                Loggers.SSE.info("[{}ms] send  data : {} : {} -> {}", chrono.getDurationInMillis(),
                                 System.currentTimeMillis(), event.getChannel(), json);
            }
            return null;
        };
    }

}
