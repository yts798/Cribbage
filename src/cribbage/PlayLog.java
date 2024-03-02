package cribbage;

import ch.aplu.jcardgame.*;
import java.io.PrintWriter;

public class PlayLog extends NormalLog {
	private int total;
	private Card card;
	
	public PlayLog(int player, PrintWriter pw, Deck deck, int total, Card card) {
		super(player, pw, deck);
		this.total = total;
		this.card = card;
	}
	
	public void display() {
		/* Log the play statement */
		getPw().println("play,P"+getPlayer()+","+total+","+canonical(card));
	}
}
