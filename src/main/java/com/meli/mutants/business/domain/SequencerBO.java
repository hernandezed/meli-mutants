package com.meli.mutants.business.domain;

public class SequencerBO {

    public boolean isMutant(DnaSampleBO dna) {
        var count = 0;
        for (var i = 0; i < dna.getRowsCount() && count < 2; i++) {
            for (var j = 0; j < dna.getColumnsCount() && count < 2; j++) {
                var directions = Direction.valuesAt(i, j, dna.getRowsCount(), dna.getColumnsCount()).iterator();
                while (directions.hasNext() && count < 2) {
                    var direction = directions.next();
                    if (dna.canVisit(i, j, direction) && search(dna, 'A', direction, i, j, 0)) {
                        count++;
                    }
                }
            }
        }


        return count >= 2;
    }

    private boolean isClamped(int i, int min, int max) {
        return i >= min && i < max;
    }

    private boolean search(DnaSampleBO dna, char character, Direction direction, int row, int col, int count) {
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
