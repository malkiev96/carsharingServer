package com.carsharing.socket;

import com.carsharing.service.TrackerDataService;
import com.carsharing.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.net.ServerSocket;

@Service
public class SocketService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private TrackerDataService dataService;

    public void executeAsynchronously() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket server = new ServerSocket(4500, 0);
                    System.out.println("server is started");
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

            }
        });
    }

}
