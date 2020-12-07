
public class Algorithm {

	State finalState;
	Node[][] grid;
	BoardGenerator b;
	DrawBoard d;

	public Algorithm (Node[][] g, int i, int depth, int type){
		//changes state to new board after the best possible move has been made
		State t = new State(g);
		int v = 0;
		if(type == 0){
			v = minMax(t, depth , i, depth);

			if(finalState == null && t.x!=0 && t.y!= 0){

			}
			if(finalState == null && t.y == 0){
					System.out.println("Game OVER White Wins!");
				return;
			} else if(finalState == null && t.x == 0){
					System.out.println("Game OVER Red Wins!");
				return;
			} else if(finalState.x == 0){
				System.out.println("Game OVER Red Wins!");
				return;
			}
			else if(finalState.y == 0){
				System.out.println("Game OVER White Wins!");
				return;
			}
			//System.out.println("Final Heuristic of Minimax Search: " + Math.abs(finalState.h));
			System.out.println("-----------------");
			System.out.println();
		} else {
			v = alphaBeta(t, depth , 0,0,i, depth);
			if(finalState == null && t.y == 0){
					System.out.println("Game OVER White Wins!");
				return;
			} else if(finalState == null && t.x == 0){
					System.out.println("Game OVER Red Wins!");
				return;
			}else if(finalState.x == 0){
				System.out.println("Game OVER Red Wins!");
				return;
			}else if(finalState.y == 0){
				System.out.println("Game OVER White Wins!");
				return;
			}
			System.out.println();
			//System.out.println("Final Heuristic of alphaBeta pruning with alphaBeta Search: " + Math.abs(finalState.h));
			System.out.println("-----------------");
			System.out.println();
		}
	}

	// Working Minimax Algorithm
	public int minMax(State t, int depth, int turn, int start) {
		// Base case
		//System.out.println("Depth: " + depth);
		if(t.x == 0 && turn == 1){
			return -20;
		} else if(t.y == 0 && turn == 0){
			return 20;
		}
		if (depth == 0 || t.grid == null ) {
			//System.out.println(100000);
			return t.h;
		}
		// Homes turn
		if (turn == 0) {
			Queue p = turns(t, 0);
			int val = -100;
		//	System.out.println("p init: " +p.size);

			while (!p.isEmpty()) {
				State a = p.popsB();
				int runner = Math.max(val, minMax(a, depth - 1, 1,start));
				if (val < runner) {
				//	System.out.println("value + runner+ start + depth: "+ val+", "+runner + ", " + start + ", "+ depth);
					val = runner;
					if(start == depth){
						finalState = a;
							//System.out.println("A Final");
					}
				}
			}
			if(val == -100){
				return t.h;
			}
			return val;
		} else {
			Queue p = turns(t, 1);
			int val = 100;
			while (!p.isEmpty()) {
				State a = p.pop();
				int runner = Math.min(val, minMax(a, depth - 1, 0,start));
				if (val >runner) {
						val = runner;
					if(start == depth){//Sets the final best heuristic to final state
						finalState = a;
					}
				}
			}
			if(val == 100){
				return t.h;
			}
			return val;
		}
	}

	// Working alphaBeta Algorithm
	public int alphaBeta(State t, int depth, int alpha, int beta, int turn, int start) {
		// Base case

		if(t.x == 0 && turn == 1){
			return -20;
		} else if(t.y == 0 && turn == 0){
			return 20;
		}
		if (depth == 0 || t.grid == null ) {
			return t.h;
		}
		// Homes turn
		if (turn == 0) {
			Queue p = turns(t, 0);
			int val = -100;
			while (!p.isEmpty()) {
				State a = p.popsB();
				int runner = Math.max(val, alphaBeta(a, depth - 1, alpha, beta,1,start));

				if (val < runner) {
					val = runner;
					if(start == depth){
						finalState = a;
					}
				}
				alpha = Math.max(alpha, val);
				if(alpha>=beta){
					break;
				}
			}
			if(val == -100){
				return t.h;
			}
			return val;
		} else {
			Queue p = turns(t, 1);
			int val = 100;
			while (!p.isEmpty()) {
				State a = p.pop();
				int runner = Math.min(val, alphaBeta(a, depth - 1, alpha, beta, 0,start));
				if (val > runner) {
						val = runner;
					if(start == depth){//Sets the final best heuristic to final state
						finalState = a;
					}
				}
				beta = Math.min(beta, val);
				if(alpha>=beta){
					break;
				}
			}
			if(val == 100){
				return t.h;
			}
			return val;
		}
	}


	// Explores a state and returns priority Queue continaining the best moves
	public static Queue turns(State t, int turn) {
		Queue result = new Queue();

		if (turn == 0) {
			for (int i = 0; i < t.dimension; i++) {
				for (int k = 0; k < t.dimension; k++) {
					if(t.grid[i][k].side == 0) {

						//Double Loop for local moves
						for (int j = -1; j <= 1; j++) {
							for (int p = -1; p <= 1; p++) {

								int tempX = i + j;
								int tempY = k + p;
								if(tempX < 0 || tempY < 0 || (j == 0 && p == 0)){
									continue;
								} else if (tempX >= t.dimension || tempY >= t.dimension || t.grid[tempX][tempY].side == 0 || t.grid[tempX][tempY].isPit) {
									continue;

								} else {
									if (t.grid[tempX][tempY].side == 1  ) { //is their peice

										State s = new State(t.grid);

										Node Brian = s.grid[tempX][tempY].resolve(s.grid[i][k]);
										if(Brian.type == t.grid[i][k].type && Brian.side != -1){
											s.grid[tempX][tempY] = new Node(Brian);
											s.grid[i][k] = new Node(i,k);
											s.h ++;

											result.add(s);

										} else if(t.grid[tempX][tempY].type == s.grid[i][k].type){
											s.grid[tempX][tempY] = new Node(Brian);
											s.grid[i][k] = new Node(i,k);
											s.h +=.5;
											result.add(s);

										}
									} else {
										State s = new State(t.grid);

										s.grid[i][k].reset();
										s.grid[tempX][tempY] = t.grid[i][k];
										result.add(s);
									}
								}
							}
						}
					}
				}
			}
		} else {
			for (int i = 0; i < t.dimension; i++) {
				for (int k = 0; k < t.dimension; k++) {
					if(t.grid[i][k].side == 1) {

						//Double Loop for local moves
						for (int j = -1; j <= 1; j++) {
							for (int p = -1; p <= 1; p++) {

								int tempX = i + j;
								int tempY = k + p;
								if(tempX < 0 || tempY < 0 || (j == 0 && p == 0)){
									continue;
								} else if (tempX >= t.dimension || tempY >= t.dimension || t.grid[tempX][tempY].side == 1 || t.grid[tempX][tempY].isPit) {
									continue;

								} else {

									if (t.grid[tempX][tempY].side == 0) { //is their peice
										State s = new State(t.grid);
										Node Brian = s.grid[tempX][tempY].resolve(s.grid[i][k]);
										//System.out.println("Result : " + Brian.type +s.grid[tempX][tempY].type + s.grid[i][k].type );
										if(Brian.type == t.grid[i][k].type && Brian.side != -1){

											s.grid[tempX][tempY] = new Node(Brian);
											s.grid[i][k] = new Node(i,k);
											s.h --;
											//System.out.println("True" + s.h);
											result.add(s);
										} else if(t.grid[tempX][tempY].type == s.grid[i][k].type){
											s.grid[tempX][tempY] = new Node(Brian);
											s.grid[i][k] = new Node(i,k);
											s.h -=.5;
											result.add(s);

										}
									} else {
										State s = new State(t.grid);

										s.grid[i][k].reset();
										s.grid[tempX][tempY] = t.grid[i][k];
										result.add(s);
									}
								}
							}
						}
					}
			}
		}
		}
		if(result.isSame()){
			result.jumble();
		}
		return result;
	}


}
