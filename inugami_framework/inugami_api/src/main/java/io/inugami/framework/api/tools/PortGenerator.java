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
package io.inugami.framework.api.tools;

import io.inugami.framework.interfaces.exceptions.DefaultErrorCode;
import io.inugami.framework.interfaces.exceptions.ErrorCode;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static io.inugami.framework.interfaces.exceptions.Asserts.assertNotEmpty;
import static io.inugami.framework.interfaces.exceptions.Asserts.assertTrue;
@SuppressWarnings({"java:S2245"})
@UtilityClass
public class PortGenerator {
    //==================================================================================================================
    // CLIENTS
    //==================================================================================================================
    public static final  int                  MIN_PORT            = 1024;
    public static final  int                  MAX_PORT            = Short.MAX_VALUE * 2;
    private static final Map<String, Integer> SOCKETS             = new ConcurrentHashMap<>();
    private static final int                  MAX_SEARCH          = 1000;
    private static final ErrorCode            SERVICE_REQUIRED    = DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError())
                                                                                    .errorCode("PORT_GEN-0.0")
                                                                                    .message("service required for generate port")
                                                                                    .build();
    private static final ErrorCode            NO_SOCKET_AVAILABLE = DefaultErrorCode.fromErrorCode(DefaultErrorCode.buildUndefineError())
                                                                                    .errorCode("PORT_GEN-0.1")
                                                                                    .message("no socket AVAILABLE")
                                                                                    .build();

    //==================================================================================================================
    // GENERATE
    //==================================================================================================================
    public static int generateFor(@NonNull final String service) {
        assertNotEmpty(SERVICE_REQUIRED, service);
        final Integer savedPort = SOCKETS.get(service);
        if (savedPort != null) {
            return savedPort.intValue();
        }

        final Random random    = new Random(System.nanoTime());
        int          counter   = -1;
        int          result;
        boolean      available = false;
        do {
            counter++;
            assertTrue(NO_SOCKET_AVAILABLE, counter <= MAX_SEARCH);
            result = MIN_PORT + random.nextInt(MAX_PORT);
            available = isAvailable(result);
        } while (!available);

        assertTrue(NO_SOCKET_AVAILABLE, available);
        SOCKETS.put(service, result);
        return result;
    }

    private static boolean isAvailable(final int port) {
        ServerSocket socket = null;
        try {
            socket = ServerSocketFactory.getDefault().createServerSocket(port, 1, InetAddress.getByName("localhost"));
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            close(socket);
        }
    }

    @SuppressWarnings({"java:S108"})
    private static void close(final ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
