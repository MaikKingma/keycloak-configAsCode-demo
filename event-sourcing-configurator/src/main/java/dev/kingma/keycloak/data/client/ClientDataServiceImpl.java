package dev.kingma.keycloak.data.client;

import dev.kingma.keycloak.data.realm.RealmProjectionRepository;
import dev.kingma.keycloak.domain_interaction.client.ClientDataService;
import org.springframework.stereotype.Service;

@Service
public class ClientDataServiceImpl implements ClientDataService {
    private final ClientProjectionRepository clientProjectionRepository;
    private final RealmProjectionRepository realmProjectionRepository;

    public ClientDataServiceImpl(ClientProjectionRepository clientProjectionRepository, RealmProjectionRepository realmProjectionRepository) {
        this.clientProjectionRepository = clientProjectionRepository;
        this.realmProjectionRepository = realmProjectionRepository;
    }

    @Override
    public void createClientProjection(String realmName, String clientId) {
        clientProjectionRepository.save(ClientProjection.builder()
                .clientProjectionId(new ClientProjectionId(realmName, clientId))
                .build());
    }
}
