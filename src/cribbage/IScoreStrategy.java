package cribbage;

import ch.aplu.jcardgame.*;

public interface IScoreStrategy {
	
	/* Get the score of the current segment */
	public abstract int getScore();

}
