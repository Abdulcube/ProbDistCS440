import java.util.HashMap;
import java.lang.Math;
public class FOW{

  PNode[][] distPlot;
  Node[][] grid;
  int home;
  int away;
  //Updates the distPlot based on the possible  random movements of the opponent.
  public FOW(PNode[][] distPlot, Node[][] grid, String[][] observations){
    this.distPlot = distPlot;
    this.grid = grid;
    PNode[][] result = new PNode[distPlot.length][distPlot.length];
    numCount();
    System.out.println("FOW distribution:");
    for (int i = 0; i < distPlot.length; i++) {
      for (int k = 0; k < distPlot.length; k++) {
        if(grid[i][k].side == 0 || distPlot[i][k].P == 1.0 ){
          result[i][k] = new PNode(distPlot[i][k]);
          continue;
        }
        if(!isObserved(i,k, observations)){
          result[i][k] = new PNode(distPlot[i][k]);
          continue;
        }
        result[i][k] = new PNode(distPlot[i][k]);
        double paran = 1-((double)1/(double)away);
        double sum = 0;
        int counter = 0;
        //Wumpus
        if(distPlot[i][k].M != 1.0 && distPlot[i][k].H != 1.0){
          sum = 0;
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              int x1 = i +x;
              int y1 = k +y;
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

          result[i][k].W = (paran*distPlot[i][k].W) + sum;
          if(result[i][k].W == 1){
            result[i][k].H = 0;
            result[i][k].M = 0;
            continue;
          }
        }
        //Hero
        if(distPlot[i][k].M != 1.0 && distPlot[i][k].W != 1.0){
          sum = 0;
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              int x1 = i +x;
              int y1 = k +y;
              if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              counter++;
              if(totalMoves(x1,y1)==0){
                continue;
              }
              sum += distPlot[x1][y1].H * (double)((double)1/((double)away*(double)totalMoves(x1,y1)));
            }
          }
          result[i][k].H = (paran*distPlot[i][k].H) + sum;
          if(result[i][k].H == 1){
            result[i][k].W = 0;
            result[i][k].M = 0;
            continue;
          }
          //System.out.println("" + paran + ", " + sum + ", " + totalMoves(i,k));
        }

        //Mage
        if(distPlot[i][k].H != 1.0 && distPlot[i][k].W != 1.0){
          sum = 0;
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              int x1 = i +x;
              int y1 = k +y;
              if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              counter++;
              if(totalMoves(x1,y1)==0){
                continue;
              }
              sum += (double)distPlot[x1][y1].M * (double)((double)1/((double)away*(double)totalMoves(x1,y1)));
            }
          }
          result[i][k].M = ((double)paran*distPlot[i][k].M) + (double)sum;
          if(result[i][k].M == 1){
            result[i][k].W = 0;
            result[i][k].H = 0;
            continue;
          }
        }
        // Next steps
      }
    }
    this.distPlot = result;
  }
  //Updates the distPlot based on the possible measured movements of the opponent.
  public FOW(PNode[][] distPlot, Node[][] grid, String[][] observations, int Estimator){
    this.distPlot = distPlot;
    this.grid = grid;
    PNode[][] result = new PNode[distPlot.length][distPlot.length];
    numCount();
    int totalValues = away + proximitySum();
    //System.out.println("Total " + totalValues);
    System.out.println("FOW with Estimation");
    for (int i = 0; i < distPlot.length; i++) {
      for (int k = 0; k < distPlot.length; k++) {
        if(distPlot[i][k].isOurs || distPlot[i][k].P == 1.0 ){
          result[i][k] = new PNode(distPlot[i][k]);
          continue;
        }
        if(!isObserved(i,k, observations)){
          result[i][k] = new PNode(distPlot[i][k]);
          continue;
        }
        result[i][k] = new PNode(distPlot[i][k]);
        //double paran = 1-((double)1/(double)away);
        double sum = 0;
        int counter = 0;
        //Wumpus
        if(distPlot[i][k].M != 1.0 && distPlot[i][k].H != 1.0){
          sum = 0;
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              int x1 = i +x;
              int y1 = k +y;
              if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              counter++;
              if(totalMoves(x1,y1)==0){
                continue;
              }
              sum += (double)distPlot[x1][y1].W * (double)((double)1 + surronding(x1,y1)/((double)totalValues*totalMoves(x1,y1)));
            }
          }
          result[i][k].W = ((1-((double)1/(double)totalValues))*distPlot[i][k].W) + sum;
        }


        //Hero
        if(distPlot[i][k].M != 1.0 && distPlot[i][k].W != 1.0){
          sum = 0;
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              int x1 = i +x;
              int y1 = k +y;
              if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              counter++;
              if(totalMoves(x1,y1)==0){
                continue;
              }
              sum += (double)distPlot[x1][y1].H * (double)((double)1 + surronding(x1,y1)/((double)totalValues*totalMoves(x1,y1)));
            }
          }
          result[i][k].H = ((1-((double)1/(double)totalValues))*distPlot[i][k].H) + sum;
        }


        //Mage
        if(distPlot[i][k].H != 1.0 && distPlot[i][k].W != 1.0){
          sum = 0;
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              int x1 = i +x;
              int y1 = k +y;
              if((x == 0 && y == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              counter++;
              if(totalMoves(x1,y1)==0){
                continue;
              }
              sum += (double)distPlot[x1][y1].M * (double)((double)1 + surronding(x1,y1)/((double)totalValues*totalMoves(x1,y1)));
            }
          }
          result[i][k].M = ((double)(1-((double)1/(double)totalValues))*distPlot[i][k].M) + (double)sum;
        }

      }
    }
    this.distPlot = result;
  }

  public boolean isObserved(int i, int k, String[][] Observation){
    int totalaway = 0;



    for(int tempx=-1; tempx<=1; tempx++){
      for(int tempy=-1; tempy<=1; tempy++){
        int x = i + tempx;
        int y = k + tempy;
        if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
          continue;
        }
        if(Observation[x][y].equals("") && grid[x][y].side == 0){
          System.out.println(" here");
          return false;
        }
      }
    }
    return true;
  }
  // Returns the sum of all possible surrounding values
  public int proximitySum(){
    int totalaway = 0;
    for(int i = 0; i<distPlot.length; i++){
      for(int k = 0; k<distPlot.length; k++){
        double sum = distPlot[i][k].M + distPlot[i][k].H + distPlot[i][k].W;

        if(distPlot[i][k].isOurs || sum == 0 || distPlot[i][k].P == 1){
          continue;
        }

        for(int tempx=-1; tempx<=1; tempx++){
          for(int tempy=-1; tempy<=1; tempy++){
            int x = i + tempx;
            int y = k + tempy;
            if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
              continue;
            }
            if(distPlot[x][y].isOurs){
              totalaway++;
            }
          }
        }
      }
    }
    return totalaway;
  }
  //counts the number of surrounding values the piece has
  public int surronding(int i, int k){
    int totalaway = 0;
    for(int tempx=-1; tempx<=1; tempx++){
      for(int tempy=-1; tempy<=1; tempy++){
        int x = i + tempx;
        int y = k + tempy;
        if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
          continue;
        }
        if(distPlot[x][y].isOurs){
          totalaway++;
        }
      }
    }

    return totalaway;
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
        } else if(distPlot[x1][y1].H == 1 || distPlot[x1][y1].M == 1 || distPlot[x1][y1]. W == 1 || distPlot[x1][y1].P == 1){
          continue;
        } else {
          moves++;
        }
      }
    }
    return moves;
  }
  // Makes all the sum values out of one
  public static PNode[][] balance(PNode[][] distPlot, Node[][] grid){
    double sum =0;
    for(int i = 0; i<distPlot.length; i++){
      sum =0;
      for(int k = 0; k<distPlot.length; k++){
        sum+= distPlot[k][i].P;
      }
      //System.out.println(sum);
      if(sum!=0){
        for(int k = 0; k<distPlot.length; k++){
          distPlot[k][i].P = distPlot[k][i].P/sum;
        }
      }
    }

    for(int i = 0; i<distPlot.length; i++){
      for(int k = 0; k<distPlot.length; k++){
        if(distPlot[k][i].W == 1){
          distPlot[k][i].M = 0;
          distPlot[k][i].H = 0;

        } else if(distPlot[k][i].H == 1){
          distPlot[k][i].W = 0;
          distPlot[k][i].M = 0;
        } else if(distPlot[k][i].M == 1){
          distPlot[k][i].W = 0;
          distPlot[k][i].H = 0;
        }
      }
    }
    /*
    double[] arr =typeCount(grid);
    sum =0;
    for(int i = 0; i<distPlot.length; i++){
      for(int k = 0; k<distPlot.length; k++){
        sum+= distPlot[k][i].M;
      }
    }
    sum = sum/arr[0];
    System.out.println();
    if(sum!=0){
      for(int i = 0; i<distPlot.length; i++){
        for(int k = 0; k<distPlot.length; k++){
          distPlot[k][i].M = distPlot[k][i].M/sum;
        }
      }
    }


    sum =0;
    for(int i = 0; i<distPlot.length; i++){
      for(int k = 0; k<distPlot.length; k++){
        sum+= distPlot[k][i].W;
      }
    }
    sum = sum/arr[1];

    if(sum!=0){
      for(int i = 0; i<distPlot.length; i++){
        for(int k = 0; k<distPlot.length; k++){
          distPlot[k][i].W = distPlot[k][i].W/sum;
        }
      }
    }

    sum =0;
    for(int i = 0; i<distPlot.length; i++){
      for(int k = 0; k<distPlot.length; k++){
        sum+= distPlot[k][i].H;
      }
    }
    sum = sum/arr[2];

    if(sum!=0){
      for(int i = 0; i<distPlot.length; i++){
        for(int k = 0; k<distPlot.length; k++){
          distPlot[k][i].H = distPlot[k][i].H/sum;
        }
      }
    }*/
    return distPlot;
  }
  //Updates the probability distribution based on the movement of our agent;
  public static  PNode[][] PlayerMovement(Node[][] grid, PNode[][] distPlot){
    //System.out.println("PlayerMovement");

    if(grid == null){return null;}
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(!distPlot[i][k].isOurs && grid[i][k].side == 0){
          for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
              if(distPlot[x][y].isOurs && grid[x][y].side != 0){
                distPlot[i][k] = distPlot[x][y];
                distPlot[x][y] = new PNode(0,0,0,0);
                return balance(distPlot , grid);
              }
            }
          }


        } else if(distPlot[i][k].isOurs && grid[i][k].side != 0){
          for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
              if(!distPlot[x][y].isOurs && grid[x][y].side == 0){
                distPlot[x][y] = distPlot[i][k];
                //System.out.println("Inner");
                distPlot[i][k] = new PNode(0,0,0,0);
                return balance(distPlot , grid);
              }
            }
          }
          //System.out.println("Liner");
          distPlot[i][k] = new PNode(0,0,0,0);

        }
        //
      }
    }
    return balance(distPlot , grid);
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
          //System.out.println("Stench");
          double sum =0;
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              //  System.out.println("distPlot.W: "+distPlot[x1][y1].W);
              sum+= distPlot[x1][y1].W;
            }
          }
          //  System.out.println("Sum: "+sum);
          if(sum == 0){
            System.out.println("ERROR: Opponent's Wumpus if off the grid.");
            int produce = 0;
            for (int i = -1; i <= 1 ; i++) {
              for (int k = -1; k <= 1 ; k++) {
                int x1 = i +x;
                int y1 = k +y;
                if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                  continue;
                }
                //  System.out.println("distPlot.W: "+distPlot[x1][y1].W);
                if(distPlot[x1][y1].P!= 1.0 && distPlot[x1][y1].H != 1 && distPlot[x1][y1].M != 1 || !distPlot[x1][y1].isOurs){
                  produce++;
                }
              }
            }

            for (int i = -1; i <= 1 ; i++) {
              for (int k = -1; k <= 1 ; k++) {
                int x1 = i +x;
                int y1 = k +y;
                if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                  continue;
                }
                if(distPlot[x1][y1].P!= 1.0 && distPlot[x1][y1].H != 1 && distPlot[x1][y1].M != 1 || !distPlot[x1][y1].isOurs){
                  distPlot[x1][y1].W = 1/(double)produce;
                }
              }
            }
            return distPlot;
          }
          HashMap<Integer, Integer> proximity = new HashMap<Integer, Integer>();
          for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              //System.out.println("X and y: " + x1+", "+y1+ " New Value: "+ distPlot[x1][y1].W);
              distPlot[x1][y1].W = (double)distPlot[x1][y1].W* (double)((double)1/(double)sum);
              //System.out.println("New Value: "+ distPlot[x1][y1].W);
              proximity.put(x1,y1);

            }
          }

          double[] count = typeCount(grid);
          for (int i = 0; i < distPlot.length; i++) {
            for (int k = 0; k < distPlot.length; k++) {
              boolean test = false;
              for (int x1 = -1; x1 <= 1; x1++) {
                for (int y1 = -1; y1 <= 1; y1++) {
                  if(x+x1 == i && y + y1 == k){
                    test = true;
                  }
                }
              }
              if(test){continue;}
              distPlot[i][k].W = distPlot[i][k].W * ((count[1]-1)/count[1]);
            }
          }


        }
        else {
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
          //System.out.println("Heat");
          double sum =0;
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              //  System.out.println("distPlot.M: "+distPlot[x1][y1].M);
              sum+= distPlot[x1][y1].M;
            }
          }
          //  System.out.println("Sum: "+sum);
          if(sum == 0){
            System.out.println("ERROR: Opponent's Mage if off the grid.");
            int produce = 0;
            for (int i = -1; i <= 1 ; i++) {
              for (int k = -1; k <= 1 ; k++) {
                int x1 = i +x;
                int y1 = k +y;
                if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                  continue;
                }
                //  System.out.println("distPlot.W: "+distPlot[x1][y1].W);
                if(distPlot[x1][y1].P!= 1 && distPlot[x1][y1].H != 1 && distPlot[x1][y1].W != 1 || !distPlot[x1][y1].isOurs){
                  produce++;
                }
              }
            }

            for (int i = -1; i <= 1 ; i++) {
              for (int k = -1; k <= 1 ; k++) {
                int x1 = i +x;
                int y1 = k +y;
                if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                  continue;
                }
                if(distPlot[x1][y1].P!= 1 && distPlot[x1][y1].H != 1 && distPlot[x1][y1].W != 1 || !distPlot[x1][y1].isOurs){
                  distPlot[x1][y1].M = 1/(double)produce;
                }
              }
            }
            return distPlot;
          }
          HashMap<Integer, Integer> proximity = new HashMap<Integer, Integer>();
          for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              //System.out.println("X and y: " + x1+", "+y1+ " New Value: "+ distPlot[x1][y1].M);
              distPlot[x1][y1].M = (double)distPlot[x1][y1].M* (double)((double)1/(double)sum);
              //System.out.println("New Value: "+ distPlot[x1][y1].M);
              proximity.put(x1,y1);

            }
          }

          double[] count = typeCount(grid);
          for (int i = 0; i < distPlot.length; i++) {
            for (int k = 0; k < distPlot.length; k++) {
              boolean test = false;
              for (int x1 = -1; x1 <= 1; x1++) {
                for (int y1 = -1; y1 <= 1; y1++) {
                  if(x+x1 == i && y + y1 == k){
                    test = true;
                  }
                }
              }
              if(test){continue;}
              distPlot[i][k].M = distPlot[i][k].M * ((count[0]-1)/count[0]);
            }
          }


        }
        else {
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
          //System.out.println("Noise");
          double sum =0;
          for (int i = -1; i <= 1 ; i++) {
            for (int k = -1; k <= 1 ; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              //  System.out.println("distPlot.H: "+distPlot[x1][y1].H);
              sum+= distPlot[x1][y1].H;
            }
          }
          //  System.out.println("Sum: "+sum);
          if(sum == 0){
            System.out.println("ERROR: Opponent's Hero if off the grid.");
            int produce = 0;
            for (int i = -1; i <= 1 ; i++) {
              for (int k = -1; k <= 1 ; k++) {
                int x1 = i +x;
                int y1 = k +y;
                if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                  continue;
                }
                //  System.out.println("distPlot.W: "+distPlot[x1][y1].W);
                if(distPlot[x1][y1].P!= 1 && distPlot[x1][y1].W != 1 && distPlot[x1][y1].M != 1 || !distPlot[x1][y1].isOurs){
                  produce++;
                }
              }
            }

            for (int i = -1; i <= 1 ; i++) {
              for (int k = -1; k <= 1 ; k++) {
                int x1 = i +x;
                int y1 = k +y;
                if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                  continue;
                }
                if(distPlot[x1][y1].P!= 1 && distPlot[x1][y1].W != 1 && distPlot[x1][y1].M != 1 || !distPlot[x1][y1].isOurs){
                  distPlot[x1][y1].H = 1/(double)produce;
                }
              }
            }
            return distPlot;
          }
          HashMap<Integer, Integer> proximity = new HashMap<Integer, Integer>();
          for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
              int x1 = i +x;
              int y1 = k +y;
              if((i == 0 && k == 0) || x1<0  || y1<0 || x1>=distPlot.length  || y1>=distPlot.length){
                continue;
              }
              //System.out.println("X and y: " + x1+", "+y1+ " New Value: "+ distPlot[x1][y1].H);
              distPlot[x1][y1].H = (double)distPlot[x1][y1].H* (double)((double)1/(double)sum);
              //System.out.println("New Value: "+ distPlot[x1][y1].H);
              proximity.put(x1,y1);

            }
          }

          double[] count = typeCount(grid);
          for (int i = 0; i < distPlot.length; i++) {
            for (int k = 0; k < distPlot.length; k++) {
              boolean test = false;
              for (int x1 = -1; x1 <= 1; x1++) {
                for (int y1 = -1; y1 <= 1; y1++) {
                  if(x+x1 == i && y + y1 == k){
                    test = true;
                  }
                }
              }
              if(test){continue;}
              distPlot[i][k].H = distPlot[i][k].H * ((count[2]-1)/count[2]);
            }
          }


        }
        else {
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
        //Needs to narrow down the pit location
        if(current.contains("P") && grid[x][y].side == 0){
          int sensed = 0;
          for(int counter = 2; counter <current.length(); counter++){
            if(current.charAt(counter) == 'P'){
              sensed++;
            }
          }
          double summer = 0;
          for(int tempx=-1; tempx<=1; tempx++){
            for(int tempy=-1; tempy<=1; tempy++){
              int i = y + tempx;
              int k = x + tempy;
              if(i <0 || k<0 || i>=grid.length || k>=grid.length || (tempx==0 && tempy==0) ){
                continue;
              }
              if(distPlot[k][i].P==1){
                sensed--;
              } else {
                summer+=distPlot[k][i].P;
              }
            }
          }
          //System.out.println(summer);
          if(sensed==0){
            continue;
          }
          int levels = 0;
          int[] parts = new int[3];
          for (int i1 = -1; i1 <= 1 ; i1++) {
            int i = y + i1;
            if(i<0   || i>=distPlot.length){
              continue;
            }
            boolean flag = true;
            parts[1+i1] = 0;
            double sum = 0;
            for (int k = 0; k < distPlot.length ; k++) {
              if(distPlot[k][i].P == 1.0){
                flag = false;
              } else {sum+=distPlot[k][i].P;}
              if(distPlot[k][i].P<0||distPlot[k][i].P> 1 || Double.isNaN(distPlot[k][i].P)){distPlot[k][i].P = 0;}
            }
            if(flag && sum != 0 && !Double.isNaN(sum)){
              levels++;
              parts[1+i1] = 1;
            }
          }

          if(levels == 0){
            //System.out.println("Pit Location is either present or missing, Please consult");
            return distPlot;
          }
          for(int tempx=-1; tempx<=1; tempx++){
            for(int tempy=-1; tempy<=1; tempy++){
              if(parts[1+tempx] == 0){
                continue;
              }
              int i = y + tempx;
              int k = x + tempy;
              if(i <0 || k<0 || i>=grid.length || k>=grid.length || (tempx==0 && tempy==0) ){
                continue;
              }
              if(distPlot[k][i].P==1){continue;}
              distPlot[k][i].P = (double)distPlot[k][i].P* (double)((double)1/(double)summer);
            }
          }
          for (int i1 = -1; i1 <= 1 ; i1++) {
            if(parts[1+i1] == 0){
              continue;
            }
            int i = y + i1;
            if(i<0   || i>=distPlot.length){
              continue;
            }
            for (int k = 0; k < distPlot.length ; k++) {
              boolean test = false;
              for (int x1 = -1; x1 <= 1; x1++) {
                for (int y1 = -1; y1 <= 1; y1++) {
                  if(x+x1 == k && y + y1 == i){
                    test = true;

                  } else {
                  }
                }
              }
              if(test){continue;}
              distPlot[k][i].P = (1-(1/(double)levels)) * distPlot[k][i].P;
            }
          }
        }
        else {

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
    return balance(distPlot , grid);
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
  //Makes our agent move: Problem number 4
  public static Node[][] movement(Node[][] grid, PNode[][] distPlot, int turn){
    if(turn == 0){

      boolean flag = true;
      int[] move = new int[4];
      double max = -100;
      for(int i = 0; i<distPlot.length; i++){
        for(int k = 0; k<distPlot.length; k++){
          if(distPlot[i][k].isOurs){
            flag = false;
            double current =-20;
            int x1 = 0;
            int y1 = 0;
            switch(grid[i][k].type){
              case 'w':
              for(int tempx=-1; tempx<=1; tempx++){
                for(int tempy=-1; tempy<=1; tempy++){
                  int x = i + tempx;
                  int y = k + tempy;
                  if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
                    continue;
                  } else if( distPlot[x][y].P==1 || distPlot[x][y].isOurs){
                    continue;
                  }
                  double value = distPlot[x][y].M -(2*distPlot[x][y].H) - (.5*distPlot[x][y].W) - (2*distPlot[x][y].P);
                  if(current<value){
                    current = value;
                    x1 = x;
                    y1 = y;
                  }else if (current==value){
                    if(Math.random()>.5){
                      current = value;
                      x1 = x;
                      y1 = y;
                    }
                  }
                }
              }
              break;
              case 'm':
              for(int tempx=-1; tempx<=1; tempx++){
                for(int tempy=-1; tempy<=1; tempy++){
                  int x = i + tempx;
                  int y = k + tempy;
                  if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
                    continue;
                  } else if( distPlot[x][y].P==1 || distPlot[x][y].isOurs){
                    continue;
                  }
                  double value = distPlot[x][y].H -(2*distPlot[x][y].W) - (.5*distPlot[x][y].M) - (2*distPlot[x][y].P);
                  if(current<value){
                    current = value;
                    x1 = x;
                    y1 = y;
                  } else if (current==value){
                    if(Math.random()>.5){
                      current = value;
                      x1 = x;
                      y1 = y;
                    }
                  }
                }
              }
              break;
              case 'h':
              for(int tempx=-1; tempx<=1; tempx++){
                for(int tempy=-1; tempy<=1; tempy++){
                  int x = i + tempx;
                  int y = k + tempy;
                  if(x <0 || y<0 || x>=grid.length || y>=grid.length || (tempx==0 && tempy==0) ){
                    continue;
                  } else if( distPlot[x][y].P==1 || distPlot[x][y].isOurs){
                    continue;
                  }
                  double value = distPlot[x][y].W -(2*distPlot[x][y].M) - (.5*distPlot[x][y].H) - (2*distPlot[x][y].P);
                  if(current<value){
                    current = value;
                    x1 = x;
                    y1 = y;
                  }else if (current==value){
                    if(Math.random()>.5){
                      current = value;
                      x1 = x;
                      y1 = y;
                    }
                  }
                }
              }
              break;
            }
            if(current>max){
              int[] setter = {i,k,x1,y1};
              move = setter;
              max = current;
            }
          }
        }
      }
      /*try
      {
        Thread.sleep(1000);
      }
      catch(InterruptedException ex)
      {
        Thread.currentThread().interrupt();
      }*/
      if(flag){return null;}
      Node winner = grid[move[2]][move[3]].resolve(grid[move[0]][move[1]]);
      grid[move[2]][move[3]] = new Node(winner);
      grid[move[0]][move[1]] = new Node(move[0],move[1]);
      return grid;
    }
    Algorithm j = new Algorithm(grid, turn , 3, 0);
    if(j.finalState == null){
      //System.out.println("Game OVER");
      System.exit(0);
    }

    if(j.finalState.x == 0 || j.finalState.y == 0){
      return grid;
    }
    return j.finalState.grid;
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
  // Checks for winner
  public static boolean game(Node[][] grid){
    int home = 0;
    int away = 0;
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(grid[i][k].side ==0){
          home++;
        } else if (grid[i][k].side ==1){
          away++;
        }
      }
    }
    if(home == 0 && away == 0){
      System.out.println("DRAW!!");
      return true;
    }
    if(home != 0 && away == 0){
      System.out.println("Home Wins!!");
      return true;
    }
    if(home == 0 && away != 0){
      System.out.println("Away Wins!!");
      return true;
    }
    return false;

  }
}
