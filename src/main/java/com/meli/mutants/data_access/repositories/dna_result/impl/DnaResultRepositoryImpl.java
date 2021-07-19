package com.meli.mutants.data_access.repositories.dna_result.impl;


import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.settings.DnaResultPrefixSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DnaResultRepositoryImpl implements DnaResultRepository {

    private final RedisTemplate<String, DnaResult> template;
    private final DnaResultPrefixSettings dnaResultPrefixSettings;

    @Override
    public void saveAndLog(DnaResult dnaResult) {
        if (template.opsForSet().add(dnaResultPrefixSettings.getEntryKey(dnaResult.getResult().name().toLowerCase()), dnaResult) >= 1L) {
            template.opsForHyperLogLog().add(dnaResultPrefixSettings.getHllKey(dnaResult.getResult().name().toLowerCase()), dnaResult);
        }
    }
}
