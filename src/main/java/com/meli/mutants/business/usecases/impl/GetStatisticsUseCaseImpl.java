package com.meli.mutants.business.usecases.impl;

import com.meli.mutants.business.domain.StatisticsBO;
import com.meli.mutants.business.exceptions.CannotRetrieveStatsException;
import com.meli.mutants.business.usecases.GetStatisticsUseCase;
import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetStatisticsUseCaseImpl implements GetStatisticsUseCase {
    private final DnaResultRepository dnaResultRepository;

    @Override
    @Cacheable("result-stats")
    public StatisticsBO execute() {
        try {
            return new StatisticsBO(dnaResultRepository.count(DnaResultType.MUTANT),
                    dnaResultRepository.count(DnaResultType.HUMAN));
        } catch (DataAccessException ex) {
            throw new CannotRetrieveStatsException(ex);
        }
    }
}
