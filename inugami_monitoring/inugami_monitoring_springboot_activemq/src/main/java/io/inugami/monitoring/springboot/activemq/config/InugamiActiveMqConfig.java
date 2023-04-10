package io.inugami.monitoring.springboot.activemq.config;

import io.inugami.commons.marshaling.jaxb.JaxbAdapterSpi;
import io.inugami.commons.marshaling.jaxb.JaxbClassRegister;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class InugamiActiveMqConfig {


    @ConditionalOnMissingBean
    @Bean
    public ActiveMQConnectionFactory connectionFactory(@Value("${spring.activemq.broker-url}") final String brokerUrl,
                                                       @Value("${spring.activemq.user}") final String user,
                                                       @Value("${spring.activemq.password}") final String password) {
        return new ActiveMQConnectionFactory(user, password, brokerUrl);
    }

    @Bean
    public JmsTemplate jmsTemplateQueue(final ActiveMQConnectionFactory connectionFactory,
                                        final MessageConverter messageConvertor) {
        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setMessageConverter(messageConvertor);
        return jmsTemplate;
    }


    @Bean
    public JmsTemplate jmsTemplateTopic(final ActiveMQConnectionFactory connectionFactory,
                                        final MessageConverter messageConvertor) {
        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setMessageConverter(messageConvertor);
        return jmsTemplate;
    }

    @ConditionalOnProperty(name = "inugami.monitoring.activemq.marshalling.jaxb.enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public MessageConverter jaxbMessageConvertor(@Autowired(required = false) final List<JaxbClassRegister> classesRegisters,
                                                 @Autowired(required = false) final List<JaxbAdapterSpi> adapters) {
        final Jaxb2Marshaller converter = new Jaxb2Marshaller();

        if (classesRegisters != null) {
            final Set<Class<?>> classes = new LinkedHashSet<>();
            for (final JaxbClassRegister register : classesRegisters) {
                final List<Class<?>> registerClasses = register.register();
                if (registerClasses != null) {
                    classes.addAll(registerClasses);
                }
            }
            converter.setClassesToBeBound(classes.toArray(new Class<?>[]{}));
        }

        if (adapters != null) {
            final Set<XmlAdapter<?, ?>> adaptersToRegister = new LinkedHashSet<>();
            for (final JaxbAdapterSpi jaxbAdapterRegister : adapters) {
                final XmlAdapter<?, ?> adapter = jaxbAdapterRegister.getAdapter();
                if (adapter != null) {
                    adaptersToRegister.add(adapter);
                }
            }
            converter.setAdapters(adaptersToRegister.toArray(new XmlAdapter<?, ?>[]{}));
        }

        return new MarshallingMessageConverter(converter);
    }
}
