package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;

public interface Visitor<T> {
    T visit(Variable variable);

    T visit(Function function);

    T visit(AST node);

    T visit(AssignNode node);

    T visit(BinaryOpNode node);

    T visit(CastNode node);

    T visit(FunctionCallNode node);

    T visit(LiteralNode node);

    T visit(UnaryOpNode node);

    T visit(VariableNode node);

    T visit(BlockNode node);

    T visit(EmptyNode node);

    T visit(ExprStmtNode node);

    T visit(ReturnNode node);
}
