package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
public class Cribbage extends CardGame {
	static Cribbage cribbage;  // Provide access to singleton
	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	public enum Rank {
		// Order of cards is tied to card images
		ACE(1,1), KING(13,10), QUEEN(12,10), JACK(11,10), TEN(10,10), NINE(9,9), EIGHT(8,8), SEVEN(7,7), SIX(6,6), FIVE(5,5), FOUR(4,4), THREE(3,3), TWO(2,2);
		public final int order;
		public final int value;
		Rank(int order, int value) {
			this.order = order;
			this.value = value;
		}
	}

	static int cardValue(Card c) { return ((Cribbage.Rank) c.getRank()).value; }

	/*
	Canonical String representations of Suit, Rank, Card, and Hand
	*/
	String canonical(Suit s) { return s.toString().substring(0, 1); }

	String canonical(Rank r) {
		switch (r) {
			case ACE:case KING:case QUEEN:case JACK:case TEN:
				return r.toString().substring(0, 1);
			default:
				return String.valueOf(r.value);
		}
	}

    String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }

    String canonical(Hand h) {
		Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
		for (Card C: h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
		h1.sort(Hand.SortType.POINTPRIORITY, false);
		return "[" + h1.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
    }

	class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
		public int[] values(Enum suit) {  // Returns the value for each card in the suit
			return Stream.of(Rank.values()).mapToInt(r -> (((Rank) r).order-1)*(Suit.values().length)+suit.ordinal()).toArray();
		}
	}

	static Random random;

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }

	static boolean ANIMATE;

	void transfer(Card c, Hand h) {
		if (ANIMATE) {
			c.transfer(h, true);
		} else {
			c.removeFromHand(true);
			h.insert(c, true);
		}
  }
  
  private void dealingOut(Hand pack, Hand[] hands) {
	  for (int i = 0; i < nStartCards; i++) {
		  for (int j=0; j < nPlayers; j++) {
			  Card dealt = randomCard(pack);
			  dealt.setVerso(false);  // Show the face
			  transfer(dealt, hands[j]);
		  }
	  }
  }

	static int SEED;

	public static Card randomCard(Hand hand){
      int x = random.nextInt(hand.getNumberOfCards());
      return hand.get(x);
  }

  private final String version = "0.1";
  static public final int nPlayers = 2;
  public final int nStartCards = 6;
  public final int nDiscards = 2;
  private final int handWidth = 400;
  private final int cribWidth = 150;
  private final int segmentWidth = 180;
  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
  private final Location[] handLocations = {
			  new Location(360, 75),
			  new Location(360, 625)
	  };
  private final Location[] scoreLocations = {
			  new Location(590, 25),
			  new Location(590, 675)
	  };
  private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
			new Location(150, 350),
			new Location(400, 350),
			new Location(650, 350)
	};
  private final Location starterLocation = new Location(50, 625);
  private final Location cribLocation = new Location(700, 625);
  private final Location seedLocation = new Location(5, 25);
  // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
  private final Actor[] scoreActors = {null, null}; //, null, null };
  private final Location textLocation = new Location(350, 450);
  private final Hand[] hands = new Hand[nPlayers];
  private Hand starter;
  private Hand crib;

  public static void setStatus(String string) { cribbage.setStatusText(string); }

static private final IPlayer[] players = new IPlayer[nPlayers];
private final int[] scores = new int[nPlayers];

final Font normalFont = new Font("Serif", Font.BOLD, 24);
final Font bigFont = new Font("Serif", Font.BOLD, 36);
private static ArrayList<ArrayList<Card>> allCombination;

private void initScore() {
	 for (int i = 0; i < nPlayers; i++) {
		 scores[i] = 0;
		 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }

private void updateScore(int player) {
	removeActor(scoreActors[player]);
	scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private void deal(Hand pack, Hand[] hands) {
	for (int i = 0; i < nPlayers; i++) {
		hands[i] = new Hand(deck);
		// players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
		players[i].setId(i);
		players[i].startSegment(deck, hands[i]);
	}
	RowLayout[] layouts = new RowLayout[nPlayers];
	for (int i = 0; i < nPlayers; i++)
	{
		layouts[i] = new RowLayout(handLocations[i], handWidth);
		layouts[i].setRotationAngle(0);
		// layouts[i].setStepDelay(10);
		hands[i].setView(this, layouts[i]);
		hands[i].draw();
	}
	layouts[0].setStepDelay(0);

	dealingOut(pack, hands);
	for (int i = 0; i < nPlayers; i++) {
		hands[i].sort(Hand.SortType.POINTPRIORITY, true);
	}
	layouts[0].setStepDelay(0);
}

private void discardToCrib() {
	crib = new Hand(deck);
	RowLayout layout = new RowLayout(cribLocation, cribWidth);
	layout.setRotationAngle(0);
	crib.setView(this, layout);
	// crib.setTargetArea(cribTarget);
	crib.draw();
	for (IPlayer player: players) {
		for (int i = 0; i < nDiscards; i++) {
			transfer(player.discard(), crib);
		}
		crib.sort(Hand.SortType.POINTPRIORITY, true);
	}
}

private void starter(Hand pack) {
	starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
	RowLayout layout = new RowLayout(starterLocation, 0);
	layout.setRotationAngle(0);
	starter.setView(this, layout);
	starter.draw();
	Card dealt = randomCard(pack);
	dealt.setVerso(false);
	transfer(dealt, starter);
}

static int total(Hand hand) {
	int total = 0;
	for (Card c: hand.getCardList()) total += cardValue(c);
	return total;
}

class Segment {
		Hand segment;
		boolean go;
		int lastPlayer;
		boolean newSegment;

		void reset(final List<Hand> segments) {
			segment = new Hand(deck);
			segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
			segment.draw();
			go = false;        // No-one has said "go" yet
			lastPlayer = -1;   // No-one has played a card yet in this segment
			newSegment = false;  // Not ready for new segment yet
		}
}

private void play(PrintWriter pw) {
	final int thirtyone = 31;
	final int fifteen = 15;
	List<Hand> segments = new ArrayList<>();
	int currentPlayer = 0; // Player 1 is dealer
	Segment s = new Segment();
	s.reset(segments);
	while (!(players[0].emptyHand() && players[1].emptyHand())) {
		Card nextCard = players[currentPlayer].lay(thirtyone-total(s.segment));
		if (nextCard == null) {
			if (s.go) {
				// Another "go" after previous one with no intervening cards
				// lastPlayer gets 1 point for a "go"
				s.newSegment = true;
			} else {
				// currentPlayer says "go"
				s.go = true;
			}
			currentPlayer = (currentPlayer+1) % 2;
		} else {
			s.lastPlayer = currentPlayer; // last Player to play a card in this segment
			transfer(nextCard, s.segment);
			new PlayLog(currentPlayer,pw,deck,total(s.segment),nextCard).display();
			if (total(s.segment) == thirtyone) {
				scores[currentPlayer] += 2;
				new PlayScoreLog(currentPlayer,pw,deck,scores[currentPlayer],2,"thirtyone").display();
				s.newSegment = true;
				currentPlayer = (currentPlayer+1) % 2;
			} else {
				if ( checkisGo(hands,31-total(s.segment))) {
					scores[currentPlayer] += 1;
					new PlayScoreLog(currentPlayer,pw,deck,scores[currentPlayer],1,"go").display();
				}
				if ( total(s.segment) == fifteen) {
					scores[currentPlayer] += 2;
					new PlayScoreLog(currentPlayer,pw,deck,scores[currentPlayer],2,"fifteen").display();
				}
				if (!s.go) { // if it is "go" then same player gets another turn
					currentPlayer = (currentPlayer+1) % 2;
				}
			}

			int score = new StrategyFactory().getScoreStrategy("PlayRun", s.segment).getScore();
			if ( score >= 3 ) {
				scores[s.lastPlayer] += score;
				new PlayScoreLog(s.lastPlayer,pw,deck,scores[s.lastPlayer],score,"run").display();
			}
			score = new StrategyFactory().getScoreStrategy("PlayPair", s.segment).getScore();
			if ( score >= 2 ) {
				scores[s.lastPlayer] += score;
				new PlayScoreLog(s.lastPlayer,pw,deck,scores[s.lastPlayer],score,"pair").display();
			}
			updateScore(s.lastPlayer);

		}
		if (s.newSegment) {
			segments.add(s.segment);
			s.reset(segments);
		}
	}
}

boolean checkisGo(Hand[] hands, int limit) {
	boolean isGo = true;
	for ( int i=0; i<2; i++ ) {
		for (Card c: ((ArrayList<Card>) hands[i].getCardList().clone())) {  // Modify list, so need to iterate over clone
			if (Cribbage.cardValue(c) <= limit) {
				isGo = false;
				break;
			}
		}
	}
	return isGo;
}

void showHandsCrib(Hand nonDealer, Hand dealer, Hand cribShow, PrintWriter pw) {
	new ShowLog(0,pw,deck,starter,nonDealer).display();
	showScore(nonDealer,pw,0);
	new ShowLog(1,pw,deck,starter,dealer).display();
	showScore(dealer,pw,1);
	new ShowLog(1,pw,deck,starter,cribShow).display();
	showScore(cribShow,pw,1);
	
	updateScore(0);
	updateScore(1);
}


void showScore(Hand hand, PrintWriter pw, int playerid) {
	ArrayList<Hand> combination = new ArrayList<Hand>();
	IScoreStrategy showFifteen = new StrategyFactory().getScoreStrategy("ShowFifteen", hand,starter,deck);
	int score = showFifteen.getScore();
	combination = ((ShowStrategy)showFifteen).getCombination();
	new ShowScoreLog(playerid,pw,deck,scores[playerid],score,"fifteen",combination).display();
	for ( Hand cards: combination) {
		scores[playerid] += score;
	}
	IScoreStrategy showRun = new StrategyFactory().getScoreStrategy("ShowRun", hand,starter,deck);
	score = showRun.getScore();

	combination = ((ShowStrategy)showRun).getCombination();
	new ShowScoreLog(playerid,pw,deck,scores[playerid],score,"run",combination).display();
	for ( Hand cards: combination) {
		scores[playerid] += score;
	}
	
	IScoreStrategy showPair = new StrategyFactory().getScoreStrategy("ShowPair", hand,starter,deck);
	score = showPair.getScore();
	combination = ((ShowStrategy)showPair).getCombination();
	new ShowScoreLog(playerid,pw,deck,scores[playerid],score,"pair",combination).display();
	for ( Hand cards: combination) {
		scores[playerid] += (score*(score-1));
	}
	
	IScoreStrategy showFlush = new StrategyFactory().getScoreStrategy("ShowFlush", hand,starter,deck);
	score = showFlush.getScore();
	combination = ((ShowStrategy)showFlush).getCombination();
	new ShowScoreLog(playerid,pw,deck,scores[playerid],score,"flush",combination).display();
	for ( Hand cards: combination) {
		scores[playerid] += score;
	}
	IScoreStrategy showJack = new StrategyFactory().getScoreStrategy("ShowJack", hand,starter,deck);
	score = showJack.getScore();
	combination = ((ShowStrategy)showJack).getCombination();
	new ShowScoreLog(playerid,pw,deck,scores[playerid],score,"jack",combination).display();
	for ( Hand cards: combination) {
		scores[playerid] += score;
	}
}

int maxCardDiff(ArrayList<Card> combination) {
	int max = 1, min = 13;
	for (Card card: combination) {
		if (((Cribbage.Rank) card.getRank()).order > max) {
			max = ((Cribbage.Rank) card.getRank()).order;
		}
		if (((Cribbage.Rank) card.getRank()).order < min) {
			min = ((Cribbage.Rank) card.getRank()).order;
		}
	}
	return max - min;
}

  public Cribbage()
  {
    super(850, 700, 30);
    cribbage = this;
    setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    initScore();

	  Hand pack = deck.toHand(false);
	  RowLayout layout = new RowLayout(starterLocation, 0);
	  layout.setRotationAngle(0);
	  pack.setView(this, layout);
	  pack.setVerso(true);
	  pack.draw();
	  addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

	  /* Play the round */
	  try (PrintWriter pw = new PrintWriter(new FileWriter("cribbage.log"))) {
		  deal(pack, hands);
		  ArrayList<Card> nonDealer = new ArrayList<>(hands[0].getCardList());
		  ArrayList<Card> dealer = new ArrayList<>(hands[1].getCardList());
		 

		  discardToCrib();
		  
		  Hand nonDealerClone = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
		  Hand dealerClone = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
		  
		  for ( Card card: nonDealer) {
			  nonDealerClone.insert(card.getSuit(), card.getRank(), false);
		  }
		  for ( Card card: dealer) {
			  dealerClone.insert(card.getSuit(), card.getRank(), false);
		  }

		  nonDealer.removeAll(hands[0].getCardList());
		  dealer.removeAll(hands[1].getCardList());
		  Hand nonDealerDiscard = new Hand(deck);
		  Hand dealerDiscard = new Hand(deck);
		  for (Card card: nonDealer) {
			  nonDealerDiscard.insert(card.getSuit(), card.getRank(), false);
		  }
		  for (Card card: dealer) {
			  dealerDiscard.insert(card.getSuit(), card.getRank(), false);
		  }

		  starter(pack);
		  StartLog.seed = SEED;
		  new StartLog(pw,deck,nonDealerClone,dealerClone,nonDealerDiscard,dealerDiscard,starter).display();
		  ArrayList<Hand> starterHand = new ArrayList<Hand>();
		  starterHand.add(starter);
		  if (((Cribbage.Rank) starter.getFirst().getRank()).order == 11) {
			  new ShowScoreLog(1,pw,deck,scores[1],1,"starter",starterHand).display();
			  updateScore(1);
		  }
		  
		  nonDealer = new ArrayList<>(players[0].hand.getCardList());
		  dealer = new ArrayList<>(players[1].hand.getCardList());

		  play(pw);
		  for ( Card card: nonDealer ) {
			  hands[0].insert(card.getSuit(), card.getRank(), false);
		  }
		  for ( Card card: dealer ) {
			  hands[1].insert(card.getSuit(), card.getRank(), false);
		  }
		  showHandsCrib(hands[0],hands[1],crib,pw);
	  } catch (IOException e) {
		  e.printStackTrace();
	  }

    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText("Game over.");
    refresh();
  }

  public static void main(String[] args)
		  throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
		  	InstantiationException, IllegalAccessException {
	  /* Handle Properties */
	  Properties cribbageProperties = new Properties();
	  // Default properties
	  cribbageProperties.setProperty("Animate", "true");
	  cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
	  cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

	  // Read properties
	  try (FileReader inStream = new FileReader("cribbage.properties")) {
		  cribbageProperties.load(inStream);
	  }

	  // Control Graphics
	  ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

	  // Control Randomisation
	  /* Read the first argument and save it as a seed if it exists */
	  if (args.length > 0 ) { // Use arg seed - overrides property
		  SEED = Integer.parseInt(args[0]);
	  } else { // No arg
		  String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
		  if (seedProp != null) { // Use property seed
			  SEED = Integer.parseInt(seedProp);
		  } else { // and no property
			  SEED = new Random().nextInt(); // so randomise
		  }
	  }
	  random = new Random(SEED);
	  
	  // Control Player Types
	  Class<?> clazz;
	  clazz = Class.forName(cribbageProperties.getProperty("Player0"));
	  players[0] = (IPlayer) clazz.getConstructor().newInstance();
	  StartLog.p0Type = clazz.getName();
	  clazz = Class.forName(cribbageProperties.getProperty("Player1"));
	  players[1] = (IPlayer) clazz.getConstructor().newInstance();
	  StartLog.p1Type = clazz.getName();

	  // End properties

	  new Cribbage();
  }
}
