package Connect4;

public interface GameBoard {

	public void displayBoard(); // init the board
	public void resetBoard(); // reset board to empty
	
	public boolean isEmpty(); // is board empty?
	public boolean isFull(); // is board full?
	
}
