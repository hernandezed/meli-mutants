package com.meli.mutants.data_access.repositories.dna_result.impl;


import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import com.meli.mutants.data_access.repositories.dna_result.settings.DnaResultPrefixSettings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DnaResultRepositoryImpl implements DnaResultRepository {

    private final RedisTemplate<String, String[]> dnaResultTemplate;
    private final DnaResultPrefixSettings dnaResultPrefixSettings;

    public DnaResultRepositoryImpl(RedisTemplate<String, String[]> dnaResultTemplate, DnaResultPrefixSettings dnaResultPrefixSettings) {
        this.dnaResultTemplate = dnaResultTemplate;
        this.dnaResultPrefixSettings = dnaResultPrefixSettings;
    }

    @Override
    public void save(DnaResult dnaResult) {
        dnaResultTemplate.opsForSet().add(dnaResultPrefixSettings.getEntryKey(dnaResult.getResult().name().toLowerCase()), dnaResult.getDna());
    }

    @Override
    public Long count(DnaResultType dnaResultType) {
        return dnaResultTemplate.opsForSet().size(dnaResultPrefixSettings.getEntryKey(dnaResultType.name().toLowerCase()));
    }

    @Override
    public Boolean exists(String[] dna, DnaResultType dnaResultType) {
        return dnaResultTemplate.opsForSet().isMember(dnaResultPrefixSettings.getEntryKey(dnaResultType.name().toLowerCase()), dna);
    }
}
