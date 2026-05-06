package model;

/**
 * Pawn: moves one square forward (or two on first move); captures diagonally.
 * No en passant in Phase 1.
 */
public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? '\u2659' : '\u265F'; // ♙ ♟
    }

    private int forwardDir() {
        return getColor() == Color.WHITE ? -1 : 1;
    }

    private int startRow() {
        return getColor() == Color.WHITE ? 6 : 1;
    }

    @Override
    public boolean isLegalMove(Board board, Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();
        Piece target = board.getPiece(to);

        if (dc == 0) {
            // Forward move
            if (dr != forwardDir() && !(from.getRow() == startRow() && dr == 2 * forwardDir())) {
                return false;
            }
            if (from.getRow() == startRow() && dr == 2 * forwardDir()) {
                Position mid = new Position(from.getRow() + forwardDir(), from.getCol());
                if (board.getPiece(mid) != null || target != null) return false;
                return true;
            }
            return target == null;
        }

        if (Math.abs(dc) == 1 && dr == forwardDir()) {
            return target != null && target.getColor() != getColor();
        }
        return false;
    }
}
