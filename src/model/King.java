package model;

/**
 * King: moves one square in any direction.
 */
public class King extends Piece {
    public King(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? '\u2654' : '\u265A'; // ♔ ♚
    }

    @Override
    public boolean isLegalMove(Board board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        if (dr > 1 || dc > 1) return false;
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }
}
