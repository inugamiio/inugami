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
package io.inugami.commons.test.mock.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.inugami.commons.test.mock.MockContext;
import io.inugami.commons.test.mock.MockOpenApiContext;
import io.inugami.framework.api.marshalling.JsonMarshaller;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import io.inugami.framework.interfaces.exceptions.dto.ProblemDTO;
import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.tools.ListUtils;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;

import static io.inugami.commons.test.mock.utils.MockGeneratorUtils.resolvePackageName;
import static io.inugami.framework.interfaces.functionnals.FunctionalUtils.applyIfNotNull;

@SuppressWarnings({"java:S1172", "java:S2386", "java:S2737"})
@UtilityClass
public class MockGeneratorOpenApiUtils {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    public static final List<String> DEFAULT_ANNOTATIONS                        = List.of("@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})", "@Retention(RetentionPolicy.RUNTIME)", "@Inherited");
    public static final List<String> DEFAULT_CLASS_IMPORTS                      = ListUtils.toList("io.inugami.framework.interfaces.rest.*", "io.swagger.v3.oas.annotations.media.*", "io.swagger.v3.oas.annotations.responses.*", "java.lang.annotation.*", "lombok.experimental.UtilityClass");
    public static final String       API_RESPONSE                               = "@ApiResponse";
    public static final String       API_RESPONSES                              = "@ApiResponses({";
    public static final String       APPLICATION_JSON                           = "application/json";
    public static final String       AUTHORIZATION_ERROR_ON_EXECUTING_OPERATION = "Authorization error on executing operation";
    public static final String       CLOSE_ANNOATION                            = "})";
    public static final String       COMMA                                      = ";";
    public static final String       CONTENT                                    = "content";
    public static final String       CONTENT_ANNOTATION                         = "@Content";
    public static final String       DESCRIPTION                                = "description";
    public static final String       DOMAIN                                     = " domain";
    public static final String       EMPTY                                      = "";
    public static final String       EQUALS                                     = "=";
    public static final String       ERROR_CODE                                 = "errorCode";
    public static final String       ERROR_MESSAGE                              = " errorMessage";
    public static final String       ERROR_MESSAGE_DETAIL                       = " errorMessageDetail";
    public static final String       EXAMPLES                                   = "examples";
    public static final String       EXAMPLE_OBJECT                             = "@ExampleObject";
    public static final String       FUNCTIONAL_ERROR_ON_EXECUTING_OPERATION    = "Functional error on executing operation";
    public static final String       HTTP_STATUS                                = " httpStatus";
    public static final String       KEYWORD_IMPORT                             = "import";
    public static final String       KEYWORD_PACKAGE                            = "package";
    public static final String       LINE                                       = "\n";
    public static final String       MEDIA_TYPE                                 = "mediaType";
    public static final String       NAME                                       = "name";
    public static final String       NAME_ERROR_CODES                           = "Error codes";
    public static final String       NAME_RESPONSE_WITH_ERROR_CODE              = "Response with error code";
    public static final String       NOMINAL                                    = "Nominal";
    public static final String       POTENTIAL_ERROR                            = "@PotentialError";
    public static final String       POTENTIAL_ERRORS                           = "@PotentialErrors({";
    public static final String       PUBLIC_CLASS                               = "public class ";
    public static final String       PUBLIC_INTERFACE_DOC                       = "public @interface Doc";
    public static final String       QUOT                                       = "\"";
    public static final String       RESPONSE_CODE                              = "responseCode";
    public static final String       SEPARATOR                                  = ",";
    public static final String       SPACE                                      = " ";
    public static final String       SUB_DOMAIN                                 = " subDomain";
    public static final String       SUCCESSFUL_OPERATION                       = "Successful operation";
    public static final String       TABS_SEPARATOR                             = "|";
    public static final String       TECHNICAL_ERROR_ON_EXECUTING_OPERATION     = "Technical error on executing operation";
    public static final String       TYPE                                       = " type";
    public static final String       URL                                        = " url";
    public static final String       UTILITY_CLASS                              = "@UtilityClass";
    public static final String       VALUE                                      = "value";
    public static final int          TAB                                        = 4;
    public static final int          TAB_SIZE_1                                 = 1;
    public static final int          TAB_SIZE_2                                 = 2;

    // =================================================================================================================
    // RENDER OPEN API
    // =================================================================================================================
    public static @NonNull File renderOpenApiDocumentation(final @NonNull File packageFolder,
                                                           final @NonNull Map<String, List<MockContext>> mocks,
                                                           final @NonNull MockOpenApiContext context) throws IOException {

        final File result = new File(
                packageFolder.getAbsoluteFile() + File.separator + context.getRestClientClass().getSimpleName() +
                "DOC.java");

        MockGeneratorUtils.createFolderIfNotExists(result.getParentFile());
        try (Writer writer = new FileWriter(result)) {
            render(mocks, context, writer, result.getName().split("[.]java")[0]);
            writer.flush();
        } catch (IOException e) {
            throw e;
        }
        return result;
    }

    // =================================================================================================================
    // RENDERING
    // =================================================================================================================
    private static void render(final @NonNull Map<String, List<MockContext>> mocks,
                               final @NonNull MockOpenApiContext context,
                               final @NonNull Writer writer,
                               final String className) throws IOException {

        renderJavaHeader(context, writer);
        writer.flush();

        line(writer);
        writer.write(UTILITY_CLASS);
        line(writer);
        writer.write(PUBLIC_CLASS + className);
        space(writer, 1);
        openClass(writer);
        line(writer);

        for (Map.Entry<String, List<MockContext>> entry : mocks.entrySet()) {
            renderMethodAnnotation(entry.getKey(), entry.getValue(), context, writer);
            line(writer);
            line(writer);
        }

        line(writer);
        closeClass(writer);
    }


    private static void renderJavaHeader(final @NonNull MockOpenApiContext context,
                                         final @NonNull Writer writer) throws IOException {
        final String packageName = resolvePackageName(context.getRestClientClass());

        //--- package
        writer.write(KEYWORD_PACKAGE);
        space(writer, 1);
        writer.write(packageName);
        writer.write(COMMA);
        line(writer);
        line(writer);

        //--- import
        final List<String> classToImport = new ArrayList<>(DEFAULT_CLASS_IMPORTS);
        applyIfNotNull(context.getClassToImport(), classToImport::addAll);
        Collections.sort(classToImport);
        for (String classImport : new LinkedHashSet<>(classToImport)) {
            writer.write(KEYWORD_IMPORT);
            space(writer, 1);
            writer.write(classImport);
            writer.write(COMMA);
            line(writer);
        }

        line(writer);
    }

    private static void renderMethodAnnotation(final @NonNull String key,
                                               final @NonNull List<MockContext> value,
                                               final @NonNull MockOpenApiContext context,
                                               final @NonNull Writer writer) throws IOException {
        if (value.isEmpty()) {
            return;
        }

        writePotentialErrors(value, context, writer);
        writeApiResponses(value, context, writer);
        final List<String> docAnnotations = new ArrayList<>(DEFAULT_ANNOTATIONS);
        applyIfNotNull(context.getCustomAnnotations(), docAnnotations::addAll);
        for (String docAnnotation : docAnnotations) {
            space(writer, TAB);
            writer.write(docAnnotation);
            line(writer);
        }
        space(writer, TAB);
        writer.write(PUBLIC_INTERFACE_DOC + key.substring(0, 1).toUpperCase() + key.substring(1));
        space(writer, 1);
        openClass(writer);
        closeClass(writer);
    }


    //------------------------------------------------------------------------------------------------------------------
    // POTENTIAL ERRORS
    //------------------------------------------------------------------------------------------------------------------
    private static void writePotentialErrors(final @NonNull List<MockContext> values,
                                             final @NonNull MockOpenApiContext context,
                                             final @NonNull Writer writer) throws IOException {

        final Map<String, ErrorCode> errorCodes = new LinkedHashMap<>();
        values.stream()
              .map(MockContext::getErrorCode)
              .filter(Objects::nonNull)
              .forEach(item -> errorCodes.put(item.getErrorCode(), item));
        if (errorCodes.isEmpty()) {
            return;
        }

        space(writer, TAB);
        writer.write(POTENTIAL_ERRORS);
        line(writer);

        final var keys = new ArrayList<>(errorCodes.keySet());
        Collections.sort(keys);

        final var errorIterator = keys.iterator();
        while (errorIterator.hasNext()) {
            final ErrorCode error = errorCodes.get(errorIterator.next());
            space(writer, TAB);
            space(writer, TAB);
            writer.write(renderPotentialErrorItem(error));
            if (errorIterator.hasNext()) {
                writer.write(SEPARATOR);
            }
            line(writer);
        }

        space(writer, TAB);
        writer.write(CLOSE_ANNOATION);
        line(writer);
    }

    private static String renderPotentialErrorItem(final ErrorCode error) {
        final JsonBuilder result = new JsonBuilder();
        result.write(POTENTIAL_ERROR).openTuple();

        result.write(ERROR_CODE).write(EQUALS).valueQuot(error.getErrorCode()).addSeparator();
        result.write(HTTP_STATUS).write(EQUALS).write(error.getStatusCode());
        applyIfNotNull(error.getErrorType(), v -> result.addSeparator().write(TYPE).write(EQUALS).valueQuot(v));
        applyIfNotNull(error.getDomain(), v -> result.addSeparator().write(DOMAIN).write(EQUALS).valueQuot(v));

        applyIfNotNull(error.getSubDomain(), v -> result.addSeparator().write(SUB_DOMAIN).write(EQUALS).valueQuot(v));

        applyIfNotNull(error.getMessage(), v -> result.addSeparator().write(ERROR_MESSAGE).write(EQUALS).valueQuot(v));

        applyIfNotNull(error.getMessage(), v -> result.addSeparator()
                                                      .write(ERROR_MESSAGE_DETAIL)
                                                      .write(EQUALS)
                                                      .valueQuot(v));

        applyIfNotNull(error.getUrl(), v -> result.addSeparator().write(URL).write(EQUALS).valueQuot(v));
        result.closeTuple();
        return result.toString();
    }

    //------------------------------------------------------------------------------------------------------------------
    // OPEN API RESPONSE
    //------------------------------------------------------------------------------------------------------------------
    private static void writeApiResponses(final @NonNull List<MockContext> values,
                                          final @NonNull MockOpenApiContext context,
                                          final @NonNull Writer writer) throws IOException {
        final Map<Integer, List<MockContext>> responses = sortMockContextByStatus(values);

        space(writer, TAB);
        writer.write(API_RESPONSES);
        line(writer);


        final List<Integer> status = new ArrayList<>(responses.keySet());
        Collections.sort(status);
        final Iterator<Integer> statusIterator = status.iterator();
        while (statusIterator.hasNext()) {
            final Integer itemStatus  = statusIterator.next();
            final String  apiResponse = renderResponse(itemStatus, responses.get(itemStatus));
            writer.write(indent(apiResponse, TAB * TAB_SIZE_2));
            if (statusIterator.hasNext()) {
                space(writer, TAB * TAB_SIZE_2);
                writer.write(SEPARATOR);
                line(writer);
            }
        }
        space(writer, TAB);
        writer.write(CLOSE_ANNOATION);
        line(writer);
    }

    private static String renderResponse(final Integer itemStatus, final List<MockContext> mockContexts) {
        final JsonBuilder result = new JsonBuilder();
        result.write(API_RESPONSE).openTuple().line();

        result.tab().write(RESPONSE_CODE).eq().valueQuot(itemStatus).addSeparator().line();
        result.tab().write(DESCRIPTION).eq().valueQuot(resolveDescription(itemStatus));


        final Map<String, List<MockContext>> contents = sortMockContextByContentType(mockContexts);
        if (!contents.isEmpty()) {
            result.addSeparator().line().tab().write(CONTENT).eq().openObject().line();

            final Iterator<Map.Entry<String, List<MockContext>>> contentIterator = contents.entrySet().iterator();
            while (contentIterator.hasNext()) {
                final var    content         = contentIterator.next();
                final String contentRendered = renderContent(content.getKey(), content.getValue(), itemStatus);
                result.write(indent(contentRendered, TAB * 3));
                if (contentIterator.hasNext()) {
                    result.addSeparator();
                }
            }
            result.tab().tab().closeObject();
            result.line();
        }


        result.tab().closeTuple();
        return result.toString();
    }


    private static String renderContent(final String key, final List<MockContext> values, final Integer status) {
        final JsonBuilder result = new JsonBuilder();
        result.write(CONTENT_ANNOTATION).openTuple().line();
        result.tab().write(MEDIA_TYPE).eq().valueQuot(key);
        if (!values.isEmpty()) {
            result.addSeparator().line().tab().write(EXAMPLES).eq().openObject();
            if (status < 400) {
                result.write(indent(renderExampleObjects(values), TAB * 4));
            } else {
                result.write(indent(renderErrorExampleObjects(values), TAB * 4));
            }
            result.line().tab().closeObject();
        }
        result.line().closeTuple();
        return result.toString();
    }

    private static String renderErrorExampleObjects(final List<MockContext> values) {
        final List<MockContext>      data               = Optional.ofNullable(values).orElse(List.of());
        final Map<String, ErrorCode> errorCodes         = new LinkedHashMap<>();
        final List<MockContext>      dataWithErrorCodes = new ArrayList<>();
        final List<MockContext>      dataStandard       = new ArrayList<>();
        final List<MockContext>      buffer             = new ArrayList<>();

        for (MockContext mockCtx : data) {
            if (mockCtx.getErrorCode() == null) {
                dataStandard.add(mockCtx);
            } else {
                errorCodes.put(mockCtx.getErrorCode().getErrorCode(), mockCtx.getErrorCode());
                dataWithErrorCodes.add(mockCtx);
            }
        }

        final var errorKeys = new ArrayList<>(errorCodes.keySet());
        Collections.sort(errorKeys);


        if (!errorCodes.isEmpty()) {
            final var firstErrorCode = errorCodes.get(errorKeys.get(0));
            final MockContext firstMockWithErrorCode = dataWithErrorCodes.stream()
                                                                         .filter(item -> item.getErrorCode()
                                                                                             .getErrorCode()
                                                                                             .equals(firstErrorCode.getErrorCode()))
                                                                         .findFirst()
                                                                         .orElse(MockContext.builder().build())
                                                                         .toBuilder()
                                                                         .name(NAME_RESPONSE_WITH_ERROR_CODE)
                                                                         .build();
            final MockContext allErrorCodes = firstMockWithErrorCode.toBuilder()
                                                                    .name(NAME_ERROR_CODES)
                                                                    .response(renderErrorCodeTab(errorCodes))
                                                                    .build();
            buffer.add(allErrorCodes);
            buffer.add(firstMockWithErrorCode.toBuilder()
                                             .responsePayload(renderJson(ProblemDTO.builder()
                                                                                   .with(ERROR_CODE, firstMockWithErrorCode.getErrorCode())
                                                                                   .status(firstMockWithErrorCode.getErrorCode()
                                                                                                                 .getStatusCode())
                                                                                   .build()))
                                             .build());

        }
        buffer.addAll(dataStandard);
        return renderExampleObjects(buffer);
    }


    private static String renderErrorCodeTab(final Map<String, ErrorCode> values) {
        final JsonBuilder     result          = new JsonBuilder();
        final List<ErrorCode> errorCodeValues = values.entrySet().stream().map(Map.Entry::getValue).toList();
        final List<String>    errorCodes      = new ArrayList<>(values.keySet());
        Collections.sort(errorCodes);

        final int errorCodeSize = computeTabSize(errorCodes);
        final int typeSize = computeTabSize(errorCodeValues.stream()
                                                           .map(ErrorCode::getErrorType)
                                                           .filter(Objects::nonNull)
                                                           .toList());
        final int domainSize = computeTabSize(errorCodeValues.stream()
                                                             .map(ErrorCode::getDomain)
                                                             .filter(Objects::nonNull)
                                                             .toList());
        final int subSize = computeTabSize(errorCodeValues.stream()
                                                          .map(ErrorCode::getSubDomain)
                                                          .filter(Objects::nonNull)
                                                          .toList());
        result.openList().line();
        final var errorCodesIterator = errorCodes.iterator();
        while (errorCodesIterator.hasNext()) {
            final var errorCode      = errorCodesIterator.next();
            final var errorCodeValue = values.get(errorCode);
            result.quot();
            result.write(errorCode)
                  .write(space((errorCodeSize - errorCode.length()) + TAB_SIZE_1))
                  .write(TABS_SEPARATOR)
                  .write(space(TAB_SIZE_1));

            result.write(errorCodeValue.getErrorType())
                  .write(space((typeSize - errorCodeValue.getErrorType().length()) + TAB_SIZE_1))
                  .write(TABS_SEPARATOR)
                  .write(space(TAB_SIZE_1));

            if (domainSize > 0) {
                result.write(orEmpty(errorCodeValue.getDomain()))
                      .write(space((typeSize - orEmpty(errorCodeValue.getDomain()).length()) + TAB_SIZE_1))
                      .write(TABS_SEPARATOR)
                      .write(space(TAB_SIZE_1));
            }
            if (subSize > 0) {
                result.write(orEmpty(errorCodeValue.getSubDomain()))
                      .write(space((subSize - orEmpty(errorCodeValue.getSubDomain()).length()) + TAB_SIZE_1))
                      .write(TABS_SEPARATOR)
                      .write(space(TAB_SIZE_1));
            }
            result.write(orEmpty(errorCodeValue.getMessage()));
            result.quot();
            if (errorCodesIterator.hasNext()) {
                result.addSeparator();
                result.line();
            }
        }
        result.line();
        result.closeList();

        return result.toString();
    }


    private static String renderExampleObjects(final List<MockContext> values) {
        final JsonBuilder           result   = new JsonBuilder();
        final Iterator<MockContext> iterator = Optional.ofNullable(values).orElse(List.of()).iterator();

        result.line();
        while (iterator.hasNext()) {
            final var mockContext = iterator.next();
            result.write(EXAMPLE_OBJECT).openTuple().line();
            result.tab().write(NAME).eq().valueQuot(Optional.ofNullable(mockContext.getName()).orElse(NOMINAL));
            applyIfNotNull(mockContext.getResponse(), v -> result.addSeparator()
                                                                 .line()
                                                                 .tab()
                                                                 .write(VALUE)
                                                                 .eq()
                                                                 .tripeQuot()
                                                                 .line()
                                                                 .write(cleanContent(v))
                                                                 .tripeQuot());

            result.line().closeTuple();
            if (iterator.hasNext()) {
                result.addSeparator().line();
            }
        }

        return result.toString();
    }

    private static String cleanContent(final String content) {
        if (content == null) {
            return EMPTY;
        }
        var data = String.join(LINE, String.join(QUOT, content.split("\\\\\"")).split("\\\\n")).trim();
        if (data.startsWith(QUOT)) {
            data = data.substring(1);
        }
        if (data.endsWith(QUOT)) {
            data = data.substring(0, data.length() - 1);
        }
        return indent(data, TAB * 2);
    }


    private static String resolveDescription(@NonNull final Integer status) {
        if (status < 400) {
            return SUCCESSFUL_OPERATION;
        } else if (status == 400) {
            return FUNCTIONAL_ERROR_ON_EXECUTING_OPERATION;
        } else if (status == 401 || status == 403) {
            return AUTHORIZATION_ERROR_ON_EXECUTING_OPERATION;
        } else if (status < 500) {
            return FUNCTIONAL_ERROR_ON_EXECUTING_OPERATION;
        } else {
            return TECHNICAL_ERROR_ON_EXECUTING_OPERATION;
        }
    }


    // =================================================================================================================
    // TOOLS
    // =================================================================================================================
    private static @NonNull Map<Integer, List<MockContext>> sortMockContextByStatus(final @NonNull List<MockContext> values) {
        return storeMockContextByT(values, MockContext::getStatus);
    }

    private static @NonNull Map<String, List<MockContext>> sortMockContextByContentType(@NonNull final List<MockContext> mockContexts) {
        return storeMockContextByT(mockContexts, i -> Optional.ofNullable(i.getContentType()).orElse(APPLICATION_JSON));
    }

    private static @NonNull <T> Map<T, List<MockContext>> storeMockContextByT(@NonNull final List<MockContext> values,
                                                                              @NonNull final Function<MockContext, T> extractor) {
        final Map<T, List<MockContext>> result = new LinkedHashMap<>();

        for (MockContext context : values) {
            T identifier = extractor.apply(context);
            if (identifier == null) {
                continue;
            }
            List<MockContext> bucket = result.get(identifier);
            if (bucket == null) {
                bucket = new ArrayList<>();
                result.put(identifier, bucket);
            }
            bucket.add(context);
        }

        return result;
    }

    private void line(Writer writer) throws IOException {
        writer.append(LINE);
    }

    private static String space(final int times) {
        final StringBuilder result = new StringBuilder();
        for (int i = times; i >= 0; i--) {
            result.append(SPACE);
        }
        return result.toString();
    }

    private static void space(final @NonNull Writer writer, final int times) throws IOException {
        for (int i = times; i >= 0; i--) {
            writer.write(SPACE);
        }
    }

    private static void openClass(final @NonNull Writer writer) throws IOException {
        writer.append("{");
    }

    private static void closeClass(final @NonNull Writer writer) throws IOException {
        writer.append("}");
    }


    private static String indent(final String value, final int tabs) {
        final String[]    parts  = Optional.ofNullable(value).orElse(EMPTY).split(LINE);
        final JsonBuilder result = new JsonBuilder();
        for (String part : parts) {
            result.write(space(tabs)).write(part).addLine();
        }
        return result.toString();
    }

    private static int computeTabSize(@NonNull final List<String> values) {
        int size = 0;
        for (String value : values) {
            if (value.length() > size) {
                size = value.length();
            }
        }
        return size;
    }

    private static String orEmpty(final String value) {
        return value == null ? EMPTY : value;
    }

    private static String renderJson(final Object value) {
        try {
            return JsonMarshaller.getInstance().getIndentedObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
