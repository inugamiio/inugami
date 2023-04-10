package io.inugami.monitoring.springboot.activemq.iolog;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Setter
@Getter
public class MonitoredJmsListenerContainerFactory extends DefaultJmsListenerContainerFactory {
    @Override
    protected DefaultMessageListenerContainer createContainerInstance() {
        return new JmsIologListener();
    }
}
