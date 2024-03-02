package cribbage;

import java.util.ArrayList;
import java.util.Collections;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class PlayPairStrategy extends PlayStrategy {
	
	public PlayPairStrategy(Hand hand) {
		super(hand);
	}
	
	/* Check whether a given segment has a pair */
	public void checkPair() {
		ArrayList<Card> cards = new ArrayList<Card>(getSegment().getCardList());
		int pairNum = 0;
		
		/* Find the order of the last card */
		Collections.reverse(cards);
		int lastOrder = ((Cribbage.Rank) cards.get(0).getRank()).order;

		for (Card card: cards) {
			/* Check how many cards have the same order */
			if (((Cribbage.Rank) card.getRank()).order == lastOrder) {
				pairNum++;
			} else {
				break;
			}
		}
		
		setScore(pairNumToPoint(pairNum));
	}
	
	public int getScore() {
		checkPair();
		return super.getScore();
	}
	
	/* Convert the pair number to the score */
	private int pairNumToPoint(int pairNum) {
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
