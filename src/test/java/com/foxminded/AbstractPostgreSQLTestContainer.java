package com.foxminded;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AbstractPostgreSQLTestContainer.Initializer.class)
public abstract class AbstractPostgreSQLTestContainer {
    private static final String CONTAINER_VERSION = "postgres:latest";
    public static final PostgreSQLContainer<?> postgres;
    public static final DriverManagerDataSource testDatasource;

    static {
        postgres = new PostgreSQLContainer<>(CONTAINER_VERSION);
        postgres.start();

        testDatasource = new DriverManagerDataSource();
        testDatasource.setDriverClassName(postgres.getDriverClassName());
        testDatasource.setUrl(postgres.getJdbcUrl());
        testDatasource.setUsername(postgres.getUsername());
        testDatasource.setPassword(postgres.getPassword());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext,
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            );
        }
    }
}
