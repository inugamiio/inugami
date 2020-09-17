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
package io.inugami.core.services.sse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import io.inugami.api.constants.JvmKeyValues;
import io.inugami.api.exceptions.Asserts;
import io.inugami.api.loggers.Loggers;
import io.inugami.api.models.data.basic.Json;
import io.inugami.api.models.data.basic.JsonObject;
import io.inugami.api.models.data.basic.StringJson;
import io.inugami.commons.engine.JavaScriptEngine;
import io.inugami.core.model.system.UserConnection;
import io.inugami.core.model.system.UserSocket;
import org.jboss.resteasy.plugins.providers.sse.SseEventProvider;
import org.jboss.resteasy.plugins.providers.sse.SseImpl;

/**
 * SseService
 * 
 * @author patrick_guillerm
 * @since 23 janv. 2017
 */
public final class SseService {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static int                       MAX_USER_SOCKETS    = Integer.parseInt(JvmKeyValues.APPLICATION_MAX_USER_SOCKETS.or("10"));
    
    public final static String                     ALL_PLUGINS_DATA    = "all-plugins-data";
    
    public static final String                     SSE_ADMIN_CHANNEL   = "administration";
    
    public static final String                     SSE_GLOBALE_CHANNEL = "globale";
    
    public static final Sse                        SSE                 = new SseImpl();
    
    public static final SseBroadcaster             BROADCASTER         = initBroadcaster();
    
    public static final SseServiceSender           SSE_SENDER          = new SseServiceSender(BROADCASTER, SSE);
    
    private static final Map<String, UserSocket>   USERS_SOCKETS       = new ConcurrentHashMap<>();
    
    private static final Map<String, SseEventSink> SOCKETS             = new ConcurrentHashMap<>();
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    
    private static SseBroadcaster initBroadcaster() {
        final SseBroadcaster result = SSE.newBroadcaster();
        result.onClose((event) -> {
            Loggers.SSE.info("Chunked output has been closed.");
            SseSocketSensor.decrementAndGet();
            closeUserSocket(event);
        });
        
        result.onError((eventSink, error) -> {
            Loggers.SSE.warn("An exception has been thrown while broadcasting to an event output :"
                             + error.getMessage());
            SseSocketSensor.decrementAndGet();
            closeUserSocket(eventSink);
            
        });
        return result;
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static synchronized void start() {
        SSE_SENDER.start();
    }
    
    public static synchronized void shutdown() {
        SSE_SENDER.shutdown();
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public static synchronized void close() {
        BROADCASTER.close();
    }
    
    public static synchronized void registerSocket(final String login, final String ip, final String agent,
                                                   final String uuid) {
        final String localLogin = login == null ? "undefine_user" : login;
        final UserConnection userConnection = buildUserConnection(localLogin, ip, agent, uuid);
        SseEventSink eventSink = SOCKETS.get(userConnection.getUid());
        final UserSocket userSockets = USERS_SOCKETS.get(localLogin);
        if (eventSink == null) {
            if (userSockets.getConnections().size() >= MAX_USER_SOCKETS) {
                throw new MaxUserSocketException(login);
            }
            eventSink = new SseSocket(new SseEventProvider(), userConnection);
            userSockets.newConnexion(userConnection);
            BROADCASTER.register(eventSink);
            SseSocketSensor.incrementAndGet();
            SOCKETS.put(userConnection.getUid(), eventSink);
        }
    }
    
    private static UserConnection buildUserConnection(final String login, final String ip, final String agent,
                                                      final String uuid) {
        final String userLogin = login == null ? "undefine user" : login;
        UserSocket userSocket = USERS_SOCKETS.get(userLogin);
        
        if (userSocket == null) {
            userSocket = new UserSocket(login);
            USERS_SOCKETS.put(userLogin, userSocket);
        }
        
        return new UserConnection(login, ip, agent, uuid);
    }
    
    public static void closeUserSocket(final SseEventSink sseEvent) {
        if (sseEvent instanceof SseSocket) {
            final SseSocket sse = (SseSocket) sseEvent;
            final UserSocket userSocket = USERS_SOCKETS.get(sse.getUserConnexion().getLogin());
            if (userSocket != null) {
                userSocket.removeConnection(sse.getUserConnexion().getUid());
            }
        }
        sseEvent.close();
    }
    
    public static void sendGlobaleEvent(final String event, final JsonObject data) {
        sendEvent(SSE_GLOBALE_CHANNEL, event, data, null);
    }
    
    public static void sendAlertsUpdate() {
        sendAdminEvent("alerts_update", new Json(null));
    }
    
    public static void sendAdminEvent(final String event, final JsonObject data) {
        sendEvent(SSE_ADMIN_CHANNEL, event, data, null);
    }
    
    public static void sendEvent(final String channel, final String event, final JsonObject data) {
        sendEvent(channel, event, data, null);
    }
    
    public static void sendEvent(final String channel, final String event, final JsonObject data,
                                 final String cronExpr) {
        Asserts.notEmpty("SSE channel is mandatory! " + event, channel);
        Asserts.notEmpty(String.format("SSE event name is mandatory (channel:%s)", channel), event);
        
        final String json = data.convertToJson();
        
        if (JavaScriptEngine.getInstance().validateJson(json)) {
            try {
                SSE_SENDER.addEvent(new SendSseEvent(data, event, cronExpr, channel));
            }
            catch (final Exception e) {
                Loggers.PROVIDER.error("can't serialize data : {}", event);
            }
        }
        else {
            Loggers.PROVIDER.error("can't serialize json data : {}  : {}", event, json);
        }
    }
    
    public static synchronized int getNbSocketsOpen() {
        return SseSocketSensor.get();
    }
    
    public static JsonObject buildTimeStamp() {
        return new StringJson(String.valueOf(System.currentTimeMillis()));
    }
    
    public static List<UserSocket> getUserSockets() {
        final List<UserSocket> result = new ArrayList<>();
        for (final Map.Entry<String, UserSocket> entry : USERS_SOCKETS.entrySet()) {
            if (entry.getValue() != null) {
                result.add(entry.getValue());
            }
        }
        //@formatter:off
        result.sort((ref,value) ->           (ref.getLogin() == null ? "" : ref.getLogin())
                                   .compareTo(value.getLogin() == null ? "" : value.getLogin()));
        //@formatter:on
        return Collections.unmodifiableList(result);
    }
}
