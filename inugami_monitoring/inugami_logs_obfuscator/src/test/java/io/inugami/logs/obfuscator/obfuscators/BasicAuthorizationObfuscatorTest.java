package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.ObfuscatorSpi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BasicAuthorizationObfuscatorTest implements ObfuscatorTestUtils {

    @Test
    void accept_withPassword_shouldAccept() {
        ObfuscatorSpi obfuscator = new BasicAuthorizationObfuscator();
        assertThat(obfuscator.accept(buildEvent("Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isTrue();
    }

    @Test
    void obfuscate_withPassword_shouldObfuscate() {
        ObfuscatorSpi obfuscator = new BasicAuthorizationObfuscator();

        assertThat(obfuscator.obfuscate(buildEvent(IOLOG))).isEqualTo("[POST] /user\n" +
                                                                      "headers :\n" +
                                                                      "\tAuthorization: xxxxxxMjM0N\n" +
                                                                      "\tx-b3-traceid: 79bd7a82ec8d\n" +
                                                                      "\taccept: */*\n" +
                                                                      "\tx-correlation-id: b0116bae-1da0-465d-845b-79bd7a82ec8d\n" +
                                                                      "\tcontent-type: application/json; charset=UTF-8\n" +
                                                                      "\tcontent-length: 441\n" +
                                                                      "\thost: localhost:34961\n" +
                                                                      "\tconnection: Keep-Alive\n" +
                                                                      "\tuser-agent: Apache-HttpClient/4.5.13 (Java/11.0.2)\n" +
                                                                      "\taccept-encoding: gzip,deflate\n" +
                                                                      "payload :\n" +
                                                                      "{\n" +
                                                                      "  \"birthday\" : \"1988-04-12\",\n" +
                                                                      "  \"canton\" : \"VD\",\n" +
                                                                      "  \"city\" : \"Cheseaux-sur-Lausanne\",\n" +
                                                                      "  \"deviceIdentifier\" : \"401f0498-c43f-43ad-a3f4-2888838332ad\",\n" +
                                                                      "  \"firstName\" : \"Émilie\",\n" +
                                                                      "  \"id\" : 1,\n" +
                                                                      "  \"lastName\" : \"Lalonde\",\n" +
                                                                      "  \"nationality\" : \"CH\",\n" +
                                                                      "  \"old\" : 35,\n" +
                                                                      "  \"phoneNumber\" : \"0615031522\",\n" +
                                                                      "  \"sex\" : \"FEMALE\",\n" +
                                                                      "  \"socialId\" : \"7564971247732\",\n" +
                                                                      "  \"streetName\" : \"du Château\",\n" +
                                                                      "  \"streetNumber\" : \"10\",\n" +
                                                                      "  \"streetType\" : \"Chem.\",\n" +
                                                                      "  \"zipCode\" : \"1033\"\n" +
                                                                      "}");


        assertThat(obfuscator.obfuscate(buildEvent("Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isEqualTo("Authorization=xxxxxxMjM0N");
        assertThat(obfuscator.obfuscate(buildEvent("Authorization = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isEqualTo("Authorization = xxxxxxMjM0N");
        assertThat(obfuscator.obfuscate(buildEvent("Authorization : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N"))).isEqualTo("Authorization : xxxxxxMjM0N");


    }
}