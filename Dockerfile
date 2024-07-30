FROM registry.access.redhat.com/ubi8-minimal:8.10 AS builder
RUN microdnf update -y && \
    microdnf install -y java-21-openjdk-headless && microdnf clean all && rm -rf /var/cache/yum/* && \
    echo "keycloak:x:0:root" >> /etc/group && \
    echo "keycloak:x:1000:0:keycloak user:/opt/keycloak:/sbin/nologin" >> /etc/passwd

COPY --chown=keycloak:keycloak keycloak/target/keycloak-25.0.2  /opt/keycloak

USER 1000

RUN /opt/keycloak/bin/kc.sh build --db=postgres

FROM registry.access.redhat.com/ubi8-minimal:8.10

RUN microdnf update -y && \
    microdnf reinstall -y tzdata && \
    microdnf install -y java-21-openjdk-headless && \
    microdnf clean all && rm -rf /var/cache/yum/* && \
    echo "keycloak:x:0:root" >> /etc/group && \
    echo "keycloak:x:1000:0:keycloak user:/opt/keycloak:/sbin/nologin" >> /etc/passwd && \
    ln -sf /usr/share/zoneinfo/Europe/Amsterdam /etc/localtime # set timezone

COPY --from=builder --chown=1000:0 /opt/keycloak /opt/keycloak
RUN mkdir -p /opt/keycloak-config && chown 1000:0 /opt/keycloak-config
COPY --chown=1000:0 java-configuration/target/java-configuration.jar /opt/keycloak-config
COPY --chown=1000:0 java-configuration/target/classes/scripts/start-configuration.sh /opt/keycloak-config


USER 1000
WORKDIR /opt/keycloak-config

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]
