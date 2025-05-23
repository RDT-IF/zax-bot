package org.rdtif.zaxbot.userinterface;

import java.awt.Point;
import java.util.Objects;

public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    Point toPoint() {
        return new Point(row, column);
    }

    Position translateBy(Extent extent) {
        return new Position(row + extent.getRows(), column + extent.getColumns());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position position = (Position) other;
            return position.row == row && position.column == column;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
