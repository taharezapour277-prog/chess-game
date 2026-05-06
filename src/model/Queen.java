package model;

/**
 * Queen: moves any number of squares along rank, file, or diagonal.
 */
public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? '\u2655' : '\u265B'; // ♕ ♛
    }

    @Override
    public boolean isLegalMove(Board board, Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();
        if (dr == 0 && dc == 0) return false;
        boolean diagonal = Math.abs(dr) == Math.abs(dc);
        boolean straight = (dr == 0) || (dc == 0);
        if (!diagonal && !straight) return false;
        if (!board.isPathClear(from, to)) return false;
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }
}
