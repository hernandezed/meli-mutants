package com.meli.mutants.configurations;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class RedisTemplateConfig {
    @Bean
    public RedisTemplate<String, DnaResult> dnaResultRedisTemplate(LettuceConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, DnaResult>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }
}
