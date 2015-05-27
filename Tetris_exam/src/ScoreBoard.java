import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JLabel;


public class ScoreBoard extends JPanel {

	/**
	 * Create the panel.
	 */
	private int score;
	private JLabel label;
	
	public ScoreBoard() {
		
		score = 0;
		label = new JLabel("0");
		add(label);

	}
	
	public void incrementScore() {
		score ++;
		label.setText(""+score);
	}
	
	public void incrementScore(int points) {
		score += points;
		label.setText(""+score);
	}
	
	public int getScore() {
		return score;
	}
	
	public void setGameOver() {
		label.setText("GAME OVER. "+score+" points");
		label.setForeground(Color.red);
	}

}
