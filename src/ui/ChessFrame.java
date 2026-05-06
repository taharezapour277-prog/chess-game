package ui;

import game.Game;
import game.GameStatus;
import model.Color;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Main application window for the chess game.
 * Handles chess clock (UI only), status, Save/Load, and Restart.
 * Game logic remains in Game; timer and file I/O stay in UI.
 */
public class ChessFrame extends JFrame {
    private static final String SAVE_FILE = "chess.save";
    /** Initial time per player in seconds (configurable). */
    private static final int INITIAL_TIME_SECONDS = 300;

    private Game game;
    private final ChessBoardPanel boardPanel;
    private final JLabel statusLabel;
    private final JLabel whiteTimeLabel;
    private final JLabel blackTimeLabel;

    private int whiteRemainingSeconds;
    private int blackRemainingSeconds;
    private boolean timeUp;
    private javax.swing.Timer statusTimer;
    private javax.swing.Timer clockTimer;

    public ChessFrame() {
        super("Chess");
        this.game = new Game();
        this.boardPanel = new ChessBoardPanel(game);
        this.statusLabel = new JLabel("", SwingConstants.CENTER);
        this.whiteTimeLabel = new JLabel("", SwingConstants.CENTER);
        this.blackTimeLabel = new JLabel("", SwingConstants.CENTER);

        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        whiteTimeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        blackTimeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));

        resetTimers();
        this.timeUp = false;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        add(buildTopPanel(), BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        statusTimer = new javax.swing.Timer(200, e -> updateStatus());
        statusTimer.start();

        clockTimer = new javax.swing.Timer(1000, e -> tickClock());
        clockTimer.start();

        updateStatus();
        updateTimeDisplay();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        top.add(new JLabel("White: "));
        top.add(whiteTimeLabel);
        top.add(new JLabel("   "));
        top.add(new JLabel("Black: "));
        top.add(blackTimeLabel);
        return top;
    }

    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(statusLabel, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton saveBtn = new JButton("Save Game");
        JButton loadBtn = new JButton("Load Game");
        JButton restartBtn = new JButton("Restart Game");
        saveBtn.addActionListener(e -> saveGame());
        loadBtn.addActionListener(e -> loadGame());
        restartBtn.addActionListener(e -> restartGame());
        buttons.add(saveBtn);
        buttons.add(loadBtn);
        buttons.add(restartBtn);
        bottom.add(buttons, BorderLayout.SOUTH);
        return bottom;
    }

    private void resetTimers() {
        whiteRemainingSeconds = INITIAL_TIME_SECONDS;
        blackRemainingSeconds = INITIAL_TIME_SECONDS;
    }

    private void tickClock() {
        if (timeUp) return;
        GameStatus status = game.getGameStatus();
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE) return;

        Color current = game.getCurrentTurn();
        if (current == Color.WHITE) {
            whiteRemainingSeconds--;
            if (whiteRemainingSeconds <= 0) {
                whiteRemainingSeconds = 0;
                endGameOnTime(Color.BLACK);
            }
        } else {
            blackRemainingSeconds--;
            if (blackRemainingSeconds <= 0) {
                blackRemainingSeconds = 0;
                endGameOnTime(Color.WHITE);
            }
        }
        updateTimeDisplay();
    }

    private void endGameOnTime(Color winner) {
        timeUp = true;
        clockTimer.stop();
        boardPanel.setEnabled(false);
        String msg = (winner == Color.WHITE) ? "White wins on time" : "Black wins on time";
        statusLabel.setText(msg);
        JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTimeDisplay() {
        whiteTimeLabel.setText(formatTime(whiteRemainingSeconds));
        blackTimeLabel.setText(formatTime(blackRemainingSeconds));
    }

    private static String formatTime(int totalSeconds) {
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%d:%02d", m, s);
    }

    private void updateStatus() {
        if (timeUp) return;
        GameStatus status = game.getGameStatus();
        String text;
        switch (status) {
            case CHECK:
                text = "CHECK";
                break;
            case CHECKMATE:
                Color winner = (game.getCurrentTurn() == Color.WHITE) ? Color.BLACK : Color.WHITE;
                text = "CHECKMATE - " + (winner == Color.WHITE ? "White" : "Black") + " wins";
                break;
            case STALEMATE:
                text = "DRAW - STALEMATE";
                break;
            default:
                text = "Turn: " + (game.getCurrentTurn() == Color.WHITE ? "White" : "Black");
                break;
        }
        statusLabel.setText(text);
    }

    private void saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(game);
            JOptionPane.showMessageDialog(this, "Game saved to " + SAVE_FILE, "Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "No saved game found.", "Load", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Game loaded = (Game) ois.readObject();
            game = loaded;
            boardPanel.setGame(game);
            resetTimers();
            timeUp = false;
            boardPanel.setEnabled(true);
            clockTimer.start();
            updateStatus();
            updateTimeDisplay();
            JOptionPane.showMessageDialog(this, "Game loaded.", "Load", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Load failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restartGame() {
        game = new Game();
        boardPanel.setGame(game);
        resetTimers();
        timeUp = false;
        boardPanel.setEnabled(true);
        if (!clockTimer.isRunning()) clockTimer.start();
        updateStatus();
        updateTimeDisplay();
    }

    public Game getGame() {
        return game;
    }

    public ChessBoardPanel getBoardPanel() {
        return boardPanel;
    }
}
