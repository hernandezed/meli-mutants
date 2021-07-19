package com.meli.mutants.business.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SampleBO {
    private final String[] adn;
    private final HashMap<PairBO, Set<Direction>> visited;

    public SampleBO(String[] adn) {
        this.adn = adn;
        visited = new HashMap<>();
    }

    public boolean canVisit(int row, int col, Direction direction) {
        Set<Direction> directionsVisited = visited.get(new PairBO(row, col));
        boolean isNull = directionsVisited == null;
        if (!isNull) {
            isNull = !(directionsVisited.contains(direction) | directionsVisited.contains(Direction.getEquivalent(direction)));
        }

        return isNull;
    }

    public char visit(int row, int col, Direction direction) {
        var key = new PairBO(row, col);
        visited.computeIfAbsent(key, k -> new HashSet<>());
        visited.get(key).add(direction);
        return adn[row].charAt(col);
    }

    public int getRowsCount() {
        return adn.length;
    }

    public int getColumnsCount() {
        return adn[0].length();
    }

}
