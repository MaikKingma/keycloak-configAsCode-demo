package nl.the_experts.keycloak.data.client;

import nl.the_experts.keycloak.data.realm.RealmProjectionRepository;
import nl.the_experts.keycloak.domain_interaction.client.ClientDataService;
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
