package io.inugami.framework.api.models;

import io.inugami.framework.interfaces.models.JsonBuilder;
import io.inugami.framework.interfaces.models.maven.Gav;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.inugami.framework.api.tools.unit.test.UnitTestData.OTHER;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"java:S5838"})
class JsonBuilderTest {


    public static final String NAME = "name";

    @Test
    void writeFunction_nominal() {
        assertThat(new JsonBuilder().writeFunction("someFunction", NAME, "lastname")
                                    .toString()).isEqualTo(" function someFunction(name,lastname)");

        assertThat(new JsonBuilder().writeFunction("someFunction")
                                    .toString()).isEqualTo(" function someFunction()");
    }


    @Test
    void addReturnKeyword_nominal() {
        assertThat(new JsonBuilder().addReturnKeyword()
                                    .toString()).isEqualTo("return ");
    }

    @Test
    void openObject_nominal() {
        assertThat(new JsonBuilder().openObject()
                                    .toString()).isEqualTo("{");
    }

    @Test
    void closeObject_nominal() {
        assertThat(new JsonBuilder().closeObject()
                                    .toString()).isEqualTo("}");
    }

    @Test
    void openList_nominal() {
        assertThat(new JsonBuilder().openList()
                                    .toString()).isEqualTo("[");
    }

    @Test
    void closeList_nominal() {
        assertThat(new JsonBuilder().closeList()
                                    .toString()).isEqualTo("]");
    }

    @Test
    void openComment_nominal() {
        assertThat(new JsonBuilder().openComment()
                                    .toString()).isEqualTo("/*");
    }

    @Test
    void closeComment_nominal() {
        assertThat(new JsonBuilder().closeComment()
                                    .toString()).isEqualTo("*/");
    }

    @Test
    void addField_nominal() {
        assertThat(new JsonBuilder().addField(NAME)
                                    .toString()).isEqualTo("\"name\":");
    }

    @Test
    void write_boolean() {
        assertThat(new JsonBuilder().write(true)
                                    .toString()).isEqualTo("true");

        assertThat(new JsonBuilder().write(false)
                                    .toString()).isEqualTo("false");
    }

    @Test
    void write_byte() {
        assertThat(new JsonBuilder().write(NAME.getBytes(StandardCharsets.UTF_8)[0])
                                    .toString()).isEqualTo("110");
    }

    @Test
    void write_short() {
        assertThat(new JsonBuilder().write((short) 1)
                                    .toString()).isEqualTo("1");
    }

    @Test
    void write_float() {
        assertThat(new JsonBuilder().write(1.0f)
                                    .toString()).isEqualTo("1.0");
    }

    @Test
    void write_double() {
        assertThat(new JsonBuilder().write(1.0)
                                    .toString()).isEqualTo("1.0");
    }

    @Test
    void writeSpace_nominal() {
        assertThat(new JsonBuilder().writeSpace()
                                    .toString()).isEqualTo(" ");
    }

    @Test
    void writeSpace_withSize() {
        assertThat(new JsonBuilder().writeSpace(5)
                                    .toString()).isEqualTo("     ");
        assertThat(new JsonBuilder().writeSpace(0)
                                    .toString()).isEqualTo("");
    }

    @Test
    void writeParam_nominal() {
        assertThat(new JsonBuilder().writeParam(NAME, "Joe")
                                    .toString()).isEqualTo("\"name\":\"Joe\"");
    }

    @Test
    void valueQuot_nominal() {
        assertThat(new JsonBuilder().valueQuot(NAME)
                                    .toString()).isEqualTo("\"name\"");

        assertThat(new JsonBuilder().valueQuot(null)
                                    .toString()).isEqualTo("null");
    }

    @Test
    void valueNull_nominal() {
        assertThat(new JsonBuilder().valueNull()
                                    .toString()).isEqualTo("null");
    }

    @Test
    void addLine_nominal() {
        assertThat(new JsonBuilder().addLine()
                                    .toString()).isEqualTo("\n");
    }

    @Test
    void addSeparator_nominal() {
        assertThat(new JsonBuilder().addSeparator()
                                    .toString()).isEqualTo(",");
    }

    @Test
    void addEndLine_nominal() {
        assertThat(new JsonBuilder().addEndLine()
                                    .toString()).isEqualTo(";\n");
    }

    @Test
    void writeListString_nominal() {
        assertThat(new JsonBuilder().writeListString(List.of("Joe", "Smith"))
                                    .toString()).isEqualTo("[\"Joe\",\"Smith\"]");
    }

    @Test
    void writeListString_null() {
        assertThat(new JsonBuilder().writeListString(null)
                                    .toString()).hasToString("null");
    }


    @Test
    void append_object() {
        assertThat(new JsonBuilder().append(Gav.builder().addHash("io.inugami:inugami_api:3.3.0:jar").build()))
                .hasToString("Gav(groupId=io.inugami, artifactId=inugami_api, version=3.3.0, qualifier=jar)");
    }

    @Test
    void append_string() {
        assertThat(new JsonBuilder().append(OTHER))
                .hasToString("XXX");
    }

    @Test
    void append_stringBuffer() {
        assertThat(new JsonBuilder().append(new StringBuffer().append(OTHER)))
                .hasToString("XXX");
    }

    @Test
    void append_charSequence() {
        assertThat(new JsonBuilder().append(OTHER, 0, 3))
                .hasToString("XXX");
    }

    @Test
    void append_chars() {
        assertThat(new JsonBuilder().append('j', 'o', 'e'))
                .hasToString("joe");
    }


    @Test
    void append_charsArray() {
        assertThat(new JsonBuilder().append(OTHER.toCharArray()))
                .hasToString("XXX");
        assertThat(new JsonBuilder().append(OTHER.toCharArray(), 0, 3))
                .hasToString("XXX");
    }

    @Test
    void append_boolean() {
        assertThat(new JsonBuilder().append(true))
                .hasToString("true");
    }

    @Test
    void append_char() {
        assertThat(new JsonBuilder().append('j'))
                .hasToString("j");
    }

    @Test
    void append_int() {
        assertThat(new JsonBuilder().append(1))
                .hasToString("1");
    }

    @Test
    void append_long() {
        assertThat(new JsonBuilder().append(1L))
                .hasToString("1");
    }

    @Test
    void append_float() {
        assertThat(new JsonBuilder().append(1.0f))
                .hasToString("1.0");
    }

    @Test
    void append_double() {
        assertThat(new JsonBuilder().append(1.0))
                .hasToString("1.0");
    }

    @Test
    void doubleDot_nominal() {
        assertThat(new JsonBuilder().doubleDot())
                .hasToString(":");
    }

    @Test
    void deco_nominal() {
        assertThat(new JsonBuilder().deco(5))
                .hasToString("     ");
        assertThat(new JsonBuilder().deco(0))
                .hasToString("");
    }

    @Test
    void deco_withOtherChar() {
        assertThat(new JsonBuilder().deco("@", 5))
                .hasToString("@@@@@");
        assertThat(new JsonBuilder().deco("@", 0))
                .hasToString("");
    }

    @Test
    void clear_withOtherChar() {
        final JsonBuilder result = new JsonBuilder().addField(NAME);
        result.clear();
        assertThat(result).hasToString("");

    }
}