package cellular;

// Change class name
public class BasicCell extends Cell
{
	// Add a final variable for the maximum state value.
	// Change class name here for sure
	public BasicCell()
	{
		super(0);	
	}
	// Change class name here DEFINITELY
	public BasicCell(int state)
	{
		super(state);	
	}
	// Change class name here too
	public BasicCell(String state)
	{
		// Define a text file syntax for the grid cells here, and the program
		// should soon create cells from that file into a new Grid.
		super(0);
	}
	/**
	 *  Computes the nextState of the Cell from the rule for this type of Cell.
	 */
    public void computeNextState(Cell[] neighbors)
    {
    	// Here's where the fun stuff happens!
    	// Neighbors is an array of the adjacent cells, like this:
    	// 0 1 2
    	// 3 4 5
    	// 6 7 8
    	// Where 4 is the cell that's currently here.
    }
}