
import java.util.Scanner;

public class main {

    public static double[][] simplexMethod(double[] c, double[][] A, double[] b) {
        double[][] tableau = new double[A.length + 1][A[0].length + A.length + 1];

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                tableau[i][j] = A[i][j];
            }
            tableau[i][A[0].length + i] = 1;
            tableau[i][tableau[0].length - 1] = b[i];
        }

        for (int i = 0; i < c.length; i++) {
            tableau[A.length][i] = -c[i];
        }

        return calculations(tableau);
    }

    public static int[] pivot(double[][] tableau) {
        int[] pivot = {0, 0};
        double rowPiv = 0;

        
        for (int i = 0; i < tableau[0].length - 1; i++) {
            if (tableau[tableau.length - 1][i] < rowPiv) {
                rowPiv = tableau[tableau.length - 1][i];
                pivot[1] = i;  
            }
        }

        
        if (rowPiv == 0) {
            return null;
        }

        double colPiv = Double.POSITIVE_INFINITY;
        boolean hasPositiveRatio = false; 

        
        for (int i = 0; i < tableau.length - 1; i++) {
            if (tableau[i][pivot[1]] > 0) {  
                double ratio = tableau[i][tableau[i].length - 1] / tableau[i][pivot[1]];  
                if (ratio < colPiv) {
                    colPiv = ratio;
                    pivot[0] = i;  
                }
                hasPositiveRatio = true;  
            }
        }

        
        if (!hasPositiveRatio) {
            
            return null;
        }

        return pivot;
    }

    public static double[][] calculations(double[][] tableau) {
        boolean optimal = false;

        while (!optimal) {

            int[] pivot = pivot(tableau);

            if (pivot == null) {
                return null;
            }

            int pivotRow = pivot[0];
            int pivotCol = pivot[1];

            double pivotValue = tableau[pivotRow][pivotCol];
            for (int i = 0; i < tableau[pivotRow].length; i++) {
                tableau[pivotRow][i] /= pivotValue;
            }

            for (int i = 0; i < tableau.length; i++) {
                if (i != pivotRow) {
                    double factor = tableau[i][pivotCol];
                    for (int j = 0; j < tableau[i].length; j++) {
                        tableau[i][j] -= factor * tableau[pivotRow][j];
                    }
                }
            }

            optimal = true;
            for (int i = 0; i < tableau[tableau.length - 1].length - 1; i++) {
                if (tableau[tableau.length - 1][i] < 0) {
                    optimal = false;
                    break;
                }
            }
        }

        return tableau;
    }

    public static void printTableau(double[][] tableau) {

        if (tableau == null) {
            System.out.println("The method is not applicable!");
            return;
        }
        int numVariables = tableau[0].length - tableau.length;


        System.out.println("Maximum value: " + tableau[tableau.length - 1][tableau[0].length - 1]);

        for (int j = 0; j < numVariables; j++) {
            boolean isBasic = false;
            double value = 0;

            for (int i = 0; i < tableau.length - 1; i++) {
                if (tableau[i][j] == 1) {

                    boolean allZeros = true;
                    for (int k = 0; k < tableau.length - 1; k++) {
                        if (k != i && tableau[k][j] != 0) {
                            allZeros = false;
                            break;
                        }
                    }

                    if (allZeros) {
                        isBasic = true;
                        value = tableau[i][tableau[0].length - 1];
                        break;
                    }
                }
            }

            if (isBasic) {
                System.out.println("x" + (j + 1) + " = " + value);
            } else {
                System.out.println("x" + (j + 1) + " = 0");
            }
        }

    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter vector of coefficients:");
        String[] inputC = s.nextLine().split(" ");
        double[] c = new double[inputC.length];
        for (int i = 0; i < inputC.length; i++) {
            c[i] = Integer.parseInt(inputC[i]);
        }
        System.out.println("Enter vector of right-hand side numbers:");
        String[] inputB = s.nextLine().split(" ");
        double[] b = new double[inputB.length];
        for (int i = 0; i < inputB.length; i++) {
            b[i] = Integer.parseInt(inputB[i]);
        }

        String[][] inputA = new String[inputB.length][inputC.length];
        System.out.println("Enter matrix of coefficients:");
        for (int i = 0; i < inputB.length; i++) {
            String[] inputLine = s.nextLine().split(" ");

            for (int j = 0; j < inputC.length; j++) {
                if (j < inputLine.length) {
                    inputA[i][j] = inputLine[j];
                } else {
                    inputA[i][j] = "";
                }
            }
        }

        double[][] A = new double[inputB.length][inputC.length];
        for (int i = 0; i < inputB.length; i++) {
            for (int j = 0; j < inputC.length; j++) {
                A[i][j] = Integer.parseInt(inputA[i][j]);
            }
        }
        double[][] table = simplexMethod(c, A, b);
        printTableau(table);
    }
}
