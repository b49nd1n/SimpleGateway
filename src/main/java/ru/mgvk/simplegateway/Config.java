package ru.mgvk.simplegateway;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Config {

    private static List<ConfigEntry> configEntries;

    private static Logger log = Logger.getLogger("Config");


    public static List<ConfigEntry> getConfigEntries() {
        return configEntries;

    }

    public void setConfigEntries(List<ConfigEntry> configEntries) {
        Config.configEntries = configEntries;
    }

    public static Config readFromFile(Path filePath) throws IOException {

        Config config = new Config();

        if (Files.isRegularFile(filePath)) {

            config.setConfigEntries(Files.readAllLines(filePath).stream()
                    .filter(s -> !s.startsWith("#"))
                    .map(ConfigEntry::fromLine)
                    .filter(ConfigEntry::isValid)
                    .collect(Collectors.toList()));

        }

        return config;

    }

    public void onComplete(Consumer<ConfigEntry> configEntryConsumer) {

        if (configEntryConsumer != null) {
            getConfigEntries().forEach(configEntryConsumer);
        }

    }

    static class ConfigEntry {

        private String  deviceName;
        private int     port;
        private String  format;
        private boolean valid;
        private Pattern pattern;

        public ConfigEntry() {
        }

        public ConfigEntry(String deviceName, int port, String format) {
            this.deviceName = deviceName;
            this.port = port;
            this.format = format;

            pattern = Pattern.compile(format);
        }

        public static ConfigEntry fromLine(String configLine) {

            ConfigEntry configEntry = new ConfigEntry();

            String[] parts = configLine.split(";");
            if (parts.length == 3) {


                configEntry.setDeviceName(parts[0]);

                configEntry.setPort(Integer.parseInt(parts[1]));

                configEntry.setFormat(parts[2]);

                configEntry.setPattern(Pattern.compile(configEntry.getFormat()));

                configEntry.setValid(configEntry.isValid());


                log.info("Loaded config entry:" + configEntry.toString());

            }


            return configEntry;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public boolean isValid() {
            return getDeviceName() != null && getFormat() != null && getPort() > 1000 && pattern != null;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        @Override
        public String toString() {
            return "ConfigEntry{" +
                   "deviceName='" + deviceName + '\'' +
                   ", port=" + port +
                   ", format='" + format + '\'' +
                   ", valid=" + valid +
                   '}';
        }

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }
    }

}
