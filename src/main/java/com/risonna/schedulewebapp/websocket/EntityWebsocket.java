package com.risonna.schedulewebapp.websocket;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/entity")
public class EntityWebsocket {
    private static Set<Session> sessions = new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        // Handle when a new WebSocket connection is established
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle WebSocket messages from clients
        System.out.println("Message receiver, " + message);
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

        public static void notifyClients(String entity) {
        // Send a notification to all connected clients
        switch (entity){
            case("lessons"): {
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText("refetchLessons");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case("teachers"):{
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText("refetchTeachers");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case("subjects"):{
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText("refetchSubjects");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case("cabinets"):{
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText("refetchCabinets");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case("groups"):{
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText("refetchGroups");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case("adminsTeachers"):{
                for (Session session : sessions) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText("refetchAdminsTeachers");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            default:{
                System.out.println("this was invoked with a wrong string argument(PdfWebSocket.notifyAllClients)");
            }
        }

    }
}
