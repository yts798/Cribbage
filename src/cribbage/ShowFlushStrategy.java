package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class ShowFlushStrategy extends ShowStrategy implements IScoreStrategy{
	
	
	public ShowFlushStrategy(Hand hand, Hand starter, Deck deck) {
		super(hand, starter, deck);
	}

	@Override
	public int getScore() {
		Hand hand = super.getHand();
		Hand starter = super.getStarter();
		Hand combine = new Hand(super.getDeck());
		ArrayList<Card> cards = new ArrayList<Card>(hand.getCardList());
		for ( Card card: hand.getCardList() ) {
			combine.insert(card, false);
		}
		int suit = cards.get(0).getSuitId();
		/* Check whether a flush exists in the hand */
		for (Card card: cards) {
			if (card.getSuitId() != suit) {
				return 0;
			}
		}
		
		/* If there exists a flush, check whether the starter has the same suit */
		if (starter.getFirst().getSuitId() == suit) {
			combine.insert(starter.getFirst().getSuit(), starter.getFirst().getRank(), false);
			super.getCombination().add(combine);
			return 5;
			
		}
		super.getCombination().add(combine);
		return 4;
	}

}
