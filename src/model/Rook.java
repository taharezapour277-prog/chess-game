package model;

/**
 * Rook: moves any number of squares along rank or file.
 */
public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? '\u2656' : '\u265C'; // ♖ ♜
    }

    @Override
    public boolean isLegalMove(Board board, Position from, Position to) {
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();
        if (dr != 0 && dc != 0) return false;
        if (dr == 0 && dc == 0) return false;
        if (!board.isPathClear(from, to)) return false;
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }
}
