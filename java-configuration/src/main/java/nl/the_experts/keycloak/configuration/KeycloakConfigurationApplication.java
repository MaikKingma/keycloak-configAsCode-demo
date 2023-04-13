package nl.the_experts.keycloak.configuration;

import org.jboss.logging.Logger;
import org.keycloak.admin.client.Keycloak;

/**
 * Application to configure Keycloak.
 * The configuration properties to connect to Keycloak must be passed as Java system properties or
 * environment variables.
 * The following properties are required:
 * <table>
 *     <tr>
 *         <th>Property</th><th>System property</th><th>Environment variable</th>
 *     </tr>
 *     <tr>
 *         <td>Server URL</td><td>keycloak.server</td><td>KEYCLOAK_SERVER</td>
 *     </tr>
 *     <tr>
 *         <td>Username</td><td>keycloak.user</td><td>KEYCLOAK_USER</td>
 *     </tr>
 *     <tr>
 *         <td>Password</td><td>keycloak.password</td><td>KEYCLOAK_PASSWORD</td>
 *     </tr>
 *     <tr>
 *         <td>Realm</td><td>keycloak.realm</td><td>KEYCLOAK_REALM</td>
 *     </tr>
 *     <tr>
 *         <td>Environment</td><td>keycloak.environment.type</td><td>KEYCLOAK_ENVIRONMENT_TYPE</td>
 *     </tr>
 * </table>
 */
public class KeycloakConfigurationApplication {
    /**
     * Main method to start Keycloak configuration.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        try {
            KeycloakConfigurationProperties configuration = KeycloakConfigurationProperties.fromEnv();

            KeycloakClientBuilder keycloakClientBuilder = KeycloakClientBuilder.create(
                    configuration.get("KEYCLOAK_SERVER"),
                    configuration.get("KEYCLOAK_USER"),
                    configuration.get("KEYCLOAK_PASSWORD"),
                    configuration.get("KEYCLOAK_REALM"));
            Keycloak keycloak = keycloakClientBuilder.getClient();

            KeycloakConfiguration keycloakConfig = new KeycloakConfiguration(keycloak);
            keycloakConfig.configure();
        } catch (Exception all) {
            Logger.getLogger(KeycloakConfigurationApplication.class).error("Exception occurred.", all);
            throw all;
        }
    }
}
