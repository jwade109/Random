package cellular;

public class AntCell extends Cell
{
	/**
	 *  Creates a new blank AntCell.
	 */
	public AntCell()
	{
		super(0);	
	}
	/**
	 *  Creates a new AntCell of the specified state.
	 */
	public AntCell(int state)
	{
		super(state);	
	}
	/**
	 *  Creates a new AntCell from a String.
	 */
	public AntCell(String state)
	{
		// Implemented later
	}
	/**
	 *  Finds the next state of the Ant with the rules of Langton's Ant.
	 *  Although it shouldn't break with multiple ants, it doesn't deal
	 *  with ants encountering eachother.
	 */
	public void computeNextState(Cell[] neighbors)
	{
		// Revert to a blank cell, unless the next step succeeds.
		neighbors[4].setNextState(neighbors[4].getState() / 5);
		// If the ant is coming into the cell, become the ant.
		int[] vals = new int[4];
		vals[0] = 1;
		vals[1] = 3;
		vals[2] = 7;
		vals[3] = 5;
		for (int i = 0; i < 4; i++)
		{
			if (neighbors[vals[i]] != null
				&& (neighbors[vals[i]].getState() == i + 1
				|| neighbors[vals[i]].getState() == i + 6))
			{
				if (neighbors[4].getState() / 5 == 0)
				{
					if (i == 0)
					{
						neighbors[4].setNextState(4);
					}
					else
					{
						neighbors[4].setNextState(i);
					}
				}
				else
				{
					if (i == 3)
					{
						neighbors[4].setNextState(6);
					}
					else
					{
						neighbors[4].setNextState(9 - i);
					}
				}
				int nearState = neighbors[vals[i]].getState();
				neighbors[vals[i]].setNextState(1 - nearState / 5);
			}
		}
		/* */
	}
}