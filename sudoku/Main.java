public class Main {
    public static void main(String[] args) {
        SudokuSolver solver;
        if (args.length == 0)
            solver = new SudokuSolver();
        else if (args.length == 2) {
            try {
                solver = new SudokuSolver(args[0], Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                System.out.println("Usage: java Main <File name> <Size>");
                return;
            }
        }
        else {
            System.out.println("Invalid command-line argument.");
            return;
        }
        solver.run();
    }
}