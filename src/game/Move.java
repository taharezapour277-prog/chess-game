package game;

import model.Position;

import java.io.Serializable;

/**
 * Records a move for history and en passant detection.
 */
public class Move implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Position from;
    private final Position to;
    private final boolean pawnTwoSquares;

    public Move(Position from, Position to, boolean pawnTwoSquares) {
        this.from = from;
        this.to = to;
        this.pawnTwoSquares = pawnTwoSquares;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public boolean isPawnTwoSquares() {
        return pawnTwoSquares;
    }
}
