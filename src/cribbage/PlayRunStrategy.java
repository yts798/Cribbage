package cribbage;

import java.util.ArrayList;
import java.util.Collections;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class PlayRunStrategy extends PlayStrategy {
	
	public PlayRunStrategy(Hand hand) {
		super(hand);
	}
	
	/* Check whether the segment has a run */
	public void checkRun() {
		ArrayList<Card> cards = new ArrayList<Card>(super.getSegment().getCardList());
		ArrayList<Integer> currOrders = new ArrayList<>();
		int longestRun = 0;
		  
		Collections.reverse(cards);
		
		/* Find the longest run */
		for (Card card: cards) {
			int currOrder = ((Cribbage.Rank) card.getRank()).order;
			if (currOrders.contains(currOrder)) {
				break;
			}
			currOrders.add(currOrder);
			int diff = Collections.max(currOrders) - Collections.min(currOrders);
			if (diff == (currOrders.size() - 1)) {
				longestRun = currOrders.size();
			}
		}
		if (longestRun >= 3) {
			setScore(longestRun);
		} else {
			setScore(0);
		}
	}
	
	public int getScore() {
		checkRun();
		return super.getScore();
	}
}
