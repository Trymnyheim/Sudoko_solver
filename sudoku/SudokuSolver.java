public class SudokuSolver {
    
    private SolverGUI GUI;
    private Sudoku sudoku;
    private int SIZE;

    public SudokuSolver() {
        SIZE = 9;
        GUI = new SolverGUI(this, SIZE);
        sudoku = new Sudoku(9, 3, this);
    }

    public void run() {
        GUI.openWindow();
    }

    public int setValue(int val, int id) {
        int row = id / SIZE;
        int col = id % SIZE;
        if (sudoku.board[row][col] != 0) {
            sudoku.removeFromBoard(row, col); // TODO: Is the check nessecary? Should be in removeFromBoard?
        }
        try {
            sudoku.addToBoard(val, row, col);
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
            return id;
        }
        return 0;
    }

    // Clears a value from GUI
    public void writeValue(int val, int i, int j) {
        GUI.writeValue(getIndex(i, j), val);
    }

    public void clearValue(int i, int j) {
        GUI.clearValue(getIndex(i, j));
    }

    public void solve() {
        sudoku.solveSudoku();
    }

    public void clear() {
        GUI.close();
        GUI = new SolverGUI(this, SIZE);
        sudoku = new Sudoku(SIZE, 3, this);
        run();
    }

    public void showResults(boolean solved, Double time) {
        if (solved)
            System.out.println(time);
        else
            System.out.println("Unable to solve");
        // TODO: Show result in GUI
    }

    public void changeSize(int size) {
        SIZE = size;
        clear();
    }


    private int getIndex(int i, int j) {
        return i * SIZE + j;
    }
}
