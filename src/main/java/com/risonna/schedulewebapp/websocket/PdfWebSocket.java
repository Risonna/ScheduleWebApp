package com.risonna.schedulewebapp.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/pdf")
public class PdfWebSocket {
    private static Set<Session> sessions = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String, Session> taskSessionMap = new ConcurrentHashMap<>();

    // Define WebSocket event methods: OnOpen, OnMessage, OnClose, OnError
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        // Handle when a new WebSocket connection is established
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle WebSocket messages from clients
        taskSessionMap.put(message, session);
        System.out.println("Message receiver, " + message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        taskSessionMap.values().removeIf(s -> s.equals(session));
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
    public static void notifyClient(String taskId) {
        Session session = taskSessionMap.get(taskId);
        // ... (find the right session using taskId)
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(taskId);
                System.out.println("Message " + taskId + " sent to client");
                // Optionally, store the pdfData somewhere the client can access it via HTTP
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Either there is no session open whatsoever or the session wasn't registered yet");
        }
    }
}
