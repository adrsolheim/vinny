package no.vinny.nightfly.components.batch;

import no.vinny.nightfly.domain.batch.Batch;
import no.vinny.nightfly.config.Pagination;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static no.vinny.nightfly.domain.batch.BatchStatus.COMPLETED;


@ActiveProfiles(value = "dev")
@WebMvcTest(BatchController.class)
class BatchControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    BatchService batchService;
    @Mock
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