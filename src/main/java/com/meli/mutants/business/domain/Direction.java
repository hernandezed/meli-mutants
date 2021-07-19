package com.meli.mutants.business.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Direction {
    A0(1, 0),
    A45(1, -1),
    A90(0, -1),
    A135(-1, -1),
    A180(-1, 0),
    A225(-1, 1),
    A270(0, 1),
    A315(1, 1);

    private final int x;
    private final int y;
    private static final Map<Direction, Direction> equivalence = new HashMap<>();

    static {
        equivalence.put(Direction.A0, Direction.A180);
        equivalence.put(Direction.A45, Direction.A225);
        equivalence.put(Direction.A90, Direction.A270);
        equivalence.put(Direction.A135, Direction.A315);
        equivalence.put(Direction.A180, Direction.A0);
        equivalence.put(Direction.A225, Direction.A45);
        equivalence.put(Direction.A270, Direction.A90);
        equivalence.put(Direction.A315, Direction.A135);
    }

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Direction getEquivalent(Direction direction) {
        return equivalence.get(direction);
    }

    public static List<Direction> valuesAt(int x, int y, int maxX, int maxY, int searchedLength) {
        boolean canLeft = x >= searchedLength;
        boolean canRight = (maxX - x) >= searchedLength;
        boolean canUp = y >= searchedLength;
        boolean canDown = (maxY - y) >= searchedLength;

        boolean canLeftToRightFromUp = (canDown && canRight);
        boolean canRightToLeftFromDown = (canUp && canLeft);
        boolean canLeftToRightFromDown = (canUp && canRight);
        boolean canRightToLeftFromUp = (canDown && canLeft);

        List<Direction> angles = new ArrayList<>();

        if (canLeft) {
            angles.add(A180);
        }
        if (canRight) {
            angles.add(A0);
        }
        if (canUp) {
            angles.add(A90);
        }
        if (canDown) {
            angles.add(A270);
        }
        if (canLeftToRightFromUp || canLeftToRightFromDown) {
            angles.add(A315);
            angles.add(A135);
        }
        if (canRightToLeftFromDown || canRightToLeftFromUp) {
            angles.add(A45);
            angles.add(A225);
        }
        return angles;
    }
}
