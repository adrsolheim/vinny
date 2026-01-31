package no.vinny.nightfly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Profile("dev")
@Configuration
public class H2Config {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public org.h2.tools.Server h2TcpServer() throws SQLException {
        return org.h2.tools.Server.createTcpServer(
            "-tcp",
            "-tcpAllowOthers",
            "-tcpPort",
            "9092"
        );
    }
}
