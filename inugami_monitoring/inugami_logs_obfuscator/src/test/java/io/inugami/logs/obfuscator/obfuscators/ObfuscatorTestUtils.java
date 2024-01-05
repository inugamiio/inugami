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
package io.inugami.logs.obfuscator.obfuscators;

import io.inugami.logs.obfuscator.api.LogEventDto;

public interface ObfuscatorTestUtils {

    public static final String IOLOG = "[POST] /user\n" +
                                       "headers :\n" +
                                       "\tAuthorization: Basic eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0N\n" +
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
                                       "}";

    default LogEventDto buildEvent(final String message) {
        return LogEventDto.builder()
                          .message(message)
                          .build();
    }
}
