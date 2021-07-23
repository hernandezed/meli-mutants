package com.meli.mutants.unit.business.usecases;

import com.meli.mutants.business.domain.DnaSampleBO;
import com.meli.mutants.business.domain.SequencerBO;
import com.meli.mutants.business.usecases.EvaluateDnaUseCase;
import com.meli.mutants.business.usecases.impl.EvaluateDnaUseCaseImpl;
import com.meli.mutants.data_access.repositories.dna_result.DnaResultRepository;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

class EvaluateDnaUseCaseTest {

    DnaResultRepository dnaResultRepository = mock(DnaResultRepository.class);
    EvaluateDnaUseCase evaluateDnaUseCase = new EvaluateDnaUseCaseImpl(dnaResultRepository);

    @Test
    void execute_withHumanDna_mustPersistWithTypeHuman() {
        String[] dnaSequence = {"AAAA", "ATCT", "CTGT", "GTGT"};
        var sample = new DnaSampleBO(dnaSequence);
        try (MockedConstruction<SequencerBO> mockedConstruction = Mockito.mockConstruction(SequencerBO.class, (mock, context) -> {
            when(mock.isMutant(sample)).thenReturn(false);
        })) {
            DnaResult result = new DnaResult(dnaSequence, DnaResultType.HUMAN);
            doNothing().when(dnaResultRepository).save(result);
            assertThat(evaluateDnaUseCase.execute(sample)).isFalse();
            verify(dnaResultRepository, times(1)).save(result);
        }
    }

    @Test
    void execute_withHumanDnaAlreadyAnalyzed_dontPersistWithTypeHuman() {
        String[] dnaSequence = {"AAAA", "ATCT", "CTGT", "GTGT"};
        var sample = new DnaSampleBO(dnaSequence);
        try (MockedConstruction<SequencerBO> mockedConstruction = Mockito.mockConstruction(SequencerBO.class, (mock, context) -> {
            when(mock.isMutant(sample)).thenReturn(false);
        })) {
            DnaResult result = new DnaResult(dnaSequence, DnaResultType.HUMAN);
            doNothing().when(dnaResultRepository).save(result);
            when(dnaResultRepository.exists(dnaSequence, DnaResultType.HUMAN)).thenReturn(true);
            when(dnaResultRepository.exists(dnaSequence, DnaResultType.MUTANT)).thenReturn(false);
            assertThat(evaluateDnaUseCase.execute(sample)).isFalse();
            verify(dnaResultRepository, never()).save(result);
        }
    }

    @Test
    void execute_withMutantDnaAlreadyAnalyzed_dontPersistWithTypeHuman() {
        String[] dnaSequence = {"AAAA", "ATCT", "ATGT", "ATGT"};
        var sample = new DnaSampleBO(dnaSequence);
        try (MockedConstruction<SequencerBO> mockedConstruction = Mockito.mockConstruction(SequencerBO.class, (mock, context) -> {
            when(mock.isMutant(sample)).thenReturn(true);
        })) {
            DnaResult result = new DnaResult(dnaSequence, DnaResultType.MUTANT);
            doNothing().when(dnaResultRepository).save(result);
            when(dnaResultRepository.exists(dnaSequence, DnaResultType.HUMAN)).thenReturn(false);
            when(dnaResultRepository.exists(dnaSequence, DnaResultType.MUTANT)).thenReturn(true);
            assertThat(evaluateDnaUseCase.execute(sample)).isTrue();
            verify(dnaResultRepository, never()).save(result);
        }
    }

    @Test
    void execute_withMutantDna_mustPersistWithTypeMutant() {
        String[] dnaSequence = {"AAAA", "ATCT", "ATGT", "GTGT"};
        var sample = new DnaSampleBO(dnaSequence);
        try (MockedConstruction<SequencerBO> mockedConstruction = Mockito.mockConstruction(SequencerBO.class, (mock, context) -> {
            when(mock.isMutant(sample)).thenReturn(true);
        })) {
            DnaResult result = new DnaResult(dnaSequence, DnaResultType.MUTANT);
            doNothing().when(dnaResultRepository).save(result);
            assertThat(evaluateDnaUseCase.execute(sample)).isTrue();
            verify(dnaResultRepository, times(1)).save(result);
        }
    }

    @Test
    void execute_whenCantPersist_returnResult() {
        String[] dnaSequence = {"AAAA", "ATCT", "ATGT", "GTGT"};
        var sample = new DnaSampleBO(dnaSequence);
        try (MockedConstruction<SequencerBO> mockedConstruction = Mockito.mockConstruction(SequencerBO.class, (mock, context) -> {
            when(mock.isMutant(sample)).thenReturn(true);
        })) {
            DnaResult result = new DnaResult(dnaSequence, DnaResultType.MUTANT);
            doThrow(mock(DataAccessException.class)).when(dnaResultRepository).save(result);
            assertThat(evaluateDnaUseCase.execute(sample)).isTrue();
            verify(dnaResultRepository, times(1)).save(result);
        }
    }
}
