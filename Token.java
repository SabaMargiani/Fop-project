public class Token {
    TokenType type;
    String string;
    int line;

    public Token(TokenType type, int line) {
        this.type = type;
        this.line = line;
    }

    public Token(TokenType type, String string, int line) {
        this.type = type;
        this.line = line;
        this.string = string;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}