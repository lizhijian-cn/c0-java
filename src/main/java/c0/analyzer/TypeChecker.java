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
public class TypeChecker implements Visitor<Type> {

    void expectEquals(Type a, Type b) {
        if (!a.equals(b))
            throw new RuntimeException(String.format("expected %s, but got %s", a, b));
    }

    void expect(Type type, TypeVal typeVal) {
        if (!type.equals(typeVal)) {
            throw new RuntimeException(String.format("expected not %s", typeVal));
        }
    }

    void expectNot(Type type, TypeVal typeVal) {
        if (type.equals(typeVal)) {
            throw new RuntimeException(String.format("expected not %s", typeVal));
        }
    }

    @Override
    public Type visit(Variable variable) {
        expectNot(variable.getType(), TypeVal.VOID);
        expectNot(variable.getType(), TypeVal.STRING);
        return null;
    }

    @Override
    public Type visit(Function function) {
        function.getParams().forEach(x -> x.accept(this));
        function.getLocals().forEach(x -> x.accept(this));
        function.getBlockStmt().accept(this);
        var returnType = function.getReturnType();
        for (var stmt : function.getBlockStmt().getStmts()) {
            if (stmt instanceof ReturnNode returnStmt && returnStmt.getReturnValue().isPresent()) {
                expectEquals(returnType, returnStmt.getReturnValue().get().getType());
                return null;
            }
        }
        expect(returnType, TypeVal.VOID);
        return null;
    }

    @Override
    public Type visit(AST node) {
        node.getGlobals().forEach(x -> x.accept(this));
        node.getFunctions().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public Type visit(BlockNode node) {
        node.getLocals().forEach(x -> x.accept(this));
        node.getStmts().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public Type visit(EmptyNode node) {
        return null;
    }

    @Override
    public Type visit(ExprStmtNode node) {
        node.getExpr().accept(this);
        return null;
    }

    @Override
    public Type visit(ReturnNode node) {
        node.getReturnValue().ifPresent(x -> x.accept(this));
        return null;
    }

    @Override
    public Type visit(AssignNode node) {
        var rhs = node.getRhs().accept(this);
        expectEquals(node.getLhs().getType(), rhs);
        node.setType(rhs);
        return rhs;
    }

    @Override
    public Type visit(BinaryOpNode node) {
        var lhs = node.getLeft().accept(this);
        expectNot(lhs, TypeVal.STRING);
        var rhs = node.getRight().accept(this);
        expectNot(rhs, TypeVal.STRING);
        expectEquals(lhs, rhs);
        node.setType(lhs);
        return node.getType();
    }

    @Override
    public Type visit(CastNode node) {
        var lhs = node.getExpr().accept(this);
        expectNot(lhs, TypeVal.STRING);
        var rhs = node.getCastType();
        expectNot(rhs, TypeVal.VOID);
        node.setType(rhs);
        return node.getType();
    }

    @Override
    public Type visit(FunctionCallNode node) {
        node.getArgs().forEach(x -> x.accept(this));
        var params = node.getFunction().getParams();
        var args = node.getArgs();

        if (params.size() != args.size()) {
            throw new RuntimeException("argument number error");
        }
        for (int i = 0; i < params.size(); i++) {
            expectEquals(params.get(i).getType(), args.get(0).getType());
        }
        return node.getFunction().getReturnType();
    }

    @Override
    public Type visit(LiteralNode node) {
        return node.getType();
    }

    @Override
    public Type visit(UnaryOpNode node) {
        var type = node.getExpr().accept(this);
        expectNot(type, TypeVal.STRING);
        node.setType(type);
        return node.getType();
    }

    @Override
    public Type visit(VariableNode node) {
        node.setType(node.getVariable().getType());
        return node.getType();
    }
}
