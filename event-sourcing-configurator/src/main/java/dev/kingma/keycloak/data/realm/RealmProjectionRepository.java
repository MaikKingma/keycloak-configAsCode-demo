package dev.kingma.keycloak.data.realm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealmProjectionRepository extends JpaRepository<RealmProjection, String> {
}
