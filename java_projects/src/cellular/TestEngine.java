package cellular;

public class TestEngine extends Engine
{
	public TestEngine()
	{
		super(11);
	}
	/**
	 *  Computes the nextState of the Cell from the rule for this type.
	 */
	@Override
    public void computeNextState(Cell[] neighbors)
    {
    	neighbors[4].setNextState((neighbors[4].getState() + 1) % 12);
    }
}