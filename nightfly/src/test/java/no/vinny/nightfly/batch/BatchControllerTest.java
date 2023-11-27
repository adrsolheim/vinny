package no.vinny.nightfly.batch;

import no.vinny.nightfly.components.batch.BatchController;
import no.vinny.nightfly.components.batch.BatchService;
import no.vinny.nightfly.components.batch.domain.Batch;
import no.vinny.nightfly.config.Pagination;
import no.vinny.nightfly.security.SupabaseAuthService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static no.vinny.nightfly.components.batch.domain.BatchStatus.COMPLETED;


@ActiveProfiles(value = "dev")
@WebMvcTest(BatchController.class)
class BatchControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    SupabaseAuthService supabaseAuthService;
    @MockBean
    BatchService batchService;
    @MockBean
    Pagination pagination;
    @Mock
    Pageable pageable;

    /*
    @BeforeEach
    void setup() {
        when(pagination.getPageSize()).thenReturn(20);
        when(pageable.getPageSize()).thenReturn(20);
        when(pageable.getOffset()).thenReturn(0L);
        when(batchService.getAll(pageable)).thenReturn(batchDTOList());
    }
    @Test
    public void fetchAllBatches_denied_access_upon_unauthorized() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/batches"))
                .andExpect(status().isUnauthorized());
    }*/
    private List<Batch> batchDTOList() {
        List<Batch> batches = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Batch batch = Batch.builder()
                                .id(Long.valueOf(i*10))
                                .name("Foo"+i)
                                .brewfatherId("BID"+i)
                                .status(COMPLETED)
                                .build();
            batches.add(batch);
        }
        return batches;
    }
}