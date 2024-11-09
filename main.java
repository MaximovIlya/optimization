
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Main {


    //set the map size
    private static final int ROW = 9;
    private static final int COL = 9;
    private static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {

        //reading input
        int variant = Integer.parseInt(s.nextLine());
        int[] dest = new int[2];
        String[] destInput = s.nextLine().split(" ");
        for (int i = 0; i < destInput.length; i++) {
            dest[i] = Integer.parseInt(destInput[i]);
        }

        //creating map
        int[][] grid = {{1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1}};

        
        int[] src = {0, 0};

        //calling A* algorithm
        aStarSearch(grid, src, dest);
    }


    //function to check that it does not go beyond the boundaries of the maps
    private static boolean isValid(int row, int col) {
        return (row >= 0) && (row < ROW) && (col >= 0)
                && (col < COL);
    }

    private static boolean isUnBlocked(int[][] grid,
            int row, int col) {
        return grid[row][col] == 1;
    }


    //check that we have reached our destination
    private static boolean isDestination(int row, int col,
            int[] dest) {
        return row == dest[0] && col == dest[1];
    }

    //calculating heuristic value
    private static double calculateHValue(int row, int col,
            int[] dest) {
        return Math.abs(row - dest[0]) + Math.abs(col - dest[1]);
    }


    private static double checkHValue(int x, int y,
            int i, int j) {
        return Math.abs(x - i) + Math.abs(y - j);
    }

    //function to find the path
    private static void tracePath(Cell[][] cellDetails,
            int[] dest) {

        int row = dest[0];
        int col = dest[1];

        Map<int[], Boolean> path = new LinkedHashMap<>();

        while (!(cellDetails[row][col].parent_i == row
                && cellDetails[row][col].parent_j == col)) {
            path.put(new int[]{row, col}, true);
            int temp_row = cellDetails[row][col].parent_i;
            int temp_col = cellDetails[row][col].parent_j;
            row = temp_row;
            col = temp_col;
        }

        path.put(new int[]{row, col}, true);
        List<int[]> pathList
                = new ArrayList<>(path.keySet());
        Collections.reverse(pathList);
        System.out.println("e " + (pathList.size() - 1));
    }


    //same function with previous for solving problem with teleportation
    private static List<int[]> tracePathTeleport(Cell[][] cellDetails,
            int row, int col) {

        Map<int[], Boolean> path = new LinkedHashMap<>();

        while (!(cellDetails[row][col].parent_i == row
                && cellDetails[row][col].parent_j == col)) {
            path.put(new int[]{row, col}, true);
            int temp_row = cellDetails[row][col].parent_i;
            int temp_col = cellDetails[row][col].parent_j;
            row = temp_row;
            col = temp_col;
        }

        path.put(new int[]{row, col}, true);
        List<int[]> pathList
                = new ArrayList<>(path.keySet());
        Collections.reverse(pathList);

        return pathList;
    }

    //A* search algorithm
    private static void aStarSearch(int[][] grid, int[] src,
            int[] dest) {
        //printing e -1 if the map is unsolvable
        if (!isValid(src[0], src[1])
                || !isValid(dest[0], dest[1])) {
            System.out.println(
                    "e -1");
            return;
        }

        if (!isUnBlocked(grid, src[0], src[1])
                || !isUnBlocked(grid, dest[0], dest[1])) {
            System.out.println(
                    "e -1");
            return;
        }

        //if destination == start print e 0
        if (isDestination(src[0], src[1], dest)) {
            System.out.println(
                    "e 0");
            return;
        }

        //creating list to store cell that are in the path
        boolean[][] closedList = new boolean[ROW][COL];
        //creating cellDetail ti store information about eanch cell
        Cell[][] cellDetails = new Cell[ROW][COL];

        //filling cellDetails with initial values
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                cellDetails[i][j] = new Cell();
                cellDetails[i][j].f
                        = Double.POSITIVE_INFINITY;
                cellDetails[i][j].g
                        = Double.POSITIVE_INFINITY;
                cellDetails[i][j].h
                        = Double.POSITIVE_INFINITY;
                cellDetails[i][j].parent_i = -1;
                cellDetails[i][j].parent_j = -1;
            }
        }

        int i = src[0], j = src[1];
        cellDetails[i][j].f = 0;
        cellDetails[i][j].g = 0;
        cellDetails[i][j].h = 0;
        cellDetails[i][j].parent_i = i;
        cellDetails[i][j].parent_j = j;
        CurrentCell currentCell = new CurrentCell(i, j);

        //creating priority queue to store neighbors and sort them by h value and g value
        PriorityQueue<Node> openList = new PriorityQueue<>(new NodeComparator());
        openList.add(new Node(0.0, 0.0, i, j));

        boolean foundDest = false;

        //run the loop as long as there are neighbors in the queue
        while (!openList.isEmpty()) {

            //list that keeps path from currentCell to start cell
            List<int[]> teleportList1 = tracePathTeleport(cellDetails, currentCell.x, currentCell.y);

            //get the neighbor with the smallest values ​​from the queue
            Node p = openList.poll();
            //call a function to calculate h value to check whether it is possible to get from one cell to another in 1 to solve the problem with teleportation
            double hVal = checkHValue(currentCell.x, currentCell.y, p.i, p.j);
            i = p.i;
            j = p.j;
            currentCell.x = i;
            currentCell.y = j;
            //if we can get from one cell to another in 1 then display the position
            if (hVal == 1 || hVal == 0) {

                closedList[i][j] = true;

                System.out.println("m" + " " + i + " " + j);
            } else {
                //if we can get from one cell to another in 1 then we need go back to the cell which is parent of the smallest neighbor from the Queue
                //creating list the keeps path from start cell to the cell which is parent of the neighbor 
                List<int[]> teleportList2 = tracePathTeleport(cellDetails, currentCell.x, currentCell.y);
                currentCell.x = i;
                currentCell.y = j;
                //below we are looking for finding common parent of the neighbor for teleportList1 and teleportList2
                Set<List<Integer>> set1 = new HashSet<>();
                for (int[] arr : teleportList1) {
                    set1.add(Arrays.asList(arr[0], arr[1]));
                }

                Set<List<Integer>> set2 = new HashSet<>();
                for (int[] arr : teleportList2) {
                    set2.add(Arrays.asList(arr[0], arr[1]));
                }

                List<int[]> result = new ArrayList<>();

                for (List<Integer> item : set1) {
                    if (set2.contains(item)) {
                        result.add(new int[]{item.get(0), item.get(1)});
                    }
                }

                result.sort((a, b) -> {
                    if (a[0] != b[0]) {
                        return Integer.compare(a[0], b[0]);
                    } else {
                        return Integer.compare(a[1], b[1]);
                    }
                });

                int[] commonParent = result.get(result.size() - 1);
                Collections.reverse(teleportList1);
                //going down the teleportList1 to the commonParent
                for (int v = 0; v < teleportList1.size(); v++) {
                    //printing the position
                    System.out.println("m" + " " + teleportList1.get(v)[0] + " " + teleportList1.get(v)[1]);

                    //reading input
                    int enemyCount = Integer.parseInt(s.nextLine());
                    if (enemyCount != 0) {
                        for (int n = 0; n < enemyCount; n++) {
                            String[] cordinatesInput = s.nextLine().split(" ");

                        }
                    }
                    //if we reached current parent we break the loop
                    if (teleportList1.get(v)[0] == commonParent[0] && teleportList1.get(v)[1] == commonParent[1]) {
                        break;
                    }

                }
                //we check that we can get from common parent to the neighbor in 1
                if (checkHValue(commonParent[0], commonParent[1], i, j) == 1) {
                    //if it is true printing the position
                    System.out.println("m" + " " + i + " " + j);

                } else {
                    

                    //if we can't get from common parent to the neighbor then we find the way from the current parent to the current position
                    boolean flag = false;
                    for (int u = 0; u < teleportList2.size(); u++) {
                        int[] item = teleportList2.get(u);
                        if (flag) {
                            //printing new position
                            System.out.println("m" + " " + item[0] + " " + item[1]);
                            //if u is not the last cell in teleportList2 then we read input
                            if (u != (teleportList2.size()-1)) {
                                //reading input
                                int enemyCount = Integer.parseInt(s.nextLine());
                                if (enemyCount != 0) {
                                    for (int n = 0; n < enemyCount; n++) {
                                        String[] cordinatesInput = s.nextLine().split(" ");

                                    }
                                }
                            }
                        }
                        //if coordinates of cuurent position are equal to common parents's position then flag is true
                        if (item[0] == commonParent[0] && item[1] == commonParent[1]) {
                            flag = true;
                        }

                    }
                }

            }

            //reading input

            int enemyCount = Integer.parseInt(s.nextLine());
            if (enemyCount != 0) {
                for (int n = 0; n < enemyCount; n++) {
                    String[] cordinatesInput = s.nextLine().split(" ");
                    //checking for enemies
                    if ("P".equals(cordinatesInput[2]) || "S".equals(cordinatesInput[2]) || "A".equals(cordinatesInput[2])) {
                        int[] cordinates = new int[2];

                        for (int m = 0; m < 2; m++) {
                            cordinates[m] = Integer.parseInt(cordinatesInput[m]);
                        }

                        grid[cordinates[0]][cordinates[1]] = 0;
                    }
                }
            }

            double gNew, hNew, fNew;

            //findig neigbors in all four direction and adding them to the queue
            if (isValid(i - 1, j)) {
                if (isDestination(i - 1, j, dest)) {
                    cellDetails[i - 1][j].parent_i = i;
                    cellDetails[i - 1][j].parent_j = j;
                    tracePath(cellDetails, dest);

                    return;
                } else if (!closedList[i - 1][j]
                        && isUnBlocked(grid, i - 1, j)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i - 1, j, dest);
                    fNew = gNew + hNew;

                    if (cellDetails[i - 1][j].f
                            == Double.POSITIVE_INFINITY
                            || cellDetails[i - 1][j].f > fNew) {
                        openList.add(new Node(fNew, hNew, i - 1, j));

                        cellDetails[i - 1][j].f = fNew;
                        cellDetails[i - 1][j].g = gNew;
                        cellDetails[i - 1][j].h = hNew;
                        cellDetails[i - 1][j].parent_i = i;
                        cellDetails[i - 1][j].parent_j = j;
                    }
                }
            }

            
            

            
            if (isValid(i, j + 1)) {
                if (isDestination(i, j + 1, dest)) {
                    cellDetails[i][j + 1].parent_i = i;
                    cellDetails[i][j + 1].parent_j = j;
                    tracePath(cellDetails, dest);
                    return;
                } else if (!closedList[i][j + 1]
                        && isUnBlocked(grid, i, j + 1)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i, j + 1, dest);
                    fNew = gNew + hNew;

                    if (cellDetails[i][j + 1].f
                            == Double.POSITIVE_INFINITY
                            || cellDetails[i][j + 1].f > fNew) {
                        openList.add(new Node(fNew, hNew, i, j + 1));

                        cellDetails[i][j + 1].f = fNew;
                        cellDetails[i][j + 1].g = gNew;
                        cellDetails[i][j + 1].h = hNew;
                        cellDetails[i][j + 1].parent_i = i;
                        cellDetails[i][j + 1].parent_j = j;
                    }
                }
            }

            if (isValid(i + 1, j)) {
                if (isDestination(i + 1, j, dest)) {
                    cellDetails[i + 1][j].parent_i = i;
                    cellDetails[i + 1][j].parent_j = j;
                    tracePath(cellDetails, dest);
                    return;
                } else if (!closedList[i + 1][j]
                        && isUnBlocked(grid, i + 1, j)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i + 1, j, dest);
                    fNew = gNew + hNew;

                    if (cellDetails[i + 1][j].f
                            == Double.POSITIVE_INFINITY
                            || cellDetails[i + 1][j].f > fNew) {
                        openList.add(new Node(fNew, hNew, i + 1, j));

                        cellDetails[i + 1][j].f = fNew;
                        cellDetails[i + 1][j].g = gNew;
                        cellDetails[i + 1][j].h = hNew;
                        cellDetails[i + 1][j].parent_i = i;
                        cellDetails[i + 1][j].parent_j = j;
                    }
                }
            }

            
            if (isValid(i, j - 1)) {
                if (isDestination(i, j - 1, dest)) {
                    cellDetails[i][j - 1].parent_i = i;
                    cellDetails[i][j - 1].parent_j = j;
                    tracePath(cellDetails, dest);
                    return;
                } else if (!closedList[i][j - 1]
                        && isUnBlocked(grid, i, j - 1)) {
                    gNew = cellDetails[i][j].g + 1;
                    hNew = calculateHValue(i, j - 1, dest);
                    fNew = gNew + hNew;

                    if (cellDetails[i][j - 1].f
                            == Double.POSITIVE_INFINITY
                            || cellDetails[i][j - 1].f > fNew) {
                        openList.add(new Node(fNew, hNew, i, j - 1));

                        cellDetails[i][j - 1].f = fNew;
                        cellDetails[i][j - 1].g = gNew;
                        cellDetails[i][j - 1].h = hNew;
                        cellDetails[i][j - 1].parent_i = i;
                        cellDetails[i][j - 1].parent_j = j;
                    }
                }
            }
        }
        //if we didn't reach the destination print e -1

        if (!foundDest) {
            System.out.println(
                    "e -1");
        }
    }
}

//class for storing information of the each cell
class Cell {

    int parent_i, parent_j;
    double f, g, h;

    Cell() {
        this.parent_i = 0;
        this.parent_j = 0;
        this.f = 0;
        this.g = 0;
        this.h = 0;
    }
}

//class for storing information for current cell

class CurrentCell {

    int x;
    int y;

    CurrentCell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

//class for storing inforamtion of the neighbor
class Node {

    double f;
    double h;
    int i, j;

    Node(double f, double h, int i, int j) {
        this.f = f;
        this.i = i;
        this.j = j;
        this.h = h;
    }
}

//class for sorting neighbors that are in the queue
class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        
        int compareF = Double.compare(n1.f, n2.f);
        if (compareF != 0) {
            return compareF; 
        }
        
        return Double.compare(n1.h, n2.h);
    }
}
