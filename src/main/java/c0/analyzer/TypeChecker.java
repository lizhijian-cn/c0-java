package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;
import c0.parser.scope.Scope;
import c0.type.Type;
import c0.type.TypeVal;

import java.nio.channels.NonReadableChannelException;
import java.util.Deque;
import java.util.LinkedList;

/**
 * void return
 * static type checker
 * variable type void
 */
public class TypeChecker implements Visitor<Type, Void> {

    void validExprType(Type ...types) {
        for (var type : types) {
            if (type.equals(TypeVal.STRING) || type.equals(TypeVal.VOID)) {
                throw new RuntimeException(String.format("%s cant be operand", type));
            }
        }
    }
    @Override
    public Void visit(Variable variable) {
        return null;
    }

    @Override
    public Void visit(Function function) {
        return null;
    }

    @Override
    public Void visit(AST node) {
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        return null;
    }

    @Override
    public Void visit(EmptyNode node) {
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        return null;
    }

    @Override
    public Type visit(AssignNode node) {
        return null;
    }

    @Override
    public Type visit(BinaryOpNode node) {
        var lhs = node.getLeft().accept(this);
        var rhs = node.getRight().accept(this);
        validExprType(lhs, rhs);
        if (lhs.equals(rhs)) {
            return lhs;
        }
        throw new RuntimeException("type error");
    }

    @Override
    public Type visit(CastNode node) {
        var lhs = node.getExpr().accept(this);
        var rhs = node.getCastType();
        return null;
    }

    @Override
    public Type visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public Type visit(LiteralNode node) {
        return null;
    }

    @Override
    public Type visit(UnaryOpNode node) {
        return null;
    }

    @Override
    public Type visit(VariableNode node) {
        return null;
    }
}
