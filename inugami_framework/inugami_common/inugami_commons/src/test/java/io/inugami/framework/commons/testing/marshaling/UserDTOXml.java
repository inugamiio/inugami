package io.inugami.framework.commons.testing.marshaling;


import io.inugami.framework.commons.marshaling.jaxb.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTOXml {
    private Long   id;
    private String firstName;
    private String lastName;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime creationDate;
}
