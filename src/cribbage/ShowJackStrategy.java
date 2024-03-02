package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class ShowJackStrategy extends ShowStrategy implements IScoreStrategy {
	
	
	public ShowJackStrategy(Hand hand, Hand starter, Deck deck) {
		super(hand, starter, deck);
	}

	@Override
	public int getScore() {
		Hand hand = super.getHand();
		Hand starter = super.getStarter();
		int starterSuit = starter.getFirst().getSuitId();
		ArrayList<Card> cards = new ArrayList<Card>(hand.getCardList());
		for (Card card: cards) {
			/* There exists a jack that has the same suit with the starter */
			if (card.getSuitId() == starterSuit && ((Cribbage.Rank) card.getRank()).order == 11) {
				Hand jack = new Hand(super.getDeck());
				jack.insert(card, false);
				super.getCombination().add(jack);
				return 1;
			}
		}
		return 0;
	}
}
