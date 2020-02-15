package ru.mgvk.simplegateway;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class Main implements NetWorker.OnDataRecieve {


    private Logger log = Logger.getLogger("Main");

    public Main() {
        try {

            Config.readFromFile(Paths.get("devices_config")).onComplete(configEntry -> {
                Publisher.getInstance().addCollector(configEntry);

            });

            Publisher.getInstance().startServer();

            NetWorker.initFromConfig(Config.getConfigEntries()).setOnDataRecieve(this).start();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(
                ("handlers = java.util.logging.FileHandler,java.util.logging.ConsoleHandler\n"
                 + "java.util.logging.FileHandler.level    = INFO\n"
                 + "java.util.logging.FileHandler.formatter = ru.mgvk.simplegateway.MyFormatter\n"
                 + "java.util.logging.FileHandler.append    = true\n"
                 + "java.util.logging.FileHandler.pattern   = log.%u.%g.txt\n"
                 + "java.util.logging.ConsoleHandler.formatter = ru.mgvk.simplegateway.MyFormatter").getBytes()));

        new Main();
    }

    @Override
    public void onRecieve(Config.ConfigEntry configEntry, final String data) {
        log.info("Recieved!: " + data);

        if (data.contains(";")) {

            String[] parts = data.split(";");
            Matcher  m;
            for (String part : parts) {

                try {

                    if (part.matches(configEntry.getFormat())) {

                        m = configEntry.getPattern().matcher(part);
                        if (m.find() && m.groupCount() >= 2) {
                            Publisher.getInstance().publish(configEntry, m.group("type"),
                                    Double.parseDouble(m.group("value")));
                        } else {
                            log.log(Level.parse("ERROR"), "Non matching data!");
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }
}