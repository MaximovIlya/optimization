public class Main3 {

    public static void main(String[] args) {
        String [][] array = new String [9][9];
        
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                array[i][j] = ".";
            }
        }

        array[0][0] = "N";
        array[0][2] = "P";
        array[0][1] = "P";
        array[1][1] = "P";
        array[1][2] = "A";
        array[2][1] = "P";
        array[2][2] = "P";
        array[0][8] = "K";
        array[0][3] = "P";
        array[1][3] = "P";
        array[2][3] = "P";

        array[3][1] = "P";
        array[4][1] = "S";
        array[4][0] = "P";
        array[5][1] = "P";
        array[4][2] = "P";



        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();  
        }
    }
}
