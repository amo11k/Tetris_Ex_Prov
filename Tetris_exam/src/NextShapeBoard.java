import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;



public class NextShapeBoard extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static final int COLUMNS = 5;
	private static final int ROWS = 11;
	private Tetrominoes[][] matrixBoard;
	private int currentRow;
	private int currentColumn;
	private Shape nextShape;
	
	public NextShapeBoard(){
		matrixBoard = new Tetrominoes[ROWS][COLUMNS];
		initMatrixBoard();
		createNextShape();
	
	}
	
	private void initMatrixBoard() {
		for (int i=0; i<ROWS; i++)
			for (int j=0; j<COLUMNS; j++)
				matrixBoard[i][j]=Tetrominoes.NoShape;
	}
	
	private void drawBoard(Graphics g) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				drawSquare(g, j * squareWidth(), i * squareHeight(),
						matrixBoard[i][j]);
			}
		}
	}
	
	
	private void createNextShape() {
		currentRow=0;
		currentColumn=COLUMNS/2;
		nextShape = new Shape();
		nextShape.setRandomShape();
	}
	
	private void drawNextShape(Graphics g) {
		if (nextShape != null) {
			for (int i = 0; i < 4; i++) {
				drawSquare(g, (currentColumn + nextShape.getX(i))
						* squareWidth(), (currentRow + nextShape.getY(i))
						* squareHeight(), nextShape.getShape());
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBoard(g);
		drawNextShape(g);
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
}
