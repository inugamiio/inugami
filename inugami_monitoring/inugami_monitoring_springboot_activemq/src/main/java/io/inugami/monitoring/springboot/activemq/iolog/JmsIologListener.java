package io.inugami.monitoring.springboot.activemq.iolog;


import io.inugami.framework.api.monitoring.MdcService;
import io.inugami.framework.interfaces.exceptions.*;
import io.inugami.framework.interfaces.models.tools.Chrono;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.monitoring.logger.MDCKeys;
import io.inugami.framework.interfaces.monitoring.models.IoInfoDTO;
import io.inugami.framework.interfaces.spi.SpiLoader;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import java.util.*;

import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;
import static io.inugami.monitoring.springboot.activemq.config.InugamiActiveMqConstants.*;

@SuppressWarnings({"java:S1181", "java:S108"})
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
                log.warn(
                        "Rejecting received message because of listener container having been stopped in the meantime:" +
                        message);
            }
            rollbackIfNecessary(session);
            throw new MessageRejectedWhileStoppingException();
        }


        Chrono    chrono    = Chrono.startChrono();
        Throwable exception = null;
        IoInfoDTO ioInfo    = traceIologIn(message);

        try {
            invokeListener(session, message);
        } catch (final JMSException | RuntimeException | Error ex) {
            rollbackIfNecessary(session);
            exception = ex;
            throw ex;
        } finally {
            chrono.stop();
            traceIologOut(ioInfo, chrono.getDuration(), exception);
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

        final var mdc = MdcService.getInstance()
                                         .ioinfoIoLog(result)
                                         .callType(JMS);

        applyIfNotNull(extractProperty(APPLICATION, message), mdc::callFrom);
        applyIfNotNull(extractProperty(MDCKeys.messageId, message), mdc::messageId);
        applyIfNotNull(extractProperty(DEVICE_IDENTIFIER, message), mdc::deviceIdentifier);
        applyIfNotNull(extractProperty(CORRELATION_ID, message), mdc::correlationId);
        applyIfNotNull(extractProperty(CONVERSATION_ID, message), mdc::conversationId);
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

        final Enumeration<String> properties;
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


    private String extractProperty(final MDCKeys key, final Message message) {
        return extractProperty(key.name(), message);
    }

    private String extractProperty(final String key, final Message message) {
        if (message == null) {
            return null;
        }

        try {
            return message.getStringProperty(key);
        } catch (final JMSException e) {
            return null;
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
