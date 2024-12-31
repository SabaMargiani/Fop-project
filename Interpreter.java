import java.util.List;
import java.util.Map;
import java.util.HashMap;

class Interpreter {
    final Map<String, Object> variables = new HashMap<>();
    int pos = 0;

    void interpret(List<Statement> li) {
        while (pos < li.size()) {
            execute(li.get(pos));
            ++pos;
        }
    }

    private void execute(Statement statement) {
             if (statement instanceof LetStatement)     executeLet((LetStatement) statement);
        else if (statement instanceof ForStatement)     executeFor((ForStatement) statement);
        else if (statement instanceof WhileStatement)   executeWhile((WhileStatement) statement);
        else if (statement instanceof IfStatement)      executeIf((IfStatement) statement);
        else if (statement instanceof PrintStatement)   executePrint((PrintStatement) statement);

        // we didn't use exceptions before because we wanted to check our results,
        // during execution it's fine i don't really care. 
        // + saves us from having a lot of returning nulls and verbose code
        else throw new RuntimeException("statement #" + (pos + 1) + " has unknown type");
    }

    private void executeLet(LetStatement let) {
        Object value = evaluateExpression(let.value);
        if (!(value instanceof Integer || value instanceof Boolean)) throw new RuntimeException("statement # " + pos + " -> only integer and boolean variable types");
        variables.put(let.identifier.string, value);
    }

    private void executeFor(ForStatement forStatement) {
        String varName = forStatement.variable.string;
        Object start = evaluateExpression(forStatement.start);
        Object end = evaluateExpression(forStatement.end);

        if (!(start instanceof Integer && end instanceof Integer)) throw new RuntimeException("statement # " + pos + " -> For loop bounds must be integers");

        for (int i = (Integer) start; i <= (Integer) end; i++) {
            variables.put(varName, i);
            for (Statement stmt : forStatement.body) execute(stmt);
        }
        variables.remove(varName);
    }

    private void executeWhile(WhileStatement whileStatement) {
        Object condition = evaluateExpression(whileStatement.condition);
        if (!(condition instanceof Boolean)) throw new RuntimeException("statement # " + pos + " -> While condition must be a boolean expression");

        while ((Boolean) condition) {
            for (Statement stmt : whileStatement.body) execute(stmt);
            condition = evaluateExpression(whileStatement.condition);
        }
    }

    private void executeIf(IfStatement ifStatement) {
        Object condition = evaluateExpression(ifStatement.condition);
        if (!(condition instanceof Boolean)) {
            throw new RuntimeException("statement # " + pos + " -> If condition must be a boolean expression");
        }

        if ((Boolean) condition) {
            for (Statement stmt : ifStatement.thenBranch) {
                execute(stmt);
            }
        } else if (ifStatement.elseBranch != null) {
            for (Statement stmt : ifStatement.elseBranch) {
                execute(stmt);
            }
        }
    }

    private void executePrint(PrintStatement printStatement) {
        StringBuilder output = new StringBuilder();
        for (Expression expr : printStatement.expressions) {
            Object value = evaluateExpression(expr);
            if (!(value instanceof Integer || value instanceof Boolean || value instanceof String)) {
                throw new RuntimeException("statement # " + pos + " -> Print statement can only print integers, booleans, or strings");
            }
            output.append(value).append("");
        }
        System.out.println(output.toString().trim());
    }

    private Object evaluateExpression(Expression expr) {
        if (expr instanceof LiteralExpression) {
            String literal = ((LiteralExpression) expr).literal.string;
            if (literal.equals("true")) return true;
            if (literal.equals("false")) return false;
            try {
                return Integer.parseInt(literal);
            } catch (NumberFormatException e) {
                // todo: maybe add logic to parser to check this
                return literal; // assume its a string literal for print
            }
        }

        if (expr instanceof VariableExpression) {
            String varName = ((VariableExpression) expr).identifier.string;
            if (!variables.containsKey(varName)) throw new RuntimeException("Undefined variable: " + varName);
            return variables.get(varName);
        }

        if (expr instanceof BinaryExpression) return evaluateBinary((BinaryExpression) expr);
        if (expr instanceof UnaryExpression) return evaluateUnary((UnaryExpression) expr);

        throw new RuntimeException("Unknown expression type: " + expr.getClass().getSimpleName());
    }

    private Object evaluateBinary(BinaryExpression binary) {
        Object left = evaluateExpression(binary.left);
        Object right = evaluateExpression(binary.right);
        TokenType opt = binary.operator.type;

        if (opt == TokenType.EQUAL) return left.equals(right);
        if (opt == TokenType.NEQT)  return left.equals(right);

        if (opt == TokenType.AND || opt == TokenType.OR) {
            if (!(left instanceof Boolean && right instanceof Boolean)) throw new RuntimeException("statement # " + pos + " -> " + opt.toString() + " operator only defined for boolean expressions");

            if (opt == TokenType.AND) return (Boolean)left && (Boolean)right;
                                      return (Boolean)left || (Boolean)right;
        }

        if (opt == TokenType.PLUS
         || opt == TokenType.MINUS
         || opt == TokenType.PIPQI
         || opt == TokenType.SLASH
         || opt == TokenType.MOD
         || opt == TokenType.LESS
         || opt == TokenType.LEQT
         || opt == TokenType.GREATER
         || opt == TokenType.GEQT) {
            if (!(left instanceof Integer && right instanceof Integer)) throw new RuntimeException("statement # " + pos + " -> " + opt.toString() + " operator only defined for integer expressions");
            Integer l = (Integer) left;
            Integer r = (Integer) right;
            
            switch (opt) {
                // automatically cast to -> Integer/Boolean -> Object
                case PLUS:  return l + r;
                case MINUS: return l - r;
                case PIPQI: return l * r;
                case SLASH:
                    if (r == 0) throw new RuntimeException("statement # " + pos + " -> can't divide by zero");
                    return l / r;
                case MOD:  return l % r;
                case LESS: return l < r;
                case LEQT: return l <= r;
                case GREATER: return l > r;
                case GEQT: return l >= r;
                default:

            }
         }
         throw new RuntimeException("statement # " + pos + " -> Unknown binary operator: " + binary.operator.type.toString());  
    }

    private Object evaluateUnary(UnaryExpression unary) {
        Object value = evaluateExpression(unary.right);
        if (unary.operator.type == TokenType.NOT) {
            if (value instanceof Boolean) return !(Boolean)value;
            throw new RuntimeException("statement # " + pos + " -> NOT operator only defined for boolean expression");   
        }
        if (unary.operator.type == TokenType.SQR) {
            if (value instanceof Integer) return ((Double)Math.sqrt((Integer)value)).intValue();
            throw new RuntimeException("statement # " + pos + " -> NOT operator only defined for boolean expression");   
        }

        throw new RuntimeException("statement # " + pos + " -> Unknown unary operator: " + unary.operator.type.toString());
        
    }
}

