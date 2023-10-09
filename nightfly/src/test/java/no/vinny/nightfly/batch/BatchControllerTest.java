package no.vinny.nightfly.batch;

import no.vinny.nightfly.config.Pagination;
import no.vinny.nightfly.security.SupabaseAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.mock;


//@Profile(value = "dev")
//@SpringJUnitConfig(CoreApplicationConfig.class)
@ActiveProfiles(value = "dev")
@WebFluxTest
class BatchControllerTest {

    private WebTestClient testClient;
    @MockBean
    private SupabaseAuthService supabaseAuthService;
    @MockBean
    private BatchService batchService;
    @MockBean
    private Pagination pagination;

    @BeforeEach
    void setUp(ApplicationContext context) {
        supabaseAuthService = mock(SupabaseAuthService.class);
        batchService = mock(BatchService.class);
        pagination = mock(Pagination.class);
        testClient = WebTestClient
                .bindToApplicationContext(context)
                .build();
    }

    @Test
    public void fetchAllBatches_denied_access_upon_unauthorized() {
        testClient
                .get()
                .uri("/api/batches")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}