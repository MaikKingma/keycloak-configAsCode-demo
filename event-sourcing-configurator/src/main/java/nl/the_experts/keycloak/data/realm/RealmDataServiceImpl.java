package nl.the_experts.keycloak.data.realm;

import nl.the_experts.keycloak.domain_interaction.realm.RealmDataService;
import org.springframework.stereotype.Service;


@Service
public class RealmDataServiceImpl implements RealmDataService {
    private final RealmProjectionRepository realmProjectionRepository;

    public RealmDataServiceImpl(RealmProjectionRepository realmProjectionRepository) {
        this.realmProjectionRepository = realmProjectionRepository;
    }

    @Override
    public void createRealmProjection(String name, String displayName) {
        realmProjectionRepository.save(RealmProjection.builder().name(name).displayName(displayName).build());
    }
}
