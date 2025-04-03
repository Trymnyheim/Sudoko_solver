/*
 * Creates a board object with check-structures to see if an added value fits within the rules.
 * Initialization fails if the width of the inner squares does not fit within the width of the board.
*/

import java.util.*;
import java.io.*;

public class Sudoku {
    
    int[][] board;
    int size;
    int innerSize;
    HashMap<Integer, HashSet<Integer>> rows = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> cols = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> squares = new HashMap<>();

    public Sudoku(int size, int innerSize, String filename) {
        if (size % innerSize != 0) {
            System.out.println("Width of board does not match width of inner squares.");
            System.exit(-1);
        }
        board = new int[size][size];
        this.size = size;
        this.innerSize = innerSize;
        createBoard(filename);
    }

    private void createBoard(String filename) {
        // Initializing check-structures:
        for (int i = 0; i < size; i++) {
            rows.put(i, new HashSet<Integer>());
            cols.put(i, new HashSet<Integer>());
            squares.put(i, new HashSet<Integer>());
        }

        // Read input file and add to board:
        File file = new File("./files/" + filename);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextInt()) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        board[i][j] = scanner.nextInt();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException | InputMismatchException e) {
            System.out.println(e);
            System.exit(-1);
        }
        setChecks();
    }

    // Adds all numbers into their check-structures:
    private void setChecks() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0)
                    continue;
                addToCheck(i, j);
            }
        }
    }

    // Adds a single number into check-structures:
    private void addToCheck(int i, int j) {
        rows.get(i).add(board[i][j]);
        cols.get(j).add(board[i][j]);
        squares.get(getSquare(i, j)).add(board[i][j]);
    }

    // Removes number from check-structures:
    private void removeFromChecks(int i, int j) {
        rows.get(i).remove(board[i][j]);
        cols.get(j).remove(board[i][j]);
        squares.get(getSquare(i, j)).remove(board[i][j]);
    }

    // Adds number and returns true if possible, if not returns false:
    public boolean addNumber(int val, int i, int j) {
        if (!checkNumber(val, i, j)) return false;
        board[i][j] = val;
        addToCheck(i, j);
        return true;
    }

    // Returns true if a value fits, else false:
    public boolean checkNumber(int val, int i, int j) {
        if (rows.get(i).contains(val))
            return false;
        if (cols.get(j).contains(val))
            return false;
        if (squares.get(getSquare(i, j)).contains(val))
            return false;
        return true;
    }

    // Calculates which inner square indexes are in:
    public int getSquare(int i, int j) {
        return (i / innerSize) * innerSize + (j / innerSize);
    }

    // Solves sudoku with brute-force using recursiveSolve, returns time for solving or -1 if unsolveable:
    public double solveSudoku() {
        long start = System.nanoTime();
        if (recursiveSolve(0, 0)) {
            long end = System.nanoTime();
            return (double)(end - start) / 1000000;
        }
        return -1;
    }

    public boolean recursiveSolve(int i, int j) {
        if (i == size) {
            return true; // Solution found!
        }
        int next_j = (j + 1) % size;
        int next_i = (j == size - 1) ? i + 1 : i;

        // If value is set, go to next:
        if (board[i][j] != 0) {
            return recursiveSolve(next_i, next_j);
        }

        // Try all combinations and backtrack if necessary:
        for (int val = 1; val <= size; val++) {
            if (checkNumber(val, i, j)) {
                addNumber(val, i, j);
                if (recursiveSolve(next_i, next_j)) {
                    return true; // Solution found!              
                }
                // Backtracking:
                removeFromChecks(i, j);
                board[i][j] = 0;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        // Making separator:
        String separator = "\n";
        String doubleSep = "\n";
        for (int k = 0; k < size; k++) {
            separator += "------";
            doubleSep += "======";
        }
        separator += "-\n";
        doubleSep += "=\n";
        // Creating board str:
        String printBoard = separator;
        for (int i = 0; i < size; i++) {
            printBoard += "|";
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) printBoard += "     ";
                else printBoard += "  "+ board[i][j] + "  ";
                if ((j + 1) % innerSize == 0 && j + 1 != size) printBoard += "║";
                else printBoard += "|";
            }
            if ((i + 1) % innerSize == 0) printBoard += doubleSep;
            else printBoard += separator;
        }
        return printBoard;
    }
}