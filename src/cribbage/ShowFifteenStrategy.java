package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class ShowFifteenStrategy extends ShowStrategy implements IScoreStrategy{
	private Combination combination;
	private ArrayList<ArrayList<Card>> allCombination = new ArrayList<>();
	
	public ShowFifteenStrategy(Hand hand, Hand starter, Deck deck) {
		super(hand, starter, deck);
	}
	
	/* Find all combinations in the hand */
	private void getallCombination() {
		Card[] handArray;
		Hand hand = super.getHand();
		Hand starter = super.getStarter();
		ArrayList<Card> cards = new ArrayList<Card>(hand.getCardList());
		ArrayList<Card> handWithStarter = new ArrayList<>(cards);
		handWithStarter.add(starter.getFirst());
		handArray = (Card[]) handWithStarter.toArray(new Card[handWithStarter.size()]);
		for (int i = 2; i <= 5; i++) {
			combination = new Combination(handArray,i);
			for ( ArrayList<Card> combine: combination.getAllCombination() ) {
				allCombination.add(combine);
			}
		}
	}
	@Override
	public int getScore() {
		boolean scoreValid = false;
		int score = 0;
		getallCombination();
		/* Check the total each combination */
		for (ArrayList<Card> combination: allCombination) {
			Hand seg = new Hand(super.getDeck());
			for ( Card card: combination ) {
				seg.insert(card, false);
			}
			/* If the total is 15, store the combination */
			if (Cribbage.total(seg) == 15) {
				scoreValid = true;
				super.getCombination().add(seg);
			}
		}
		if ( scoreValid ) {
			score = 2;
		}
		return score;
	}

}
