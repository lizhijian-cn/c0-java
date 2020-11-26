package c0.analyzer;

import c0.ast.AST;
import c0.ast.AbstractNode;
import c0.ast.expr.*;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.Variable;

import java.io.PrintStream;
import java.util.List;

public class Dumper implements Visitor<Void, Void> {
    final String indentStr = "    ";
    PrintStream s;
    int indent;

    public Dumper(PrintStream s) {
        this.s = s;
        this.indent = 0;
    }

    void printIndent() {
        for (int i = 0; i < indent; i++) {
            s.print(indentStr);
        }
    }

    void indent() {
        indent++;
    }

    void unindent() {
        indent--;
    }

    void printClassName(AbstractNode node) {
        printIndent();
        s.println(String.format("<<%s>>: ", node.getClass()));
    }

    void printMember(String name, String value) {
        printIndent();
        s.println(String.format("%s: %s", name, value));
    }

    void printMember(String name, AbstractNode node) {
        printIndent();
        s.println(String.format("%s:", name));
        indent();
        if (node instanceof ExprNode expr) {
            expr.accept(this);
        }
        if (node instanceof StmtNode stmt) {
            stmt.accept(this);
        }
        unindent();
    }

    void printList(String name, List<? extends AbstractNode> nodes) {
        printIndent();
        s.println(name + ":");
        indent();
        for (var node : nodes) {
            if (node instanceof ExprNode expr) {
                expr.accept(this);
            }
            if (node instanceof StmtNode stmt) {
                stmt.accept(this);
            }
        }
        unindent();
    }

    @Override
    public Void visit(Variable variable) {
        printClassName(variable);
        printMember("type", variable.getType().toString());
        printMember("name", variable.getName());
        printMember("value", variable.getExpr());
    }

    @Override
    public Void visit(Function function) {
        printClassName(function);
        printMember("return type", function.getReturnType().toString());
        printMember("name", function.getName());
        printList("args", function.getArgs());
        printList("locals", function.getLocals());
        printMember("block", function.getBlockStmt());
        return null;
    }

    @Override
    public Void visit(AST node) {
        printList("global variables", node.getGlobals());
        printList("functions", node.getFunctions());
        return null;
    }

    @Override
    public Void visit(AssignNode node) {
        printClassName(node);
        printMember("lhs", node.getLhs());
        printMember("rhs", node.getRhs());
        return null;
    }

    @Override
    public Void visit(BinaryOpNode node) {
        printClassName(node);
        printMember("op", node.getOp().toString());
        printMember("left", node.getLeft());
        printMember("right", node.getRight());
        return null;
    }

    @Override
    public Void visit(CastNode node) {
        printClassName(node);
        printMember("expr", node.getExpr());
        printMember("as type", node.getCastType().toString());
        return null;
    }

    @Override
    public Void visit(FunctionCallNode node) {
        printClassName(node);
        printList("args", node.getArgs());
        printMember("function", node.getFunction());
        return null;
    }

    @Override
    public Void visit(LiteralNode node) {
        printClassName(node);
        printMember("type", node.getType().toString());
        printMember("literal", node.getValue().toString());
        return null;
    }

    @Override
    public Void visit(UnaryOpNode node) {
        printClassName(node);
        printMember("prefix op", node.getOp().toString());
        printMember("expr", node.getExpr());
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        printClassName(node);
        printMember("variable", node.getVariable());
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        printClassName(node);
        printList("statements in block", node.getStmts());
        return null;
    }

    @Override
    public Void visit(EmptyNode node) {
        printClassName(node);
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        printClassName(node);
        printMember("expr", node.getExpr());
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        printClassName(node);
        printMember("return value", node.getReturnValue());
        return null;
    }
}
