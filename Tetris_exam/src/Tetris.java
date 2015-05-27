import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class Tetris extends JFrame {

	private JPanel contentPane;
	private ScoreBoard scoreBoard;
	private NextShapeBoard nextShape;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tetris frame = new Tetris();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tetris() {
		setTitle("Tetris");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 200, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		scoreBoard = new ScoreBoard();
		nextShape = new NextShapeBoard();
		contentPane.add(nextShape, BorderLayout.EAST);
		contentPane.add(scoreBoard, BorderLayout.SOUTH);
		
		Board board = new Board(scoreBoard, nextShape);
		contentPane.add(board, BorderLayout.CENTER);
		
		board.run();
	}

}
