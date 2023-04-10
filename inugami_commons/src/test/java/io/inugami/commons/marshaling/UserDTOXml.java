package io.inugami.commons.marshaling;


import io.inugami.commons.marshaling.jaxb.LocalDateTimeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
