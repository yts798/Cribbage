package cribbage;

import java.io.PrintWriter;

import ch.aplu.jcardgame.*;

public class ShowLog extends NormalLog {
	Hand starter;
	Hand hand;
	
	public ShowLog(int player, PrintWriter pw, Deck deck, Hand starter, Hand hand) {
		super(player, pw, deck);
		this.starter = starter;
		this.hand = hand;
	}
	
	public void display() {
		getPw().println("show,P"+getPlayer()+","+canonical(starter.getFirst())+"+"+canonical(hand));
	}
}
