package com.meli.mutants;

import com.meli.mutants.configuration.TestRedisConfiguration;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public abstract class MeliMutantsApplicationTests {

    @SpyBean
    RedisTemplate<String, DnaResult> template;

}
