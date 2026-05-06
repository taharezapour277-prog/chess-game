package game;

import model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls game state: turn, move validation, castling, en passant, promotion, and status.
 */
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ROOK_KING_SIDE_COL = 7;
    private static final int ROOK_QUEEN_SIDE_COL = 0;

    private final Board board;
    private Color currentTurn;
    private GameStatus gameStatus;
    private Move lastMove;

    public Game() {
        this.board = new Board();
        this.currentTurn = Color.WHITE;
        this.gameStatus = GameStatus.ONGOING;
        this.lastMove = null;
        board.setupInitialPosition();
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Returns true if the given color's king is under attack on the current board.
     */
    public boolean isInCheck(Color color) {
        return isInCheckOnBoard(color, board);
    }

    /**
     * Returns true if after making the move (from -> to) the moving side's king would be in check.
     * Handles normal moves, castling, and en passant.
     */
    public boolean wouldBeInCheckAfterMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null) {
            return true;
        }
        Board copy = board.copy();
        if (isCastlingMove(from, to)) {
            applyCastlingToBoard(copy, from, to);
        } else if (isEnPassantCapture(from, to)) {
            applyEnPassantToBoard(copy, from, to);
        } else {
            copy.setPiece(to, piece);
            copy.clear(from);
            maybePromoteOnBoard(copy, to, piece);
        }
        return isInCheckOnBoard(piece.getColor(), copy);
    }

    /**
     * Returns true if the given color has at least one legal move.
     */
    public boolean hasAnyLegalMove(Color color) {
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Position from = new Position(r, c);
                Piece p = board.getPiece(from);
                if (p == null || p.getColor() != color) {
                    continue;
                }
                for (Position to : allDestinations(from)) {
                    if (from.equals(to)) continue;
                    if (isLegalMove(from, to)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the list of positions that are legal destinations for the piece at from.
     * Used by UI to highlight legal moves. No chess rules in UI.
     */
    public List<Position> getLegalDestinations(Position from) {
        List<Position> out = new ArrayList<>();
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != currentTurn) {
            return out;
        }
        for (Position to : allDestinations(from)) {
            if (from.equals(to)) continue;
            if (isLegalMove(from, to)) {
                out.add(to);
            }
        }
        return out;
    }

    /**
     * Attempts to move the piece at from to to.
     * Handles castling, en passant, promotion, hasMoved, lastMove, and game status.
     */
    public boolean tryMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }
        if (gameStatus == GameStatus.CHECKMATE || gameStatus == GameStatus.STALEMATE) {
            return false;
        }

        if (isCastlingMove(from, to)) {
            if (!tryCastling(from, to)) {
                return false;
            }
        } else if (isEnPassantCapture(from, to)) {
            if (!tryEnPassant(from, to)) {
                return false;
            }
        } else {
            if (!piece.isLegalMove(board, from, to) || wouldBeInCheckAfterMove(from, to)) {
                return false;
            }
            executeNormalMove(from, to, piece);
        }

        if (!isCastlingMove(from, to)) {
            piece.setHasMoved(true);
        }
        recordLastMove(from, to, piece);
        switchTurn();
        updateGameStatus();
        return true;
    }

    public Piece getPieceAt(Position pos) {
        return board.getPiece(pos);
    }

    // ---------- Private helpers ----------

    private boolean isLegalMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null) return false;
        if (isCastlingMove(from, to)) {
            return isCastlingLegal(from, to) && !wouldBeInCheckAfterMove(from, to);
        }
        if (isEnPassantCapture(from, to)) {
            return !wouldBeInCheckAfterMove(from, to);
        }
        return piece.isLegalMove(board, from, to) && !wouldBeInCheckAfterMove(from, to);
    }

    private List<Position> allDestinations(Position from) {
        List<Position> list = new ArrayList<>();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                list.add(new Position(r, c));
            }
        }
        return list;
    }

 private void updateGameStatus() {
    Color sideToMove = currentTurn; 
    boolean inCheck = isInCheck(sideToMove);
    boolean hasMove = hasAnyLegalMove(sideToMove);

    if (inCheck && !hasMove) {
        gameStatus = GameStatus.CHECKMATE;
    } else if (!inCheck && !hasMove) {
        gameStatus = GameStatus.STALEMATE;
    } else if (inCheck) {
        gameStatus = GameStatus.CHECK;
    } else {
        gameStatus = GameStatus.ONGOING;
    }
}

    private void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void recordLastMove(Position from, Position to, Piece piece) {
        boolean pawnTwo = piece instanceof Pawn && Math.abs(to.getRow() - from.getRow()) == 2;
        lastMove = new Move(from, to, pawnTwo);
    }

    // ---- Normal move ----
    private void executeNormalMove(Position from, Position to, Piece piece) {
        board.clear(from);
        board.setPiece(to, piece);
        promotePawnAt(to);
    }

    private void promotePawnAt(Position pos) {
        Piece p = board.getPiece(pos);
        if (p instanceof Pawn) {
            int lastRank = (p.getColor() == Color.WHITE) ? 0 : 7;
            if (pos.getRow() == lastRank) {
                board.setPiece(pos, new Queen(p.getColor()));
            }
        }
    }

    private static void maybePromoteOnBoard(Board b, Position to, Piece piece) {
        if (!(piece instanceof Pawn)) return;
        int lastRank = (piece.getColor() == Color.WHITE) ? 0 : 7;
        if (to.getRow() == lastRank) {
            b.setPiece(to, new Queen(piece.getColor()));
        }
    }

    // ---- Castling ----
    private boolean isCastlingMove(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (!(piece instanceof King)) return false;
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        return dr == 0 && dc == 2;
    }

    private boolean isCastlingLegal(Position from, Position to) {
        Piece king = board.getPiece(from);
        if (!(king instanceof King) || king.hasMoved()) return false;
        int row = from.getRow();
        int colFrom = from.getCol();
        int colTo = to.getCol();
        int rookCol = colTo > colFrom ? ROOK_KING_SIDE_COL : ROOK_QUEEN_SIDE_COL;
        Position rookPos = new Position(row, rookCol);
        Piece rook = board.getPiece(rookPos);
        if (!(rook instanceof Rook) || rook.hasMoved()) return false;
        if (isInCheck(king.getColor())) return false;
        Position kingPathEnd = new Position(row, colFrom + (colTo > colFrom ? 2 : -2));
        if (!board.isPathClear(from, kingPathEnd)) return false;
        int step = colTo > colFrom ? 1 : -1;
        for (int c = colFrom + step; c != colTo; c += step) {
            Board copy = board.copy();
            copy.setPiece(new Position(row, c), king);
            copy.clear(from);
            if (isInCheckOnBoard(king.getColor(), copy)) return false;
        }
        if (wouldBeInCheckAfterMove(from, to)) return false;
        return true;
    }

    private boolean tryCastling(Position from, Position to) {
        if (!isCastlingLegal(from, to)) return false;
        Piece king = board.getPiece(from);
        int row = from.getRow();
        int colTo = to.getCol();
        int rookCol = colTo > from.getCol() ? ROOK_KING_SIDE_COL : ROOK_QUEEN_SIDE_COL;
        Position rookFrom = new Position(row, rookCol);
        Piece rook = board.getPiece(rookFrom);
        int rookToCol = colTo > from.getCol() ? 5 : 3;
        Position rookTo = new Position(row, rookToCol);

        board.clear(from);
        board.clear(rookFrom);
        board.setPiece(to, king);
        board.setPiece(rookTo, rook);
        king.setHasMoved(true);
        rook.setHasMoved(true);
        return true;
    }

    private void applyCastlingToBoard(Board copy, Position from, Position to) {
        Piece king = copy.getPiece(from);
        int row = from.getRow();
        int colTo = to.getCol();
        int rookCol = colTo > from.getCol() ? ROOK_KING_SIDE_COL : ROOK_QUEEN_SIDE_COL;
        Position rookFrom = new Position(row, rookCol);
        Piece rook = copy.getPiece(rookFrom);
        int rookToCol = colTo > from.getCol() ? 5 : 3;
        Position rookTo = new Position(row, rookToCol);
        copy.clear(from);
        copy.clear(rookFrom);
        copy.setPiece(to, king);
        copy.setPiece(rookTo, rook);
    }

    // ---- En passant ----
    private boolean isEnPassantCapture(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (!(piece instanceof Pawn)) return false;
        if (board.getPiece(to) != null) return false;
        int dr = to.getRow() - from.getRow();
        int dc = to.getCol() - from.getCol();
        int forwardDir = (piece.getColor() == Color.WHITE) ? -1 : 1;
        if (dr != forwardDir || Math.abs(dc) != 1) return false;
        if (lastMove == null || !lastMove.isPawnTwoSquares()) return false;
        if (lastMove.getTo().getCol() != to.getCol()) return false;
        int epRank = lastMove.getTo().getRow();
        int ourRank = (piece.getColor() == Color.WHITE) ? epRank + 1 : epRank;
        int captureSquareRank = epRank + forwardDir;
        return from.getRow() == ourRank && to.getRow() == captureSquareRank;
    }

    private boolean tryEnPassant(Position from, Position to) {
        if (wouldBeInCheckAfterMove(from, to)) return false;
        Piece pawn = board.getPiece(from);
        Position capturedPawnPos = lastMove.getTo();
        board.clear(from);
        board.clear(capturedPawnPos);
        board.setPiece(to, pawn);
        promotePawnAt(to);
        return true;
    }

    private void applyEnPassantToBoard(Board copy, Position from, Position to) {
        Piece pawn = copy.getPiece(from);
        Position capturedPos = lastMove.getTo();
        copy.clear(from);
        copy.clear(capturedPos);
        copy.setPiece(to, pawn);
        maybePromoteOnBoard(copy, to, pawn);
    }

    // ---- Check detection ----
    private boolean isInCheckOnBoard(Color color, Board b) {
        Position kingPos = b.findKing(color);
        if (kingPos == null) return false;
        Color opponent = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Position pos = new Position(r, c);
                Piece p = b.getPiece(pos);
                if (p != null && p.getColor() == opponent && p.isLegalMove(b, pos, kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }
}
