package cribbage;

import java.io.PrintWriter;

import ch.aplu.jcardgame.Deck;

public class PlayScoreLog extends ScoreLog {
	
	public PlayScoreLog(int player, PrintWriter pw, Deck deck, int playerScore, int score, String type) {
		super(player, pw, deck, playerScore, score, type);
	}
	
	public void display() {
		String typeSuffix = checkSuffix(type, score);
		/* Display the score statement */
		getPw().println("score,P"+getPlayer()+","+playerScore+","+score+","+type+typeSuffix);
	}
	
	/* Check the number of pair or run */
	public String checkSuffix(String type, int score) {
		String typeSuffix = "";
		if (type.equals("pair")) {
			switch(score) {
				case 2:
					typeSuffix = 2 + "";
					break;
				case 6:
					typeSuffix = 3 + "";
					break;
				case 12:
					typeSuffix = 4 + "";
					break;
			}
		} else if (type.equals("run")){
			typeSuffix = score + "";
		}
		return typeSuffix;
	}
}
