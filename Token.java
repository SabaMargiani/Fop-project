import java.util.*;

class Token {
    enum Type { PRINT, LET, IDENTIFIER, NUMBER, EQUALS, PLUS, MINUS, END, EOF }

    Type type;
    String value;

    Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }
}