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

import java.util.List;
import java.util.Set;

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

    @BeforeEach
    void setup() {
        mutantEntryKey = settings.getEntryKey("mutant");
        humanEntryKey = settings.getEntryKey("human");
        dnaResultTemplate.delete(mutantEntryKey);
        dnaResultTemplate.delete(humanEntryKey);
        dnaResultTemplate.delete(settings.getHllKey("mutant"));
        dnaResultTemplate.delete(settings.getHllKey("human"));
    }

    @Test
    void saveAndLog_withMutantResult_persistInRedisSetAndHyperLogLog() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCT", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.saveAndLog(dnaResult);
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        assertThat(mutantsDnaResults).containsExactly(dnaResult);
        assertThat(humansDnaResults).isEmpty();
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
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        assertThat(mutantsDnaResults).containsExactlyInAnyOrder(dnaResult, otherDnaResult);
        assertThat(humansDnaResults).isEmpty();
    }

    @Test
    void saveAndLog_twoTimesSameMutantResult_persistInRedisSetAndHyperLogLogOnlyOneTime() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCA", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.saveAndLog(dnaResult);
        dnaResultRepository.saveAndLog(dnaResult);
        assertThat(dnaResultTemplate.opsForSet().size(mutantEntryKey)).isEqualTo(1);
        assertThat(dnaResultTemplate.opsForSet().size(humanEntryKey)).isZero();
    }

    @Test
    void saveAndLog_humanDna_persistInRedisSetAndHyperLogLog() {
        var dnaResult = new DnaResult(new String[]{
                "AAGA", "CTCA", "CTCT", "CTCT"
        }, DnaResultType.HUMAN);
        dnaResultRepository.saveAndLog(dnaResult);
        dnaResultRepository.saveAndLog(dnaResult);
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        assertThat(mutantsDnaResults).isEmpty();
        assertThat(humansDnaResults).containsExactly(dnaResult);
    }

    @Test
    void count_withoutInsertions_mustReturnZero() {
        Long count = dnaResultRepository.count(DnaResultType.MUTANT);
        assertThat(count).isZero();
    }

    @Test
    void count_withMultipleInsertions_mustReturnSameNumber() {
        dnaResultTemplate.opsForSet().add(settings.getEntryKey(DnaResultType.MUTANT.name().toLowerCase()),
                new DnaResult(null, DnaResultType.MUTANT));
        assertThat(dnaResultRepository.count(DnaResultType.MUTANT)).isEqualTo(1);
    }

}
