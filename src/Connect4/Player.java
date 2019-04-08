package Connect4;

public class Player implements Comparable<Player> {

	private String name, marker;
	private int numGames, numWins, numLosses;
	
	// default constructor
	public Player() {
		name = "";
		marker = "";
		numGames = 0;
		numWins = 0;
		numLosses = 0;
	}
	
	// overloaded constructor
	// set player name and desired marker
	public Player(String name, String marker) {
		this();
		this.name = name;
		this.marker = marker;
	}
	
	// START : MODIFY STATS OF GAMES AND WINNINGS
	
	// player gets a win
	public void addWin() {
		numGames++;
		numWins++;
	}
	
	// player gets a loss
	public void addLoss() {
		numGames++;
		numLosses++;
	}
	
	// players tie
	public void addDraw() {
		numGames++;
	}
	// END
	
	// START GETTERS
	public String getName() {
		return name;
	}
	
	public String getMarker() {
		return marker;
	}
	
	public int getNumGames() {
		return numGames;
	}
	
	public int getNumWins() {
		return numWins;
	}
	
	public int getNumLosses() {
		return numLosses;
	}
	
	// END GETTERS
	
	// equals()
	// Compare 2 objects if they are equal to each other
	@Override
	public boolean equals(Object o) {
		if(o instanceof Player) {
			Player op = (Player) o;
			if((this.name.equalsIgnoreCase(op.name))
			&& (this.numGames == op.numGames)
			&& (this.numWins == op.numWins)
			&& (this.numLosses == op.numLosses)) {
				return true;
			}
		}
		return false;
	}

	// toString()
	// Return string of all vars in class
	@Override
	public String toString() {
		return "Player [name=" + name + 
					 ", numGames=" + numGames + 
					 ", numWins=" + numWins + 
					 ", numLosses=" + numLosses + "]";
	}
	
	// compareTo()
	@Override
	public int compareTo(Player p) {
		if(this.numWins > p.numWins) {
			return 1;
		} else if(this.numWins < p.numWins) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
