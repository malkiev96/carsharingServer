package com.carsharing.socket;

import com.carsharing.service.TrackerDataService;
import com.carsharing.service.TrackerService;
import lombok.AllArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.net.ServerSocket;

@Service
@AllArgsConstructor
public class SocketService {

    private TaskExecutor taskExecutor;
    private TrackerService trackerService;
    private TrackerDataService dataService;

    public void executeAsynchronously() {
        taskExecutor.execute(() -> {
            try {
                ServerSocket server = new ServerSocket(4500, 0);
                System.out.println("serverSocket is started");
                // слушаем порт
                while (true) {
                    // ждём нового подключения, после чего запускаем обработку клиента
                    // в новый вычислительный поток и увеличиваем счётчик на единичку
                    new SampleServer(server.accept(), trackerService, dataService);
                }
            } catch (Exception e) {
                System.out.println("start error: " + e);
                e.printStackTrace();
            }
        });
    }

}
