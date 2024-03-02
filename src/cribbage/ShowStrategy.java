package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public abstract class ShowStrategy {
	private Hand hand;
	private Hand starter;
	private ArrayList<Hand> combination = new ArrayList<Hand>();
	private Deck deck;
	

	public ShowStrategy(Hand hand, Hand starter, Deck deck) {
		this.hand = hand;
		this.starter = starter;
		this.deck = deck;
	}

	public Deck getDeck() {
		return deck;
	}

	public Hand getHand() {
		return hand;
	}

	public Hand getStarter() {
		return starter;
	}

	public ArrayList<Hand> getCombination() {
		return combination;
	}
	
	public int pairNumToPoint(int pairNum) {
		int point;
		switch(pairNum) {
			case 2:
		  		point = 2;
		  		break;
		  	case 3:
		  		point = 6;
		  		break;
		  	case 4:
		  		point = 12;
		  		break;
		  	default:
		  		point = 0;
		  			
		}
		return point;
	}

}
