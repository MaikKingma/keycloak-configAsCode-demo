package nl.the_experts.keycloak.configuration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class with configuration properties for configuring Keycloak.
 * Configuration properties can be set using a combination of environment variables and Java system properties
 * (the latter has higher precedence) using {@link #fromEnv()}.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KeycloakConfigurationProperties {

    private final Map<String, String> configuration;

    /**
     * Gets the value for given configuration property
     *
     * @param name Name of the configuration property to get a value for.
     * @return Value for the configuration property or null if the property doesn't exist.
     */
    public String get(String name) {
        return configuration.get(name);
    }

    /**
     * Creates a new configuration based on System environment variables and Java system properties.
     * Java system properties have higher precedence than environment variables.
     *
     * @return New {@link KeycloakConfigurationProperties} instance.
     */
    public static KeycloakConfigurationProperties fromEnv() {
        Map<String, String> systemProperties = System.getProperties().entrySet().stream()
                .collect(Collectors.toMap(e -> convertKey((String) e.getKey()), e -> (String) e.getValue()));
        Map<String, String> configMap = new HashMap<>(System.getenv());
        configMap.putAll(systemProperties);

        return new KeycloakConfigurationProperties(configMap);
    }

    private static String convertKey(String key) {
        return key.replace('.', '_').toUpperCase(Locale.ROOT);
    }
}
