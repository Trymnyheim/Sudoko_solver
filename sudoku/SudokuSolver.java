import java.io.FileNotFoundException;
import java.util.InputMismatchException;

public class SudokuSolver {
    
    private SolverGUI GUI;
    private Sudoku sudoku;
    private int SIZE;
    private boolean hasError = false;

    public SudokuSolver() {
        SIZE = 9;
        GUI = new SolverGUI(this, SIZE);
        sudoku = new Sudoku(SIZE, this);
    }

        public SudokuSolver(int size) {
        SIZE = size;
        GUI = new SolverGUI(this, SIZE);
        sudoku = new Sudoku(SIZE, this);
    }

    // Loads sudoku from file
    // returns true if success, displays error message and returns false if error
    public void loadFromFile(String filename) throws FileNotFoundException, InputMismatchException {
        try {
            sudoku.loadFromFile(filename);
        } catch (FileNotFoundException e) {
            openPopUp("File not found", "Unable to find file with name \"" + filename + "\".", true);
            hasError = true;
            throw new FileNotFoundException("Unable to find file with name \"" + filename + "\".");
        } catch (InputMismatchException e) {
            openPopUp("Error", "The sudoku in file with name \"" + filename + "\"contains and error.", true);
            hasError = true;
            throw new InputMismatchException("Error in sudoku");
        }
    }

    public void run() {
        if (hasError)
            return;
        GUI.openWindow();
        writeAll();
    }

    // Adds value from GUI to sudoku-object
    public boolean setValue(int val, int id) {
        int row = id / SIZE;
        int col = id % SIZE;
        sudoku.removeFromBoard(row, col);
        try {
            sudoku.addToBoard(val, row, col);
        } catch (InvalidMoveException e) {
            return false;
        }
        return true;
    }

    // Writes a value to GUI
    public void writeValue(int val, int i, int j) {
        if (val >= 1 && val <= SIZE)
            GUI.writeValue(getIndex(i, j), val);
    }

    // Writes all values to GUI
    public void writeAll() {
        int val;
        for (int i = 0; i < SIZE; i ++) {
            for (int j = 0; j < SIZE; j++) {
                val = sudoku.getBoardValue(i, j);
                writeValue(val, i, j);
            }
        }
    }

    // Clears a value from GUI
    public void clearValue(int i, int j) {
        GUI.clearValue(getIndex(i, j));
    }

    // Reloads the GUI with a new sudoku board
    public void clear() {
        GUI.close();
        GUI = new SolverGUI(this, SIZE);
        sudoku = new Sudoku(SIZE, this);
        run();
    }

    // Changes the board size and clears the board
    public void changeSize(int size) {
        SIZE = size;
        clear();
    }

    // Solves the sudoku
    public void solve(boolean withVisual) {
        sudoku.solveSudoku(withVisual);
    }

    // Show a pop-up with solving time, error message if unsuccessful
    public void showResults(boolean solved, Double time) {
        if (solved)
            openPopUp("Finished!", "Solved in " + time + "ms.", false);
        else
            openPopUp("Error!", "Unable to solve sudoku.", false);
    }

    // Display a pop-up with a message!
    public void openPopUp(String title, String message, boolean withClose) {
        PopUp popUp = new PopUp(title, message, withClose);
        popUp.openPopUp();
    } 

    // Converts from row and col to 1D array index
    private int getIndex(int i, int j) {
        return i * SIZE + j;
    }
}
