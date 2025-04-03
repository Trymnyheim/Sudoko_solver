import java.io.*;
import java.util.*;

public class testSudoku {
    
    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku(9, 3, "sudoku1.in");
        System.out.println("\nRunning test:\n");
        testChecks(sudoku);
        testCheckNumber(sudoku);
        checkSolution(sudoku);
        System.out.println("Test is successful if no other output is given.\n");
    }

    public static void testChecks(Sudoku sudoku) {
        int i = 1, j = 3;
        HashSet<Integer> set;
        set = sudoku.rows.get(i);
        if (set.size() != 3 && !set.contains(3) && !set.contains(7) && !set.contains(8))
            System.out.println("Fail: Check-structure of row does not contain the right values.");
        set = sudoku.cols.get(j);
        if (set.size() != 2 && !set.contains(7) && !set.contains(8))
            System.out.println("Fail: Check-structure of cols does not contain the right values.");
        set = sudoku.squares.get(sudoku.getSquare(i, j));
        if (set.size() != 3 && !set.contains(5) && !set.contains(7) && !set.contains(2))
            System.out.println("Fail: Check-structure of cols does not contain the right values.");
    }

    public static void testCheckNumber(Sudoku sudoku) {
        int i = 1, j = 3;
        if (sudoku.checkNumber(1, i, j) == false) System.out.println("Fail test 1.");
        if (sudoku.checkNumber(2, i, j) == true) System.out.println("Fail test 2.");
        if (sudoku.checkNumber(3, i, j) == true) System.out.println("Fail test 3.");
        if (sudoku.checkNumber(4, i, j) == false) System.out.println("Fail test 4.");
        if (sudoku.checkNumber(5, i, j) == true) System.out.println("Fail test 5.");
        if (sudoku.checkNumber(6, i, j) == false) System.out.println("Fail test 6.");
        if (sudoku.checkNumber(7, i, j) == true) System.out.println("Fail test 7.");
        if (sudoku.checkNumber(8, i, j) == true) System.out.println("Fail test 8.");
        if (sudoku.checkNumber(9, i, j) == false) System.out.println("Fail test 9.");
    }

    public static void checkSolution(Sudoku sudoku) {
        int size = 9;
        File file = new File("sudoku1_check.in");
        int[][] checkBoard = new int[size][size];
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextInt()) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        checkBoard[i][j] = scanner.nextInt();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException | InputMismatchException e) {
            System.out.println(e);
            System.exit(-1);
        }
        sudoku.solveSudoku();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (checkBoard[i][j] != sudoku.board[i][j]) {
                    System.out.println("Test failed at (" + i + ", " + j + ").");
                    return;
                }
            }
        }
    }
}