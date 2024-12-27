class Token {
    enum Type {
        PRINT, LET, IDENTIFIER, NUMBER, EQUALS, PLUS, MINUS,
        LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL,
        WHILE, ENDWHILE, END, EOF
    }

    Type type;
    String value;

    Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }
}
