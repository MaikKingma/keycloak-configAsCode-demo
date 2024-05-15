package nl.the_experts.keycloak.data.realm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealmRepository  extends JpaRepository<RealmProjection, String> {
}
