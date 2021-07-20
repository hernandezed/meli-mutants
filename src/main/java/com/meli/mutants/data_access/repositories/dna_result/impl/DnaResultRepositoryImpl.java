package com.meli.mutants.data_access.repositories.dna_result.impl;


import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import com.meli.mutants.data_access.repositories.dna_result.settings.DnaResultPrefixSettings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class DnaResultRepositoryImpl implements DnaResultRepository {

    private final RedisTemplate<String, DnaResult> dnaResultTemplate;
    private final DnaResultPrefixSettings dnaResultPrefixSettings;

    private final HashMap<DnaResultType, RedisAtomicLong> counters;

    public DnaResultRepositoryImpl(RedisTemplate<String, DnaResult> dnaResultTemplate, DnaResultPrefixSettings dnaResultPrefixSettings) {
        this.dnaResultTemplate = dnaResultTemplate;
        this.dnaResultPrefixSettings = dnaResultPrefixSettings;
        counters = new HashMap<>();
        for (DnaResultType resultType : DnaResultType.values()) {
            counters.put(resultType, new RedisAtomicLong(dnaResultPrefixSettings.dnaCounterKey(resultType.name().toLowerCase()),
                    dnaResultTemplate.getConnectionFactory()));
        }
    }

    @Override
    public void saveAndLog(DnaResult dnaResult) {
        String sufix = dnaResult.getResult().name().toLowerCase();
        if (dnaResultTemplate.opsForHyperLogLog().add(dnaResultPrefixSettings.getHllKey(sufix), dnaResult) == 1) {
            dnaResultTemplate.opsForList().rightPush(dnaResultPrefixSettings.getEntryKey(sufix), dnaResult);
            counters.get(dnaResult.getResult()).incrementAndGet();
        }
    }

    @Override
    public long count(DnaResultType dnaResultType) {
        return counters.get(dnaResultType).get();
    }
}
