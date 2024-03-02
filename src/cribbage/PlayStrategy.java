package cribbage;

import ch.aplu.jcardgame.Hand;

public abstract class PlayStrategy implements IScoreStrategy {
	private Hand segment;
	private int score;
	
	public PlayStrategy(Hand hand) {
		this.segment = hand;
	}
	
	public Hand getSegment() {
		return segment;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
}
