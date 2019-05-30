package graphics;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JPanel;

import search.Solver;

public class SudokuPanel extends JPanel {	
	private static final int BOARD_SIZE = 9;
	private static int MARGIN = 10;
	private int[][] solution;
	private int[][] original;
	
	public SudokuPanel(String filename){
		original = new int[BOARD_SIZE][BOARD_SIZE];
		
		try {
			Scanner scanner = new Scanner(new File(filename));
			while (scanner.hasNextLine()) {

				// read a line, split it up
				String line = scanner.nextLine();
				String[] tokens = line.split(" ");
				//System.out.println(line);
				if(tokens.length != 3) {
					System.err.println("Error: badly formed file");
					System.exit(1);
				}

				int row = Integer.parseInt(tokens[0]);
				int col = Integer.parseInt(tokens[1]);
				int val = Integer.parseInt(tokens[2]);
				if(row < 1 || row > BOARD_SIZE || col < 1 || col > BOARD_SIZE) {
					System.err.println("Error: file contains invalid row/col coordinates");
					System.exit(1);
				}
				if(val < 1 || val > 9) {
					System.err.println("Error: values are out of range");
					System.exit(1);
				}
				original[row-1][col-1] = val;
			}
			
			// all read in--close Scanner & convert to int[][]
			scanner.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("Error: Could not find file \"" +filename+ "\".");
			System.exit(1);
		}
		catch(NumberFormatException e) {
			System.err.println("File contains non-integer values");
			System.exit(1);
		}
		
		// This calls your solver method to solve the Sudoku puzzle
		solution = Solver.solve(original);	
		if(solution == null) {
			solution = original;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		Graphics2D g2 = (Graphics2D) g;
		
		final int TILE_WIDTH = (getWidth()-2*MARGIN)/BOARD_SIZE;
		final int TILE_HEIGHT = (getHeight()-2*MARGIN)/BOARD_SIZE;
		
		FontMetrics metrics = g2.getFontMetrics();
		int fontHeight = metrics.getHeight();
		
		Font originalValues = new Font("Times Roman", Font.BOLD, 26);
		Font inferredValues = new Font("Times Roman", Font.PLAIN, 22);
		
		
		int x = MARGIN, y = MARGIN;
		for(int i = 0; i < BOARD_SIZE; i++) {
			for(int j = 0; j < BOARD_SIZE; j++) {
				g2.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
				
				if(original[i][j] == 0) {
					g2.setFont(inferredValues);
				}
				else {
					g2.setFont(originalValues);
				}
				
				int val = solution[i][j];
				if(val != 0) {
					int fontWidth = metrics.stringWidth(""+val);
					int fontX = x + (TILE_WIDTH-fontWidth)/2;
					// The x,y coordinates of text is the bottom left point instead of the top left
					// Thus to center the bottom left point I add the fontHeight
					int fontY = y + (TILE_HEIGHT-fontHeight)/2 + fontHeight;
					
					g2.drawString(""+val, fontX, fontY);
				}
				
				x += TILE_WIDTH;
			}
			x = MARGIN;
			y += TILE_HEIGHT;
		}		
		
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(MARGIN + 3*TILE_WIDTH, MARGIN+1, MARGIN+3*TILE_WIDTH, MARGIN+9*TILE_HEIGHT-1);
		g2.drawLine(MARGIN + 6*TILE_WIDTH, MARGIN+1, MARGIN+6*TILE_WIDTH, MARGIN+9*TILE_HEIGHT-1);
		g2.drawLine(MARGIN+1, MARGIN+3*TILE_HEIGHT, MARGIN+9*TILE_WIDTH-1, MARGIN+3*TILE_HEIGHT);
		g2.drawLine(MARGIN+1, MARGIN+6*TILE_HEIGHT, MARGIN+9*TILE_WIDTH-1, MARGIN+6*TILE_HEIGHT);
	}
}
