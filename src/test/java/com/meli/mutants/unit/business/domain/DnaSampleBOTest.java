package com.meli.mutants.unit.business.domain;

import com.meli.mutants.business.domain.Direction;
import com.meli.mutants.business.domain.PairBO;
import com.meli.mutants.business.domain.DnaSampleBO;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DnaSampleBOTest {

    @Test
    void canVisit_whenTheCellItsNeverVisited_thenReturnTrue() {
        var sample = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});

        boolean canVisit = sample.canVisit(0, 0, Direction.A0);
        assertThat(canVisit).isTrue();
    }

    @Test
    void canVisit_whenTheCellIsAlreadyVisitedInSameDirection_thenReturnFalse() {
        int row = 0;
        int col = 0;
        Direction direction = Direction.A0;
        HashMap<PairBO, Set<Direction>> mockVisited = new HashMap<>();
        mockVisited.put(new PairBO(row, col), Sets.newLinkedHashSet(direction));
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});
        ReflectionTestUtils.setField(sampleBO, "visited", mockVisited);
        var result = sampleBO.canVisit(row, col, direction);
        assertThat(result).isFalse();
    }

    @Test
    void canVisit_whenTheCellIsAlreadyVisitedInEquivalentDirection_thenReturnFalse() {
        int row = 0;
        int col = 0;
        Direction direction = Direction.A180;
        HashMap<PairBO, Set<Direction>> mockVisited = new HashMap<>();
        mockVisited.put(new PairBO(row, col), Sets.newLinkedHashSet(direction));
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});
        ReflectionTestUtils.setField(sampleBO, "visited", mockVisited);
        var result = sampleBO.canVisit(row, col, Direction.A0);
        assertThat(result).isFalse();
    }

    @Test
    void canVisit_whenTheCellIsAlreadyVisitedInDifferentDirection_thenReturnTrue() {
        int row = 0;
        int col = 0;
        Direction direction = Direction.A45;
        HashMap<PairBO, Set<Direction>> mockVisited = new HashMap<>();
        mockVisited.put(new PairBO(row, col), Sets.newLinkedHashSet(direction));
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});
        ReflectionTestUtils.setField(sampleBO, "visited", mockVisited);
        var result = sampleBO.canVisit(row, col, Direction.A0);
        assertThat(result).isTrue();
    }

    @Test
    void visit_whenNeverVisitThePosition_visitAndAddDirection() {
        HashMap<PairBO, Set<Direction>> mockVisited = new HashMap<>();
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});
        ReflectionTestUtils.setField(sampleBO, "visited", mockVisited);
        sampleBO.visit(0, 0, Direction.A0);

        Assertions.assertThat(mockVisited).containsEntry(new PairBO(0, 0), Sets.newLinkedHashSet(Direction.A0));
    }

    @Test
    void visit_whenBeforeVisitThePositionFromOtherDirection_visitAndAddDirection() {
        HashMap<PairBO, Set<Direction>> mockVisited = new HashMap<>();
        mockVisited.put(new PairBO(0, 0), Sets.newLinkedHashSet(Direction.A45));
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});
        ReflectionTestUtils.setField(sampleBO, "visited", mockVisited);
        sampleBO.visit(0, 0, Direction.A0);

        Assertions.assertThat(mockVisited).containsEntry(new PairBO(0, 0), Sets.newLinkedHashSet(Direction.A45, Direction.A0));
    }

    @Test
    void visit_whenBeforeVisitThePositionFromSameDirection_dontAddDirection() {
        HashMap<PairBO, Set<Direction>> mockVisited = new HashMap<>();
        mockVisited.put(new PairBO(0, 0), Sets.newLinkedHashSet(Direction.A0));
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});
        ReflectionTestUtils.setField(sampleBO, "visited", mockVisited);
        sampleBO.visit(0, 0, Direction.A0);

        Assertions.assertThat(mockVisited).containsEntry(new PairBO(0, 0), Sets.newLinkedHashSet(Direction.A0));
    }

    @Test
    void testGetRowsCountAndGetColumnsCount() {
        var sampleBO = new DnaSampleBO(new String[]{
                "AAAATA", "CCGTGC", "TTATGT", "AGAAGG", "CCGCTA", "TCACTG"});

        assertThat(sampleBO.getRowsCount())
                .isEqualTo(6);
        assertThat(sampleBO.getColumnsCount())
                .isEqualTo(6);
    }
}
