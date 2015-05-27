import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

public class Board extends JPanel {

	class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (tryToMove(currentShape, currentColumn - 1, currentRow))
					currentColumn--;
				break;
			case KeyEvent.VK_RIGHT:
				if (tryToMove(currentShape, currentColumn + 1, currentRow))
					currentColumn++;
				break;
			case KeyEvent.VK_UP:
				Shape piece = currentShape.rotateRight();
				if (tryToMove(piece, currentColumn, currentRow))
					currentShape = piece;
				break;
			case KeyEvent.VK_DOWN:
				while (tryToMove(currentShape, currentColumn, currentRow + 1)) {
					currentRow++;
				}
				break;
			default:
				break;
			}
			repaint();
		}
	}

	private static final int COLUMNS = 10;
	private static final int ROWS = 22;
	private Tetrominoes[][] matrixBoard;
	private int currentRow;
	private int currentColumn;
	private Shape currentShape;
	private ScoreBoard scoreBoard;
	private NextShapeBoard nextShape;
	private Timer timer;
	private boolean gameOver;
	MyKeyAdapter keyAdepter;

	private void createCurrentShape() {
		currentRow = 0;
		currentColumn = COLUMNS / 2;
		currentShape = new Shape();
		currentShape.setRandomShape();
	}

	public Board(ScoreBoard scoreBoard, NextShapeBoard nextShape) {
		this.scoreBoard = scoreBoard;
		this.nextShape = nextShape;
		
		matrixBoard = new Tetrominoes[ROWS][COLUMNS];
		initMatrixBoard();
		createCurrentShape();
		setFocusable(true);
	}

	private void initMatrixBoard() {
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLUMNS; j++)
				matrixBoard[i][j] = Tetrominoes.NoShape;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
		drawCurrentShape(g);
	}

	private void drawBoard(Graphics g) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				drawSquare(g, j * squareWidth(), i * squareHeight(),
						matrixBoard[i][j]);
			}
		}
	}

	private void drawCurrentShape(Graphics g) {
		if (currentShape != null) {
			for (int i = 0; i < 4; i++) {
				drawSquare(g, (currentColumn + currentShape.getX(i))
						* squareWidth(), (currentRow + currentShape.getY(i))
						* squareHeight(), currentShape.getShape());
			}
		}
	}

	public void run() {
		keyAdepter = new MyKeyAdapter();
		addKeyListener(keyAdepter);
		gameOver = false;

		timer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (tryToMove(currentShape, currentColumn, currentRow + 1)) {
					currentRow++;
				} else {
					if (gameOver) {
						processGameOver();
					} else {
						movePieceToBoard(currentShape, matrixBoard, currentRow,
								currentColumn);
						createCurrentShape();
					}
				}
				repaint();
			}
		});
		timer.start();
	}

	private void movePieceToBoard(Shape piece, Tetrominoes[][] board, int row,
			int column) {
		int newRow, newColumn;
		for (int i = 0; i < 4; i++) {
			newRow = row + piece.getY(i);
			newColumn = column + piece.getX(i);
			if ((newRow >= 0) && (newRow < ROWS) && (newColumn >= 0)
					&& (newColumn < COLUMNS))
				board[newRow][newColumn] = piece.getShape();
		}
		detectCompletedLines();
	}

	private void detectCompletedLines() {
		Boolean completed;
		for (int i = 0; i < ROWS; i++) {
			completed = true;
			for (int j = 0; j < COLUMNS; j++) {
				if (matrixBoard[i][j] == Tetrominoes.NoShape) {
					completed = false;
					break;
				}
			}
			if (completed) {
				removeLine(matrixBoard, i);
				scoreBoard.incrementScore();
			}
		}
	}

	private void removeLine(Tetrominoes[][] board, int row) {
		for (int i = row; i > 0; i--) {
			for (int j = 0; j < COLUMNS; j++) {
				board[i][j] = board[i - 1][j];
			}
		}
		for (int j = 0; j < COLUMNS; j++) {
			board[0][j] = Tetrominoes.NoShape;
		}
	}

	private boolean tryToMove(Shape piece, int column, int row) {
		if (row + piece.maxY() > ROWS - 1) {
			return false;
		}
		if ((column + piece.minX() < 0)
				|| (column + piece.maxX() > COLUMNS - 1)) {
			return false;
		}
		// Detect collision with the rest of the matrixBoard
		for (int i = 0; i < 4; i++) {
			int pointRow = row + piece.getY(i);
			int pointColumn = column + piece.getX(i);
			if ((pointRow >= 0) && (pointRow < ROWS) && (pointColumn >= 0)
					&& (pointColumn < COLUMNS))
				if (matrixBoard[pointRow][pointColumn] != Tetrominoes.NoShape) {
					if (currentRow < 1) {
						gameOver = true;
					}
					return false;
				}
		}

		return true;
	}

	private void drawSquare(Graphics g, int x, int y,

	Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
				new Color(102, 204, 102), new Color(102, 102, 204),
				new Color(204, 204, 102), new Color(204, 102, 204),
				new Color(102, 204, 204), new Color(218, 170, 0) };

		Color color = colors[shape.ordinal()];
		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);
		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y
				+ squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x
				+ squareWidth() - 1, y + 1);
	}

	private int squareWidth() {
		return getWidth() / COLUMNS;
	}

	private int squareHeight() {
		return getHeight() / ROWS;
	}

	private void processGameOver() {
		currentShape = null;
		timer.stop();
		removeKeyListener(keyAdepter);
		timer = new Timer(10, new ActionListener() {
			int row = 0;
			int col = 0;
			int increment = 1;

			@Override
			public void actionPerformed(ActionEvent e) {

				matrixBoard[row][col] = Tetrominoes.LineShape;
				col += increment;
				if (col > COLUMNS - 1) {
					row++;
					increment = -1;
					col--;
				} else {
					if (col < 0) {
						row++;
						col = 0;
						increment = 1;
					}
				}
				if (row == ROWS) {
					timer.stop();
					scoreBoard.setGameOver();
				}
				repaint();
			}
		});
		timer.start();
	}

}
