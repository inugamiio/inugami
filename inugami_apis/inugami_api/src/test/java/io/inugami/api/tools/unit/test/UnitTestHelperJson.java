package io.inugami.api.tools.unit.test;

import com.fasterxml.jackson.core.type.TypeReference;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.DefaultErrorCode;
import io.inugami.api.exceptions.UncheckedException;
import io.inugami.api.marshalling.JsonMarshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnitTestHelperJson {

    // =========================================================================
    // LOAD
    // =========================================================================
    static <T> T loadIntegrationJson(final String path, final TypeReference<T> refObjectType) {
        Asserts.assertNotNull(path, "can't load Json Object from null path");
        final String json = UnitTestHelperFile.readFileIntegration(path);
        return convertFromJson(json, refObjectType);
    }

    static <T> T loadIntegrationJson(final String path, final Class<? extends T> objectType) {
        Asserts.assertNotNull(path, "can't load Json Object from null path");
        final String json = UnitTestHelperFile.readFileIntegration(path);
        return convertFromJson(json, objectType);
    }

    static <T> T loadJson(final String path, final TypeReference<T> refObjectType) {
        Asserts.assertNotNull(path, "can't load Json Object from null path");
        final String json = UnitTestHelperFile.readFileRelative(path);
        return convertFromJson(json, refObjectType);
    }

    static <T> T loadJson(final String path, final Class<? extends T> objectType) {
        Asserts.assertNotNull(path, "can't load Json Object from null path");
        final String json = UnitTestHelperFile.readFileRelative(path);
        return convertFromJson(json, objectType);
    }


    static <T> T convertFromJson(final byte[] data, final TypeReference<T> refObjectType) {
        T result = null;
        if (data != null && refObjectType != null) {
            try {
                final String json = new String(data, StandardCharsets.UTF_8);
                result = convertFromJson(json, refObjectType);

            } catch (final Exception error) {
                throw new UncheckedException(error.getMessage(), error);
            }

        }
        return result;
    }


    // =========================================================================
    // CONVERT
    // =========================================================================
    static String convertToJson(final Object value) {
        String result = null;
        if (value == null) {
            return UnitTestHelperCommon.NULL;
        }
        try {
            result = JsonMarshaller.getInstance()
                                   .getIndentedObjectMapper()
                                   .writeValueAsString(value);
        } catch (final Exception e) {
            UnitTestHelperException.throwException(e);
        }
        return UnitTestHelperCommon.cleanWindowsChar(result);
    }


    static String convertToJsonWithoutIndent(final Object value) {
        String result = null;
        try {
            result = JsonMarshaller.getInstance()
                                   .getDefaultObjectMapper()
                                   .writeValueAsString(value);
        } catch (final Exception e) {
            UnitTestHelperException.throwException(e);
        }
        return UnitTestHelperCommon.cleanWindowsChar(result);
    }

    static <T> T convertFromJson(final String json, final TypeReference<T> refObjectType) {
        try {
            return json == null ? null : JsonMarshaller.getInstance()
                                                       .getDefaultObjectMapper()
                                                       .readValue(json, refObjectType);
        } catch (final IOException e) {
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), e, e.getMessage());
        }
    }

    static <T> T convertFromJson(final String json, final Class<? extends T> objectType) {
        try {
            return json == null ? null : JsonMarshaller.getInstance()
                                                       .getDefaultObjectMapper()
                                                       .readValue(json, objectType);
        } catch (final IOException e) {
            throw new UncheckedException(DefaultErrorCode.buildUndefineError(), e, e.getMessage());
        }
    }

    static <T> T convertFromJson(final byte[] data, final Class<? extends T> refObjectType) {
        T result = null;
        if (data != null && refObjectType != null) {
            try {
                final String json = new String(data, StandardCharsets.UTF_8);
                result = convertFromJson(json, refObjectType);

            } catch (final Exception error) {
                throw new UncheckedException(error.getMessage(), error);
            }

        }
        return result;
    }


}
