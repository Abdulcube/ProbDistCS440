import java.lang.*;

public class State {
	// A data structure, where positions is a 2d array with 2 rows, one with
	// all the white pieces, and one with all the black pieces,
	// h is the current tree nodes heuristic values
	// x is number of home, y is Away
	Node[][] grid;
	int dimension;
	int x;
	int y;
	int h;

	public State() {
	}

	public State(Node[][] j) {
		dimension = j.length;
		grid = new Node[j.length][j.length];

		for (int i = 0; i < dimension; i++) {
			for (int k = 0; k < dimension; k++) {
				Node n = new Node(j[i][k]);
				grid[i][k] = n;
				//System.out.println("i,k: " + i + "," + k);
			}
		}

		x = 0;
		y = 0;

		for (int i = 0; i < dimension; i++) {
			for (int k = 0; k < dimension; k++) {
				//System.out.println("x,y: " + i +","+ k);
				if (grid[i][k].side == 0) {
					x++;
				} else if (grid[i][k].side == 1) {
					y++;
				}
			}
		}
		if (x == 0 && y == 0) {
			System.out.print("GAME OVER: Draw");
			System.exit(0);
		}
		h = x - y;
	}

	public void traverse() {
		for (int i = 0; i < dimension; i++) {
			for (int k = 0; k < dimension; k++) {
				System.out.print("" + grid[i][k].type +  " ");
			}
			System.out.println();
		}
		return;
	}

	public void traverseS() {
		for (int i = 0; i < dimension; i++) {
			for (int k = 0; k < dimension; k++) {
				System.out.print("" + grid[i][k].type + " : " + grid[i][k].side + " ");
			}
			System.out.println();
		}
		return;
	}

}
