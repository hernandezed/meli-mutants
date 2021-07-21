package com.meli.mutants;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public abstract class MeliMutantsApplicationTests {
    public static final int REDIS_PORT = 6379;

    @SpyBean
    RedisTemplate<String, DnaResult> template;

    @Container
    static GenericContainer redis = new GenericContainer<>("redis")
            .withExposedPorts(REDIS_PORT)
            .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", () -> "localhost");
        registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));
    }
}
