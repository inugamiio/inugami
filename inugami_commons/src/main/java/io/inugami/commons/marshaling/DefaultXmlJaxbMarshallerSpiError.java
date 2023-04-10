package io.inugami.commons.marshaling;

import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.ErrorCode;

import static io.inugami.api.exceptions.DefaultErrorCode.newBuilder;

public enum DefaultXmlJaxbMarshallerSpiError implements ErrorCode {
    MARSHALLING_ERROR(newBuilder()
                              .errorCode("INUGAMI_COMMONS_XML-0_0")
                              .statusCode(500)
                              .message("unable to serialize to XML")
                              .errorTypeTechnical()
                              .category(DefaultXmlJaxbMarshallerSpiError.DOMAIN)),

    UNMARSHALLING_ERROR(newBuilder()
                                .errorCode("INUGAMI_COMMONS_XML-0_1")
                                .statusCode(500)
                                .message("unable to deserialize to XML")
                                .errorTypeTechnical()
                                .category(DefaultXmlJaxbMarshallerSpiError.DOMAIN));
    private static final String    DOMAIN = "inugami";
    private final        ErrorCode errorCode;

    private DefaultXmlJaxbMarshallerSpiError(final DefaultErrorCode.DefaultErrorCodeBuilder errorBuilder) {
        errorCode = errorBuilder.build();
    }

    @Override
    public ErrorCode getCurrentErrorCode() {
        return errorCode;
    }
}