package cribbage;

import java.io.PrintWriter;
import ch.aplu.jcardgame.*;

public class StartLog extends Log {
	public static int seed;
	public static String p0Type;
	public static String p1Type;
	Hand p0Deal;
	Hand p1Deal;
	Hand p0Discard;
	Hand p1Discard;
	Hand starter;
	
	
	public StartLog(PrintWriter pw, Deck deck, Hand p0Deal, Hand p1Deal, Hand p0Discard, Hand p1Discard, Hand starter) {
		super(pw, deck);
		this.p0Deal = p0Deal;
		this.p1Deal = p1Deal;
		this.p0Discard = p0Discard;
		this.p1Discard = p1Discard;
		this.starter = starter;
	}
	
	/* Log all the statements at the beginning of a game */
	public void display() {	
		getPw().println("seed,"+seed);
		getPw().println(p0Type+",P0");
		getPw().println(p1Type+",P1");
		getPw().println("deal,P0,"+canonical(p0Deal));
		getPw().println("deal,P1,"+canonical(p1Deal));
		getPw().println("discard,P0,"+canonical(p0Discard));
		getPw().println("discard,P1,"+canonical(p1Discard));
		getPw().println("starter,"+canonical(starter.getFirst()));
	}
}
