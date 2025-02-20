package dev.kingma.keycloak.data.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProjectionRepository extends JpaRepository<ClientProjection, String> {
}
