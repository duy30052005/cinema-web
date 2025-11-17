package com.example.demo.websocket;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketServe {
    private ServerSocket serverSocket;
    private boolean running = true;
    // Lưu trữ các kết nối client (username -> socket)
    private final ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        new Thread(() -> start(8081)).start(); // Khởi động server trên cổng 8081
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Socket Server started on port {}", port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            log.error("Error starting Socket Server: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            log.error("Error stopping Socket Server: {}", e.getMessage());
        }
    }

    // Xử lý mỗi client
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String message;
                while ((message = reader.readLine()) != null) {
                    log.info("Received from {}: {}", username, message);

                    // Xử lý các lệnh
                    if (message.startsWith("JOIN ")) {
                        username = message.split(" ")[1];
                        clients.put(username, clientSocket);
                        broadcast(username + " joined the chat");
                    } else if (message.startsWith("LEAVE ")) {
                        if (username != null) {
                            clients.remove(username);
                            broadcast(username + " left the chat");
                            username = null;
                        }
                    } else if (message.startsWith("SEND ")) {
                        String content = message.substring(5); // Lấy nội dung sau SEND
                        if (username != null) {
                            broadcast(username + ": " + content);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Error handling client: {}", e.getMessage());
            } finally {
                if (username != null) {
                    clients.remove(username);
                    broadcast(username + " left the chat");
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    log.error("Error closing client socket: {}", e.getMessage());
                }
            }
        }

        // Gửi tin nhắn đến tất cả client
        private void broadcast(String message) {
            for (Socket client : clients.values()) {
                try {
                    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    writer.println(message);
                } catch (IOException e) {
                    log.error("Error broadcasting to client: {}", e.getMessage());
                }
            }
        }
    }
}
