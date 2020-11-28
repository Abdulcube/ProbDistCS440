public class PNode{
  // H is for hero, M is for Mage, and P is for pits
  int side = -1; //0 for home (white) 1 for away (dark) -1 for non peices

  double M;
  double H;
  double W;
  double P;
  boolean isOurs;
  public PNode(){
    M = 0;
    H = 0;
    W = 0;
    P = 0;
    isOurs = true;
  }

  public PNode(double M, double H, double W, double P){
    this.M = M;
    this.H = H;
    this.W = W;
    this.P = P;
    isOurs = false;
  }

  public void setter(double M, double H, double W, double P){
    this.M = M;
    this.H = H;
    this.W = W;
    this.P = P;
    isOurs = false;
  }
}
