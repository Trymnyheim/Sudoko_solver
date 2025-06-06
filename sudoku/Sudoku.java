import java.util.*;
import java.io.*;

public class Sudoku {
    
    int[][] board;
    int SIZE;
    HashMap<Integer, HashSet<Integer>> rows = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> cols = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> squares = new HashMap<>();
    SudokuSolver controller;

    public Sudoku(int size, SudokuSolver solver) {
        board = new int[size][size];
        SIZE = size;
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

    public int getBoardValue(int i, int j) {
        return board[i][j];
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

    public void solveSudoku(boolean withVisual) {
        solveSudoku(5, withVisual);
    }

    public void solveSudoku(int msDelay, boolean withVisual) {
        new Thread(() -> {
            long start = System.nanoTime();
            boolean solved = recursiveSolve(0, 0, msDelay, withVisual);
            long end = System.nanoTime();
            double time = (double)(end - start) / 1_000_000;
            if (!withVisual)
                controller.writeAll();
            controller.showResults(solved, time);
        }).start();
    }

    public boolean recursiveSolve(int i, int j, int msDelay, boolean withVisual) {
        if (i == SIZE) {
            return true; // Solution found!
        }
        int next_j = (j + 1) % SIZE;
        int next_i = (j == SIZE - 1) ? i + 1 : i;

        // If value is set, go to next:
        if (board[i][j] != 0) {
            return recursiveSolve(next_i, next_j, msDelay, withVisual);
        }

        // Try all combinations and backtrack if necessary:
        for (int val = 1; val <= SIZE; val++) {
            if (checkNumber(val, i, j)) {
                board[i][j] = val;
                if (controller != null && withVisual) {
                    controller.writeValue(val, i, j);
                    try {
                        Thread.sleep(msDelay); // Delay for 10 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                addToCheck(i, j);
                if (recursiveSolve(next_i, next_j, msDelay, withVisual)) {
                    return true; // Solution found!              
                }
                // Backtracking:
                removeFromChecks(i, j);
                board[i][j] = 0;
                if (controller != null && withVisual)
                    controller.clearValue(i, j);

            }
        }
        return false;
    }

    // Calculates which inner square indexes are in:
    public int getSquare(int i, int j) {
        if (SIZE == 9)
            return (i / 3) * 3 + (j / 3);
        if (SIZE == 12)
            return (i / 3) * 3 + (j / 4);
        if (SIZE == 16)
            return (i / 4) * 4 + (j / 4);
        return 0;
    }

    // Read input file and add to board:
    public void loadFromFile(String path) throws FileNotFoundException, InputMismatchException  {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextInt()) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = scanner.nextInt();
                }
            }
        }
        scanner.close();
        setChecks();
    }

    // For checking if two boards are the same
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
