package com.risonna.schedulewebapp.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/pdf/{taskId}")
public class PdfWebSocket {
    private static Set<Session> sessions = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String, Session> taskSessionMap = new ConcurrentHashMap<>();

    // Define WebSocket event methods: OnOpen, OnMessage, OnClose, OnError
    @OnOpen
    public void onOpen(Session session, @PathParam("taskId") String taskId) {
        sessions.add(session);
        session.getUserProperties().put("taskId", taskId);
        System.out.println("TASK ID IN ONOPEN WEBSOCKET IS " + taskId);
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

    public static void notifyClient(String taskId) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                String taskIdFromUser = (String) session.getUserProperties().get("taskId");
                System.out.println("TASK ID IN SESSION IS " + taskIdFromUser);
                System.out.println("TASKID GIVEN TO METHOD IS " + taskId);

                if (taskIdFromUser != null && taskId.equals(taskIdFromUser)) {
                    try {
                        session.getBasicRemote().sendText(taskIdFromUser);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    System.out.println("NotifyClientIf Doesnt Work!!!!");
                }
            }
        }
    }
}
