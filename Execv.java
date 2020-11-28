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
						temp.setter(0,0,0,round(power, 2));
						distPlot[i][j] = temp;
					} else {
						distPlot[i][j] = temp;
					}
				}
			}
			while(!k.equals(" ")){
				traverse(distPlot);
				System.out.print("Press Enter when Player turn ends: ");
				//AI Turn table assuming switch is random
				//obs is an array where each value of string is a type around it
				String[][] obs = FOW.obsCheck(b.Board);
				FOW.obsTraverse(obs);
				k = IO.readString();

				State t = new State(b.Board);
				b.Board = Algorithm.turns(t, 1).pop().grid;
				d.updateBoard();

				distPlot = FOW(displot, b.Board, obs).distPlot;
				System.out.print("Current board:");
				

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
		for(int i =0; i<distPlot.length; i++){
			for(int j =0; j<distPlot.length; j++){
				if(distPlot[i][j].isOurs == true){
					System.out.print("Agents Piece ");
				} else {
					System.out.print(""+distPlot[i][j].M+","+distPlot[i][j].H + "," + distPlot[i][j].W+","+ distPlot[i][j].P +" " );
				}
			}
			System.out.println();
		}
	}

	public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}
}
