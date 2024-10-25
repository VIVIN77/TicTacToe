import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TicTacToe {
    // Define game board dimensions and initial scores
    int boardWidth = 600;
    int boardHeight = 700; // 50px extra for the scoreboard
    int scoreX = 0; // Track X's score
    int scoreO = 0; // Track O's score

    // Declare GUI components
    JFrame frame = new JFrame("Tic Tac Toe");
    JLabel textLabel = new JLabel(); // Label for displaying messages
    JLabel scoreLabel = new JLabel(); // Label for displaying scores
    JPanel textPanel = new JPanel(); // Panel to hold the text label
    JPanel boardPanel = new JPanel(); // Panel to hold the game board (3x3 grid)
    JPanel buttonPanel = new JPanel(); // Panel to hold the "Next Round" and "New Game" buttons
    JButton[][] board = new JButton[3][3]; // 2D array for board buttons (3x3 grid)
    JButton nextRoundButton = new JButton("Next Round"); // Button for starting next round
    JButton newGameButton = new JButton("New Game"); // Button for resetting the game

    // Player symbols
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX; // Start with player X

    // Game state variables
    boolean gameOver = false;
    int turns = 0;

    // Constructor to set up the game UI and initialize components
    TicTacToe() {
        // Configure the main game frame
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set up the text label for displaying game messages
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true); // Make label background visible

        // Set up the score label for displaying player scores
        scoreLabel.setBackground(Color.darkGray);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        updateScoreLabel(); // Initial score display
        scoreLabel.setOpaque(true);

        // Combine text label and score label in textPanel
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.NORTH);
        textPanel.add(scoreLabel, BorderLayout.SOUTH);
        frame.add(textPanel, BorderLayout.NORTH); // Position text panel at the top

        // Configure the board panel (3x3 grid layout)
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        // Loop to initialize each tile on the game board
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile; // Store tile in the board array
                boardPanel.add(tile); // Add tile to the board panel
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false); // Prevents keyboard focus highlighting

                // Add action listener for each tile
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return; // Ignore clicks if game is over
                        JButton tile = (JButton) e.getSource(); // Get clicked tile
                        if (tile.getText().equals("")) { // If tile is empty
                            tile.setText(currentPlayer); // Set tile to current player's symbol
                            turns++; // Increase turn count
                            checkWinner(); // Check for winner after each move
                            if (!gameOver) { // Switch players if game is not over
                                currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                                textLabel.setText(currentPlayer + "'s turn.");
                            }
                        }
                    }
                });
            }
        }

        // Create panel for "Next Round" and "New Game" buttons
        buttonPanel.setLayout(new GridLayout(1, 2));
        nextRoundButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextRoundButton.setBackground(Color.lightGray);
        nextRoundButton.setForeground(Color.BLACK);
        newGameButton.setFont(new Font("Arial", Font.BOLD, 20));
        newGameButton.setBackground(Color.lightGray);
        newGameButton.setForeground(Color.BLACK);
        buttonPanel.add(nextRoundButton);
        buttonPanel.add(newGameButton);
        frame.add(buttonPanel, BorderLayout.SOUTH); // Attach button panel at the bottom

        // Action listener for "Next Round" button
        nextRoundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetBoard(); // Clear board for the next round but keep scores
            }
        });

        // Action listener for "New Game" button
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetScores(); // Reset scores and clear board
            }
        });
    }

    // Method to check for a winner or tie after each move
    void checkWinner() {
        // Check rows for a win
        for (int r = 0; r < 3; r++) {
            if (!board[r][0].getText().equals("") &&
                board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                for (int i = 0; i < 3; i++) setWinner(board[r][i]); // Highlight winning tiles
                updateScores(); // Update scores for the winning player
                gameOver = true;
                return;
            }
        }

        // Check columns for a win
        for (int c = 0; c < 3; c++) {
            if (!board[0][c].getText().equals("") &&
                board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                for (int i = 0; i < 3; i++) setWinner(board[i][c]); // Highlight winning tiles
                updateScores(); // Update scores for the winning player
                gameOver = true;
                return;
            }
        }

        // Check diagonal (top-left to bottom-right) for a win
        if (!board[0][0].getText().equals("") &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            for (int i = 0; i < 3; i++) setWinner(board[i][i]); // Highlight winning tiles
            updateScores(); // Update scores for the winning player
            gameOver = true;
            return;
        }

        // Check anti-diagonal (top-right to bottom-left) for a win
        if (!board[0][2].getText().equals("") &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            for (int i = 0; i < 3; i++) setWinner(board[i][2 - i]); // Highlight winning tiles
            updateScores(); // Update scores for the winning player
            gameOver = true;
            return;
        }

        // Check for a tie if all tiles are filled and no winner
        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) setTie(board[r][c]); // Highlight tiles for a tie
            }
            textLabel.setText("It's a Tie!");
            gameOver = true;
        }
    }

    // Method to set tile colors when there's a winner
    void setWinner(JButton tile) {
        tile.setForeground(Color.green); // Winning tiles text in green
        tile.setBackground(Color.lightGray); // Winning tiles background
        textLabel.setText(currentPlayer + " is the Winner!"); // Display winning message
    }

    // Method to set tile colors when it's a tie
    void setTie(JButton tile) {
        tile.setForeground(Color.red); // Set tie tiles text in red
        tile.setBackground(Color.gray); // Set tie tiles background
    }

    // Method to update scores and display them
    void updateScores() {
        if (currentPlayer.equals(playerX)) {
            scoreX++; // Increment X's score
        } else {
            scoreO++; // Increment O's score
        }
        updateScoreLabel(); // Update score display
    }

    // Method to update the score label with current scores
    void updateScoreLabel() {
        scoreLabel.setText("Score: X = " + scoreX + " | O = " + scoreO);
    }

    // Method to reset the game board for the next round, keeping scores
    void resetBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText(""); // Clear all tiles
                board[r][c].setBackground(Color.darkGray); // Reset tile background color
                board[r][c].setForeground(Color.white); // Reset tile text color
            }
        }
        gameOver = false; // Reset game state
        turns = 0; // Reset turn counter
        currentPlayer = playerX; // Start with player X
        textLabel.setText("Tic-Tac-Toe"); // Reset the display label
    }

    // Method to reset the entire game, including scores
    void resetScores() {
        scoreX = 0; // Reset X's score
        scoreO = 0; // Reset O's score
        updateScoreLabel(); // Update score display
        resetBoard(); // Reset the board
    }
}
