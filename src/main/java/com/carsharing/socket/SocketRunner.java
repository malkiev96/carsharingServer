package com.carsharing.socket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketRunner implements CommandLineRunner {


    private final SocketService socketService;

    public SocketRunner(SocketService socketService) {
        this.socketService = socketService;
    }

    @Override
    public void run(String... args) throws Exception {
        socketService.executeAsynchronously();
    }
}
