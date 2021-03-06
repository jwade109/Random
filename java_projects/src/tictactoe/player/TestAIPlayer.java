package tictactoe.player;

import tictactoe.game.Coordinate;
import tictactoe.game.TicTacGrow;

/**
 * Basic AI that moves randomly with wild abandon,
 * so much so it refuses to check if the moves are even legal.
 * 
 * @author William McDermott
 * @version 2016.12.26
 */
public class TestAIPlayer implements Player
{
    /**
     * The name of this TestAIPlayer.
     */
    public String name;
    
    /**
     * Makes a new TestAIPlayer with the default name.
     */
    public TestAIPlayer()
    {
        this("FiteMe");
    }
    
    /**
     * Makes a new TestAIPlayer.
     * @param name  The name of this object.
     */
    public TestAIPlayer(String name)
    {
        this.name = name;
    }
    
    /**
     * Tests whether this is an AI or not.
     * @return  True, because this is an AI.
     */
    @Override
    public boolean isAI()
    {
        return true;
    }

    /**
     * Gets the name for this AI.
     * @return The name initialized in the constructor.
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Move in an arbitrary location, which will only be valid if it passes
     * the test of a legal move in TicTacGrow.
     * Improper moves will end the simulation with an OccupiedSpotException,
     * or IllegalMoveException (when out of the scope of the current move).
     * 
     * @param board The board to play on, which is a temporary object.
     * 
     * @return A coordinate of where to move in the grid at large.
     * It MUST have length equal to the order of the board, and it must be
     * only a suffix of lastMove, which will be checked by the board.
     */
    @Override
    public Coordinate move(TicTacGrow gameBoard)
    {
        int[] dummyMove = new int[gameBoard.getOrder()];
        for (int i = 0; i < gameBoard.getOrder(); i++)
        {
            dummyMove[i] = (int) (9 * Math.random()); // - 0.0000001);
        }
        return new Coordinate(dummyMove); 
    }
}
