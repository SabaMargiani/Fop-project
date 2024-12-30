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
            Util.printError("readFile on specified path returned null");
            scanner.close();
            return;
        }
        // convert into lowercase since BASIC is case-insensitive
        file_contents = file_contents.toLowerCase();

        // turn string into tokens via lexer
        Lexer lexer = new Lexer(file_contents);
        List<Token> tokenized = lexer.tokenize();
        // build a syntax tree from tokens via parser

        // execute the syntax tree via interpreter

        scanner.close();

        // ==== ==== ==== TESTING ==== ==== ====
        if (lexer.error_while_tokenizing) System.out.println("error_while_tokenizing true");
        printTokenList(tokenized);

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
            Util.printError(e.getMessage());
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
            System.out.print(li.get(i).toString() + "  ");
        }
        return res;
    }
}
