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
                Util.printError(Util.currentLocation(), "statement() returned null -> syntax error");
                error_while_parsing = true;
                break;
            }
            statements.add(s);
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
            case EOF:
                Util.printError(Util.currentLocation(), "EOF where statement is expected");
                return null;
            default:
                Util.printError(Util.currentLocation(), "token " + posToken.type + " at line " + posToken.line + " doesn't match any statement syntax");
                return null;
        }
    }

    private Statement letStatement() {
        // SYNTAX: LET [variableName] = [value]
        if (tokens.get(pos).type != TokenType.LET) {
            Util.printError(Util.currentLocation(), "let statement doesn't start with LET keyword");
            return null;
        }
        Token left = tokens.get(++pos);
        if (left.type != TokenType.IDENTIFIER) {
            Util.printError(Util.currentLocation(), "left-side of let statement isn't a variable");
            return null;
        }
        if (tokens.get(++pos).type != TokenType.EQUAL) {
            Util.printError(Util.currentLocation(), "asignment of let statement not in place");
            return null;
        }
        ++pos;
        Expression right = expression();
        if (right == null) {
            Util.printError(Util.currentLocation(), "right side of let statement can't be fit to an expression");
            return null;
        }
        return new LetStatement(left, right);
    }

    private Statement forStatement() {

        if (tokens.get(pos).type != TokenType.FOR) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - for statement doesn't start with FOR keyword.");
            return null;
        }

        if (tokens.get(++pos).type != TokenType.LET) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - invalid loop variable declaration");
            return null;
        }

        Token iStart = tokens.get(++pos);
        if (iStart.type != TokenType.IDENTIFIER) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - invalid loop variable declaration");
            return null;
        }

        if (tokens.get(++pos).type != TokenType.EQUAL) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - invalid loop variable declaration");
            return null;
        }

        ++pos;
        Expression startValue = expression();
        if (startValue == null) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - can't evaluate startValue for loop variable");
            return null;
        }

        if (tokens.get(++pos).type != TokenType.TO) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - missing TO keyword after loop variable declaration");
            return null;
        }

        ++pos;
        Expression endValue = expression();
        if (endValue == null) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - can't evaluate endValue for loop variable");
            return null;
        }

        ++pos;
        List<Statement> body = new ArrayList<>();
        // if we have EOF before NEXT it will error so its fine
        // statement -> expression -> ... -> primary errors
        while (tokens.get(pos).type != TokenType.NEXT) {
            Statement s = statement();
            if (s == null) return null;
            body.add(s);
            ++pos;
        }

        // only reachable if syntax was valid untill NEXT
        Token iEnd = tokens.get(++pos);
        if (iEnd.type != TokenType.IDENTIFIER) {
            Util.printError(Util.currentLocation(), "line " + iEnd.line + " expected: loop variable. got: token of type " + iEnd.type);
            return null;
        }

        if (!iEnd.string.equals(iStart.string)) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " expected: loop variable [" + iStart.string + "]. got: variable [" + iEnd.string +"]");
            return null;
        }

        return new ForStatement(iStart, startValue, endValue, body);
    }

    private Statement whileStatement() {
        if (tokens.get(pos).type != TokenType.WHILE) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - while statement doesn't start with WHILE keyword");
            return null;
        }

        ++pos;
        Expression condition = expression();
        if (condition == null) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - coudln't evaluate while loop condition");
            return null;
        }

        ++pos;
        List<Statement> body = new ArrayList<>();
        while (tokens.get(pos).type != TokenType.WEND) {
            Statement s = statement();
            if (s == null) return null;
            body.add(s);
            ++pos;
        }

        return new WhileStatement(condition, body);
    }

    private Statement ifStatement() {
        if (tokens.get(pos).type != TokenType.IF) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - if statement doesn't start with IF keyword");
            return null;
        }

        ++pos;
        Expression condition = expression();
        if (condition == null) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - couldn't evaluate if statement condition");
            return null;
        }

        ++pos;
        if (tokens.get(pos).type != TokenType.THEN) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - THEN keyword missing after IF [condition]");
            return null;
        }

        List<Statement> thenBranch = new ArrayList<>();
        while (tokens.get(++pos).type != TokenType.ELSE && tokens.get(pos).type != TokenType.END) {
            Statement s = statement();
            if (s == null) return null;
            thenBranch.add(s);
        }

        List<Statement> elseBranch = null;
        while (tokens.get(pos).type == TokenType.ELSE) {

            if (tokens.get(++pos).type == TokenType.IF) {

                ++pos;
                Expression elseifCondition = expression();
                if (elseifCondition == null) {
                    Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - couldn't evaluate ELSE IF condition");
                    return null;
                }

                ++pos;
                if (tokens.get(pos).type != TokenType.THEN) {
                    Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - THEN keyword missing after ELSE IF [condition]");
                    return null;
                }

                List<Statement> elseifThenBranch = new ArrayList<>();
                while (tokens.get(++pos).type != TokenType.ELSE && tokens.get(pos).type != TokenType.END) {
                    Statement s = statement();
                    if (s == null) return null;
                    elseifThenBranch.add(s);
                }

                IfStatement elseifStatement = new IfStatement(elseifCondition, elseifThenBranch, null);
                if (elseBranch == null) elseBranch = new ArrayList<>();
                elseBranch.add(elseifStatement);
                continue;
            }

            elseBranch = new ArrayList<>();
            while (tokens.get(pos).type != TokenType.END) {
                Statement s = statement();
                if (s == null) return null;
                elseBranch.add(s);
                ++pos;

            }
        }

        if (tokens.get(pos).type != TokenType.END) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - expected: END keyword for if statement.  got: " + tokens.get(pos).type);
            return null;
        }

        if (tokens.get(++pos).type != TokenType.IF) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - expected: IF keyword for END-IF pair.  got: " + tokens.get(pos).type);
            return null;
        }

        return new IfStatement(condition, thenBranch, elseBranch);
    }



    private Statement printStatement() {
        if (tokens.get(pos).type != TokenType.PRINT) {
            Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - print statement doesn't start with PRINT keyword");
            return null;
        }

        List<Expression> expressions = new ArrayList<>();

        while (true) {
            ++pos;
            Expression output = expression();
            if (output == null) {
                Util.printError(Util.currentLocation(), "line " + tokens.get(pos).line + " - couldn't evaluate expression to print");
                return null;
            }
            expressions.add(output);

            Token posToken = tokens.get(++pos);
            if (posToken.type == TokenType.SEMICOLON) continue;
            --pos;
            return new PrintStatement(expressions);
        }
    }


    private Expression expression() {
        return booleanExpression();
    }

    private Expression booleanExpression() {
        Expression expr = comparison();
        if (expr == null) return null;

        if (pos == tokens.size() - 2) return expr;
        while (tokens.get(++pos).type == TokenType.EQUAL
                || tokens.get(pos).type == TokenType.NEQT
                || tokens.get(pos).type == TokenType.AND
                || tokens.get(pos).type == TokenType.OR) {
            Token operator = tokens.get(pos);
            ++pos;
            Expression right = comparison();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        --pos;
        return expr;
    }

    private Expression comparison() {
        Expression expr = addition();
        if (expr == null) return null;

        if (pos == tokens.size() - 2) return expr;

        while (tokens.get(++pos).type == TokenType.LESS
                || tokens.get(pos).type == TokenType.GREATER
                || tokens.get(pos).type == TokenType.LEQT
                || tokens.get(pos).type == TokenType.GEQT) {
            Token operator = tokens.get(pos);
            ++pos;
            Expression right = addition();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        --pos;
        return expr;
    }

    private Expression addition() {
        Expression expr = multiplication();
        if (expr == null) return null;

        if (pos == tokens.size() - 2) return expr;

        while (tokens.get(++pos).type == TokenType.PLUS || tokens.get(pos).type == TokenType.MINUS) {
            Token operator = tokens.get(pos);
            ++pos;
            Expression right = multiplication();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        --pos;
        return expr;
    }

    private Expression multiplication() {
        Expression expr = unary();
        if (expr == null) return null;

        if (pos == tokens.size() - 2) return expr;

        while (tokens.get(++pos).type == TokenType.PIPQI || tokens.get(pos).type == TokenType.SLASH || tokens.get(pos).type == TokenType.MOD) {
            Token operator = tokens.get(pos);
            ++pos;
            Expression right = unary();
            if (right == null) return null;
            expr = new BinaryExpression(expr, operator, right);
        }
        --pos;
        return expr;
    }

    private Expression unary() {
        Token operator = tokens.get(pos);
        if (operator.type == TokenType.NOT || operator.type == TokenType.SQR) {
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
            Util.printError(Util.currentLocation(), "reached EOF -> possible unfinished expression");
        }

        if (posToken.type == TokenType.NUMBER
                || posToken.type == TokenType.STRING
                || posToken.type == TokenType.TRUE
                || posToken.type == TokenType.FALSE) {
            return new LiteralExpression(posToken);
        } // prolly this right?
        if (posToken.type == TokenType.IDENTIFIER) {
            return new VariableExpression(posToken);
        }
        if (posToken.type == TokenType.LPARENT) {
            ++pos;
            Expression expr = expression();
            if (expr == null) {
                Util.printError(Util.currentLocation(), "line " + posToken.line + " - can't evaluate expression inside parentheses");
                return null;
            }

            if (tokens.get(++pos).type != TokenType.RPARENT) {
                Util.printError(Util.currentLocation(), "unclosed parentheses");
                return null;
            }
            return expr;
        }
        Util.printError(Util.currentLocation(), "unexpected token");
        return null;
    }


}
