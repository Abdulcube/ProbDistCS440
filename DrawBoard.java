import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.*;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class DrawBoard {
	int guyan = 0;
	int spacer = 0;
	int sizer = 0;
	int minal = 0;

	int first = 153;
	int second =0;
	int dimension = 3; //default
	JFrame frame;
	Board canvas;
	BoardGenerator board;
	PNode[][] distPlot;
	String[][] obs;

	JTextArea prin = new JTextArea();

	JRadioButton jRadioButtonON;
	JRadioButton jRadioButtonOFF;

	JButton jButton;

	ButtonGroup group;




	//Default Constructor - 3 x 3
	public DrawBoard() {
		this.dimension = 3;
		this.board = new BoardGenerator();
		CSIZE = XSIZE / dimension;
		distPlot = null;
		obs = null;

		init();
	}

	//Constructor for size of board.
	public DrawBoard(BoardGenerator b) {
		distPlot = null;
		obs = null;
		this.dimension = b.dimension;
		this.board = b;
		this.board.turn = b.turn;

		CSIZE = XSIZE / dimension;

		init();
	}

	private final int X = 640;
	private final int Y = 730;
	private final int XSIZE = 600;
	private int CSIZE =  XSIZE / dimension;


	public void toggle(boolean value) {
		if(value){
			first = 153;
			second = 0;
		} else {
			first = 170;
			second = 170;
		}
		updateBoard();
	}
	public void init() {


		frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(X,Y);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/*
		 *
		 */




		jRadioButtonON = new JRadioButton();
		jRadioButtonOFF = new JRadioButton();
		jButton = new JButton("Update");
		group = new ButtonGroup();
		prin = new JTextArea();


		jRadioButtonON.setText("Fog Of War");
		jRadioButtonOFF.setText("Normal");

		jRadioButtonON.setBounds(20,610,120,50);
		jRadioButtonOFF.setBounds(150,610,120,50);
		jButton.setBounds(75, 650, 120, 50);
		prin.setBounds(240, 620, 150, 100);

		jRadioButtonOFF.setSelected(true);
		prin.setEditable(false);

		group.add(jRadioButtonON);
		group.add(jRadioButtonOFF);

		frame.add(prin);
		frame.add(jButton);
		frame.add(jRadioButtonON);
		frame.add(jRadioButtonOFF);


		jButton.addActionListener(new ActionListener() {
	            // Anonymous class.

	            public void actionPerformed(ActionEvent e)
	            {
	                if (jRadioButtonON.isSelected()) {
	                	toggle(false);
	                } else if (jRadioButtonOFF.isSelected()) {
	                	toggle(true);
	                }
	            }
	     });

		 /*
		  *
		  */
		initBoard();

		frame.getContentPane().add(canvas);
		updateBoard();

	}

	public void initBoard() {

		canvas = new Board();
		canvas.setBounds(10, 10, XSIZE+1, XSIZE+1);

		//prin.setText("Nothing is on Radar");

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
			String a = "";
			if(obs != null) {
				for(int p = 0; p < dimension; p ++) {
					for(int y = 0; y < dimension; y ++) {
						if(obs[y][p].equals("")) {
							a = a.concat("       ,");
						} else {
							a = a.concat(obs[y][p] + ",");
						}
					}
					a = a.concat("\n");
				}
				prin.setText(a);
			}

			for(int x = 0; x < dimension; x++) {
				for(int k = 0; k < dimension; k++ ) {


					g.setColor(new Color(170,170,170)); //Light Gray
					g.fillRect(x*CSIZE,k*CSIZE,CSIZE,CSIZE);
					g.setColor(Color.BLACK);
					g.drawRect(x*CSIZE,k*CSIZE,CSIZE,CSIZE);

					if(board.Board[x][k].isPit == true) {
						if(first == 153){
							g.setColor(new Color(75,75,75)); //Dark Grey
							g.fillOval(x*CSIZE,k*CSIZE,CSIZE,CSIZE);
						}
					}

					//For Pieces to be Drawn
					if(board.Board[x][k].side == 0) {
						//Cream
						g.setColor(new Color(255,255,200));
					} else {
						//Dark
						g.setColor(new Color(first,second,second));
					}





					if(distPlot == null || distPlot[x][k].isOurs || board.Board[x][k].side == 0){
						if (board.Board[x][k].type == 'm') {

							g.setFont(new Font("Arial",Font.BOLD,CSIZE/4));
							//Draw String in JPanel
							g.drawString("M",x*CSIZE+5,k*CSIZE+(140/dimension));

						} else if (board.Board[x][k].type == 'h') {

							g.setFont(new Font("Arial",Font.BOLD,CSIZE/4));
							//Draw String in JPanel
							g.drawString("H",x*CSIZE+5,k*CSIZE+(140/dimension));

						} else if (board.Board[x][k].type == 'w') {

							g.setFont(new Font("Arial",Font.BOLD,CSIZE/4));
							//Draw String in JPanel
							g.drawString("W",x*CSIZE+5,k*CSIZE+(140/dimension));
						} //For Pieces to be Drawn


					} else {
						if(dimension == 3) {
							spacer = 65;
							sizer = 250;

							guyan = 20;
						} else if (dimension == 6) {
							spacer = 80;
							sizer = 250;

							guyan = 30;
						} else {
							spacer = 120;
							sizer = 250;

							guyan = 40;
							minal = -30;
						}
						g.setFont(new Font("Arial",Font.BOLD,(spacer + guyan)/dimension));
						//Draw String in JPanel
						g.setColor(new Color(0,0,0));
						if(true){
							if(Execv.round(distPlot[x][k].M,2) != 0){
								g.drawString("M: "+ Execv.round(distPlot[x][k].M,2) + "" ,x*CSIZE+15,k*CSIZE +(sizer/dimension));
							}
							if (Execv.round(distPlot[x][k].H,2) != 0) {
								g.drawString("H: "+ Execv.round(distPlot[x][k].H,2) + "" ,x*CSIZE+15,k*CSIZE +((sizer + spacer)/dimension));
							}
							if (Execv.round(distPlot[x][k].W,2) != 0) {
								g.drawString("W: "+ Execv.round(distPlot[x][k].W,2) + "" ,x*CSIZE+15,k*CSIZE +((sizer+ spacer + spacer)/dimension));
							}
							if (Execv.round(distPlot[x][k].P,2) != 0) {
								g.drawString("P: "+ Execv.round(distPlot[x][k].P,2) + "" ,x*CSIZE+15,k*CSIZE +((sizer+ spacer + spacer+ spacer)/dimension));
							}
						} else {
							g.drawString("M: "+ Execv.round(distPlot[x][k].M,2) + "" ,x*CSIZE+15,k*CSIZE +(sizer/dimension));
							g.drawString("H: "+ Execv.round(distPlot[x][k].H,2) + "" ,x*CSIZE+15,k*CSIZE +((sizer + spacer)/dimension));
							g.drawString("W: "+ Execv.round(distPlot[x][k].W,2) + "" ,x*CSIZE+15,k*CSIZE +((sizer+ spacer + spacer)/dimension));
							g.drawString("P: "+ Execv.round(distPlot[x][k].P,2) + "" ,x*CSIZE+15,k*CSIZE +((sizer+ spacer + spacer+ spacer)/dimension));
						}
						/*//         Labeled
						g.drawString(Execv.round(distPlot[x][k].M,2) + "%w" ,x*CSIZE+15,k*CSIZE +(sizer/dimension));
						g.drawString(Execv.round(distPlot[x][k].H,2) + "%h" ,x*CSIZE+15,k*CSIZE +((sizer + spacer)/dimension));
						g.drawString(Execv.round(distPlot[x][k].W,2) + "%m" ,x*CSIZE+15,k*CSIZE +((sizer+ spacer + spacer)/dimension));
						*/

						g.setColor(new Color(first,second,second));

						if (board.Board[x][k].type == 'm') {

							g.setFont(new Font("Arial",Font.BOLD,CSIZE/4));
							//Draw String in JPanel
							g.drawString("M" ,x*CSIZE+15,k*CSIZE+(140/dimension));

						} else if (board.Board[x][k].type == 'h') {

							g.setFont(new Font("Arial",Font.BOLD,CSIZE/4));
							//Draw String in JPanel
							g.drawString("H",x*CSIZE+15,k*CSIZE+(140/dimension));

						} else if (board.Board[x][k].type == 'w') {

							g.setFont(new Font("Arial",Font.BOLD,CSIZE/4));
							//Draw String in JPanel
							g.drawString("W",x*CSIZE+15,k*CSIZE+(140/dimension));
						} //For Pieces to be Drawn
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
