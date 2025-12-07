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
package io.inugami.framework.interfaces.rest;

import io.inugami.framework.interfaces.monitoring.models.Headers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.lang.annotation.*;

@Operation(
        parameters = {
                @Parameter(
                        name = Headers.X_DEVICE_IDENTIFIER,
                        in= ParameterIn.HEADER,
                        description = "Unique device identifier",
                        example = "821c15b3-db80-4209-be71-7331df86d953"
                ),
                @Parameter(
                        name = Headers.X_CORRELATION_ID,
                        in= ParameterIn.HEADER,
                        description = "User session identifier",
                        example = "15b634d6-8ef1-48d0-96d2-ea27931527e9"
                ),
                @Parameter(
                        name = Headers.X_CONVERSATION_ID,
                        in= ParameterIn.HEADER,
                        description = "Functionnal process identifier",
                        example = "register_new_user"
                ),
                @Parameter(
                        name = Headers.X_B_3_TRACEID,
                        in= ParameterIn.HEADER,
                        description = "Request identifier between each internal applications",
                        example = "584eb91e-e10b-47c6-b14e-8dca1641a97a"
                ),
                @Parameter(
                        name = Headers.X_AUTHORIZATION_TOKEN,
                        in= ParameterIn.HEADER,
                        description = "User Jwt token"
                ),
                @Parameter(
                        name = Headers.AUTHORIZATION,
                        in= ParameterIn.HEADER,
                        description = "Basic authorization"
                ),
                @Parameter(
                        name = Headers.X_DEVICE_TYPE,
                        in= ParameterIn.HEADER,
                        description = "Device type (mobile phone, desktop, ...)"
                ),
                @Parameter(
                        name = Headers.X_DEVICE_VERSION,
                        in= ParameterIn.HEADER,
                        description = "Device version (Android 16, Windows 11, ...)"
                ),
                @Parameter(
                        name = Headers.X_FRONT_VERSION,
                        in= ParameterIn.HEADER,
                        description = "Fronted application version"
                ),
                @Parameter(
                        name = Headers.X_APPLICATION,
                        in= ParameterIn.HEADER,
                        description = "Fronted application name"
                )
        }
)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GenericHeaders {
}
