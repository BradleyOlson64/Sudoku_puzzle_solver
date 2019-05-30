package search;
import csp.BinaryDiffConstraint;
import csp.Constraints;
import csp.Variable;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * @author Bradley Olson
 * This class creates a node object to be used with backtracking search.
 */
public class Node {
	private Variable[][] grid;
	private Node predecessor;
	public static final Constraints CONSTRAINTS = new Constraints();
	//Creating globals
	
	/**
	 * This constructor makes a node based directly on another node. It is used to clone a given node.
	 */
	public Node(Node another) {
		this.predecessor = another.predecessor;
		Variable[][] newGrid = new Variable[9][9];
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				newGrid[i][j] = new Variable(another.getGrid()[i][j]);
			}
		}
		this.grid = newGrid;
	}
	
	/**
	 * This constructor takes in a sudoku grid and creates a new node based on that grid.
	 * @param grid
	 */
 	public Node(int[][] grid) {
 		// creating new empty grid
		this.grid = new Variable[9][9];
		// filling values to match given grid
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				Variable newVar;
				if(grid[i][j] == 0) {
					newVar = new Variable();
				}
				else {
					newVar = new Variable(grid[i][j]);
				}
				this.grid[i][j] = newVar;
			}
		}
		// revising domains to reflect hard values
		this.grid = reviseDomains(this.grid);
		
		this.predecessor = null;
	}
	
	/**
	 * This constructor takes in a parent node and parameters for inserting a new value, making a new node. 
	 * @param predecessor
	 * @param newSelectionRow
	 * @param newSelectionCol
	 * @param newSelectionNum
	 */
	public Node(Node predecessor, int newSelectionRow, int newSelectionCol, int newSelectionNum) {
		// Creating copy of parent grid
		this.predecessor = predecessor;
		Variable[][] newGrid = new Variable[9][9];
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				newGrid[i][j] = new Variable(predecessor.getGrid()[i][j]);
			}
		}
		
		// Filling in new value
		Variable newVar = new Variable(newSelectionNum);
		newGrid[newSelectionRow][newSelectionCol] = newVar;
		this.grid = newGrid;
		this.grid = reviseDomains(this.grid);
	}
	
	/**
	 * Revises the domains of non filled slots in the given grid.
	 * @param grid
	 */
	private Variable[][] reviseDomains(Variable[][] newGrid){
		//Looping through each position of potential filled slot in grid
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				// Looping through constraints for that position and value
				if(newGrid[i][j].getValue() != -1) {
					for(BinaryDiffConstraint constr: CONSTRAINTS.cons) {
						if(i==constr.row1 && j== constr.col1) {
								// reduce domain
								newGrid[constr.row2][constr.col2].removeOne(newGrid[i][j].getValue()); 
						}
					}
				}
			}
		}
		return newGrid;
	}
	
	/**
	 * This method returns the current grid state.
	 * @return
	 */
	public Variable[][] getGrid(){
		return grid;
	}
	
	/**
	 * This method returns a node's predecessor or null if the current node is the root node.
	 * @return
	 */
	public Node getPredecessor() {
		return predecessor;
	}
	
	/**
	 * This method returns the string view of the current grid state.
	 */
	@Override
	public String toString() {
		String returnString = "";
		for(int i=0;i<9;i++) {
			if(i!=0) returnString = returnString + "\n";
			for(int j=0;j<9;j++) {
				if(grid[i][j].getDomain().size() == 1) {
					returnString = returnString + grid[i][j].getValue() + " ";
				}
				else returnString = returnString + 0 + " ";
			}
		}
		returnString += "\n";
		return returnString;
	}
	
	/**
	 * This method determines a hash code for this node based on its grid.
	 */
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + Arrays.deepHashCode(grid);
	    return result;
	}
	
	/**
	 * This method determines whether the grid states of two nodes are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node) {
			Node theObj = (Node) obj;
			
			for(int i= 0;i<9;i++) {
				if(!Arrays.equals(this.grid[i], theObj.grid[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether the board is filled to a goal state. The constraint 
	 * handling occurs at getSuccessors.
	 * @return
	 */
	public boolean isGoal() {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(grid[i][j].getValue() == -1) {
					return false;
				}
			}
		}
//		// Checking to see if constraints are met for this selection
//		Boolean isValid = true;
//		for(BinaryDiffConstraint constr: CONSTRAINTS.cons) {
//			if(i==constr.row1 && j== constr.row2) {
//				if(k == grid[constr.row2][constr.col2].getValue()) {
//					isValid = false;
//				}
//			}
//		}
		return true;
	}
	
	/**
	 * Returns the list of valid successors.
	 * @return
	 */
	public ArrayList<Node> getSuccessors(){
		//Initializing return list
		ArrayList<Node> successors = new ArrayList<Node>();
		// looping through all positions in grid
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				// checking to see that position is empty
				if(grid[i][j].getValue() == -1) {
					// looping through domain values
					if(grid[i][j].getDomain().size() > 0) {
						for(int k: grid[i][j].getDomain()) {
								Node newNode = new Node(this,i,j,k);
								if(isValid(newNode)) {
									successors.add(newNode);
								}
						}
					}
				}
			}
		}
		return successors;
	}
	
	/**
	 * Returns whether the set of selected values obeys all relevant constraints.
	 * @param current
	 * @return
	 */
	private static Boolean isValid(Node current) {
		for(BinaryDiffConstraint cons : CONSTRAINTS.cons) {
			if(current.getGrid()[cons.row1][cons.col1].getValue() > 0) {
				if(current.getGrid()[cons.row1][cons.col1].getValue() == current.getGrid()[cons.row2][cons.col2].getValue() ) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		int[][] theGrid = new int[9][9];
		theGrid[0][0] = 1;
		Node test = new Node(theGrid);		
//		Node test2 = new Node(test,0,1,2);
		ArrayList<Node> successors = test.getSuccessors();
		System.out.println(test.toString());
		System.out.println(test.getGrid()[0][2].getDomain().toString());
//		System.out.println(test2.toString());
		System.out.println(successors.size());
		System.out.println(successors.get(670).toString());
	}
	

	
}
