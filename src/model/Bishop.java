package model;

/**
 * Bishop: moves any number of squares diagonally.
 */
public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? '\u2657' : '\u265D'; // ♗ ♝
    }

    @Override
    public boolean isLegalMove(Board board, Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();
        if (Math.abs(dr) != Math.abs(dc)) return false;
        if (dr == 0 && dc == 0) return false;
        if (!board.isPathClear(from, to)) return false;
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }
}
