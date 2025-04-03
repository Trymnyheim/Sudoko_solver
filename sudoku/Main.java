public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Provide filename, size and size of inner squares as command-line argument.");
            System.exit(-1);
        }
        int size = Integer.parseInt(args[1]);
        int innerSize = Integer.parseInt(args[2]);
        Sudoku sudoku = new Sudoku(size, innerSize, args[0]);
        System.out.println("\nSudoku:");
        System.out.println(sudoku);
        Double duration = sudoku.solveSudoku();
        if (duration <= 0) {
            System.out.println("Unable to solve sudoku.");
        } else {
            System.out.println("Solved Sudoku (in "+ duration + " ms):");
            System.out.println(sudoku);
        }
    }
}