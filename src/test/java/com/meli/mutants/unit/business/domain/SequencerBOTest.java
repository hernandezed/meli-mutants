package com.meli.mutants.unit.business.domain;

import com.meli.mutants.business.domain.DnaSampleBO;
import com.meli.mutants.business.domain.SequencerBO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SequencerBOTest {

    SequencerBO sequencer = new SequencerBO();

    @Test
    void sequence_withHorizontalAndVerticalSequenceOfMutantSample_mustReturnTrue() {
        var sample = new DnaSampleBO(new String[]{
                "AAAATA",
                "CCGTGC",
                "TTATGT",
                "AGAAGG",
                "CACCGA",
                "TCACTG"});
        boolean result = sequencer.isMutant(sample);
        assertThat(result).isTrue();
    }

    @Test
    void sequence_withDiagonalSequenceOfMutantSample_mustReturnTrue() {
        var sample = new DnaSampleBO(new String[]{
                "AACATAT",
                "CCGTGCG",
                "TTCTCTC",
                "AGACGGA",
                "CACCCAT",
                "TCACTGC"});
        boolean result = sequencer.isMutant(sample);
        assertThat(result).isTrue();
    }

    @Test
    void sequence_withConsecutiveSequences_mustReturnTrue() {
        var sample = new DnaSampleBO(new String[]{
                "AAAAAAAA",
                "TAGTACAA",
                "ATGTGTAA",
                "TGAATGCC",
                "ACCCGACC",
                "CCACTGCC",
                "ATTTGGGA",
                "AGGTTCCT"});
        boolean result = sequencer.isMutant(sample);
        assertThat(result).isTrue();
    }

    @Test
    void sequence_withNormalHumanSample_mustReturnFalse() {
        var sample = new DnaSampleBO(new String[]{
                "AACAGA", "CCGTGC", "TTATGT", "AGAATG", "CCGCTA", "TCACTG"});
        boolean result = sequencer.isMutant(sample);
        assertThat(result).isFalse();
    }
}
