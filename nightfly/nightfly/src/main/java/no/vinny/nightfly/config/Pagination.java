package no.vinny.nightfly.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="pagination.batch")
@Data
public class Pagination {
    @Min(value=2, message="must be between 2 and 100")
    @Max(value=100, message="must be between 2 and 100")
    private int pageSize;
}
