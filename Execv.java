import java.util.Scanner;

public class Execv {
	public static void main(String[] args) {



		//Can be 3,6, or 9

	//	DrawBoard d = new DrawBoard(b);
	// Works!!! to an extent lol
		System.out.print("Run regular minimax(0) or Alpha Pruning(1): ");
		int typer = IO.readInt();
		System.out.println();
		String k ="0";
		System.out.print("What board size(must be 3, 6, 9): ");
		int bSize = IO.readInt();
		BoardGenerator b = new BoardGenerator(bSize);
		DrawBoard d = new DrawBoard(b);
		System.out.println();
		System.out.print("Agent vs Agent(2) or PVA(1):");
		int AI = IO.readInt();
		System.out.println();
		System.out.print("Press Enter when Player turn ends: ");

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
		//.	sleep(2);

			d.updateBoard();
			System.out.print("Press Enter when Player turn ends: ");
		}
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
		//.	sleep(2);

			d.updateBoard();
			System.out.print("Press Enter when Next Agent steps: ");
		}


/*
		// Works!!!
		int k=0;
		while(k!=1){
			k = IO.readInt();
			State s = new State(b.Board);
			//b.traverse();
			s.traverse();
			Queue q = Algorithm.turns(s,1);
			q.traverse();
			s.traverse();
			System.out.println();
			State r = q.pop();
			System.out.println();
			r.traverse();
			System.out.println("Heuristic: " + r.h);

			b.Board = r.grid;
			d.updateBoard();

		}*/


		//int turn = d.board.turn;

	//	System.out.println("turn: " + 	d.board.turn);

		/*
		while(true) { //game not over


			if(d.board.getTurn() == 0) { //users turn
				continue;
			}
			if(d.board.getTurn() == 1) {
				System.out.println(":");
				Algorithm a = new Algorithm(b.Board);
				b.Board = a.finalState.grid;

				//System.out.println(":");

				d.updateBoard();

				d.board.turn = 0;
			}
		}
		*/
	/*	while(true) {
			 Scanner scan = new Scanner(System.in);
			 int num = scan.nextInt();

			  if (num == 1) {
					System.out.println(":");
					Algorithm a = new Algorithm(d.board.Board); //b.Board
					d.board.Board = a.finalState.grid;

					//System.out.println(":");

					d.updateBoard();
			  }
		}
*/
	}
}
