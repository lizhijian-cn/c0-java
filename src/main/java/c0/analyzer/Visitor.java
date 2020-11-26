package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;

public interface Visitor<T, E> {
    T visit(AssignNode node);

    T visit(BinaryOpNode node);

    T visit(CastNode node);

    T visit(FunctionCallNode node);

    T visit(LiteralNode node);

    T visit(UnaryOpNode node);

    T visit(VariableNode node);

    E visit(Variable variable);

    E visit(Function function);

    E visit(AST node);

    E visit(BlockNode node);

    E visit(EmptyNode node);

    E visit(ExprStmtNode node);

    E visit(ReturnNode node);
}
