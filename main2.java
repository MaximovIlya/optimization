
import java.util.*;

public class Main {

    private static final int ROW = 9;
    private static final int COL = 9;
    private static Scanner s = new Scanner(System.in);
    //creating a variable for finding minimal path
    private static int minimalCost = 100000;

    public static void main(String[] args) {
        //reading input
        int variant = Integer.parseInt(s.nextLine());
        int[] dest = new int[2];
        String[] destInput = s.nextLine().split(" ");
        for (int i = 0; i < destInput.length; i++) {
            dest[i] = Integer.parseInt(destInput[i]);
        }

        //creating a map 

        int[][] grid = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        //creating two-dimensional array for storing g costs of all cells

        int[][] g_costs = new int[9][9];
        

        //filling cells of two-dimensinal array with high number
        for (int i = 0; i < g_costs.length; i++) {
            for (int j = 0; j < g_costs.length; j++) {
                g_costs[i][j] = 10000;
            }
        }

        g_costs[0][0] = 0;

        int[] src = {0, 0};


        //a variable with which we will compare cells by g value in order to optimize the algorithm
        int currentCost = 0;


        //calling dfs function

        dfs(grid, src[0], src[1], dest, currentCost, g_costs);

        //if minimal costs == 100000 that means that map is unsolvable
        if (minimalCost == 100000) {
            System.out.println("e -1");
        } else {
            //print minimal path length
            System.out.println("e" + " " + minimalCost);
        }
       

    }

    //function to check that it does not go beyond the boundaries of the maps

    private static boolean isValid(int row, int col) {
        return (row >= 0) && (row < ROW) && (col >= 0) && (col < COL);
    }

    private static boolean isUnBlocked(int[][] grid, int row, int col) {
        return grid[row][col] == 1;
    }

    private static void dfs(int[][] grid, int row, int col, int[] dest, int currentCost, int[][] g_costs) {


        if (!isValid(row, col) || !isUnBlocked(grid, row, col)) {
            return;
        }

        //printing current cell 
        System.out.println("m" + " " + row + " " + col);


        //reading input
        int enemyCount = Integer.parseInt(s.nextLine());
        for (int n = 0; n < enemyCount; n++) {
            String[] coordinatesInput = s.nextLine().split(" ");
            //checking for enemies
            if ("P".equals(coordinatesInput[2]) || "S".equals(coordinatesInput[2]) || "A".equals(coordinatesInput[2])) {
                int rowInput = Integer.parseInt(coordinatesInput[0]);
                int colInput = Integer.parseInt(coordinatesInput[1]);
                grid[rowInput][colInput] = 0;
            }
        }

        //all possible direction  
        int[] rowDir = {-1, 0, 1, 0};
        int[] colDir = {0, 1, 0, -1};


        //finding neighbors
        for (int i = 0; i < 4; i++) {
            int newRow = row + rowDir[i];
            int newCol = col + colDir[i];
            //if reached the destination update minimalCost
            if (newRow == dest[0] && newCol == dest[1]) {
                if (currentCost + 1 < minimalCost) {
                    minimalCost = currentCost + 1;
                }
            }

            //if neighbour does not go beyond boundaries compare neghbour's g cost with current cost
            if (isValid(newRow, newCol) && isUnBlocked(grid, newRow, newCol)) {
                //if currentCost + 1 smaller than g cost of current cell then we update g cost of current cell
                if (currentCost + 1 < g_costs[newRow][newCol]) {
                    g_costs[newRow][newCol] = currentCost + 1;
                    //calling dfs with new neighbor
                    dfs(grid, newRow, newCol, dest, currentCost + 1, g_costs);

                    //printing position
                    System.out.println("m" + " " + row + " " + col);
                    
                    //reading input
                    enemyCount = Integer.parseInt(s.nextLine());
                    for (int n = 0; n < enemyCount; n++) {
                        String[] coordinatesInput = s.nextLine().split(" ");
                        if ("P".equals(coordinatesInput[2]) || "S".equals(coordinatesInput[2]) || "A".equals(coordinatesInput[2])) {
                            int rowInput = Integer.parseInt(coordinatesInput[0]);
                            int colInput = Integer.parseInt(coordinatesInput[1]);
                            grid[rowInput][colInput] = 0;
                        }
                    }
                }

            }
        }
    }
}
