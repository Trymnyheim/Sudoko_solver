public class SudokuSolver {
    
    private SolverGUI GUI;
    private Sudoku sudoku;
    private int SIZE;

    public SudokuSolver() {
        SIZE = 9;
        GUI = new SolverGUI(this, SIZE);
        sudoku = new Sudoku(SIZE, this);
    }

    public void run() {
        GUI.openWindow();
        openPopUp("Test av Pop-Up", "Dette er bare en liten test av pop-up-en!");
    }

    public boolean setValue(int val, int id) {
        int row = id / SIZE;
        int col = id % SIZE;
        sudoku.removeFromBoard(row, col);
        try {
            sudoku.addToBoard(val, row, col);
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    // Writes a value to GUI
    public void writeValue(int val, int i, int j) {
        GUI.writeValue(getIndex(i, j), val);
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
    public void solve() {
        sudoku.solveSudoku();
    }

    // TODO: Show a pop-up with solving time.
    public void showResults(boolean solved, Double time) {
        if (solved)
            System.out.println(time);
        else
            System.out.println("Unable to solve");
    }

    // TODO: Display a pop-up with a message!
    public void openPopUp(String title, String message) {
        PopUp popUp = new PopUp(title, message);
        popUp.openPopUp();
    } 

    // Converts from row and col to 1D array index
    private int getIndex(int i, int j) {
        return i * SIZE + j;
    }
}
