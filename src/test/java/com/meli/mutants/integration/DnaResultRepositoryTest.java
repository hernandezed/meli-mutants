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
    private String mutantHllKey;
    private String humanHllKey;

    @BeforeEach
    void setup() {
        mutantEntryKey = settings.getEntryKey("mutant");
        humanEntryKey = settings.getEntryKey("human");
        mutantHllKey = settings.getHllKey("mutant");
        humanHllKey = settings.getHllKey("human");
        dnaResultTemplate.delete(mutantEntryKey);
        dnaResultTemplate.delete(humanEntryKey);
        dnaResultTemplate.delete(mutantHllKey);
        dnaResultTemplate.delete(humanHllKey);
    }

    @Test
    void saveAndLog_withMutantResult_persistInRedisSetAndHyperLogLog() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCT", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.saveAndLog(dnaResult);
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        Long mutantCount = dnaResultTemplate.opsForHyperLogLog().size(mutantHllKey);
        Long humanCount = dnaResultTemplate.opsForHyperLogLog().size(humanHllKey);
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
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        Long mutantCount = dnaResultTemplate.opsForHyperLogLog().size(mutantHllKey);
        Long humanCount = dnaResultTemplate.opsForHyperLogLog().size(humanHllKey);
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
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        Long mutantCount = dnaResultTemplate.opsForHyperLogLog().size(mutantHllKey);
        Long humanCount = dnaResultTemplate.opsForHyperLogLog().size(humanHllKey);
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
        Set<DnaResult> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<DnaResult> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        Long mutantCount = dnaResultTemplate.opsForHyperLogLog().size(mutantHllKey);
        Long humanCount = dnaResultTemplate.opsForHyperLogLog().size(humanHllKey);
        assertThat(mutantsDnaResults).isEmpty();
        assertThat(humansDnaResults).containsExactly(dnaResult);
        assertThat(mutantCount).isEqualTo(0);
        assertThat(humanCount).isEqualTo(1);
    }


}
