package cribbage;

import java.io.PrintWriter;

import ch.aplu.jcardgame.Deck;

public abstract class NormalLog extends Log {
	private int player;
	
	public NormalLog(int player, PrintWriter pw, Deck deck) {
		super(pw, deck);
		this.player = player;
	}
	
	public int getPlayer() {
		return player;
	}
}
