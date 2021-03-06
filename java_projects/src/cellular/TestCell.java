package cellular;

public class TestCell extends Cell
{
	public final int MAX_STATE = 11;
	public TestCell()
	{
		super(0);	
	}
	public TestCell(String state)
	{
		// Just to get it to compile, this class isn't supposed to do much.	
	}
	/**
	 *  Computes the nextState of the Cell from the rule for this type of Cell.
	 */
    public void computeNextState(Cell[] neighbors)
    {
    	setNextState((getState() + 1) % 12);
    }
}