
public class MainFile {
    public static void main (String[] args) {
        if (args.length != 1) {
            System.out.println("No path or too many arguments given");
            return;
        }
        Scanner scanner = new Scanner(args[0]);
        Parser parser = new Parser(scanner);
        parser.Parse();
    }
}