package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;
import c0.type.Type;
import c0.type.TypeVal;

/**
 * void return
 * static type checker
 * variable type void
 */
public class TypeChecker implements Visitor<Type, Void> {

    void expectValidExprType(Type... types) {
        for (var type : types) {
            if (type.equals(TypeVal.STRING) || type.equals(TypeVal.VOID)) {
                throw new RuntimeException(String.format("%s cant be operand", type));
            }
        }
    }

    void expectEquals(Type a, Type b) {
        if (!a.equals(b))
            throw new RuntimeException(String.format("expected %s, but got %s", a, b));
    }

    void expectNot(Type type, TypeVal typeVal) {
        if (type.equals(typeVal)) {
            throw new RuntimeException(String.format("expected not %s", typeVal));
        }
    }

    @Override
    public Void visit(Variable variable) {
        expectNot(variable.getType(), TypeVal.VOID);
        return null;
    }

    @Override
    public Void visit(Function function) {
        function.getArgs().forEach(x -> x.accept(this));
        function.getLocals().forEach(x -> x.accept(this));
        var returnType = function.getReturnType();
        if (returnType.equals(TypeVal.VOID)) {
            for (var )
        }
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

        expectEquals();
        return null;
    }

    @Override
    public Type visit(BinaryOpNode node) {
        var lhs = node.getLeft().accept(this);
        var rhs = node.getRight().accept(this);
        expectValidExprType(lhs, rhs);
        expectEquals(lhs, rhs);
        return lhs;
    }

    @Override
    public Type visit(CastNode node) {
        var lhs = node.getExpr().accept(this);
        var rhs = node.getCastType();
        expectValidExprType(lhs, rhs);
        return rhs;
    }

    @Override
    public Type visit(FunctionCallNode node) {
        int n = node.getArgs().size();
        var args = node.getFunction().getArgs();
        if (n != args.size()) {
            throw new RuntimeException("argument number error");
        }
        for (int i = 0; i < n; i++) {
            var t1 = node.getArgs().get(i).getType();
            var t2 = args.get(0).getType();
            expectValidExprType(t1, t2);
            expectEquals(t1, t2);
        }
        return node.getFunction().getReturnType();
    }

    @Override
    public Type visit(LiteralNode node) {
        return node.getType();
    }

    @Override
    public Type visit(UnaryOpNode node) {
        expectValidExprType(node.getType());
        return node.getType();
    }

    @Override
    public Type visit(VariableNode node) {
        return node.getVariable().getType();
    }
}
