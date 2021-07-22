package com.meli.mutants.functional;

import com.meli.mutants.MeliMutantsApplicationTests;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import com.meli.mutants.data_access.repositories.dna_result.settings.DnaResultPrefixSettings;
import com.meli.mutants.harness.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class MutantControllerGetStatisticsTest extends MeliMutantsApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    LettuceConnectionFactory connectionFactory;
    @Autowired
    DnaResultPrefixSettings dnaResultPrefixSettings;

    RedisAtomicLong humanCounter;
    RedisAtomicLong mutantCounter;

    @BeforeEach
    void setup() {
        humanCounter = new RedisAtomicLong(dnaResultPrefixSettings.dnaCounterKey(DnaResultType.HUMAN.name().toLowerCase()), connectionFactory);
        mutantCounter = new RedisAtomicLong(dnaResultPrefixSettings.dnaCounterKey(DnaResultType.MUTANT.name().toLowerCase()), connectionFactory);
        humanCounter.set(0);
        mutantCounter.set(0);
    }

    @Test
    void doGet_whenDnasWereInserted_returnOkResponse() throws IOException {
        String response = FileReader.read("./src/test/resources/harness/response/GET_statistics_when_dnas_were_inserted.json");

        humanCounter.set(100);
        mutantCounter.set(40);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/stats", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(response).isEqualTo(responseEntity.getBody());
    }

    @Test
    void doGet_whenNoDnasWereInserted_returnOkResponse() throws IOException {
        String response = FileReader.read("./src/test/resources/harness/response/GET_statistics_when_no_dnas_were_inserted.json");

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/stats", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(response).isEqualTo(responseEntity.getBody());
    }


}
