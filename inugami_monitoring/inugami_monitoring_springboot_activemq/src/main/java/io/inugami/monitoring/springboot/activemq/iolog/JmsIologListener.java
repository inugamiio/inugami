package io.inugami.monitoring.springboot.activemq.iolog;

import io.inugami.api.exceptions.*;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.tools.Chrono;
import io.inugami.api.monitoring.MdcService;
import io.inugami.api.monitoring.models.IoInfoDTO;
import io.inugami.api.spi.SpiLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.*;

import static io.inugami.api.functionnals.FunctionalUtils.applyIfNotNull;
import static io.inugami.monitoring.springboot.activemq.config.InugamiActiveMqConstants.*;

@Slf4j
public class JmsIologListener extends DefaultMessageListenerContainer {

    private static final String JMS            = "JMS";
    private static final int    STATUS_ERROR   = 500;
    private static final int    STATUS_SUCCESS = 200;
    private static final String SUCCESS        = "success";

    private final List<ErrorCodeResolver> errorCodeResolvers;

    public JmsIologListener() {
        errorCodeResolvers = SpiLoader.getInstance().loadSpiServicesByPriority(ErrorCodeResolver.class);
    }

    @Override
    protected void doExecuteListener(final Session session, final Message message) throws JMSException {
        MdcService.getInstance().clear();
        if (!isAcceptMessagesWhileStopping() && !isRunning()) {
            if (log.isWarnEnabled()) {
                log.warn("Rejecting received message because of listener container having been stopped in the meantime:" + message);
            }
            rollbackIfNecessary(session);
            throw new MessageRejectedWhileStoppingException();
        }

        IoInfoDTO ioInfo    = null;
        Chrono    chrono    = null;
        Throwable exception = null;

        try {
            ioInfo = traceIologIn(message);
            chrono = Chrono.startChrono();
            invokeListener(session, message);
        } catch (final JMSException | RuntimeException | Error ex) {
            rollbackIfNecessary(session);
            exception = ex;
            throw ex;
        } finally {
            chrono.stop();
            traceIologOut(ioInfo, chrono.getDurationInMillis(), exception);
            MdcService.getInstance().clear();
        }
        commitIfNecessary(session, message);
    }


    // ========================================================================
    // TRACE IOLOG
    // ========================================================================
    private IoInfoDTO traceIologIn(final Message message) {
        final Map<String, Collection<String>> headers = resolveHeaders(message);

        final IoInfoDTO result = IoInfoDTO.builder()
                                          .method(JMS)
                                          .url(resolveQueue(message))
                                          .addPayload(readPayload(message))
                                          .headers(headers)
                                          .build();

        final MdcService mdc = MdcService.getInstance()
                                         .ioinfoIoLog(result)
                                         .callType(JMS);

        applyIfNotNull(extractProperty(APPLICATION, message), value -> mdc.callFrom(value));
        applyIfNotNull(extractProperty(MdcService.MDCKeys.messageId, message), value -> mdc.messageId(value));
        applyIfNotNull(extractProperty(DEVICE_IDENTIFIER, message), value -> mdc.deviceIdentifier(value));
        applyIfNotNull(extractProperty(CORRELATION_ID, message), value -> mdc.correlationId(value));
        applyIfNotNull(extractProperty(CONVERSATION_ID, message), value -> mdc.conversationId(value));
        applyIfNotNull(extractProperty(TRACE_ID, message), value -> {
            mdc.traceId(value);
            mdc.requestId(value);
        });


        MdcService.getInstance().lifecycleIn(() -> Loggers.IOLOG.info(result.toString()));

        return result;
    }


    private void traceIologOut(final IoInfoDTO ioInfo, final long durationInMillis, final Throwable exception) {
        final MdcService                 mdc     = MdcService.getInstance();
        final IoInfoDTO.IoInfoDTOBuilder builder = ioInfo.toBuilder();

        builder.duration(durationInMillis);

        if (exception == null) {
            builder.status(STATUS_SUCCESS);
            builder.message(SUCCESS);
            mdc.globalStatusSuccess();
        } else {
            builder.status(STATUS_ERROR);
            builder.message(exception.getMessage());
            mdc.errorCode(resolveErrorCode(exception));
            mdc.globalStatusError();
        }


        final IoInfoDTO iolog = builder.build();
        mdc.ioinfoIoLog(iolog);

        if (exception == null) {
            MdcService.getInstance().lifecycleOut(() -> Loggers.IOLOG.info(iolog.toString()));
        } else {
            MdcService.getInstance().lifecycleOut(() -> Loggers.IOLOG.error(iolog.toString()));
        }
    }


    // ========================================================================
    // TOOLS
    // ========================================================================
    private String resolveQueue(final Message message) {
        try {
            return message.getJMSDestination() == null ? null : String.valueOf(message.getJMSDestination());
        } catch (final JMSException e) {
            return null;
        }
    }

    private String readPayload(final Message message) {
        String result = null;
        if (message instanceof TextMessage) {
            try {
                result = ((TextMessage) message).getText();
            } catch (final JMSException e) {
                return null;
            }
        }
        return result;
    }

    private Map<String, Collection<String>> resolveHeaders(final Message message) {
        final Map<String, Collection<String>> result = new LinkedHashMap<>();

        final Enumeration properties;
        try {
            properties = message.getPropertyNames();
            while (properties.hasMoreElements()) {
                final String key      = String.valueOf(properties.nextElement());
                final Object rawValue = message.getObjectProperty(key);
                final String value    = rawValue == null ? null : String.valueOf(rawValue);
                if (value != null) {
                    result.put(key, List.of(value));
                }
            }
        } catch (final JMSException e) {
        }
        return result;
    }


    private String extractProperty(final MdcService.MDCKeys key, final Message message) {
        return extractProperty(key.name(), message);
    }

    private String extractProperty(final String key, final Message message) {
        try {
            return message.getStringProperty(key);
        } catch (final JMSException e) {
            throw null;
        }
    }

    private ErrorCode resolveErrorCode(final Throwable exception) {
        if (errorCodeResolvers == null) {
            return DefaultErrorCode.buildUndefineError();
        } else if (exception instanceof ExceptionWithErrorCode) {
            return ((ExceptionWithErrorCode) exception).getErrorCode();
        } else {
            return DefaultErrorCode.buildUndefineError();
        }
    }


    // ========================================================================
    // TOOLS
    // ========================================================================
    public static class MessageRejectedWhileStoppingException extends UncheckedException {

    }
}
