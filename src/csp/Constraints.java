package csp;
import java.util.ArrayList;

/**
 * This class should contain a list of all 1,944 constraints
 */
public class Constraints {
	public ArrayList<BinaryDiffConstraint> cons = new ArrayList<>();
	
	/**
	 * Initializing the full list of constraints.
	 */
	public Constraints() {
		//Loop to create horizontal constraints
		addHorizontals(0,8,0,8);
		//Loop to create vertical constraints
		addVerticals(0,8,0,8);
		//Loop to create box constraints
		addBox(0,0);
		addBox(3,0);
		addBox(6,0);
		
		addBox(0,3);
		addBox(3,3);
		addBox(6,3);
		
		addBox(0,6);
		addBox(3,6);
		addBox(6,6);
		
	}
	
	private void addVerticals(int rowStart, int rowFinish,int colStart,int colFinish) {
		for(int i=colStart;i<colFinish+1;i++) {
			for(int j=rowStart;j<rowFinish+1;j++) {
				for(int k=j;k<rowFinish+1;k++) {
					if(k != j) {
						BinaryDiffConstraint newCons = new BinaryDiffConstraint(j,i,k,i);
						BinaryDiffConstraint newCons2 = new BinaryDiffConstraint(k,i,j,i);
						cons.add(newCons);
						cons.add(newCons2);
					}
				}
			}
		}
	}
	
	private void addHorizontals(int rowStart, int rowFinish, int colStart, int colFinish) {
		for(int i=rowStart;i<rowFinish+1;i++) {
			for(int j=colStart;j<colFinish+1;j++) {
				for(int k=j;k<colFinish+1;k++) {
					if(k != j) {
						BinaryDiffConstraint newCons = new BinaryDiffConstraint(i,j,i,k);
						BinaryDiffConstraint newCons2 = new BinaryDiffConstraint(i,k,i,j);
						cons.add(newCons);
						cons.add(newCons2);
					}
				}
			}
		}
	}
	
	private void addBox(int top, int left) {
		for(int i=top;i<top+3;i++) {
			for(int j=left;j<left+3;j++) {
				for(int k=top;k<top+3;k++) {
					for(int l=left;l<left+3;l++) {
						if(i!=k || j!=l) {
							BinaryDiffConstraint newCons = new BinaryDiffConstraint(i,j,k,l);
							cons.add(newCons);
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Constraints straints = new Constraints();
		System.out.println(straints.cons.size());
		System.out.println(straints.cons.get(0).row1);
		System.out.println(straints.cons.get(0).col1);
		System.out.println(straints.cons.get(0).row2);
		System.out.println(straints.cons.get(0).col2);
	}
}
