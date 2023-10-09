package no.vinny.nightfly.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchRepository;
import no.vinny.nightfly.batch.BatchService;
import no.vinny.nightfly.batch.impl.BatchRepositoryImpl;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
public class DatabaseConfig  {

    @Autowired
    private Environment env;

    @Bean
    @Profile("prod")
    public ConnectionFactory connectionFactory() {
       MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
               .host(env.getProperty("spring.r2dbc.host"))
               .database(env.getProperty("spring.r2dbc.database"))
               .username(env.getProperty("spring.r2dbc.username"))
               .password(env.getProperty("spring.r2dbc.password"))
               .build();
       log.info("Initializing DB config {}", conf);
       return new MariadbConnectionFactory(conf);
    }

    @Bean
    @Profile("!prod")
    public ConnectionFactory h2ConnectionFactory() {
        log.info("Initializing H2 connection factory: {}", env.getProperty("spring.r2dbc.url"));
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(env.getProperty("spring.r2dbc.url"));
        return ConnectionFactoryBuilder.withOptions(options.mutate())
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("h2init.sql")));
        return initializer;
    }


    //@Bean
    //public CommandLineRunner dataLoader(BatchService batchService) {
    //    return args -> {
    //        batchService.add(BatchDTO.builder()
    //                .brewfatherId("HY27A73dYWZNMxapgE4UdljPtNvDOL")
    //                .name("MarisOtter SMASH")
    //                .status(Batch.Status.COMPLETED)
    //                .build());
    //        batchService.add(BatchDTO.builder()
    //                .brewfatherId("LAXI2KWZXcU2pBpzrfg6B3Uy5940vQ")
    //                .name("Eldon")
    //                .status(Batch.Status.COMPLETED)
    //                .build());
    //        batchService.add(BatchDTO.builder()
    //                .brewfatherId("NLltIcoo87foHbTz1N8rH0v0KXht6q")
    //                .name("Lutra")
    //                .status(Batch.Status.COMPLETED)
    //                .build());
    //    };
    //}

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .namedParameters(true)
                .build();
    }

}
