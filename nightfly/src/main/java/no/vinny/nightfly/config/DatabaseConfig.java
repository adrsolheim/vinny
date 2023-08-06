package no.vinny.nightfly.config;

import io.r2dbc.spi.ConnectionFactory;
import no.vinny.nightfly.batch.Batch;
import no.vinny.nightfly.batch.BatchDTO;
import no.vinny.nightfly.batch.BatchService;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig  {

    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() {
       MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
               .host(env.getProperty("spring.r2dbc.host"))
               .database(env.getProperty("spring.r2dbc.database"))
               .username(env.getProperty("spring.r2dbc.username"))
               .password(env.getProperty("spring.r2dbc.password"))
               .build();
        return new MariadbConnectionFactory(conf);
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

    //@Bean
    //public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
    //    return DatabaseClient.builder()
    //            .connectionFactory(connectionFactory)
    //            .namedParameters(true)
    //            .build();
    //}

    //@Bean
    //public TransactionManager transactionManager(ConnectionFactory connectionFactory) {
    //    return new R2dbcTransactionManager(connectionFactory);
    //}
}
