package ru.mgvk.simplegateway;

import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Publisher {


    public static  Map<Config.ConfigEntry, Gauge> collectors = new HashMap<>();
    private static Publisher                      publisher;
    private        HTTPServer                     httpServer;
    private        int                            port       = 5555;

    public Publisher(int port) {
        this.port = port;
    }

    public Publisher() {
    }

    public static Publisher getInstance() {
        if (publisher == null) {
            publisher = new Publisher();
        }
        return publisher;
    }


    void startServer() throws IOException {
        httpServer = new HTTPServer(port);
    }

    public void addCollector(Config.ConfigEntry config) {
        collectors.put(config, Gauge.build()
                .name(config.getDeviceName())
                .help(config.getDeviceName())
                .labelNames("type","updateDate")
                .register());
    }

    public void publish(Config.ConfigEntry configEntry, String type, Double value, String date) {
        collectors.get(configEntry).labels(type, date).set(value);
    }


}
