package ru.mgvk.simplegateway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class NetWorker {


    private static NetWorker      netWorker;
    private        List<MyThread> threadsPool = new ArrayList<>();
    private        OnDataRecieve  onDataRecieve;
    private        Logger         log         = Logger.getLogger("NetWorker");

    public NetWorker() {
    }

    public static NetWorker initFromConfig(List<Config.ConfigEntry> configs) {

        if (netWorker == null) {
            netWorker = new NetWorker();
        }

        configs.forEach(configEntry -> {

            netWorker.createThread(configEntry);

        });


        return netWorker;

    }

    public OnDataRecieve getOnDataRecieve() {
        return onDataRecieve;
    }

    public NetWorker setOnDataRecieve(OnDataRecieve onDataRecieve) {
        this.onDataRecieve = onDataRecieve;
        return this;
    }


    void createThread(Config.ConfigEntry configEntry) {

        threadsPool.add(new MyThread(configEntry));
    }

    void start() {

        threadsPool.forEach(Thread::start);

    }

    void stop() {
        threadsPool.forEach(myThread -> {
            if (myThread != null && !myThread.isInterrupted()) {
                myThread.interrupt();
            }
        });
    }

    public interface OnDataRecieve {

        void onRecieve(Config.ConfigEntry configEntry, String data);

    }


    class MyThread extends Thread {

        private Config.ConfigEntry configEntry;
        private ServerSocket       socket;

        public MyThread(Config.ConfigEntry configEntry) {
            this.configEntry = configEntry;
            log.info(String.format("Created thread for device %s; port %s",
                    configEntry.getDeviceName(), configEntry.getPort()));
        }

        @Override
        public void run() {
            try {
                socket = new ServerSocket(configEntry.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.interrupted()) {
                try (Socket s = socket.accept()) {
                    Scanner scanner = new Scanner(s.getInputStream());
                    String  data    = "";
                    if (scanner.hasNextLine()) {
                        data = scanner.nextLine();
                    }
                    if (onDataRecieve != null) {
                        onDataRecieve.onRecieve(configEntry, data);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
