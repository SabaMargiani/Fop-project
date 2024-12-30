enum TokenType {

    // tokens for numerical expressions
    PLUS, MINUS, PIPQI, SLASH, MOD, SQR, LPARENT, RPARENT,
    APO, QUOT, SEMICOLON,
    // tokens for num (expression -> bool expression) operators
    EQUAL, NEQT,
    GREATER, GEQT,
    LESS, LEQT,
    // tokens for boolean expressions
    AND, OR, NOT, TRUE, FALSE,

    // literals.
    IDENTIFIER, STRING, NUMBER,

    // general language keywords
    LET, IF, THEN, ELSE, ELSEIF, FOR, TO, NEXT, WHILE, WEND, EXIT, END, PRINT,

    EOF

}    