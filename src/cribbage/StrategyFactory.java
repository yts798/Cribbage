package cribbage;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

/* A factory that is used to create different kinds of strategies */
public class StrategyFactory {
	private IScoreStrategy scoreStrategy = null;
	
	public IScoreStrategy getScoreStrategy(String type, Hand segment) {
		if (type.equals("PlayRun")) {
			scoreStrategy = (IScoreStrategy) new PlayRunStrategy(segment);
		} else if (type.equals("PlayPair")){
			scoreStrategy = (IScoreStrategy) new PlayPairStrategy(segment);
		}
		return scoreStrategy;
	}
	
	public IScoreStrategy getScoreStrategy(String type, Hand hand, Hand starter, Deck deck) {
		if ( type.equals("ShowFifteen")) {
			scoreStrategy = (IScoreStrategy) new ShowFifteenStrategy(hand,starter,deck);
		} else if ( type.equals("ShowFlush")) {
			scoreStrategy = (IScoreStrategy) new ShowFlushStrategy(hand,starter,deck);
		} else if ( type.equals("ShowJack") ) {
			scoreStrategy = (IScoreStrategy) new ShowJackStrategy(hand,starter,deck);
		} else if ( type.equals("ShowPair") ) {
			scoreStrategy = (IScoreStrategy) new ShowPairStrategy(hand,starter,deck);
		} else if ( type.equals("ShowRun") ) {
			scoreStrategy = (IScoreStrategy) new ShowRunStrategy(hand,starter,deck);
		}
		return scoreStrategy;
	}
}
