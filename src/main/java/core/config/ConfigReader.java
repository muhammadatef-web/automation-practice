package core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    // Runs automatically once when ConfigReader is first referenced
    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in src/test/resources!");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        // 1. Check Maven CLI override (e.g., mvn test -Dbrowser=firefox)
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }

        // 2. Check OS Environment Variable (e.g., BASE_URL)
        String envKey = key.toUpperCase().replace('.', '_');
        String envVar = System.getenv(envKey);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }

        // 3. Fallback to config.properties
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value;
    }
}