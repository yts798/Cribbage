package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class ShowRunStrategy extends ShowStrategy implements IScoreStrategy{
	
	
	
	public ShowRunStrategy(Hand hand, Hand starter, Deck deck) {
		super(hand, starter, deck);
	}


	@Override
	public int getScore() {
		int run = 0;
		boolean valid = true;
		Hand[] hands = null;
		Hand hand = super.getHand();
		Hand starter = super.getStarter();
		hand.insert(starter.getFirst().getSuit(), starter.getFirst().getRank(), false);
		/* Check whether run exists in the hand */
		for ( int i=5; i>=3; i-- ) {
			hands = hand.extractSequences(i);
			if ( hands.length != 0 ) {
				run = i;
				break;
			}
		}
		/* If there exists a run, store it */
		if ( run > 0 ) {
			for (Hand cards: hands) {
				valid = checkifAQK(cards);
				if ( valid ) {
					super.getCombination().add(cards);
				}
			}
		}
		return run;
	}
	
	
	public boolean checkifAQK(Hand cards) {
		ArrayList<Card> hand = cards.getCardList();
		int order = 0, count = 0;
		boolean valid = true;
		for ( Card card: hand ) {
			order = ((Cribbage.Rank) card.getRank()).order;
			if ( order <= 1 || order >= 12 ) {
				count ++;
			}
		}
		if ( count == 3 ) {
			valid = false;
		}
		return valid;
	}
	
}
