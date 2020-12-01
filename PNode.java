public class PNode{
  // H is for hero, M is for Mage, and P is for pits
  int side = -1; //0 for home (white) 1 for away (dark) -1 for non peices

  double M; //Probability for Mage in this position
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

  public PNode(PNode cop){
    this.M = cop.M;
    this.H = cop.H;
    this.W = cop.W;
    this.P = cop.P;
    this.isOurs = cop.isOurs;
  }

  public void setter(double M, double H, double W, double P){
    this.M = M;
    this.H = H;
    this.W = W;
    this.P = P;
    isOurs = false;
  }
}
