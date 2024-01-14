package no.vinny.nightfly.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.slf4j.Slf4j;
import org.mariadb.jdbc.MariaDbDataSource;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Configuration
@EnableTransactionManagement
public class DatabaseConfig  {

    @Autowired
    private Environment env;

    //@Profile("prod")
    //@Bean
    //public DataSource dataSource() throws SQLException {
    //    MariaDbDataSource dataSource = new MariaDbDataSource(env.getProperty("spring.datasource.jdbc-url"));
    //    dataSource.setUser(env.getProperty("spring.datasource.username"));
    //    dataSource.setUser(env.getProperty("spring.datasource.password"));
    //    return dataSource;
    //}

    @Bean
    public DataSource h2DataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("spring.datasource.jdbc-url"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("prod")
    public ConnectionFactory connectionFactory() {
        log.info("url {}, db {}, user {}, pw {}", env.getProperty("spring.r2dbc.url"), env.getProperty("spring.r2dbc.database"), env.getProperty("spring.r2dbc.username"), env.getProperty("spring.r2dbc.password"));
       MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
               .host(env.getProperty("spring.r2dbc.host"))
               .port(Integer.valueOf(env.getProperty("spring.r2dbc.port")))
               .database(env.getProperty("spring.r2dbc.database"))
               .username(env.getProperty("spring.r2dbc.username"))
               .password(env.getProperty("spring.r2dbc.password"))
               .build();
       log.info("Initializing DB config {}", conf);
       return new MariadbConnectionFactory(conf);
    }

    @Bean
    @Primary
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

    @Primary
    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .namedParameters(true)
                .build();
    }

    @Bean
    public DatabaseClient asyncDatabaseClient(ConnectionFactory asyncH2ConnectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(asyncH2ConnectionFactory)
                .namedParameters(true)
                .build();
    }
    @Bean
    @Profile("!prod")
    public ConnectionFactory asyncH2ConnectionFactory() {
        log.info("Initializing H2 connection factory: {}", env.getProperty("spring.r2dbc.url"));
        ConnectionFactoryOptions options = ConnectionFactoryOptions.parse(env.getProperty("spring.r2dbc.url"));
        return ConnectionFactoryBuilder.withOptions(options.mutate())
                .username(env.getProperty("spring.r2dbc.username"))
                .password(env.getProperty("spring.r2dbc.password"))
                .build();
    }
}
