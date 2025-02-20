package dev.kingma.keycloak.data.realm;

import dev.kingma.keycloak.domain_interaction.realm.RealmDataService;
import dev.kingma.keycloak.domain_interaction.realm.RealmFlow;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


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

    @Override
    public Set<RealmFlow.RealmDTO> findAll() {
        return realmProjectionRepository.findAll().stream()
            .map(realmProjection -> new RealmFlow.RealmDTO(
                realmProjection.getName(),
                realmProjection.getDisplayName()))
            .collect(Collectors.toSet());
    }
}
