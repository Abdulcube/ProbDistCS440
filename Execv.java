import java.util.Scanner;

public class Execv {
	public static void main(String[] args) {
		System.out.print("Run regular minimax(0), Alpha Pruning(1) or State Distribution(2): ");
		int typer = IO.readInt();
		System.out.println();
		String k ="0";
		System.out.print("What board size(must be 3, 6, 9): ");
		int bSize = IO.readInt();
		BoardGenerator b = new BoardGenerator(bSize);
		DrawBoard d = new DrawBoard(b);


		// Fog of war, assignment 3 implementation
		if(typer == 2){
			System.out.println("Creating Distribution Board...");
			//Starting distribution
			//Making a displot array that has Pnodes with the corresponding probabilities
			PNode[][] distPlot = new PNode[bSize][bSize];
			for(int i =0; i<distPlot.length; i++){
				for(int j =0; j<distPlot.length; j++){
					PNode temp = new PNode();
					if(b.Board[i][j].type == 'h' && b.Board[i][j].side == 1){
						temp.setter(0,1,0,0);
						distPlot[i][j] = temp;
					} else if(b.Board[i][j].type == 'm' && b.Board[i][j].side == 1){
						temp.setter(1,0,0,0);
						distPlot[i][j] = temp;
					}  else if(b.Board[i][j].type == 'w' && b.Board[i][j].side == 1){
						temp.setter(0,0,1,0);
						distPlot[i][j] = temp;
					}  else if(b.Board[i][j].side == -1){
						double power = 1/(double)bSize;
						temp.setter(0,0,0,power);
						distPlot[i][j] = temp;
					} else {
						distPlot[i][j] = temp;
					}
				}
			}
			String[][] obs;
			//obs is an array where each value of string is a type around it
			//observations has the displot update
			/*1. opponent
			2. observations
			3. Player(Agent)
			4. observations*/
			while(!k.equals(" ")){

				//AI Turn table assuming switch is random
				//4
				obs = FOW.obsCheck(b.Board);
				distPlot = FOW.obsUpdate(distPlot, b.Board, obs);
				//FOW.obsTraverse(obs);
				traverse(distPlot);
				//1
				System.out.print("Press Enter when ready for opponent Movement: ");
				k = IO.readString();
				/*State t = new State(b.Board); Opponent Movement
				b.Board = Algorithm.turns(t, 1).pop().grid;*/
				if(b.Board == null){System.exit(0);}
				d.updateBoard();
				distPlot = (new FOW(distPlot, b.Board)).distPlot;
				//2
				obs = FOW.obsCheck(b.Board);
				distPlot = FOW.obsUpdate(distPlot, b.Board, obs);
				//FOW.obsTraverse(obs);
				traverse(distPlot);
				//3
				System.out.print("Press Enter when Player turn ends: ");
				k = IO.readString();
				//b.Board = FOW.movement(b.Board, distPlot);
				d.updateBoard();
				if(b.Board == null){System.exit(0);}
			}
			System.out.println("Ended!");
			System.exit(0);
		}

		System.out.println();
		System.out.print("Agent vs Agent(2) or PVA(1):");
		int AI = IO.readInt();
		System.out.println();
		System.out.print("Press Enter when Player turn ends: ");

		// Player vs Agent
		while(AI==1){
			k = IO.readString();
			//Change the 3rd parameter to adjust depth!
		 	Algorithm j = new Algorithm(b.Board, 1 , 3, typer);
			if(j.finalState == null){
					//System.out.println("Game OVER");
				System.exit(0);
			}
			b.Board = j.finalState.grid;
			if(j.finalState.x == 0 || j.finalState.y == 0){
				return ;
			}

			d.updateBoard();
			System.out.print("Press Enter when Player turn ends: ");
		}
		// Agent vs Agent
		int turn = 0;
		while(AI==2){
			k = IO.readString();
			//Change the 3rd parameter to adjust depth!
			Algorithm j = new Algorithm(b.Board, turn , 3, typer);
			if(j.finalState == null){
					//System.out.println("Game OVER");
				System.exit(0);
			}
			b.Board = j.finalState.grid;
			if(j.finalState.x == 0 || j.finalState.y == 0){
				return ;
			}
			if(turn == 0){
				turn =1;
			} else {
				turn = 0;
			}
			d.updateBoard();
			System.out.print("Press Enter when Next Agent steps: ");
		}



	}

	public static void traverse(PNode[][] distPlot){
		System.out.println("<");
		for(int j =0; j<distPlot.length; j++){
			for(int i =0; i<distPlot.length; i++){
			//	System.out.println("" + i + j);
				if(distPlot[i][j].isOurs == true){
					System.out.print("Age : ");
				} else {
					System.out.print(""+Execv.round(distPlot[i][j].H,4)+ " : ");
					//System.out.print(""+distPlot[i][j].W+", "+distPlot[i][j].H + ", " + distPlot[i][j].M+", "+ distPlot[i][j].P +" :: " );
				}
			}
			System.out.println();
		}
		System.out.println(">");
	}

	public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}
}
