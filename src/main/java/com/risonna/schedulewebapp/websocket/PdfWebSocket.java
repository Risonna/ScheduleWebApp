package com.risonna.schedulewebapp.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
public class PdfWebSocket {
    private static Set<Session> sessions = new CopyOnWriteArraySet<>();
    private Session session;

    // Define WebSocket event methods: OnOpen, OnMessage, OnClose, OnError
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        // Handle when a new WebSocket connection is established
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle WebSocket messages from clients
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        // Handle when a WebSocket connection is closed
    }

    @OnError
    public void onError(Throwable error) {
        // Handle WebSocket errors
    }

    public static void notifyClients() {
        // Send a notification to all connected clients
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText("pdf_ready");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
