package search;
import java.util.ArrayList;
import java.util.LinkedList;
import csp.BinaryDiffConstraint;
import csp.Variable;


/**
 * Solves a sudoku puzzle 
 */
public class Solver {

	/**
	 * This method is the overall search algorithm. It should attempt to solve using AC3 and otherwise
	 * resort to backtracking search.
	 * @param original
	 * @return
	 */
	public static int[][] solve(int[][] original){
		// Generating initial node description of problem
		Node initial = new Node(original);
		// AC3 first
		AC3(initial);
		if(initial.isGoal()) return intify(initial.getGrid());
		// End with backtracking
		return backTrack(initial);
	}
	
	/**
	 * This method runs a back tracking search using the least remaining values heuristic and 
	 * using AC3 for the inference step.
	 * @param current
	 * @return
	 */
	private static int[][] backTrack(Node current){
		// returning if complete
		if(current.isGoal()) return intify(current.getGrid());
		// Selecting unassigned variable via minimum remaining values
		int bestVal = 10;
		int besti = 10;
		int bestj = 10;
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				if(current.getGrid()[i][j].getDomain().size() < bestVal && current.getGrid()[i][j].getDomain().size() > 1) {
					bestVal = current.getGrid()[i][j].getDomain().size();
					besti = i;
					bestj = j;
				}
			}
		}
		// Looping over available choices
		for(int value : current.getGrid()[besti][bestj].getDomain()) {
			// Finding new node to simulate assignment of value
			ArrayList<Node> children = current.getSuccessors();
			Node chosen = new Node(new int[9][9]);
			Boolean foundChild = false;
			for(Node child: children) {
				if(child.getGrid()[besti][bestj].getValue() == value) {
					chosen = child;
					foundChild = true;
				}
			}
			if(foundChild == true) {
				// Adding inference
				// Must create deep clone of Node
				Node chosenClone = new Node(chosen);
				Boolean AC3Outcome = AC3(chosenClone);
				if(AC3Outcome == true) {
					AC3(chosen);
					int[][] result = backTrack(chosen);
					if(result != null) {
						return result;
					}
				}
//				int[][] result = backTrack(chosen);
//				if(result != null) {
//					return result;
//				}
			}
		}
		return null;
	}
	
	/**
	 * This method runs AC3 on the current node. The node is either made arc consistent, returning true,
	 * or is found to be unsolvable, returning false.
	 * @param current
	 * @return
	 */
	private static Boolean AC3(Node current) {
		// Instantiating and filling queue
		LinkedList<BinaryDiffConstraint> arcQueue = new LinkedList<>();
		for(BinaryDiffConstraint arc: Node.CONSTRAINTS.cons) {
			// Adds to tail. Use arcQueue.remove() to remove from head
			arcQueue.add(arc);
		}
		// Main while loop
		while(arcQueue.size() > 0) {
			// removing and storing first element in queue
			BinaryDiffConstraint checking = arcQueue.remove();
			// Skipping early if domain is already 1
			
			// Checking to see if we revise
			if(revise(current,checking)) {
				if(current.getGrid()[checking.row1][checking.col1].getDomain().size() <= 0) return false;
				for(BinaryDiffConstraint add : neighbors(checking.row1,checking.col1,checking.row2, checking.col2)) {
					arcQueue.add(add);
				}
			}
		}
		return true;
	}
	
	private static Boolean revise(Node current, BinaryDiffConstraint checking) {
		ArrayList<Integer> xiDomain = current.getGrid()[checking.row1][checking.col1].getDomain();
		ArrayList<Integer> xjDomain = current.getGrid()[checking.row2][checking.col2].getDomain();
		Boolean revised = false;
		for(int diVal : xiDomain) {
			Boolean satisfied = false;
			if(xjDomain.size() > 1) {
				satisfied = true;
			} else {
				for(int djVal : xjDomain) {
					if(diVal != djVal) satisfied = true;
				}
			}
			if(satisfied == false) {
				current.getGrid()[checking.row1][checking.col1].removeOne(diVal);
				revised = true;
			}
		}
		return revised;
	}
	//checked
	
	private static ArrayList<BinaryDiffConstraint> neighbors(int row, int col, int rowNot, int colNot){
		ArrayList<BinaryDiffConstraint> neighbors = new ArrayList<>();
		for(BinaryDiffConstraint arc: Node.CONSTRAINTS.cons) {
			if(arc.row2 == row && arc.col2 == col) {
				if(arc.row1 != rowNot || arc.col1 != colNot) neighbors.add(arc);
			}
		}
		return neighbors;
	}
	
	
	private static int[][] intify(Variable[][] grid){
		int[][] returnArray = new int[9][9];
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				returnArray[i][j] = grid[i][j].getValue();
			}
		}
		return returnArray;
	}
	
	public static String toString(int[][] grid) {
		String returnString = "";
		for(int i=0;i<9;i++) {
			if(i!=0) returnString = returnString + "\n";
			for(int j=0;j<9;j++) {
				returnString = returnString + grid[i][j] + " ";
			}
		}
		returnString += "\n";
		return returnString;
	}
	
	public static void main(String[] args) {
		int[][] theGrid = new int[9][9];
		theGrid[0][2] = 2;
		theGrid[0][3] = 3;
		theGrid[0][4] = 4;
		theGrid[0][5] = 5;
		theGrid[0][6] = 6;
		theGrid[7][0] = 7;
		theGrid[0][8] = 8;
		System.out.println(toString(solve(theGrid)));
	}
}
