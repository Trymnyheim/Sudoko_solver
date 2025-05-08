import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class SolverGUI {

    private SudokuSolver solver;
    private final int SIZE;
    private JFrame frame;
    private JPanel options;
    private JPanel sudoku;
    private OnEnter onEnter;
    private OnSolve onSolve;
    private OnClear onClear;
    private JTextField[] squares;
    private JCheckBox withVisual;
    private static int SQSIZE = 50;
    private boolean hasError = false;

    public SolverGUI(SudokuSolver solver, int size) {
        this.solver = solver;
        SIZE = size;
        frame = new JFrame("Sudoku Solver");
        options = new JPanel();
        sudoku = new JPanel();
        onEnter = new OnEnter(); 
        onSolve = new OnSolve();
        onClear = new OnClear();
        squares = new JTextField[SIZE * SIZE];
    }

    public void openWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.exit(-1);
        }

        showOptions();
        showSudoku();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(options, BorderLayout.NORTH);
        frame.add(sudoku, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showOptions() {
        JButton[] sizes = { new JButton("9 x 9"),
                            new JButton("12 x 12"),
                            new JButton("16 x 16") };
        for (JButton button : sizes) {
            button.addActionListener(new SetSize(button.getText()));
            button.setFont(new Font("Ariel", Font.BOLD, 16));
            options.add(button);
        }

        JButton solve = new JButton("Solve");
        solve.addActionListener(onSolve);
        solve.setFont(new Font("Ariel", Font.BOLD, 16));
        options.add(solve);

        JButton clear = new JButton("Clear");
        clear.setFont(new Font("Ariel", Font.BOLD, 16));
        clear.addActionListener(onClear);
        options.add(clear);

        withVisual = new JCheckBox("With Visuals");
        options.add(withVisual);
    }

    public void showSudoku() {
        int row, col;
        if (SIZE == 9) {
            row = col = 3;
        } else if (SIZE == 12) {
            row = 3;
            col = 4;
        } else {
            row = col = 4;
        }
    
        GridLayout layout = new GridLayout(col, row, 0, 0);
        sudoku.setLayout(layout);
    
        JPanel[] boxes = new JPanel[SIZE];
    
        GridLayout boxLayout = new GridLayout(row, col, 0, 0); // Internal layout of each box
    
        for (int boxIndex = 0; boxIndex < SIZE; boxIndex++) {
            boxes[boxIndex] = new JPanel();
            boxes[boxIndex].setLayout(boxLayout);
            boxes[boxIndex].setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    // Calculate 1D array index:
                    int index = ((boxIndex / row) * row + i) * SIZE + (boxIndex % row) * col + j;

                    // Create square:
                    JTextField square = new JTextField();
                    square.addActionListener(onEnter);
                    square.setHorizontalAlignment(JTextField.CENTER);
                    square.setMargin(new Insets(0, 0, 0, 0));
                    square.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    square.setFont(new Font("Ariel", Font.BOLD, 20));
                    square.setText("");
    
                    boxes[boxIndex].add(square);
                    squares[index] = square;
                }
            }
    
            sudoku.add(boxes[boxIndex]);
        }
    
        sudoku.setPreferredSize(new Dimension(SQSIZE * SIZE, SQSIZE * SIZE));
    }

    public void writeValue(int id, int val) {
        squares[id].setText("" + val);
    }

    public void clearValue(int id) {
        squares[id].setText("");
    }
    
    public void close() {
        frame.dispose();
    }

    // For changing size of sudoku, takes dimensions as a str parameter, f.ex. "9 x 9".
    private class SetSize implements ActionListener {

        private final String SIZESTR;

        public SetSize(String size) {
            SIZESTR = size;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (SIZESTR.equals("9 x 9") && SIZE != 9) {
                solver.changeSize(9);
            }
            if (SIZESTR.equals("12 x 12") && SIZE != 12) {
                solver.changeSize(12);
            }
            if (SIZESTR.equals("16 x 16") && SIZE != 16) {
                solver.changeSize(16);
            }
        }
    }

    // For solving the sudoku
    private class OnEnter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField square;
            String input;
            LineBorder errorBorder = new LineBorder(Color.RED, 2);
            hasError = false;

            for (int i = 0; i < SIZE * SIZE; i++) {
                square = squares[i];
                square.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Reset border incase of error:
                input = square.getText(); 
                if (input.equals(""))
                    continue; // Ignoring blank squares 
                int val;
                try {
                    val = Integer.parseInt(input);
                    if (val < 1 || val > SIZE)
                        throw new NumberFormatException();
                } catch (NumberFormatException err) {
                    square.setBorder(new LineBorder(Color.RED, 2));
                    val = -1;
                    hasError = true;
                }
                if (val == -1)
                    continue;

                if (!solver.setValue(val, i)) {
                    // A collision occured
                    squares[i].setBorder(errorBorder);
                    hasError = true;
                    break;
                }
            }
        }
    }

    private class OnSolve implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            onEnter.actionPerformed(e); // Insert all values from textfields
            // Solve if no errors:
            if (!hasError)
                solver.solve(withVisual.isSelected());
            else {
                solver.openPopUp("Error", "Fix the error marked in red before solving the sudoku.");
            }
        }
    }

    private class OnClear implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            solver.clear();
        }
    }
}
