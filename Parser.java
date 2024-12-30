import java.util.ArrayList;
import java.util.List;

public class Parser {
    List<Token> tokens;
    int pos = 0;
    boolean error_while_parsing = false;  
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (tokens.get(pos).type != TokenType.EOF) {
            Statement s = statement();
            if (s == null) {
                Util.printError("statement() returned null -> syntax error");
                error_while_parsing = true;
                break;
            }
            
            statements.add(s);
            if (error_while_parsing) {
                Util.printError("syntax error if / for / while loop body. returning partial body");
                break;
            }
            ++pos;
        }
        return statements;
    }

    private Statement statement() {
        Token posToken = tokens.get(pos);
        switch (posToken.type) {
            // i swear i'm not crazy with this indentation
            case LET:    return   letStatement();
            case FOR:    return   forStatement();
            case WHILE:  return whileStatement();
            case IF:     return    ifStatement();
            case PRINT:  return printStatement();
            default: 
                Util.printError("token " + posToken.type + " at line " + posToken.line + " doesn't match any statement syntax");
                return null;
        }
    }

    private Statement letStatement() {
        // SYNTAX: LET [variableName] = [value]
        if (tokens.get(pos).type != TokenType.LET) {
            Util.printError("let statement doesn't start with LET keyword");
            return null;
        }
        Token left = tokens.get(++pos);
        if (left.type != TokenType.IDENTIFIER) {
            Util.printError("left-side of let statement isn't a variable");
            return null;
        }
        if (tokens.get(++pos).type != TokenType.EQUAL) {
            Util.printError("asignment of let statement not in place");
            return null;
        }
        ++pos;
        Expression right = expression();
        if (right == null) {
            Util.printError("right side of let statement can't be fit to an expression");
            return null;
        }
        return new LetStatement(left, right);
    }

    private Statement forStatement() {

        if (tokens.get(pos).type != TokenType.FOR) {
            Util.printError("line " + tokens.get(pos).line + " - for statement doesn't start with FOR keyword.");
            return null;
        }
        if (tokens.get(++pos).type != TokenType.LET) {
            Util.printError("line " + tokens.get(pos).line + " - invalid loop variable declaration");
            return null;
        }

        Token iStart = tokens.get(++pos);
        if (iStart.type != TokenType.IDENTIFIER) {
            Util.printError("line " + tokens.get(pos).line + " - invalid loop variable declaration");
            return null;
        }

        if (tokens.get(++pos).type != TokenType.EQUAL) {
            Util.printError("line " + tokens.get(pos).line + " - invalid loop variable declaration");
            return null;
        }

        ++pos;
        Expression startValue = expression();
        if (startValue == null) {
            Util.printError("line " + tokens.get(pos).line + " - can't evaluate startValue for loop variable");
            return null;
        }

        if (tokens.get(++pos).type != TokenType.TO) {
            Util.printError("line " + tokens.get(pos).line + " - missing TO keyword after loop variable declaration");
            return null;
        }

        ++pos;
        Expression endValue = expression();
        if (endValue == null) {
            Util.printError("line " + tokens.get(pos).line + " - can't evaluate endValue for loop variable");
            return null;
        }

        ++pos;
        List<Statement> body = new ArrayList<>();
        // if we have EOF before NEXT it will error so its fine
        // statement -> expression -> ... -> primary errors
        while (tokens.get(pos).type != TokenType.NEXT) {
            Statement s = statement();
            // error message will be in statement itself
            if (s == null) break;
            body.add(s);
            ++pos;
        }

        // logically, this can only execute if while is exited with a break
        if (tokens.get(pos).type != TokenType.NEXT) {
            Util.printError("line " + tokens.get(pos).line + " - incorrect syntax in for loop body");
            error_while_parsing = true;
            return new ForStatement(iStart, startValue, endValue, body);
        }
        
        Token iEnd = tokens.get(++pos);
        if (iEnd.type != TokenType.IDENTIFIER) {
            Util.printError("");
            return null;
        }
        if (!iEnd.string.equals(iStart.string)) {
            Util.printError("line " + tokens.get(pos).line + " loop variable inconsistent");
            return null;
        }

        return new ForStatement(iStart, startValue, endValue, body);
    }

    private Statement whileStatement() {
        if (tokens.get(pos).type != TokenType.WHILE) {
            Util.printError("line " + tokens.get(pos).line + " - while statement doesn't start with WHILE keyword");
            return null;
        }

        Expression condition = expression();
        if (condition == null) {
            Util.printError("line " + tokens.get(pos).line + " - coudln't evaluate while loop condition");
            return null;
        }

        List<Statement> body = new ArrayList<>();
        while (tokens.get(pos).type != TokenType.WEND) {
            
            Statement s = statement();
            if (s == null) break;
            body.add(s);
            // TODO: remove this here and in other code if parser starts skipping tokens
            ++pos;
        }
        
        // logically, this can only execute if while is exited with a break
        if (tokens.get(pos).type != TokenType.WEND) {
            Util.printError("line " + tokens.get(pos).line + " - incorrect syntax in while loop body");
            error_while_parsing = true;
        }
        return new WhileStatement(condition, body);
    }

    private Statement ifStatement() {
        if (tokens.get(pos).type != TokenType.IF) {
            Util.printError("line " + tokens.get(pos).line + " - if statement doesn't start with IF keyword");
            return null;
        }

        ++pos;
        Expression condition = expression();
        if (condition == null) {
            Util.printError("line " + tokens.get(pos).line + " - coudln't evaluate if statement condition");
            return null;
        }

        ++pos;
        if (tokens.get(pos).type == null) {
            Util.printError("line " + tokens.get(pos).line + " - THEN keyword missing after IF [condition]");
            return null;
        }

        List<Statement> thenBranch = new ArrayList<>();
        while (tokens.get(pos).type != TokenType.ELSE && tokens.get(pos).type != TokenType.END) {
            Statement s = statement();
            if (s == null) break;
            // bliad ar gamodis ese
            thenBranch.add(s);
        }



        List<Statement> elseBranch = null;
        if (tokens.get(pos).type == TokenType.ELSE) {
            consume(TokenType.ELSE);
            elseBranch = new ArrayList<>();
            while (pos < tokens.size() && tokens.get(pos).type != TokenType.END) {
                elseBranch.add(statement());
            }
        }
        consume(TokenType.END);
        consume(TokenType.IF);
        return new IfStatement(condition, thenBranch, elseBranch);
    }

    private Statement printStatement() {
        consume(TokenType.PRINT);
        List<Expression> expressions = new ArrayList<>();
        expressions.add(expression());
        while (tokens.get(pos).type == TokenType.SEMICOLON) {
            consume(TokenType.SEMICOLON);
            expressions.add(expression());
        }
        return new PrintStatement(expressions);
    }


    private Expression expression() {
        return booleanExpression();
    }

    private Expression booleanExpression() {
        Expression expr = comparison();
        if (expr == null) return null;
        
        Token operator = tokens.get(pos);
        while (operator.type == TokenType.EQUAL 
            || operator.type == TokenType.NEQT
            || operator.type == TokenType.AND
            || operator.type == TokenType.OR) {
            ++pos;
            Expression right = comparison();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression comparison() {
        Expression expr = addition();
        if (expr == null) return null;

        Token operator = tokens.get(pos);
        while (operator.type == TokenType.LESS
            || operator.type == TokenType.GREATER 
            || operator.type == TokenType.LEQT
            || operator.type == TokenType.GEQT) {
            ++pos;
            Expression right = addition();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression addition() {
        Expression expr = multiplication();
        if (expr == null) return null;

        Token operator = tokens.get(pos);
        while (operator.type == TokenType.PLUS || operator.type == TokenType.MINUS) {
            ++pos;
            Expression right = multiplication();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression multiplication() {
        Expression expr = unary();
        if (expr == null) return null;

        Token operator = tokens.get(pos);
        while (operator.type == TokenType.PIPQI || operator.type == TokenType.SLASH || operator.type == TokenType.MOD) {         
            Expression right = unary();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        return expr;
    }

    private Expression unary() {
        Token operator = tokens.get(pos);
        if (operator.type == TokenType.NOT) {
            ++pos;
            Expression right = unary();
            if (right == null) return null;
            return new UnaryExpression(operator, right);
        }
        return primary();
    }

    private Expression primary() {
        Token posToken = tokens.get(pos);

        if (posToken.type == TokenType.EOF) {
            Util.printError("reached EOF -> possible unfinished expression");
        }

        if (posToken.type == TokenType.NUMBER
         || posToken.type == TokenType.STRING
         || posToken.type == TokenType.TRUE 
         || posToken.type == TokenType.FALSE) {
            return new LiteralExpression(tokens.get(pos++));
        }
        if (posToken.type == TokenType.IDENTIFIER) {
            return new VariableExpression(tokens.get(pos++));
        }
        if (posToken.type == TokenType.LPARENT) {
            ++pos;
            Expression expr = expression();
            if (tokens.get(pos).type != TokenType.RPARENT) {
                Util.printError("unclosed parentheses");
            }
            return expr;
        }
        Util.printError("unexpected token");
        return null;
    }


    private Token consume(TokenType type) {
        if (tokens.get(pos).type == type) return advance();
        throw new RuntimeException("Expected token: " + type + ", but found: " + tokens.get(pos).type);
    }

    private Token advance() {
        if (tokens.get(pos).type != TokenType.EOF) pos++;
        return tokens.get(pos - 1);
    }
}


// =====================

abstract class Statement {}

class Program {
    final List<Statement> statements;

    Program(List<Statement> statements) {
        this.statements = statements;
    }
}

class LetStatement extends Statement {
    final Token identifier;
    final Expression value;

    LetStatement(Token identifier, Expression value) {
        this.identifier = identifier;
        this.value = value;
    }
}

class ForStatement extends Statement {
    final Token variable;
    final Expression start;
    final Expression end;
    final List<Statement> body;

    ForStatement(Token variable, Expression start, Expression end, List<Statement> body) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.body = body;
    }
}

class WhileStatement extends Statement {
    final Expression condition;
    final List<Statement> body;

    WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }
}

class IfStatement extends Statement {
    final Expression condition;
    final List<Statement> thenBranch;
    final List<Statement> elseBranch;

    IfStatement(Expression condition, List<Statement> thenBranch, List<Statement> elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}

class PrintStatement extends Statement {
    final List<Expression> expressions;

    PrintStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }
}

abstract class Expression {}

class BinaryExpression extends Expression {
    final Expression left;
    final Token operator;
    final Expression right;

    BinaryExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

class UnaryExpression extends Expression {
    final Token operator;
    final Expression right;

    UnaryExpression(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}

class LiteralExpression extends Expression {
    final Token literal;

    LiteralExpression(Token literal) {
        this.literal = literal;
    }
}

class VariableExpression extends Expression {
    final Token identifier;

    VariableExpression(Token identifier) {
        this.identifier = identifier;
    }
}
