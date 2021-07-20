package com.meli.mutants.business.usecases.impl;

import com.meli.mutants.business.domain.DnaSampleBO;
import com.meli.mutants.business.domain.SequencerBO;
import com.meli.mutants.business.usecases.EvaluateDnaUseCase;
import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;


@RequiredArgsConstructor
public class EvaluateDnaUseCaseImpl implements EvaluateDnaUseCase {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DnaResultRepository dnaResultRepository;

    @Override
    public boolean execute(DnaSampleBO dnaSampleBO) {
        SequencerBO sequencerBO = new SequencerBO();
        boolean result = sequencerBO.isMutant(dnaSampleBO);
        try {
            dnaResultRepository.saveAndLog(dnaSampleBO.toDnaResult(result));
        } catch (DataAccessException e) {
            logger.error("An error occurred when trying to save registry", e);
        }
        return result;
    }
}
