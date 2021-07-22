package com.meli.mutants.business.domain;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DnaSampleBO {
    private final String[] dna;
    private final HashMap<PairBO, Set<Direction>> visited;

    public DnaSampleBO(String[] dna) {
        this.dna = dna;
        visited = new HashMap<>();
    }

    public boolean canVisit(int row, int col, Direction direction) {
        Set<Direction> directionsVisited = visited.get(new PairBO(row, col));
        boolean isNull = directionsVisited == null;
        if (!isNull) {
            isNull = !(directionsVisited.contains(direction) || directionsVisited.contains(Direction.getEquivalent(direction)));
        }

        return isNull;
    }

    public char visit(int row, int col, Direction direction) {
        var key = new PairBO(row, col);
        visited.computeIfAbsent(key, k -> new HashSet<>());
        visited.get(key).add(direction);
        return dna[row].charAt(col);
    }

    public int getRowsCount() {
        return dna.length;
    }

    public int getColumnsCount() {
        return dna[0].length();
    }

    public DnaResult toDnaResult(boolean result) {
        return new DnaResult(dna, DnaResultType.valueOf(result));
    }

}
