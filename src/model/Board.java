package model;

import java.io.Serializable;

/**
 * 8x8 chess board. Holds pieces and provides queries.
 * Indices are 0-based: row 0 is black's back rank, row 7 is white's back rank (white at bottom when displayed).
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SIZE = 8;
    private final Piece[][] grid;

    public Board() {
        this.grid = new Piece[SIZE][SIZE];
    }

    public Piece getPiece(Position pos) {
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setPiece(Position pos, Piece piece) {
        grid[pos.getRow()][pos.getCol()] = piece;
    }

    public void clear(Position pos) {
        grid[pos.getRow()][pos.getCol()] = null;
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public boolean isWithinBounds(Position pos) {
        return isWithinBounds(pos.getRow(), pos.getCol());
    }

    /**
     * Returns a deep copy of this board (same piece references; used for move simulation).
     */
    public Board copy() {
        Board copy = new Board();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null) {
                    copy.setPiece(new Position(r, c), p);
                }
            }
        }
        return copy;
    }

    /**
     * Returns the position of the king of the given color, or null if not found.
     */
    public Position findKing(Color color) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Position pos = new Position(r, c);
                Piece p = getPiece(pos);
                if (p != null && p instanceof King && p.getColor() == color) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Returns true if the path from (fromRow, fromCol) to (toRow, toCol) is clear.
     * Assumes the path is horizontal, vertical, or diagonal (one step per row or col).
     * Excludes the start and end squares.
     */
    public boolean isPathClear(Position from, Position to) {
        int dr = Integer.signum(to.getRow() - from.getRow());
        int dc = Integer.signum(to.getCol() - from.getCol());
        int r = from.getRow() + dr;
        int c = from.getCol() + dc;
        while (r != to.getRow() || c != to.getCol()) {
            if (getPiece(new Position(r, c)) != null) {
                return false;
            }
            r += dr;
            c += dc;
        }
        return true;
    }

    /**
     * Sets up the initial standard chess position.
     */
    public void setupInitialPosition() {
        // Clear first
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null;
            }
        }
        // Black back rank
        setPiece(new Position(0, 0), new Rook(Color.BLACK));
        setPiece(new Position(0, 1), new Knight(Color.BLACK));
        setPiece(new Position(0, 2), new Bishop(Color.BLACK));
        setPiece(new Position(0, 3), new Queen(Color.BLACK));
        setPiece(new Position(0, 4), new King(Color.BLACK));
        setPiece(new Position(0, 5), new Bishop(Color.BLACK));
        setPiece(new Position(0, 6), new Knight(Color.BLACK));
        setPiece(new Position(0, 7), new Rook(Color.BLACK));
        for (int c = 0; c < SIZE; c++) {
            setPiece(new Position(1, c), new Pawn(Color.BLACK));
        }
        // White back rank
        setPiece(new Position(7, 0), new Rook(Color.WHITE));
        setPiece(new Position(7, 1), new Knight(Color.WHITE));
        setPiece(new Position(7, 2), new Bishop(Color.WHITE));
        setPiece(new Position(7, 3), new Queen(Color.WHITE));
        setPiece(new Position(7, 4), new King(Color.WHITE));
        setPiece(new Position(7, 5), new Bishop(Color.WHITE));
        setPiece(new Position(7, 6), new Knight(Color.WHITE));
        setPiece(new Position(7, 7), new Rook(Color.WHITE));
        for (int c = 0; c < SIZE; c++) {
            setPiece(new Position(6, c), new Pawn(Color.WHITE));
        }
    }
}
