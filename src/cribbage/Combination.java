package cribbage;

import java.util.ArrayList;
import java.util.Arrays;

import ch.aplu.jcardgame.Card;

public class Combination {
	private ArrayList<ArrayList<Card>> allCombination = new ArrayList<>();
	private Card[] segment;
	private int length;
	
	public Combination(Card[] segment, int length) {
		this.segment = segment;
		this.length = length;
	}
	
	/* Find the combinations in a list of card */
	public void combinationSelect(Card[] dataList, int n) {
	      combinationSelect(dataList, 0, new Card[n], 0);
	  }
	  
	public void combinationSelect(Card[] dataList, int dataIndex, Card[] resultList, int resultIndex) {
	      int resultLen = resultList.length;
	      int resultCount = resultIndex + 1;
	      if (resultCount > resultLen) {
	    	  
	    	  allCombination.add(new ArrayList<>(Arrays.asList(resultList)));
	          return;
	      }
	      for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
	          resultList[resultIndex] = dataList[i];
	          combinationSelect(dataList, i + 1, resultList, resultIndex + 1);
	      }
	  }
	
	public ArrayList<ArrayList<Card>> getAllCombination() {
		combinationSelect(segment, length);
		return allCombination;
	}
}
