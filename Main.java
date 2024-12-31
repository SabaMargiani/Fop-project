import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // get input
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter file path: ");
        String file_path = scanner.nextLine();

        // store the contents(hopefully BASIC code) of specified file into a string variable
        String file_contents = readFile(file_path);

        if (file_contents == null) {
            Util.printError(Util.currentLocation(), "readFile on specified path returned null");
            scanner.close();
            return;
        }
        // convert into lowercase since BASIC is case-insensitive
        file_contents = file_contents.toLowerCase();

        // turn string into tokens via lexer
        Lexer lexer = new Lexer(file_contents);
        List<Token> tokenized = lexer.tokenize();

        // build a syntax tree from tokens via parser
        Parser parser = new Parser(tokenized);
        Program parsed = new Program(parser.parse());

        // execute the syntax tree via interpreter
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(parsed.statements);

        scanner.close();

    }

    // CAN USE sout(parsed.toString()) TO CHECK PARSER RESULT
    // CAN USE THIS TO CHECK LEXER RESULT
    @SuppressWarnings("unused")
    private static void lexerCheck(Lexer lexer, Scanner scanner, List<Token> tokenized) {
        if (lexer.error_while_tokenizing) {
            System.out.println("error_while_tokenizing true");
            scanner.close();
            return;
        }
        printTokenList(tokenized);
        System.out.println("");

    }



    public static String readFile(String file_path) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            Util.printError(Util.currentLocation(), e.getMessage());
            return null;
        }
    }

    public static String printTokenList(List<Token> li) {
        if (li == null) {
            System.out.println("given token list is null");
            return null;
        }
        String res = "";
        int line = 0;
        for (int i = 0; i < li.size(); i++) {
            int diff = li.get(i).line - line;
            for (int j = 0; j < diff; j++) {
                System.out.println();
                ++line;
            }
            System.out.print(li.get(i).type.toString() + "  ");
        }
        return res;
    }


}
