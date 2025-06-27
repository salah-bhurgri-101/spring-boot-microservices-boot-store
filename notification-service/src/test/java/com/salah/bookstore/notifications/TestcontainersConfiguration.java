package com.salah.bookstore.notifications;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"));
    }

    @Bean
    GenericContainer<?> mailhog() {
        return new GenericContainer<>(DockerImageName.parse("mailhog/mailhog:v1.0.1")).withExposedPorts(1025);
    }

    @Bean
    DynamicPropertyRegistrar dynamicPropertyRegistrar(GenericContainer<?> mailhog) {
        return (registry) -> {
            registry.add("spring.mail.host", mailhog::getHost);
            registry.add("spring.mail.port", mailhog::getFirstMappedPort);
        };
    }
}
