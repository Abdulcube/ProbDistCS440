public class FOW{

  PNode[][] distPlot;
  Node[][] grid;
  int home;
  int away;
  //Updates the distPlot based on the possible movements of the opponent.
  public FOW(PNode[][] distPlot, Node[][] grid){
    this.distPlot = distPlot;
    this.grid = grid;
    PNode[][] result = new PNode[distPlot.length][distPlot.length];
    numCount();
    //System.out.println("Home + away: " + home +", " + away);
    for (int i = 0; i < distPlot.length; i++) {
      for (int k = 0; k < distPlot.length; k++) {
        if(distPlot[i][k].isOurs){
          result[i][k] = new PNode(distPlot[i][k]);
          continue;
        }
        result[i][k] = new PNode(distPlot[i][k]);
        double paran = 1-((double)1/(double)away);
        double sum = 0;
        int counter = 0;
        //System.out.println("Current "+ i+ ", "+k);
        //Wumpus
        sum = 0;
        for (int x = -1; x <= 1; x++) {
          for (int y = -1; y <= 1; y++) {
            int x1 = i +x;
            int y1 = k +y;
            //System.out.println("yoooo "+ x1+y1);
            if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
              continue;
            }
            counter++;
            if(totalMoves(x1,y1)==0){
              continue;
            }
            sum += (double)distPlot[x1][y1].W * (double)((double)1/((double)away*totalMoves(x1,y1)));
          }
        }
        //System.out.println("Paran: " + paran + " : distPlot.W: " + distPlot[i][k].W + " : Sum : "+ sum);
        result[i][k].W = (paran*distPlot[i][k].W) + sum;

        //Hero
        sum = 0;
        for (int x = -1; x <= 1; x++) {
          for (int y = -1; y <= 1; y++) {
            int x1 = i +x;
            int y1 = k +y;
            //System.out.println("yoooo "+ x1+y1);
            if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
              continue;
            }
            counter++;
            if(totalMoves(x1,y1)==0){
              continue;
            }

            sum += (double)distPlot[x1][y1].H * (double)((double)1/((double)away*totalMoves(x1,y1)));
          }
        }
        result[i][k].H = (paran*distPlot[i][k].H) + sum;
        //Mage
        sum = 0;
        for (int x = -1; x <= 1; x++) {
          for (int y = -1; y <= 1; y++) {
            int x1 = i +x;
            int y1 = k +y;
            //System.out.println("yoooo "+ x1+y1);
            if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
              continue;
            }
            counter++;
            if(totalMoves(x1,y1)==0){
              continue;
            }

            sum += (double)distPlot[x1][y1].M * (double)((double)1/((double)away*totalMoves(x1,y1)));
          }
        }
        result[i][k].M = Execv.round((paran*distPlot[i][k].M) + sum, 4);
        //pit

        // Next steps
      }
    }
    this.distPlot = result;
    //Execv.traverse(result);
  }
//counts the number of agents pieces and opponent
  public void numCount(){
    home = 0;
    away = 0;
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(grid[i][k].side ==0){
          home++;
        } else if (grid[i][k].side ==1){
          away++;
        }
      }
    }

  }
//computes the total number of moves a piece can make
  public int totalMoves(int x, int y){
    int moves = 0;

    for(int i = -1; i<=1; i++){
      for(int k = -1; k<=1; k++){
        int x1 = i +x;
        int y1 = k +y;
        if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
          continue;
        } else {
          moves++;
        }
      }
    }
    //System.out.println(moves);
    return moves;
  }
//Updates the probability distribution based on the movement of our agent;
  public static  PNode[][] PlayerMovement(Node[][] grid, PNode[][] distPlot){
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(!distPlot[i][k].isOurs && grid[i][k].side == 0){
          for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
              if(distPlot[x][y].isOurs && grid[x][y].side != 0){
                distPlot[i][k] = distPlot[x][y];
                distPlot[x][y] = new PNode(0,0,0,0);
                return distPlot;
              }
            }
          }
        } else if(distPlot[i][k].isOurs && grid[i][k].side == 1){
          for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
              if(!distPlot[x][y].isOurs && grid[x][y].side != 0){
                distPlot[x][y] = distPlot[i][k];
                distPlot[i][k] = new PNode(0,0,0,0);
                return distPlot;
              }
            }
          }
        }
      }
    }
    return distPlot;
  }
//Updates the probability distribution based on the agents observations;
  public static PNode[][] obsUpdate(PNode[][] distPlot, Node[][] grid, String[][] observations){
    for (int x = 0; x < distPlot.length; x++) {
      for (int y = 0; y < distPlot.length; y++) {
        String current = observations[x][y];
        if(current.equals("") || grid[x][y].side == 1){
          continue;
        }
        if(current.contains("S") && grid[x][y].side == 0){
          System.out.println("Stench");


        } else {
          double sum = 0;
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              distPlot[x1][y1].W = 0;
            }
          }
          for (int i = 0; i < distPlot.length ; i++) {
            for (int k = 0; k < distPlot.length ; k++) {
              //System.out.println(distPlot[i][k].W);
              sum+=  distPlot[i][k].W;
            }
          }
          for (int i = 0; i < distPlot.length ; i++) {
            for (int k = 0; k < distPlot.length ; k++) {
              //System.out.println("Sum: "+sum);
              if(distPlot[i][k].W !=0 ){
                distPlot[i][k].W = (distPlot[i][k].W  / sum)*typeCount(grid)[1];
              }
            }
          }


        }
        if(current.contains("E") && grid[x][y].side == 0){
          System.out.println("Heat");
          

        } else {
          double sum = 0;
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              distPlot[x1][y1].M = 0;
            }
          }
          for (int i = 0; i < distPlot.length ; i++) {
            for (int k = 0; k < distPlot.length ; k++) {
              //System.out.println(distPlot[i][k].M);
              sum+=  distPlot[i][k].M;
            }
          }
          for (int i = 0; i < distPlot.length ; i++) {
            for (int k = 0; k < distPlot.length ; k++) {
              //System.out.println("Sum: "+sum);
              if(distPlot[i][k].M !=0 ){
                distPlot[i][k].M = (distPlot[i][k].M  / sum)*typeCount(grid)[0];
              }
            }
          }


        }
        if(current.contains("N") && grid[x][y].side == 0){
          System.out.println("Noise");


        } else {
          double sum = 0;
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              distPlot[x1][y1].H = 0;
            }
          }
          for (int i = 0; i < distPlot.length ; i++) {
            for (int k = 0; k < distPlot.length ; k++) {
              //System.out.println(distPlot[i][k].H);
              sum+=  distPlot[i][k].H;
            }
          }
          for (int i = 0; i < distPlot.length ; i++) {
            for (int k = 0; k < distPlot.length ; k++) {
              //System.out.println("Sum: "+sum);
              if(distPlot[i][k].H !=0 ){
                distPlot[i][k].H = (distPlot[i][k].H  / sum)*typeCount(grid)[2];
              }
            }
          }


        }
        if(!current.contains("P") && grid[x][y].side == 0){
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              distPlot[x1][y1].P = 0;
            }
          }
          for(int i = -1; i<=1; i++){
            double sum = 0;
            int l = i +y;
            if(  l<0  || l>=distPlot.length){
              continue;
            }
            for(int k = 0; k<grid.length; k++){
              sum+=  distPlot[k][l].P;
            }
            for(int k = 0; k<grid.length; k++){
              if(distPlot[k][l].P !=0 ){
                distPlot[k][l].P = distPlot[k][l].P / sum;
              }
            }

          }
        }
      }
    }
    return distPlot;
  }
//Returns an array with the number of each piece;[mage,wumpus,hero]
  public static double[] typeCount(Node[][] grid){
    double[] result = {0,0,0};
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(grid[i][k].side!=1){continue;}
        switch (grid[i][k].type) {
          case('m'): result[0]++; break;
          case('w'): result[1]++; break;
          case('h'): result[2]++; break;
        }
      }
    }
    return result;
  }
//Makes our agent move
//Problem number 4
  public static Node[][] movement(Node[][] grid, PNode[][] distPlot){
    State t = new State(grid);
    t = Algorithm.turns(t, 0).pop();
    if(t == null){
      return null;
    }
    return t.grid;
  }
//Observation matrix is created
  public static String[][] obsCheck(Node[][] grid){
    String[][] matrix = new String[grid.length][grid.length];
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(grid[i][k].side == 0){
          String curr = ""+grid[i][k].type+":";
          for(int tempx=-1; tempx<=1; tempx++){
            for(int tempy=-1; tempy<=1; tempy++){
              int x = i + tempx;
              int y = k + tempy;
              if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
                continue;
              }
              //System.out.println(grid[x][y].type);
              if(grid[x][y].side==1){
                String result= "";
                //System.out.println(grid[x][y].type);
                switch(grid[x][y].type){
                  case 'w': result ="S"; break;
                  case 'm': result ="E"; break;
                  case 'h': result ="N"; break;
                }
              //  System.out.println(result);
                curr = curr.concat(result);
              } else if(grid[x][y].isPit){
                curr = curr.concat("P");
              }
            }
          }
          matrix[i][k] = curr;
        } else {
          matrix[i][k] = "";
        }
      }
    }
    return matrix;
  }
//Prints observation matrix
  public static void obsTraverse(String[][] grid){

    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        System.out.print(grid[k][i]+" , ");
      }
      System.out.println();
    }
  }
}
