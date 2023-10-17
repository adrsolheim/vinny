package no.vinny.nightfly.batch;

import no.vinny.nightfly.config.Pagination;
import no.vinny.nightfly.security.SupabaseAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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
    private List<BatchDTO> batchDTOList() {
        List<BatchDTO> batches = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            BatchDTO batch = BatchDTO.builder()
                                .id(Long.valueOf(i*10))
                                .name("Foo"+i)
                                .brewfatherId("BID"+i)
                                .status("COMPLETED")
                                .build();
            batches.add(batch);
        }
        return batches;
    }
}