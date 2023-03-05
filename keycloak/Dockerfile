FROM quay.io/keycloak/keycloak:20.0.3 as builder

RUN curl -sL https://github.com/alextim1508/keycloak-user-provider/blob/main/dist/user-provider.jar?raw=true > /opt/keycloak/providers/user-provider.jar

RUN /opt/keycloak/bin/kc.sh --verbose build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_HOSTNAME=localhost

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]