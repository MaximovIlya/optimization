
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    private static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        String[] inputSupply = s.nextLine().split(" ");
        int[] supply = new int[inputSupply.length];
        int countSup = 0;
        int countDemand = 0;
        for (int i = 0; i < inputSupply.length; i++) {
            supply[i] = Integer.parseInt(inputSupply[i]);
            countSup += supply[i];
            if (supply[i] < 0) {
                System.out.println("The method is not applicable!");
                break;
            }
        }

        int[][] matrixOfCosts = new int[3][4];
        String[][] inputMatrixOfCosts = new String[3][4];
        for (int i = 0; i < 3; i++) {
            String[] inputLine = s.nextLine().split(" ");
            for (int j = 0; j < 4; j++) {
                inputMatrixOfCosts[i][j] = inputLine[j];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                matrixOfCosts[i][j] = Integer.parseInt(inputMatrixOfCosts[i][j]);
                if (matrixOfCosts[i][j] < 0) {
                    System.out.println("The method is not applicable!");
                    break;
                }
            }
        }

        String[] inputDemand = s.nextLine().split(" ");
        int[] demand = new int[inputDemand.length];
        for (int i = 0; i < inputDemand.length; i++) {
            demand[i] = Integer.parseInt(inputDemand[i]);
            countDemand += demand[i];
            if (demand[i] < 0) {
                System.out.println("The method is not applicable!");
                return;
            }
        }

        if (countDemand != countSup) {
            System.out.println("The problem is not balanced!");
            return;
        }

        int[][] finalMatrix = northWest(supply, demand, matrixOfCosts);

        System.out.println("North West");
        int totalCost = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                totalCost += finalMatrix[i][j];
                System.out.print(finalMatrix[i][j] + " ");
            }
            System.out.println("");
        }

        System.out.println("North west total cost: " + totalCost);

        System.out.println("Vogel's");
        int vogelTotalCost = 0;
        int[][] vogelMatrix = vogel(supply, demand, matrixOfCosts);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                vogelTotalCost += vogelMatrix[i][j];
                System.out.print(vogelMatrix[i][j] + " ");
            }
            System.out.println("");
        }

        System.out.println("Vogel's total cost: " + vogelTotalCost);

        System.out.println("Russell's");
        int countRussell = 0;
        int[][] russellMatrix = russell(supply, demand, matrixOfCosts);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(russellMatrix[i][j] + " ");
                countRussell += russellMatrix[i][j];
            }
            System.out.println("");
        }

        System.out.println("Russell's total: " + countRussell);

    }

    private static int[][] northWest(int[] supply, int[] demand, int[][] matrix) {
        int i = 0, j = 0;
        int[][] resultMatrix = new int[3][4];
        int[][] copiedMatrix = Arrays.copyOf(matrix, matrix.length);
        int[] copiedSupply = Arrays.copyOf(supply, supply.length);
        int[] copiedDemand = Arrays.copyOf(demand, demand.length);

        while (i < copiedSupply.length && j < copiedDemand.length) {
            int allocatedAmount = Math.min(copiedSupply[i], copiedDemand[j]);
            resultMatrix[i][j] = allocatedAmount * copiedMatrix[i][j];

            copiedSupply[i] -= allocatedAmount;
            copiedDemand[j] -= allocatedAmount;

            if (copiedSupply[i] == 0) {
                i++;
            } else if (copiedDemand[j] == 0) {
                j++;
            }
        }

        return resultMatrix;
    }

    private static int[][] vogel(int[] supply, int[] demand, int[][] matrix) {
        int[][] resultMatrix = new int[3][4];
        int[] copiedSupply = Arrays.copyOf(supply, supply.length);
        int[] copiedDemand = Arrays.copyOf(demand, demand.length);
        int[][] copiedMatrix = new int[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            copiedMatrix[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        while (Arrays.stream(copiedSupply).sum() > 0 && Arrays.stream(copiedDemand).sum() > 0) {

            int[] rowPenalties = new int[3];
            int[] colPenalties = new int[4];
            for (int i = 0; i < copiedMatrix.length; i++) {
                rowPenalties[i] = copiedSupply[i] > 0 ? calculatePenalty(copiedMatrix[i]) : -1;
            }
            for (int j = 0; j < copiedMatrix[0].length; j++) {
                int[] column = new int[copiedMatrix.length];
                for (int i = 0; i < copiedMatrix.length; i++) {
                    column[i] = copiedMatrix[i][j];
                }
                colPenalties[j] = copiedDemand[j] > 0 ? calculatePenalty(column) : -1;
            }

            int maxRowPenalty = Arrays.stream(rowPenalties).max().orElse(-1);
            int maxColPenalty = Arrays.stream(colPenalties).max().orElse(-1);

            int row, col;
            if (maxRowPenalty >= maxColPenalty) {
                row = indexOf(rowPenalties, maxRowPenalty);
                col = indexOf(copiedMatrix[row], Arrays.stream(copiedMatrix[row]).min().getAsInt());
            } else {
                col = indexOf(colPenalties, maxColPenalty);
                row = findMinInColumn(copiedMatrix, col);
            }

            int allocation = Math.min(copiedSupply[row], copiedDemand[col]);
            resultMatrix[row][col] = allocation * copiedMatrix[row][col];
            copiedSupply[row] -= allocation;
            copiedDemand[col] -= allocation;

            if (copiedSupply[row] == 0) {
                Arrays.fill(copiedMatrix[row], Integer.MAX_VALUE);
            }
            if (copiedDemand[col] == 0) {
                for (int i = 0; i < copiedMatrix.length; i++) {
                    copiedMatrix[i][col] = Integer.MAX_VALUE;
                }
            }
        }
        return resultMatrix;
    }

    private static int  calculatePenalty(int[] line) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        for (int cost : line) {
            if (cost < min1) {
                min2 = min1;
                min1 = cost;
            } else if (cost < min2) {
                min2 = cost;
            }
        }
        return min2 == Integer.MAX_VALUE ? 0 : min2 - min1;
    }

    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private static int findMinInColumn(int[][] matrix, int col) {
        int min = Integer.MAX_VALUE, minRow = -1;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][col] < min) {
                min = matrix[i][col];
                minRow = i;
            }
        }
        return minRow;
    }

    private static int[][] russell(int[] supply, int[] demand, int[][] matrix) {
        int[][] copiedMatrix = Arrays.copyOf(matrix, matrix.length);
        int[][] currentMatrix = new int[3][4];
        int[][] resultMatrix = new int[3][4];
        int[] copiedSupply = Arrays.copyOf(supply, supply.length);
        int[] copiedDemand = Arrays.copyOf(demand, demand.length);
        int[] maxRows = new int[3];
        int[] maxCols = new int[4];

        while (Arrays.stream(copiedSupply).sum() > 0 && Arrays.stream(copiedDemand).sum() > 0) {
            for (int i = 0; i < 3; i++) {
                maxRows[i] = Arrays.stream(copiedMatrix[i]).max().getAsInt();
            }

            for (int i = 0; i < 4; i++) {
                int[] column = new int[4];
                for (int j = 0; j < 3; j++) {
                    column[j] = copiedMatrix[j][i];

                }

                maxCols[i] = Arrays.stream(column).max().getAsInt();
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (copiedMatrix[i][j] == 0) {
                        currentMatrix[i][j] = Integer.MAX_VALUE;
                    } else {
                        currentMatrix[i][j] = copiedMatrix[i][j] - maxRows[i] - maxCols[j];
                    }
                    
                }
            }

            int row = 0;
            int col = 0;

            int min = 10000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (currentMatrix[i][j] < min) {
                        min = currentMatrix[i][j];
                        row = i;
                        col = j;

                    }
                }
            }

            resultMatrix[row][col] = copiedMatrix[row][col] * Math.min(copiedSupply[row], copiedDemand[col]);

            int minimum = Math.min(copiedSupply[row], copiedDemand[col]);
            copiedSupply[row] -= minimum;
            copiedDemand[col] -= minimum;
            if (copiedSupply[row] == 0) {
                Arrays.fill(copiedMatrix[row], 0);
            }
            if (copiedDemand[col] == 0) {
                for (int i = 0; i < copiedMatrix.length; i++) {
                    copiedMatrix[i][col] = 0;
                }
            }
              
        }

        return resultMatrix;

    }
}
