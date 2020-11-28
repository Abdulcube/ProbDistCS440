public class FOW{
  
  PNode[][] distPlot;
  public FOW(PNode[][] distPlot, Node[][] grid, String[][] observations){
    this.distPlot = distPlot;
  }

  public static String[][] obsCheck(Node[][] grid){
    String[][] matrix = new String[grid.length][grid.length];
    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        if(grid[i][k].side == 0){
          String curr = "";
          for(int tempx=-1; tempx<=1; tempx++){
            for(int tempy=-1; tempy<=1; tempy++){
              int x = i + tempx;
              int y = k + tempy;
              if(x <0 || y<0 || x>=grid.length || y>=grid.length || (y==0 && x==0) ){
                continue;
              }
              if(grid[x][y].side==1){
                curr = curr.concat(Character.toString(grid[x][y].type));
              } else if(grid[x][y].isPit){
                curr = curr.concat("p");
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

  public static void obsTraverse(String[][] grid){

    for(int i = 0; i<grid.length; i++){
      for(int k = 0; k<grid.length; k++){
        System.out.print(grid[i][k]+" , ");
      }
      System.out.println();
    }
  }
}
