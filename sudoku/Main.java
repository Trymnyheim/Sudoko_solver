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
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        else {
            System.out.println("Usage: java Main <File name> <Size>");
            return;
        }
        System.out.println("Hei");
        solver.run();
    }
}