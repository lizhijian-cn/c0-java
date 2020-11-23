package c0.visitor;

import c0.ast.AST;
import c0.ast.AbstractNode;
import c0.ast.expr.*;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.Variable;

import java.io.PrintStream;
import java.util.List;

public class Dumper implements Visitor {
    PrintStream s;
    final String indentStr = "    ";
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
        node.accept(this);
        unindent();
    }

    void printList(String name, List<? extends AbstractNode> nodes) {
        printIndent();
        s.println(name + ":");
        indent();
        for (var node : nodes) {
            node.accept(this);
        }
        unindent();
    }
    @Override
    public void visit(Variable variable) {
        printClassName(variable);
        printMember("type", variable.getType().toString());
        printMember("name", variable.getName());
        printMember("value", variable.getExpr());
    }

    @Override
    public void visit(Function function) {
        printClassName(function);
        printMember("return type", function.getReturnType().toString());
        printMember("name", function.getName());
        printList("args", function.getArgs());
        printMember("block", function.getBlockStmt());
    }

    @Override
    public void visit(AST node) {
        printList("decls", node.getEntities());
    }

    @Override
    public void visit(AssignNode node) {
        printClassName(node);
        printMember("lhs", node.getLhs());
        printMember("rhs", node.getRhs());
    }

    @Override
    public void visit(BinaryOpNode node) {
        printClassName(node);
        printMember("op", node.getOp().toString());
        printMember("left", node.getLeft());
        printMember("right", node.getRight());
    }

    @Override
    public void visit(CastNode node) {
        printClassName(node);
        printMember("expr", node.getExpr());
        printMember("as type", node.getType().toString());
    }

    @Override
    public void visit(FunctionCallNode node) {
        printClassName(node);
        printList("args", node.getArgs());
        var function = node.getFunction();
        if (function == null) {
            printMember("function", "null");
        } else {
            printMember("function", function);
        }
    }

    @Override
    public void visit(LiteralNode node) {
        printClassName(node);
        printMember("type", node.getType().toString());
        printMember("literal", node.getValue().toString());
    }

    @Override
    public void visit(UnaryOpNode node) {
        printClassName(node);
        printMember("prefix op", node.getOp().toString());
        printMember("expr", node.getExpr());
    }

    @Override
    public void visit(VariableNode node) {
        printClassName(node);
        printMember("variable", node.getName());
        var variable = node.getVariable();
        if (variable == null) {
            printMember("variable", "null");
        } else {
            printMember("variable", variable);
        }
    }

    @Override
    public void visit(BlockNode node) {
        printClassName(node);
        printList("statements in block", node.getStmts());
    }

    @Override
    public void visit(DeclStmtNode node) {
        printClassName(node);
        printMember("entity", node.getVariable());
    }

    @Override
    public void visit(EmptyNode node) {
        printClassName(node);
    }

    @Override
    public void visit(ExprStmtNode node) {
        printClassName(node);
        printMember("expr", node.getExpr());
    }

    @Override
    public void visit(ReturnNode node) {
        printClassName(node);
        printMember("return value", node.getReturnValue());
    }
}
