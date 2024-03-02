package cribbage;

import java.io.PrintWriter;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

public class ShowScoreLog extends ScoreLog {
	ArrayList<Hand> combination;
	
	public ShowScoreLog(int player, PrintWriter pw, Deck deck, int playerScore, int score, String type, ArrayList<Hand> combination) {
		super(player, pw, deck, playerScore, score, type);
		this.combination = combination;
	}
	
	public void display() {
		String typeSuffix = "";
		for (Hand hand: combination) {
			typeSuffix = checkSuffix(type, hand);
			/* Convert the pair number to the score */
			if ( type.equals("pair") ) {
				score = score*(score-1);
			}
			playerScore += score;
			getPw().println("score,P"+getPlayer()+","+playerScore+","+score+","+type+typeSuffix+","+canonical(hand));
		}
	}
	
	/* Check the length of run, pair or flush */
	public String checkSuffix(String type, Hand hand) {
		String typeSuffix = "";
		if (type.equals("run") || type.equals("pair") || type.equals("flush")) {
			typeSuffix = hand.getCardList().size() + "";
		}
		return typeSuffix;
	}
}
