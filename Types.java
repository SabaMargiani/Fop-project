import java.util.List;

abstract class Statement {}

class Program {
    final List<Statement> statements;

    Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        String res = "";
        for (Statement s: statements) {
            res += s.toString() + "\n";
        }
        return res;
    }
}

class LetStatement extends Statement {
    final Token identifier;
    final Expression value;

    LetStatement(Token identifier, Expression value) {
        this.identifier = identifier;
        this.value = value;
    }

    @Override
    public String toString() {
        return "let: variable " + identifier.string + ", right-side " + value.toString();
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

    @Override
    public String toString() {
        String res = "for loop: " + variable.string + " = " + start.toString() + "; " + variable.string + "++; " + variable.string + " < " + end.toString();
        for (Statement s: body) {
            res += "\n\t" + s.toString();
        }
        return res;
    }
}

class WhileStatement extends Statement {
    final Expression condition;
    final List<Statement> body;

    WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        String res = "while: " + condition.toString();
        for (Statement s: body) {
            res += "\n\t" + s.toString();
        }
        return res;
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

    @Override
    public String toString() {
        String res = "if (" + condition.toString() + "):";
        for (Statement s: thenBranch) {
            res += "\n\t" + s.toString();
        }
        res += "\nelse:";
        if (elseBranch == null) return res;
        for (Statement s: elseBranch) {
            res += "\n\t" + s.toString();
        }
        return res;
    }
}

class PrintStatement extends Statement {
    final List<Expression> expressions;

    PrintStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String toString() {
        String res = "print ";
        for (Expression e: expressions) {
            res += e.toString() + " ";
        }
        return res;
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

    @Override
    public String toString() {
        return left.toString() + " " + operator.type.toString() + " " + right.toString();
    }
}

class UnaryExpression extends Expression {
    final Token operator;
    final Expression right;

    UnaryExpression(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
    @Override
    public String toString() {
        return "not-" + right.toString();
    }
}

class LiteralExpression extends Expression {
    final Token literal;

    LiteralExpression(Token literal) {
        this.literal = literal;
    }
    
    @Override
    public String toString() {
        return literal.string;
    }
}

class VariableExpression extends Expression {
    final Token identifier;

    VariableExpression(Token identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.string;
    }
}


