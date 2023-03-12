package io.inugami.commons.test.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import flexjson.JSONSerializer;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import io.inugami.commons.marshaling.JsonMarshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonHelper {

    public static String convertToJson(final Object value) {
        String result = null;
        if (value == null) {
            return CommonsHelper.NULL;
        }
        try {
            result = JsonMarshaller.getInstance()
                                   .getIndentedObjectMapper()
                                   .writeValueAsString(value);
        } catch (final Exception e) {
            CommonsHelper.throwException(e);
        }
        return result;
    }

    public static String forceConvertToJson(final Object value) {
        return new JSONSerializer().prettyPrint(true)
                                   .exclude("*.class")
                                   .deepSerialize(value);
    }

    public static String convertToJsonWithoutIndent(final Object value) {
        String result = null;
        try {
            result = JsonMarshaller.getInstance()
                                   .getDefaultObjectMapper()
                                   .writeValueAsString(value);
        } catch (final Exception e) {
            CommonsHelper.throwException(e);
        }
        return result;
    }

    public static <T> T loadJson(final String path, final TypeReference<T> refObjectType) {
        final String json = FileReaderHelper.loadRelativeFile(path);
        return convertFromJson(json, refObjectType);
    }

    public static <T> T convertFromJson(final String json, final TypeReference<T> refObjectType) {
        try {
            return json == null ? null : JsonMarshaller.getInstance()
                                                       .getDefaultObjectMapper()
                                                       .readValue(json, refObjectType);
        } catch (final IOException e) {
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), e, e.getMessage());
        }
    }

    public static <T> T convertFromJson(final byte[] data, final TypeReference<T> refObjectType) {
        T result = null;
        if (data != null && refObjectType != null) {
            try {
                final String json = new String(data, CommonsHelper.UTF_8);
                result = convertFromJson(json, refObjectType);

            } catch (final Exception error) {
                throw new UncheckedException(error.getMessage(), error);
            }

        }
        return result;
    }

}
