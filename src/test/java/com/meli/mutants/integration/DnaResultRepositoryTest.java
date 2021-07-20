package com.meli.mutants.integration;

import com.meli.mutants.MeliMutantsApplicationTests;
import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import com.meli.mutants.data_access.repositories.dna_result.settings.DnaResultPrefixSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DnaResultRepositoryTest extends MeliMutantsApplicationTests {
    @Autowired
    DnaResultRepository dnaResultRepository;
    @Autowired
    RedisTemplate<String, DnaResult> dnaResultTemplate;

    @Autowired
    DnaResultPrefixSettings settings;
    private String mutantEntryKey;
    private String humanEntryKey;
    private String mutantCountKey;
    private String humanCountKey;

    @BeforeEach
    void setup() {
        mutantEntryKey = settings.getEntryKey("mutant");
        humanEntryKey = settings.getEntryKey("human");
        mutantCountKey = settings.dnaCounterKey("mutant");
        humanCountKey = settings.dnaCounterKey("human");
        dnaResultTemplate.delete(mutantEntryKey);
        dnaResultTemplate.delete(humanEntryKey);
        dnaResultTemplate.delete(settings.getHllKey("mutant"));
        dnaResultTemplate.delete(settings.getHllKey("human"));
        dnaResultTemplate.delete(mutantCountKey);
        dnaResultTemplate.delete(humanCountKey);
        new RedisAtomicLong(mutantCountKey, dnaResultTemplate.getConnectionFactory()).set(0);
        new RedisAtomicLong(humanCountKey, dnaResultTemplate.getConnectionFactory()).set(0);
    }

    @Test
    void saveAndLog_withMutantResult_persistInRedisSetAndHyperLogLog() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCT", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.saveAndLog(dnaResult);
        List<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForList().range(mutantEntryKey, 0, -1);
        List<DnaResult> humansDnaResults = dnaResultTemplate.opsForList().range(humanEntryKey, 0, -1);
        Long mutantCount = new RedisAtomicLong(mutantCountKey, dnaResultTemplate.getConnectionFactory()).get();
        Long humanCount = new RedisAtomicLong(humanCountKey, dnaResultTemplate.getConnectionFactory()).get();
        assertThat(mutantsDnaResults).containsExactly(dnaResult);
        assertThat(humansDnaResults).isEmpty();
        assertThat(mutantCount).isEqualTo(1);
        assertThat(humanCount).isEqualTo(0);
    }

    @Test
    void saveAndLog_withDifferentMutantResult_persistInRedisSetAndHyperLogLog() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCT", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        var otherDnaResult = new DnaResult(new String[]{
                "AAAA", "CGCT", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.saveAndLog(dnaResult);
        dnaResultRepository.saveAndLog(otherDnaResult);
        List<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForList().range(mutantEntryKey, 0, -1);
        List<DnaResult> humansDnaResults = dnaResultTemplate.opsForList().range(humanEntryKey, 0, -1);
        Long mutantCount = new RedisAtomicLong(mutantCountKey, dnaResultTemplate.getConnectionFactory()).get();
        Long humanCount = new RedisAtomicLong(humanCountKey, dnaResultTemplate.getConnectionFactory()).get();
        assertThat(mutantsDnaResults).containsExactlyInAnyOrder(dnaResult, otherDnaResult);
        assertThat(humansDnaResults).isEmpty();
        assertThat(mutantCount).isEqualTo(2);
        assertThat(humanCount).isEqualTo(0);
    }

    @Test
    void saveAndLog_twoTimesSameMutantResult_persistInRedisSetAndHyperLogLogOnlyOneTime() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCA", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.saveAndLog(dnaResult);
        dnaResultRepository.saveAndLog(dnaResult);
        List<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForList().range(mutantEntryKey, 0, -1);
        List<DnaResult> humansDnaResults = dnaResultTemplate.opsForList().range(humanEntryKey, 0, -1);
        Long mutantCount = new RedisAtomicLong(mutantCountKey, dnaResultTemplate.getConnectionFactory()).get();
        Long humanCount = new RedisAtomicLong(humanCountKey, dnaResultTemplate.getConnectionFactory()).get();
        assertThat(mutantsDnaResults).containsExactly(dnaResult);
        assertThat(humansDnaResults).isEmpty();
        assertThat(mutantCount).isEqualTo(1);
        assertThat(humanCount).isEqualTo(0);
    }

    @Test
    void saveAndLog_humanDna_persistInRedisSetAndHyperLogLog() {
        var dnaResult = new DnaResult(new String[]{
                "AAGA", "CTCA", "CTCT", "CTCT"
        }, DnaResultType.HUMAN);
        dnaResultRepository.saveAndLog(dnaResult);
        dnaResultRepository.saveAndLog(dnaResult);
        List<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForList().range(mutantEntryKey, 0, -1);
        List<DnaResult> humansDnaResults = dnaResultTemplate.opsForList().range(humanEntryKey, 0, -1);
        Long mutantCount = new RedisAtomicLong(mutantCountKey, dnaResultTemplate.getConnectionFactory()).get();
        Long humanCount = new RedisAtomicLong(humanCountKey, dnaResultTemplate.getConnectionFactory()).get();
        assertThat(mutantsDnaResults).isEmpty();
        assertThat(humansDnaResults).containsExactly(dnaResult);
        assertThat(mutantCount).isZero();
        assertThat(humanCount).isEqualTo(1);
    }

    @Test
    void count_withoutInsertions_mustReturnZero() {
        Long count = dnaResultRepository.count(DnaResultType.MUTANT);
        assertThat(count).isZero();
    }

    @Test
    void count_withMultipleInsertions_mustReturnSameNumber() {
        RedisAtomicLong rLong = new RedisAtomicLong(mutantCountKey, dnaResultTemplate.getConnectionFactory());
        int value = 1000000;
        rLong.set(value);
        assertThat(dnaResultRepository.count(DnaResultType.MUTANT)).isEqualTo(value);
    }

}
