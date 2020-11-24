import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DrawBoard {
	
	int dimension = 3; //default 
	JFrame frame;
	Board canvas;
	BoardGenerator board;
	
	//Default Constructor - 3 x 3
	public DrawBoard() {
		this.dimension = 3;
		this.board = new BoardGenerator();
		CSIZE = XSIZE / dimension;
		
		init();
	}
	
	//Constructor for size of board.
	public DrawBoard(BoardGenerator b) {
		this.dimension = b.dimension;
		this.board = b;
		this.board.turn = b.turn;
		
		CSIZE = XSIZE / dimension;
		
		init();
	}
	
	private final int X = 640;
	private final int XSIZE = 600;
	private int CSIZE =  XSIZE / dimension;
	
	public void init() {	
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(X,X);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		initBoard();
		
		frame.getContentPane().add(canvas);
		
	}
	
	public void initBoard() {
		
		canvas = new Board();
		canvas.setBounds(10, 10, XSIZE+1, XSIZE+1);

	}
	
	public void updateBoard() {
		
		
		try {
			Thread.sleep(15);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
		
		canvas.repaint();
	}
	
	class Board extends JPanel implements MouseListener  /*, MouseMotionListener*/ {	

		
		public Board() {
			addMouseListener(this);
			//addMouseMotionListener(this);
		}
		
		public void paintComponent(Graphics g) {
			for(int x = 0; x < dimension; x++) {
				for(int k = 0; k < dimension; k++ ) {

					
					g.setColor(new Color(170,170,170)); //Light Gray
					g.fillRect(x*CSIZE,k*CSIZE,CSIZE,CSIZE);
					g.setColor(Color.BLACK);
					g.drawRect(x*CSIZE,k*CSIZE,CSIZE,CSIZE);
					
					//For Peices to be Drawn
					if(board.Board[x][k].side == 0) {
						//Cream
						g.setColor(new Color(255,255,200));
					} else {
						//Dark
						g.setColor(new Color(153,0,0));
					}
					
					if (board.Board[x][k].type == 'm') {
						
						g.setFont(new Font("Arial",Font.BOLD,40));
						//Draw String in JPanel
						g.drawString("M",x*CSIZE+15,k*CSIZE+50);
						
					} else if (board.Board[x][k].type == 'h') {
						
						g.setFont(new Font("Arial",Font.BOLD,40));
						//Draw String in JPanel
						g.drawString("H",x*CSIZE+15,k*CSIZE+50);
						
					} else if (board.Board[x][k].type == 'w') {
						
						g.setFont(new Font("Arial",Font.BOLD,40));
						//Draw String in JPanel
						g.drawString("W",x*CSIZE+15,k*CSIZE+50);
					} //For Peices to be Drawn
					
					
					
					if(board.Board[x][k].isPit == true) {
						g.setColor(new Color(75,75,75)); //Dark Grey
						g.fillOval(x*CSIZE,k*CSIZE,CSIZE,CSIZE);
					}
					
					if(board.Board[x][k].isSelected == true) {
						g.setColor(new Color(255,255,50)); //Yellow
						g.fillOval(x*CSIZE,k*CSIZE,10,10);
						g.drawRect(x*CSIZE,k*CSIZE,CSIZE,CSIZE);
					}
					
				}
			}
		}
		
		/*
		@Override
		public void mouseDragged(MouseEvent e) {}
		
		@Override
		public void mouseMoved(MouseEvent e) {}
		*/
		
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				int x = e.getX()/CSIZE;	
				int y = e.getY()/CSIZE;
				Node cur = board.Board[x][y];
				
				if(board.hasSelected()) { //placing piece
					if((x > -1 && x < dimension) && (y > -1 && y < dimension)) {      //inbounds
						Node prev = board.getSelected();
						if(Math.abs(x - prev.x) <= 1  && Math.abs(y - prev.y) <= 1) { //within 1
							if(cur.canTraverse(prev) && !cur.isCollision(prev)) {                               //is Traversable
								//Move Piece
								board.Board[x][y].type = prev.type;
								board.Board[x][y].side = prev.side;
								board.Board[prev.x][prev.y].reset();
								board.turn = 1;
							}
							if(cur.isCollision(prev)) {
								//Capture Piece
								cur = cur.resolve(prev);
								board.Board[x][y].type = cur.type;
								board.Board[x][y].side = cur.side;
								board.Board[prev.x][prev.y].reset();
								board.turn = 1;
							}
						}
					}
				} else { //selecting piece
					if (cur.isPiece()) {
						cur.isSelected = true;
					}
				}
				
				updateBoard();
				
			} catch(Exception e1) {} //Needed for mouseListener
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}	
}
