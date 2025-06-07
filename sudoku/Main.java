import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        SudokuSolver solver;
        if (args.length == 0)
            solver = new SudokuSolver();
        else if (args.length == 2) {
            try {
                solver = new SudokuSolver(Integer.parseInt(args[1]));
                solver.loadFromFile(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Usage: java Main <File name> <Size>");
                return;
            } catch (NoSuchElementException e) {
                System.out.println("Size does not match size of sudoku from file \"" + args[0] + "\"");
                return;
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        }
        else {
            System.out.println("Usage: java Main <File name> <Size>");
            return;
        }
        solver.run();
    }
}