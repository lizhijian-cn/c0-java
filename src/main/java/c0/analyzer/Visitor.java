package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.StringVariable;
import c0.entity.Variable;

public interface Visitor {
    void visit(Variable variable);

    void visit(Function function);

    default void visit(StringVariable string) {

    }

    void visit(AST node);

    void visit(AssignNode node);

    void visit(BinaryOpNode node);

    void visit(CastNode node);

    void visit(FunctionCallNode node);

    void visit(STLFunctionCallNode node);

    void visit(LiteralNode node);

    void visit(UnaryOpNode node);

    void visit(VariableNode node);

    void visit(StringNode node);

    void visit(BlockNode node);

    default void visit(EmptyNode node) {}

    default void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }

    void visit(ReturnNode node);

    void visit(IfNode node);

    void visit(WhileNode node);

    default void visit(BreakNode node) {}

    default void visit(ContinueNode node) {}
}
