
/**
 * 
 * 	Tony Lu
 * 	CMP 326
 * 	Fall 2018
 * 
 */

package Connect4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class ConnectGUI extends JFrame {

	private JPanel jMain;
	// create gameboard
	Gameboard gBoard;
	// create scoreboard
	Scoreboard sBoard;

	// colors for customization
	private final Color black = new Color(30, 27, 24); // #1E1B18
	private final Color white = new Color(255, 250, 255); // #FFFAFF
	private final Color red = new Color(216, 49, 91); // #D8315B
	private final Color darkBlue = new Color(10, 36, 99); // #0A2463
	private final Color lightBlue = new Color(75, 179, 253); // #4BB3FD

	private Player currPlayer, player1, player2;

	public ConnectGUI() {

		// Ask player 1's desired name
		String p1 = JOptionPane.showInputDialog("Enter Player 1's name. (Blank will be set to 'Player 1')");
		if (p1.isEmpty()) {
			player1 = new Player("Player 1", "X");
		} else {
			player1 = new Player(p1, "X");
		}

		// ask player 2's desired name
		String p2 = JOptionPane.showInputDialog("Enter Player 2's name. (Blank will be set to 'Player 2')");
		if (p2.isEmpty()) {
			player2 = new Player("Player 2", "O");
		} else {
			player2 = new Player(p2, "O");
		}

		currPlayer = player1;

		// for scoreboard and main gameboard
		jMain = new JPanel();
		jMain.setLayout(new GridLayout(2, 1));

		// scoreboard configuration
		sBoard = new Scoreboard();
		sBoard.setBackground(white); // white

		// new gameboard
		gBoard = new Gameboard();

		// add to JPanel
		jMain.add(sBoard);
		jMain.add(gBoard);

		// add to main JFrame
		add(jMain);

		// JFrame configuration
		setSize(500, 600); // window size
		setResizable(false); // fixed resolution
		setTitle("Connect 4"); 
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private class Gameboard extends JPanel implements ActionListener, GameBoard, GamePlayer {

		private int[] nextSlot = new int[7]; // track the next available slot to drop marker
		private JButton[] buttons = new JButton[7]; // buttons for selecting where to drop
		private JLabel[][] board = new JLabel[6][7]; // the 6x7 gameboard
		Border jLabelBorder = BorderFactory.createLineBorder(black, 1); // black border
		Border btnBorder = BorderFactory.createLineBorder(black, 1); // borders for buttons

		// def constructor for the gameboard
		public Gameboard() {
			// size of JPanel
			setLayout(new GridLayout(7, 7));
			// init board
			displayButtons(); // show buttons
			displayBoard(); // show board
		}

		// display the 6x7 gameboard
		public void displayBoard() {
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[r].length; c++) {
					board[r][c] = new JLabel("", SwingConstants.CENTER);
					Font font = new Font(Font.SANS_SERIF, Font.BOLD, 48);
					board[r][c].setFont(font);
					board[r][c].setForeground(black);
					board[r][c].setBorder(jLabelBorder);
					board[r][c].setOpaque(true);
					// interchange color of every column
					if (c % 2 == 0) {
						board[r][c].setBackground(lightBlue);
					} else {
						board[r][c].setBackground(red);
					}
					add(board[r][c]);
				}
			}
		}

		// display buttons for users to press to place markers
		public void displayButtons() {
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new JButton("*");
				nextSlot[i] = 5;
				Font font = new Font(Font.SANS_SERIF, Font.BOLD, 30);
				buttons[i].setFont(font);
				buttons[i].setBorder(btnBorder);
				buttons[i].setForeground(white); // white
				buttons[i].setBackground(darkBlue); // black
				buttons[i].addActionListener(this);
				buttons[i].setVisible(true);
				buttons[i].setEnabled(true);
				add(buttons[i]);
			}
		}

		// when button pressed event
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();

			for (int x = 0; x < nextSlot.length; x++) {
				if (btn.equals(buttons[x])) { // check which index btn is from
					if (nextSlot[x] >= 0) { // make sure a marker can be placed at the location
						// set marker to the currPlayer
						if (currPlayer.equals(player1)) {
							board[nextSlot[x]][x].setForeground(Color.BLACK);
						} else if (currPlayer.equals(player2)) { // player places the marker
							board[nextSlot[x]][x].setForeground(Color.RED);
						}

						// place marker down
						board[nextSlot[x]][x].setText(currPlayer.getMarker());
						nextSlot[x]--; // decrement the available num of slots at the current index
						// check if there are no more slots left, disable everything if true
						if (nextSlot[x] < 0) {
							btn.setBackground(Color.BLACK);
							btn.setText("X");
							btn.setEnabled(false);
						}
					}
				}
			}

			// check if someone won
			if (isWinner()) {
				// someone won!
				if (currPlayer.equals(player1)) { // player 1 won!
					gameEnd(1);
				} else if (currPlayer.equals(player2)) { // player 2 won!
					gameEnd(2);
				}
			} else if (isFull()) { // no one won, it's a draw!
				gameEnd(0);
			} else {
				// no winner or draw yet, keep playing
				nextTurn();
			}

		}

		public void gameEnd(int results) {
			// results
			// 0 = draw
			// 1 = player 1 win
			// 2 = player 2 win
			if (results == 0) { // game was a draw
				// update scores and stats
				player1.addDraw();
				player2.addDraw();
				updateScores();
				int userChoice = JOptionPane.showConfirmDialog(null, "Game ended in a draw!\nPlay again?", "DRAW!",
						JOptionPane.YES_NO_OPTION);
				playAgain(userChoice); // want to play again?

			} else if (results == 1) { // player 1 wins
				player1.addWin();
				player2.addLoss();
				updateScores();
				int userChoice = JOptionPane.showConfirmDialog(null,
						"Winner is [" + currPlayer.getName() + "]\nPlay again?", "WINNER!", JOptionPane.YES_NO_OPTION);
				playAgain(userChoice);

			} else if (results == 2) { // player 2 wins
				player2.addWin();
				player1.addLoss();
				updateScores();
				int userChoice = JOptionPane.showConfirmDialog(null,
						"Winner is [" + currPlayer.getName() + "]\nPlay again?", "WINNER!", JOptionPane.YES_NO_OPTION);
				playAgain(userChoice);
			}
		}

		// ask players what to do. Play again or no?
		// x:
		// 	- 0 = yes, play again
		// 	- 1 = no, end program
		public void playAgain(int x) {
			if (x == 0) {
				resetBoard();
			} else if (x == 1) {
				System.exit(0);
			}
		}

		@Override
		public boolean isWinner() {
			// check if won by row, col or diag
			if (rowWin() || colWin() || diagsWin()) {
				return true;
			} else {
				return false;
			}
		}

		// --- START [Winning condition checkers] START ---

		// win by 4 in a row
		public boolean rowWin() {
			for (int r = 0; r < board.length; r++) {
				int matches = 0;
				for (int c = 0; c < board[r].length; c++) {
					if (board[r][c].getText().equals(currPlayer.getMarker())) {
						matches++;
						if (matches >= 4) { // 4 or more in a row. Winner!
							return true;
						}
					} else {
						matches = 0;
					}
				}
			}
			return false;
		}

		// win by 4 in a col
		public boolean colWin() {
			for (int c = 0; c < board[0].length; c++) {
				int matches = 0;
				for (int r = 0; r < board.length; r++) {
					if (board[r][c].getText().equals(currPlayer.getMarker())) {
						matches++;
						if (matches >= 4) {
							return true;
						}
					} else {
						matches = 0;
					}
				}
			}
			return false;
		}

		// win by 4 in diag
		public boolean diagsWin() {
			int matches;

			// diag check from botLeft to topRight
			for (int startRow = 3; startRow < 6; startRow++) { // check diag from 50, 40, 30
				matches = 0;
				int r = startRow;
				int c = 0;
				while (r >= 0 && c < 7) {
					if (board[r][c].getText().equals(currPlayer.getMarker())) {
						matches++;
						if (matches >= 4) {
							return true;
						}
					} else {
						matches = 0;
					}
					r--;
					c++;
				}
				if (startRow == 5) {
					for (int col = 1; col <= 3; col++) { // check diag from 51, 52, 53
						int row = startRow;
						c = col;
						while (row >= 0 && c < 7) {
							if (board[row][c].getText().equals(currPlayer.getMarker())) {
								matches++;
								if (matches >= 4) {
									return true;
								}
							} else {
								matches = 0;
							}
							row--;
							c++;
						}
					}
				}
			}

			// diag check from topLeft to botRight
			for (int rowStart = 0; rowStart < 3; rowStart++) { // check diag from 00, 10, 20
				matches = 0;
				int r = rowStart;
				int c = 0;
				while (r < 6 && c < 7) {
					if (board[r][c].getText().equals(currPlayer.getMarker())) {
						matches++;
						if (matches >= 4) {
							return true;
						}
					} else {
						matches = 0;
					}
					r++;
					c++;
				}

			}

			for (int colStart = 1; colStart < 4; colStart++) {
				matches = 0;
				int r = 0;
				int c = colStart;
				while (r < 6 && c < 7) {
					if (board[r][c].getText().equals(currPlayer.getMarker())) {
						matches++;
						if (matches >= 4) {
							return true;
						}
					} else {
						matches = 0;
					}
					r++;
					c++;
				}
			}

			return false;
		}

		// --- END [Winning condition checkers] END ---

		// change turns
		@Override
		public void nextTurn() {
			if (currPlayer.equals(player1)) {
				currPlayer = player2;
				sBoard.p2Name.setForeground(red);
				sBoard.p2Name.setText("> " + currPlayer.getName() + " <");

				sBoard.p1Name.setForeground(darkBlue);
				sBoard.p1Name.setText(player1.getName());
			} else {
				currPlayer = player1;
				sBoard.p1Name.setForeground(red);
				sBoard.p1Name.setText("> " + currPlayer.getName() + " <");

				sBoard.p2Name.setForeground(darkBlue);
				sBoard.p2Name.setText(player2.getName());
			}

		}

		// reset game board
		@Override
		public void resetBoard() {
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[r].length; c++) {
					board[r][c].setForeground(black);
					board[r][c].setText("");
					board[r][c].setOpaque(true);
					if (c % 2 == 0) {
						board[r][c].setBackground(lightBlue);
					} else {
						board[r][c].setBackground(red);
					}
				}
			}

			for (int i = 0; i < nextSlot.length; i++) {
				nextSlot[i] = 5;
			}

			for (int x = 0; x < buttons.length; x++) {
				buttons[x].setBackground(darkBlue);
				buttons[x].setText("*");
				buttons[x].setEnabled(true);
			}

		}

		// is board empty?
		@Override
		public boolean isEmpty() {
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[r].length; c++) {
					String cell = board[r][c].getText().trim();
					if (cell.isEmpty())
						return true;
				}
			}
			return false;
		}

		// is board full?
		@Override
		public boolean isFull() {
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[r].length; c++) {
					String cell = board[r][c].getText().trim();
					if (cell.equals(""))
						return false;
				}
			}
			return true;
		}

		// update score board
		public void updateScores() {
			sBoard.p1Games.setText("Games: " + Integer.toString(player1.getNumGames()));
			sBoard.p1Wins.setText("Wins: " + Integer.toString(player1.getNumWins()));
			sBoard.p1Losses.setText("Losses: " + Integer.toString(player1.getNumLosses()));

			sBoard.p2Games.setText("Games: " + Integer.toString(player2.getNumGames()));
			sBoard.p2Wins.setText("Wins: " + Integer.toString(player2.getNumWins()));
			sBoard.p2Losses.setText("Losses: " + Integer.toString(player2.getNumLosses()));
		}

	}

	// scoreboard configuration
	private class Scoreboard extends JPanel {
		private JPanel scores, p1, p2;
		private JLabel title, p1Name, p1Games, p1Wins, p1Losses, p2Name, p2Games, p2Wins, p2Losses;

		public Scoreboard() {
			setLayout(new GridLayout(2, 1));

			title = new JLabel("Connect 4", SwingConstants.CENTER);
			title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
			title.setForeground(new Color(84, 13, 110)); // purple

			add(BorderLayout.NORTH, title);

			scores = new JPanel();
			scores.setLayout(new GridLayout(0, 2));

			p1Name = new JLabel(player1.getName(), SwingConstants.CENTER);
			p1Games = new JLabel("Games: ");
			p1Wins = new JLabel("Wins: ");
			p1Losses = new JLabel("Losses: ");

			p2Name = new JLabel(player2.getName(), SwingConstants.CENTER);
			p2Games = new JLabel("Games: ");
			p2Wins = new JLabel("Wins: ");
			p2Losses = new JLabel("Losses: ");

			Font f = new Font(Font.SANS_SERIF, Font.BOLD, 32);
			Font statsFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
			Color stats = black;

			// PLAYER 1's SCOREBOARD
			p1Name.setFont(f);
			p1Name.setForeground(red);
			p1Name.setText("> " + player1.getName() + " <");

			p1Games.setFont(statsFont);
			p1Games.setForeground(stats);
			p1Games.setText("Games: " + player1.getNumGames());

			p1Wins.setFont(statsFont);
			p1Wins.setForeground(stats);
			p1Wins.setText("Wins: " + player1.getNumWins());

			p1Losses.setFont(statsFont);
			p1Losses.setForeground(stats);
			p1Losses.setText("Losses: " + player1.getNumLosses());

			p1 = new JPanel();
			p1.setLayout(new GridLayout(0, 1));
			p1.setBackground(white);
			p1.add(p1Name);
			p1.add(p1Games);
			p1.add(p1Wins);
			p1.add(p1Losses);

			// PLAYER 2's SCOREBOARD
			p2Name.setFont(f);
			p2Name.setForeground(darkBlue);

			p2Games.setFont(statsFont);
			p2Games.setForeground(stats);
			p2Games.setText("Games: " + player2.getNumGames());

			p2Wins.setFont(statsFont);
			p2Wins.setForeground(stats);
			p2Wins.setText("Wins: " + player2.getNumWins());

			p2Losses.setFont(statsFont);
			p2Losses.setForeground(stats);
			p2Losses.setText("Losses: " + player2.getNumLosses());

			p2 = new JPanel();
			p2.setLayout(new GridLayout(0, 1));
			p2.setBackground(white);
			p2.add(p2Name);
			p2.add(p2Games);
			p2.add(p2Wins);
			p2.add(p2Losses);

			scores.add(p1);
			scores.add(p2);

			add(BorderLayout.SOUTH, scores);
		}
	}

}
