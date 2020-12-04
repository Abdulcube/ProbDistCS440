
//Nodes make up the entirety of the the board spaces
public class Node {

	//Node[][] state;
	int side = -1; //0 for home (white) 1 for away (dark) -1 for non peices
	char type = 'n';  //'h' for hero 'w' for wumpus 'm' for mage 'n' for null

	boolean isPit = false;
	boolean isSelected = false;

	int x;
	int y;
	//Default Constructor for BoardGenerator
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.side = -1;
	}

	public Node(Node n) {
		this.x = n.x;
		this.y = n.y;

		this.side = n.side;
		this.type = n.type;
		this.isPit = n.isPit;
		this.isSelected = n.isSelected;
	}

	//Only pieces can move
	public boolean canTraverse(Node prev) { //requires the side the piece is on
		if (type == 'n' && !this.isPit) {
			return true;
		} else if (prev.side != this.side && !this.isPit) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isCollision(Node prev) {
		if(this.isPiece() && prev.isPiece()) {
			if( this.side != prev.side) {
				return true;
			}
		}
		return false;
	}

	public Node resolve(Node prev) {
		// h > w
		// m > h
		// w > m

		if(this.type == 'h') {
			if(prev.type == 'h') {
				//return null node in tie
				this.reset();
				return this;
			} else if (prev.type == 'm') {
				return prev;
			} else if(prev.type == 'w') {
				return this;
			}
		} else if (this.type == 'm') {
			if(prev.type == 'h') {
				return this;
			} else if (prev.type == 'm') {
				//return null node in tie
				this.reset();
				return this;
			} else if(prev.type == 'w') {
				return prev;
			}
		} else if (this.type == 'w') {
			if(prev.type == 'h') {
				return prev;
			} else if (prev.type == 'm') {
				return this;
			} else if(prev.type == 'w') {
				//return null node in tie
				this.reset();
				return this;
			}
		} else if(this.isPit) {
			return this;
		} else if(prev.isPit) {
			return prev;
		} else if(this.side == -1) {
			return prev;
		} else if(prev.side == -1) {
			return this;
		}

		//ERROR
		System.out.println("EEERRRROOORRR");
		return this;
	}

	public void reset() {
		this.side = -1;
		this.type = 'n';
		this.isSelected = false;

	}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public int getSide() {return this.side;}
	public char getType() {return this.type;}

	public void setX(int n) {this.x = n;}
	public void setY(int n) {this.y = n;}
	public void setType(char n) {this.type = n;}



	public boolean isPiece() {
		if(type == 'h' || type == 'w' || type == 'm') {
			return true;
		} else {
			return false;
		}
	}


}
