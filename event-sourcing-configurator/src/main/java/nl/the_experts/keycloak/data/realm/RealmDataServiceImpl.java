package nl.the_experts.keycloak.data.realm;

import nl.the_experts.keycloak.domain_interaction.realm.RealmDataService;
import org.springframework.stereotype.Service;


@Service
public class RealmDataServiceImpl implements RealmDataService {
    private final RealmRepository realmRepository;

    public RealmDataServiceImpl(RealmRepository realmRepository) {
        this.realmRepository = realmRepository;
    }

    @Override
    public void createRealmProjection(String name, String displayName) {
        realmRepository.save(RealmProjection.builder().name(name).displayName(displayName).build());
    }
}
