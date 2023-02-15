FROM registry.access.redhat.com/ubi8-minimal:8.7 AS builder
RUN microdnf update -y && \
    microdnf install -y java-17-openjdk-headless && microdnf clean all && rm -rf /var/cache/yum/* && \
    echo "keycloak:x:0:root" >> /etc/group && \
    echo "keycloak:x:1000:0:keycloak user:/opt/keycloak:/sbin/nologin" >> /etc/passwd

COPY --chown=keycloak:keycloak keycloak/target/keycloak-20.0.3  /opt/keycloak

USER 1000

RUN /opt/keycloak/bin/kc.sh build --db=postgres

FROM registry.access.redhat.com/ubi8-minimal:8.7

RUN microdnf update -y && \
    microdnf reinstall -y tzdata && \
    microdnf install -y java-17-openjdk-headless && \
    microdnf clean all && rm -rf /var/cache/yum/* && \
    echo "keycloak:x:0:root" >> /etc/group && \
    echo "keycloak:x:10001:0:keycloak user:/opt/keycloak:/sbin/nologin" >> /etc/passwd && \
    ln -sf /usr/share/zoneinfo/Europe/Amsterdam /etc/localtime # set timezone

COPY --from=builder --chown=1000:0 /opt/keycloak /opt/keycloak

USER 1000
WORKDIR /opt/keycloak-config

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start"]
