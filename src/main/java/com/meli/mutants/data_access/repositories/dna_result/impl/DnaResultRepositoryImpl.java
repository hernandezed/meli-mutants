package com.meli.mutants.data_access.repositories.dna_result.impl;


import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import com.meli.mutants.data_access.repositories.dna_result.settings.DnaResultPrefixSettings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DnaResultRepositoryImpl implements DnaResultRepository {

    private final RedisTemplate<String, DnaResult> dnaResultTemplate;
    private final DnaResultPrefixSettings dnaResultPrefixSettings;

    public DnaResultRepositoryImpl(RedisTemplate<String, DnaResult> dnaResultTemplate, DnaResultPrefixSettings dnaResultPrefixSettings) {
        this.dnaResultTemplate = dnaResultTemplate;
        this.dnaResultPrefixSettings = dnaResultPrefixSettings;
    }

    @Override
    public void saveAndLog(DnaResult dnaResult) {
        String sufix = dnaResult.getResult().name().toLowerCase();
        dnaResultTemplate.opsForSet().add(dnaResultPrefixSettings.getEntryKey(sufix), dnaResult);
    }

    @Override
    public Long count(DnaResultType dnaResultType) {
        return dnaResultTemplate.opsForSet().size(dnaResultPrefixSettings.getEntryKey(dnaResultType.name().toLowerCase()));
    }
}
