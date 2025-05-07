
import java.util.*;
import java.io.*;

public class Sudoku {
    
    int[][] board;
    int SIZE;
    int INNER;
    HashMap<Integer, HashSet<Integer>> rows = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> cols = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> squares = new HashMap<>();
    SudokuSolver controller;

    public Sudoku(int size, SudokuSolver solver) {
        board = new int[size][size];
        SIZE = size;
        this.INNER = 3; // TODO: SORT OUT THE DIMENSIONS OF 12 x 12 and 16 x 16!!
        controller = solver;
        initBoard();
    }

    private void initBoard() {
        // Set all values to 0:
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
        // Initializing check-structures:
        for (int i = 0; i < SIZE; i++) {
            rows.put(i, new HashSet<Integer>());
            cols.put(i, new HashSet<Integer>());
            squares.put(i, new HashSet<Integer>());
        }
    }

    public void addToBoard(int value, int i, int j) throws IndexOutOfBoundsException {
        if (value < 1 || value > SIZE)
            throw new IllegalArgumentException();
        if (!checkNumber(value, i, j))
            throw new InvalidMoveException(value, i, j);
        board[i][j] = value;
        addToCheck(i, j);
    }

    public void removeFromBoard(int i, int j) throws IndexOutOfBoundsException {
        if (board[i][j] == 0)
            return;
        removeFromChecks(i, j);
        board[i][j] = 0;
    }

    // Returns true if a value fits, else false:
    private boolean checkNumber(int val, int i, int j) {
        if (rows.get(i).contains(val))
            return false;
        if (cols.get(j).contains(val))
            return false;
        if (squares.get(getSquare(i, j)).contains(val))
            return false;
        return true;
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

    // Adds all numbers into their check-structures:
    private void setChecks() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0)
                    continue;
                addToCheck(i, j);
            }
        }
    }

    public void solveSudoku() {
        new Thread(() -> {
            long start = System.nanoTime();
            boolean solved = recursiveSolve(0, 0);
            long end = System.nanoTime();
            double time = (double)(end - start) / 1_000_000;
            controller.showResults(solved, time);
        }).start();
    }

    public boolean recursiveSolve(int i, int j) {
        if (i == SIZE) {
            return true; // Solution found!
        }
        int next_j = (j + 1) % SIZE;
        int next_i = (j == SIZE - 1) ? i + 1 : i;

        // If value is set, go to next:
        if (board[i][j] != 0) {
            return recursiveSolve(next_i, next_j);
        }

        // Try all combinations and backtrack if necessary:
        for (int val = 1; val <= SIZE; val++) {
            if (checkNumber(val, i, j)) {
                board[i][j] = val;
                if (controller != null) {
                    controller.writeValue(val, i, j);
                    try {
                        Thread.sleep(10); // Delay for 20 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                addToCheck(i, j);
                if (recursiveSolve(next_i, next_j)) {
                    return true; // Solution found!              
                }
                // Backtracking:
                removeFromChecks(i, j);
                board[i][j] = 0;
                if (controller != null)
                    controller.clearValue(i, j);

            }
        }
        return false;
    }

    // Calculates which inner square indexes are in:
    public int getSquare(int i, int j) {
        return (i / INNER) * INNER + (j / INNER);
    }

    // Read input file and add to board:
    public void loadFromFile(String path) {
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextInt()) {
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
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

    @Override
    public String toString() {
        // Making separator:
        String separator = "\n";
        String doubleSep = "\n";
        for (int k = 0; k < SIZE; k++) {
            separator += "------";
            doubleSep += "======";
        }
        separator += "-\n";
        doubleSep += "=\n";
        // Creating board str:
        String printBoard = separator;
        for (int i = 0; i < SIZE; i++) {
            printBoard += "|";
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) printBoard += "     ";
                else printBoard += "  "+ board[i][j] + "  ";
                if ((j + 1) % INNER == 0 && j + 1 != SIZE) printBoard += "║";
                else printBoard += "|";
            }
            if ((i + 1) % INNER == 0) printBoard += doubleSep;
            else printBoard += separator;
        }
        return printBoard;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Sudoku))
            return false;

        Sudoku other = (Sudoku) obj;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != other.board[i][j])
                    return false;
            }
        }
        return true;
    }
    
}

class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(int value, int i, int j) {
        super("Number " + value + " does not fit in row " + i + ", column " + j);
    }
}
