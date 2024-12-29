import java.util.List;

// Base class for all AST nodes
abstract class Node {}

// Represents a program node containing a list of statements
class ProgramNode extends Node {
    List<Node> statements;

    public ProgramNode(List<Node> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "statements=" + statements +
                '}';
    }
}

// Represents a block of statements
class BlockNode extends Node {
    List<Node> statements;

    public BlockNode(List<Node> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "BlockNode{" +
                "statements=" + statements +
                '}';
    }
}

// Represents a variable declaration or assignment
class LetNode extends Node {
    Token identifier;
    Node expression;

    public LetNode(Token identifier, Node expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "LetNode{" +
                "identifier=" + identifier +
                ", expression=" + expression +
                '}';
    }
}

// Represents an if statement
class IfNode extends Node {
    Node condition;
    Node thenBranch;
    Node elseBranch;

    public IfNode(Node condition, Node thenBranch, Node elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "condition=" + condition +
                ", thenBranch=" + thenBranch +
                ", elseBranch=" + elseBranch +
                '}';
    }
}

// Represents a while loop
class WhileNode extends Node {
    Node condition;
    Node body;

    public WhileNode(Node condition, Node body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return "WhileNode{" +
                "condition=" + condition +
                ", body=" + body +
                '}';
    }
}

// Represents a for loop
class ForNode extends Node {
    Token variable;
    Node start;
    Node end;
    Node body;

    public ForNode(Token variable, Node start, Node end, Node body) {
        this.variable = variable;
        this.start = start;
        this.end = end;
        this.body = body;
    }

    @Override
    public String toString() {
        return "ForNode{" +
                "variable=" + variable +
                ", start=" + start +
                ", end=" + end +
                ", body=" + body +
                '}';
    }
}

// Represents a binary expression
class BinaryNode extends Node {
    Node left;
    Token operator;
    Node right;

    public BinaryNode(Node left, Token operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "BinaryNode{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }
}

// Represents a literal (number, string, etc.)
class LiteralNode extends Node {
    Token value;

    public LiteralNode(Token value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LiteralNode{" +
                "value=" + value +
                '}';
    }
}

// Represents a variable reference
class VariableNode extends Node {
    Token identifier;

    public VariableNode(Token identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "VariableNode{" +
                "identifier=" + identifier +
                '}';
    }
}
