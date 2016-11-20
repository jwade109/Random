package dataideas;

/**
 * Represents the Grid nodes where the edges in a graph meet.
 * These nodes represent a versatile kind of Node that can have
 * any number of neighbors, and can have client code access them
 * at any point. GridNode's can be replaced with new GridNodes or 
 * gotten rid of as necessary, and they serve mainly as helper
 * objects.
 * 
 * @author William McDermott
 * @version 2016.11.20
 * 
 * @param T     The type of data this GridNode holds.
 */
public class GridNode<T>
{
    /**
     * The data contained in this GridNode.
     */
    private T data;
    
    /**
     * The neighborhood of a GridNode, the cells directly adjacent to it.
     */
    private GridNode<T>[] neighbors;
    
    /**
     * Creates a new GridNode 
     */
    

    /**
     * Creates a new GridNode in n dimensions.
     * @param data      The object to be held by this node.
     * @param neighbors The neighbors to this node.
     * @throws IllegalArgumentException
     */
    public GridNode(T data, GridNode<T>[] neighbors)
    {
        this.data = data;
        this.neighbors = neighbors;
    }
    
    /**
     * Gets the data of this GridNode.
     * @return  This GridNode's data.
     */
    public T getData()
    {
        return data;
    }

    /**
     * Sets the data of this GridNode.
     * @param   The new data for the GridNode to hold
     */
    public void setData(T data)
    {
        this.data = data;
    }
    
    /**
     * Gets the neighbors of this GridNode.
     * @return  The neighborhood of this GridNode.
     */
    public GridNode<T>[] getNeighbors()
    {
        return neighbors;
    }
    
    /**
     * Gets a neighbor of this GridNode in a specific direction.
     * @param vector    The direction to add the GridNode.
     * @return          One neighbor of this GridNode.
     */
    public GridNode<T> getNeighbor(int[] vector)
    {
        return neighbors[this.vectorToPosition(vector)];
    }
    
    /**
     * Sets the neighborhood of this GridNode.
     * @param neighbors     The new neighbors of this GridNode.
     * @throws IllegalArgumentException
     */
    public void setNeighborhood(GridNode<T>[] neighbors)
    {
        if (neighbors == null
            || this.neighbors.length != neighbors.length)
        {
            throw new IllegalArgumentException();
        }
        this.neighbors = neighbors;
    }
    
    /**
     * Sets a specific neighbor based on a basis vector for direction.
     * @param neighbor  A GridNode to set as a neighbor.
     * @param vector    The direction vector to add the neighbor
     * @throws IllegalArgumentException
     */
    public void setNeighbor(GridNode<T> neighbor, int[] vector)
    {
        if (neighbor == null
            || vector.length != neighbors.length)
        {
            throw new IllegalArgumentException();
        }
        neighbors[this.vectorToPosition(vector)] = neighbor;
    }
    
    /**
     * Turns a vector into a value in the array.
     * @param vector    A vector to process into a position.
     * @return          The position to return.
     * @throws IllegalArgumentException
     */
    private int vectorToPosition(int[] vector)
    {
        if (vector == null)
        {
            throw new IllegalArgumentException();
        }
        int position = 0;
        for (int i = 0; i < vector.length; i++)
        {
            if (vector[i] == 1)
            {
                position += (int) Math.pow(2, i) + 0.01;
            }
        }
        return position;
    }
}