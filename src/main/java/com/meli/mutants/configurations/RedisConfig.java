package com.meli.mutants.configurations;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class RedisConfig {
    @Bean
    public RedisTemplate<String, DnaResult> dnaResultRedisTemplate(LettuceConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, DnaResult>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, String[]> dnaArrayResultRedisTemplate(LettuceConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, String[]>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return clientConfigurationBuilder -> {
            if (clientConfigurationBuilder.build().isUseSsl()) {
                clientConfigurationBuilder.useSsl().disablePeerVerification();
            }
        };
    }

}
