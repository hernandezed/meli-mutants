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
    RedisTemplate<String, String[]> dnaResultTemplate;

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
    }

    @Test
    void save_withMutantResult_persistInRedisSet() {
        String[] dna = {
                "AAAA", "CTCT", "CTCT", "CTCT"
        };
        var dnaResult = new DnaResult(dna, DnaResultType.MUTANT);
        dnaResultRepository.save(dnaResult);
        Set<String[]> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<String[]> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        assertThat(mutantsDnaResults).containsExactly(dna);
        assertThat(humansDnaResults).isEmpty();
    }

    @Test
    void save_withDifferentMutantResult_persistInRedisSet() {
        String[] ciclopsDna = {
                "AAAA", "CTCT", "CTCT", "CTCT"
        };
        String[] wolverineDna = {
                "AAAA", "CGCT", "CTCT", "CTCT"
        };
        var dnaResult = new DnaResult(ciclopsDna, DnaResultType.MUTANT);
        var otherDnaResult = new DnaResult(wolverineDna, DnaResultType.MUTANT);
        dnaResultRepository.save(dnaResult);
        dnaResultRepository.save(otherDnaResult);
        Set<String[]> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<String[]> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        assertThat(mutantsDnaResults).containsExactlyInAnyOrder(ciclopsDna, wolverineDna);
        assertThat(humansDnaResults).isEmpty();
    }

    @Test
    void save_twoTimesSameMutantResult_persistInRedisSetOneTime() {
        var dnaResult = new DnaResult(new String[]{
                "AAAA", "CTCA", "CTCT", "CTCT"
        }, DnaResultType.MUTANT);
        dnaResultRepository.save(dnaResult);
        dnaResultRepository.save(dnaResult);
        assertThat(dnaResultTemplate.opsForSet().size(mutantEntryKey)).isEqualTo(1);
        assertThat(dnaResultTemplate.opsForSet().size(humanEntryKey)).isZero();
    }

    @Test
    void save_humanDna_persistInRedisSet() {
        String[] dna = {
                "AAGA", "CTCA", "CTCT", "CTCT"
        };
        var dnaResult = new DnaResult(dna, DnaResultType.HUMAN);
        dnaResultRepository.save(dnaResult);
        dnaResultRepository.save(dnaResult);
        Set<String[]> mutantsDnaResults = dnaResultTemplate.opsForSet().members(mutantEntryKey);
        Set<String[]> humansDnaResults = dnaResultTemplate.opsForSet().members(humanEntryKey);
        assertThat(mutantsDnaResults).isEmpty();
        assertThat(humansDnaResults).containsExactly(dna);
    }

    @Test
    void count_withoutInsertions_mustReturnZero() {
        Long count = dnaResultRepository.count(DnaResultType.MUTANT);
        assertThat(count).isZero();
    }

    @Test
    void count_withMultipleInsertions_mustReturnSameNumber() {
        dnaResultTemplate.opsForSet().add(settings.getEntryKey(DnaResultType.MUTANT.name().toLowerCase()),
                new String[]{});
        assertThat(dnaResultRepository.count(DnaResultType.MUTANT)).isEqualTo(1);
    }

    @Test
    void exists_withAlreadyInsertedDna_returnTrue() {
        String[] dna = {
                "AAGA", "CTCA", "CTCT", "CTCT"
        };
        dnaResultTemplate.opsForSet().add(settings.getEntryKey(DnaResultType.MUTANT.name().toLowerCase()),
                dna);
        assertThat(dnaResultRepository.exists(dna, DnaResultType.MUTANT)).isTrue();
    }

    @Test
    void exists_withAlreadyInsertedDna_returnFalse() {
        String[] dna = {
                "AAGA", "CTCA", "CTCT", "CTCT"
        };
        assertThat(dnaResultRepository.exists(dna, DnaResultType.MUTANT)).isFalse();
    }

}
