import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Lexer {
    static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<String, TokenType>();
        keywords.put("let", TokenType.LET);
        keywords.put("if", TokenType.IF);
        keywords.put("then", TokenType.THEN);
        keywords.put("else", TokenType.ELSE);
        keywords.put("elseif", TokenType.ELSEIF);
        keywords.put("for", TokenType.FOR);
        keywords.put("to", TokenType.TO);
        keywords.put("next", TokenType.NEXT);
        keywords.put("while", TokenType.WHILE);
        keywords.put("wend", TokenType.WEND);
        keywords.put("exit", TokenType.EXIT);
        keywords.put("end", TokenType.END);

        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);

        keywords.put("mod", TokenType.MOD);
        keywords.put("sqr", TokenType.SQR);

        keywords.put("print", TokenType.PRINT);
    }

    String input;
    int pos = 0;
    int line = 0;
    boolean error_while_tokenizing = false;

    public Lexer(String input) {
        this.input = input + "\0";
    }

    public List<Token> tokenize() {
        // quick fix function
        List<Token> li = tokenize();
        if (li == null) return null;
        li.add(new Token(TokenType.EOF, line));
        return li;
    }
    public List<Token> realTokenize() {
        if (input == null) {
            Util.printError("input string is null");
            error_while_tokenizing = true;
            return null;
        }

        List<Token> tokenList = new ArrayList<>();

        while(true) {
            char poschar = input.charAt(pos);
            switch (poschar) {
                case '\0': return tokenList;
                case '\n':
                    ++pos;
                    ++line;
                    continue;
                case '\t':
                case ' ' :
                    ++pos;
                    continue;
            }

            if (isLetter(poschar)) {
                String word = "" + poschar;
                while(true) {
                    poschar = input.charAt(++pos);
                    if (!isAlphaNumeric(poschar)) break;
                    word += poschar;
                }
                TokenType t = keywords.get(word);
                if (t == null) t = TokenType.IDENTIFIER;
                tokenList.add(new Token(t, word, line));
                continue;
            }

            if (isDigit(poschar)) {
                String numString = "" + poschar;
                while(true) {
                    poschar = input.charAt(++pos);
                    if (!isDigit(poschar)) break;
                    numString += poschar;
                }
                tokenList.add(new Token(TokenType.NUMBER, numString, line));
                continue;
            }

            if (poschar == '\'' ) {
                while (true) {
                    poschar = input.charAt(++pos);
                    if (poschar == '\n' || poschar == '\0') break;
                }
                continue;
            }

            if (poschar == '\"') {
                int openLine = line;
                String strString = "";
                while (true) {
                    poschar = input.charAt(++pos);
                    if (poschar == '\"') {
                        tokenList.add(new Token(TokenType.STRING, strString, openLine));
                        ++pos;
                        break;
                    }
                    if (poschar == '\0') {
                        error_while_tokenizing = true;
                        Util.printError("\" at line " + openLine + " is unclosed");
                        break;
                    }
                    strString += poschar;
                }
                continue;
            }

            if (poschar == '<') {
                poschar = input.charAt(++pos);
                if (poschar == '>') {
                    tokenList.add(new Token(TokenType.NEQT, line));
                    ++pos;
                    continue;
                }
                if (poschar == '=') {
                    tokenList.add(new Token(TokenType.LEQT, line));
                    ++pos;
                    continue;
                }
                tokenList.add(new Token(TokenType.LESS, line));
                continue;
            }

            if (poschar == '>') {
                if (input.charAt(++pos) == '=') {
                    tokenList.add(new Token(TokenType.GEQT, line));
                    ++pos;
                } else tokenList.add(new Token(TokenType.GREATER, line));
                continue;
            }

            switch (poschar) {
                case '+': tokenList.add(new Token(TokenType.PLUS,      line)); break;
                case '-': tokenList.add(new Token(TokenType.MINUS,     line)); break;
                case '*': tokenList.add(new Token(TokenType.PIPQI,     line)); break;
                case'\\': tokenList.add(new Token(TokenType.SLASH,     line)); break;
                case '(': tokenList.add(new Token(TokenType.LPARENT,   line)); break;
                case ')': tokenList.add(new Token(TokenType.RPARENT,   line)); break;
                case '=': tokenList.add(new Token(TokenType.EQUAL,     line)); break;
                case ';': tokenList.add(new Token(TokenType.SEMICOLON, line)); break;
                default:
                    Util.printError("unmatched symbol at line " + line);
                    return tokenList;
            }
            ++pos;

        }
    }


    public static boolean isLetter(char c) {return ('a' <= c && c <= 'z');}
    public static boolean isDigit (char c) {return ('0' <= c && c <= '9');}
    public static boolean isAlphaNumeric(char c) {return ('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || c == '_';}


}