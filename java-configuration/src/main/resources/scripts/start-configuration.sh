#!/bin/bash
echo Starting Keycloak configuration

if [[ -z "$KEYCLOAK_CONFIG_SERVER_URI" ]]; then
    KEYCLOAK_CONFIG_SERVER_URI=http://keycloak:8080
fi
if [[ -z "$KEYCLOAK_CONFIG_REALM" ]]; then
    KEYCLOAK_CONFIG_REALM=master
fi

JAR=java-configuration.jar

if [[ ! -f $JAR ]];then
  JAR=/opt/keycloak-config/$JAR
fi

# Start Java based configuration.
java -Dkeycloak.server=${KEYCLOAK_CONFIG_SERVER_URI} -Dkeycloak.realm=${KEYCLOAK_CONFIG_REALM} \
  -Dkeycloak.user=${KEYCLOAK_ADMIN} -Dkeycloak.password="${KEYCLOAK_ADMIN_PASSWORD}" \
  -jar $JAR "$@"

if [ $? -eq 0 ]
then
  echo "Keycloak configuration finished."
  exit 0
else
  echo "Keycloak configuration failed." >&2
  exit 1
fi
