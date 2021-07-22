package com.meli.mutants.unit.business.usecases;

import com.meli.mutants.business.exceptions.CannotRetrieveStatsException;
import com.meli.mutants.business.usecases.GetStatisticsUseCase;
import com.meli.mutants.business.usecases.impl.GetStatisticsUseCaseImpl;
import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataRetrievalFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetStatisticsUseCaseTest {

    private DnaResultRepository dnaResultRepository = mock(DnaResultRepository.class);
    private GetStatisticsUseCase getStatisticsUseCase = new GetStatisticsUseCaseImpl(dnaResultRepository);

    @Test
    void execute_mustCallTwoTimesToDnaResult() {
        when(dnaResultRepository.count(DnaResultType.MUTANT)).thenReturn(40L);
        when(dnaResultRepository.count(DnaResultType.HUMAN)).thenReturn(100L);

        var result = getStatisticsUseCase.execute();

        assertThat(result.getRatio())
                .isEqualTo(0.4);
        verify(dnaResultRepository, times(1)).count(DnaResultType.MUTANT);
        verify(dnaResultRepository, times(1)).count(DnaResultType.HUMAN);
    }

    @Test
    void execute_whenGetDatabaseException_throwBusinessException() {
        DataRetrievalFailureException cause = new DataRetrievalFailureException("Exception");
        when(dnaResultRepository.count(DnaResultType.MUTANT)).thenThrow(cause);
        assertThatThrownBy(() -> getStatisticsUseCase.execute())
                .isInstanceOf(CannotRetrieveStatsException.class)
                .hasCause(cause);
        verify(dnaResultRepository, times(1)).count(DnaResultType.MUTANT);
        verify(dnaResultRepository, never()).count(DnaResultType.HUMAN);
    }
}
