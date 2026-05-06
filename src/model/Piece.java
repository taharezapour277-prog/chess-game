package model;

import java.io.Serializable;

/**
 * Abstract base class for all chess pieces.
 * Subclasses define movement rules via isLegalMove.
 */
public abstract class Piece implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Color color;
    private boolean hasMoved;

    protected Piece(Color color) {
        this.color = color;
        this.hasMoved = false;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Returns the Unicode symbol for this piece (e.g. ♔ for white king).
     * Used for display.
     */
    public abstract char getSymbol();

    /**
     * Checks if moving from the given position to the target is legal
     * according to this piece's rules, ignoring other pieces.
     * The board is used to check for blocking and captures.
     */
    public abstract boolean isLegalMove(Board board, Position from, Position to);
}
