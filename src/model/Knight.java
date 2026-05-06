package model;

/**
 * Knight: moves in L-shape (2+1 or 1+2 squares). Jumps over pieces.
 */
public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? '\u2658' : '\u265E'; // ♘ ♞
    }

    @Override
    public boolean isLegalMove(Board board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        if (!((dr == 2 && dc == 1) || (dr == 1 && dc == 2))) return false;
        Piece target = board.getPiece(to);
        return target == null || target.getColor() != getColor();
    }
}
