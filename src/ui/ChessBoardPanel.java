package ui;

import game.Game;
import game.GameStatus;
import model.Piece;
import model.Position;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 8x8 clickable chess board. Row 0 at top = black's back rank; row 7 at bottom = white's back rank.
 * Highlights selected square (yellow) and legal destinations (green). Uses game logic only; no rules in UI.
 */
public class ChessBoardPanel extends JPanel {
    private static final int SQUARE_SIZE = 60;
    private static final Color LIGHT = new Color(240, 217, 181);
    private static final Color DARK = new Color(181, 136, 99);
    private static final Color SELECTED = new Color(255, 255, 100);
    private static final Color SELECTED_BORDER = new Color(200, 180, 0);
    private static final Color LEGAL_MOVE = new Color(144, 238, 144);

    private Game game;
    private final JLabel[][] squares;
    private Position selected;

    public ChessBoardPanel(Game game) {
        this.game = game;
        this.squares = new JLabel[8][8];
        this.selected = null;
        setLayout(new GridLayout(8, 8));
        setPreferredSize(new Dimension(8 * SQUARE_SIZE, 8 * SQUARE_SIZE));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        buildSquares();
    }

    /** Replaces the game (e.g. after Load or Restart). Clears selection and refreshes. */
    public void setGame(Game game) {
        this.game = game;
        this.selected = null;
        refresh();
    }

    private void buildSquares() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setOpaque(true);
                label.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                label.setFont(new Font(Font.SERIF, Font.BOLD, 32));
                final int r = row;
                final int c = col;
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onSquareClicked(r, c);
                    }
                });
                squares[row][col] = label;
                add(label);
            }
        }
        refresh();
    }

    private Position viewToModel(int viewRow, int viewCol) {
        return new Position(7 - viewRow, viewCol);
    }

    private void onSquareClicked(int viewRow, int viewCol) {
        GameStatus status = game.getGameStatus();
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE) {
            return;
        }

        Position pos = viewToModel(viewRow, viewCol);
        Piece piece = game.getPieceAt(pos);

        if (selected != null) {
            if (pos.equals(selected)) {
                selected = null;
                refresh();
                return;
            }
            boolean moved = game.tryMove(selected, pos);
            if (moved) {
                selected = null;
                refresh();
            } else {
                System.out.println("Illegal move: " + selected + " -> " + pos);
                selected = null;
                refresh();
            }
            return;
        }

        if (piece != null && piece.getColor() == game.getCurrentTurn()) {
            selected = pos;
            refresh();
        }
    }

    public void refresh() {
        List<Position> legalDestinations = (selected != null) ? game.getLegalDestinations(selected) : null;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = viewToModel(row, col);
                Piece piece = game.getPieceAt(pos);
                boolean isSelected = selected != null && selected.equals(pos);
                boolean isLegalDest = legalDestinations != null && legalDestinations.contains(pos);

                Color bg = baseColor(row, col);
                if (isSelected) {
                    bg = SELECTED;
                    squares[row][col].setBorder(new LineBorder(SELECTED_BORDER, 3));
                } else if (isLegalDest) {
                    bg = LEGAL_MOVE;
                    squares[row][col].setBorder(null);
                } else {
                    squares[row][col].setBorder(null);
                }
                squares[row][col].setBackground(bg);

                if (piece != null) {
                    squares[row][col].setText(String.valueOf(piece.getSymbol()));
                    squares[row][col].setForeground(piece.getColor() == model.Color.WHITE ? Color.WHITE : Color.BLACK);
                } else {
                    squares[row][col].setText("");
                }
            }
        }
        revalidate();
        repaint();
    }

    private static Color baseColor(int viewRow, int viewCol) {
        return ((viewRow + viewCol) % 2 == 0) ? LIGHT : DARK;
    }
}
