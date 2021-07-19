package com.meli.mutants.unit.business.domain;

import com.meli.mutants.business.domain.SampleBO;
import com.meli.mutants.business.domain.Sequencer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SequencerTest {

    Sequencer sequencer = new Sequencer();

    @Test
    void sequence_withHorizontalAndVerticalSequenceOfMutantSample_mustReturnTrue() {
        var sample = new SampleBO(new String[]{
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
        var sample = new SampleBO(new String[]{
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
        var sample = new SampleBO(new String[]{
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
        var sample = new SampleBO(new String[]{
                "AACAGA", "CCGTGC", "TTATGT", "AGAATG", "CCGCTA", "TCACTG"});
        boolean result = sequencer.isMutant(sample);
        assertThat(result).isFalse();
    }
}
