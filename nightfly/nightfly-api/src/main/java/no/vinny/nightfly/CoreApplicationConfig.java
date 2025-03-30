package no.vinny.nightfly;

import no.vinny.nightfly.config.DatabaseConfig;
import no.vinny.nightfly.config.SecurityConfig;
import no.vinny.nightfly.util.RequestExceptionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SecurityConfig.class,
        DatabaseConfig.class,
        RequestExceptionInterceptor.class,
})
public class CoreApplicationConfig {
}

