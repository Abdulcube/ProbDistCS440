
public class BoardGenerator {

	Node[][] Board;
	int dimension; //(3x3) (6x6) (9x9)
	int turn = 0;

	//Default Constructor
	public BoardGenerator() {
		Board = new Node[3][3];
		dimension = 3;
		//...
		init();
		genPits();
		genPieces();
	}

	//Constructor for dimensions
	public BoardGenerator(int d) {
		turn = 0;
		if( d == 3 || d == 6 || d == 9 ) {
			this.dimension = d;
		} else {
			System.out.println("ERROR/ cannot continue");
			System.exit(0);
		}

		Board = new Node[d][d];
		//...
		init();
		genPits();
		genPieces();
	}
	public boolean hasSelected() {
		for(int i = 0; i < dimension; i ++) {
			for( int k = 0; k < dimension; k ++) {
				if(Board[i][k].isSelected) {
					return true;
				}
			}
		}
		return false;
	}
	public int getTurn() {
		return this.turn;
	}

	public Node getSelected() {
		for(int i = 0; i < dimension; i ++) {
			for( int k = 0; k < dimension; k ++) {
				if(Board[i][k].isSelected) {
					return Board[i][k];
				}
			}
		}
		return null;
	}

	//Initializer for non-nulling Nodes in the list
	public void init() {
		for(int i = 0; i < dimension; i ++) {
			for( int k = 0; k < dimension; k ++) {

				Board[i][k] = new Node(i,k);
			}
		}
	}

	//generate the Pits for the given board
	public void genPits() {

		for(int i = 1; i < dimension-1; i ++) {

			int u = dimension;
			int l = 1;
			int r = (int) (Math.random() * (u - l)) + l;


			this.Board[r][i].isPit = true;

		}
	}

	public void traverse() {
		for(int i = 0; i < dimension; i ++) {
			for( int k = 0; k < dimension; k ++) {
				System.out.print(this.Board[i][k].type+" ");
			}
			System.out.println();
		}
	}

	//Possibly make it more random
	public void genPieces() {
		if        (dimension == 3) {
			//Away Peices on board
			Board[0][0].type = 'w';
			Board[0][0].side = 1;

			Board[1][0].type = 'h';
			Board[1][0].side = 1;

			Board[2][0].type = 'm';
			Board[2][0].side = 1;


			//Home Peices on board
			Board[0][2].type = 'w';
			Board[0][2].side = 0;

			Board[1][2].type = 'h';
			Board[1][2].side = 0;

			Board[2][2].type = 'm';
			Board[2][2].side = 0;

		} else if (dimension == 6) {
			//Away Peices on board
			Board[0][0].type = 'w';
			Board[0][0].side = 1;

			Board[1][0].type = 'h';
			Board[1][0].side = 1;

			Board[2][0].type = 'm';
			Board[2][0].side = 1;

			Board[3][0].type = 'w';
			Board[3][0].side = 1;

			Board[4][0].type = 'h';
			Board[4][0].side = 1;

			Board[5][0].type = 'm';
			Board[5][0].side = 1;


			//Home Peices on board
			Board[0][5].type = 'w';
			Board[0][5].side = 0;

			Board[1][5].type = 'h';
			Board[1][5].side = 0;

			Board[2][5].type = 'm';
			Board[2][5].side = 0;

			Board[3][5].type = 'w';
			Board[3][5].side = 0;

			Board[4][5].type = 'h';
			Board[4][5].side = 0;

			Board[5][5].type = 'm';
			Board[5][5].side = 0;


		} else if (dimension == 9) {

			//Away Peices on board
			Board[0][0].type = 'w';
			Board[0][0].side = 1;

			Board[1][0].type = 'h';
			Board[1][0].side = 1;

			Board[2][0].type = 'm';
			Board[2][0].side = 1;

			Board[3][0].type = 'w';
			Board[3][0].side = 1;

			Board[4][0].type = 'h';
			Board[4][0].side = 1;

			Board[5][0].type = 'm';
			Board[5][0].side = 1;

			Board[6][0].type = 'w';
			Board[6][0].side = 1;

			Board[7][0].type = 'h';
			Board[7][0].side = 1;

			Board[8][0].type = 'm';
			Board[8][0].side = 1;


			//Home Peices on board
			Board[0][8].type = 'w';
			Board[0][8].side = 0;

			Board[1][8].type = 'h';
			Board[1][8].side = 0;

			Board[2][8].type = 'm';
			Board[2][8].side = 0;

			Board[3][8].type = 'w';
			Board[3][8].side = 0;

			Board[4][8].type = 'h';
			Board[4][8].side = 0;

			Board[5][8].type = 'm';
			Board[5][8].side = 0;

			Board[6][8].type = 'w';
			Board[6][8].side = 0;

			Board[7][8].type = 'h';
			Board[7][8].side = 0;

			Board[8][8].type = 'm';
			Board[8][8].side = 0;

		}

	}
}
