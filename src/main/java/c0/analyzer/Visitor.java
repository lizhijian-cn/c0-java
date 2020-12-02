package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;

public interface Visitor {
    void visit(Variable variable);

    void visit(Function function);

    void visit(AST node);

    void visit(AssignNode node);

    void visit(BinaryOpNode node);

    void visit(CastNode node);

    void visit(FunctionCallNode node);

    void visit(LiteralNode node);

    void visit(UnaryOpNode node);

    void visit(VariableNode node);

    void visit(BlockNode node);

    default void visit(EmptyNode node) {

    }

    default void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }

    void visit(ReturnNode node);
}
