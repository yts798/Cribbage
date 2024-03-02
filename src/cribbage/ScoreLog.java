package cribbage;

import java.io.PrintWriter;

import ch.aplu.jcardgame.*;

public abstract class ScoreLog extends NormalLog {
	int playerScore;
	int score;
	String type;
	
	public ScoreLog(int player, PrintWriter pw, Deck deck, int playerScore, int score, String type) {
		super(player, pw, deck);
		this.playerScore = playerScore;
		this.score = score;
		this.type = type;;
	}
	
	public abstract void display();
}
