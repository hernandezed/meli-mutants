package com.meli.mutants.business.domain;

import java.util.Iterator;

public class Sequencer {

    public boolean isMutant(SampleBO dna) {
        int count = 0;
        for (int i = 0; i < dna.getRowsCount() && count < 2; i++) {
            for (int j = 0; j < dna.getColumnsCount() && count < 2; j++) {
                Iterator<Direction> directions = Direction.valuesAt(i, j, dna.getRowsCount(), dna.getColumnsCount(), 4).iterator();
                while (directions.hasNext() && count < 2) {
                    Direction direction = directions.next();
                    if (dna.canVisit(i, j, direction)) {
                        if (search(dna, 'A', direction, i, j, 0)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count >= 2;
    }

    private boolean isClamped(int i, int min, int max) {
        return i >= min && i < max;
    }

    private boolean search(SampleBO dna, char character, Direction direction, int row, int col, int count) {
        if (count == 4) {
            return true;
        }
        if (isClamped(row, 0, dna.getRowsCount()) && isClamped(col, 0, dna.getColumnsCount())) {
            char value = dna.visit(row, col, direction);
            if (value == character) {
                count++;
            } else {
                count = 1;
                character = value;
            }
            return search(dna, character, direction, row + direction.getY(), col + direction.getX(), count);
        }
        return false;
    }
}
