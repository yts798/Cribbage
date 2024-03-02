package cribbage;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class ShowPairStrategy extends ShowStrategy implements IScoreStrategy{
	
	
	public ShowPairStrategy(Hand hand, Hand starter, Deck deck) {
		super(hand, starter, deck);
	}

	@Override
	public int getScore() {
		int pairNum = 0;
		Hand hand = super.getHand();
		Hand starter = super.getStarter();
		hand.insert(starter.getFirst().getSuit(), starter.getFirst().getRank(), false);
		/* Check if the hand contains a pair4 */
		Hand[] hands = hand.extractQuads();
		if ( hands.length == 0 ) {
			/* Check if the hand contains a pair3 */
			hands = hand.extractTrips();
			if ( hands.length == 0 ) {
				/* Check if the hand contains a pair2 */
				hands = hand.extractPairs();
				if ( hands.length != 0 ) {
					pairNum = 2;
				}
			} else {
				pairNum = 3;
			}
		} else {
			pairNum = 4;
		}
		if ( pairNum != 0 ) {
			for (Hand cards: hands) {
				super.getCombination().add(cards);
			}
		}
		return pairNum;
	}
}
