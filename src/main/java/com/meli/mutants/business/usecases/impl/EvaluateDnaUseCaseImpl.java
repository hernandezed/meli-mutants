package com.meli.mutants.business.usecases.impl;

import com.meli.mutants.business.domain.DnaSampleBO;
import com.meli.mutants.business.domain.SequencerBO;
import com.meli.mutants.business.usecases.EvaluateDnaUseCase;
import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluateDnaUseCaseImpl implements EvaluateDnaUseCase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DnaResultRepository dnaResultRepository;

    @Override
    @CacheEvict(cacheNames = "result-stats", allEntries = true)
    public boolean execute(DnaSampleBO dnaSampleBO) {
        if (Boolean.TRUE.equals(dnaResultRepository.exists(dnaSampleBO.getDna(), DnaResultType.MUTANT))) {
            logger.debug("Returning MUTANT result from cache");
            return true;
        } else if (Boolean.TRUE.equals(dnaResultRepository.exists(dnaSampleBO.getDna(), DnaResultType.HUMAN))) {
            logger.debug("Returning HUMAN result from cache");
            return false;
        }
        var sequencerBO = new SequencerBO();
        boolean result = sequencerBO.isMutant(dnaSampleBO);
        try {
            dnaResultRepository.save(dnaSampleBO.toDnaResult(result));
        } catch (DataAccessException e) {
            logger.error("An error occurred when trying to save registry", e);
        }
        return result;
    }
}
