package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.StringVariable;
import c0.entity.Variable;
import c0.type.Type;
import c0.type.TypeVal;

/**
 * void return
 * static type checker
 * variable type void
 */
public class TypeChecker implements Visitor {
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

    void expectOperandType(Type type) {
        expectNot(type, TypeVal.BOOL);
        expectNot(type, TypeVal.VOID);
        expectNot(type, TypeVal.STRING);
    }
    @Override
    public void visit(Variable variable) {
        expectNot(variable.getType(), TypeVal.VOID);
        expectNot(variable.getType(), TypeVal.BOOL);
    }

    @Override
    public void visit(Function function) {
        function.getParams().forEach(x -> x.accept(this));
        function.getLocals().forEach(x -> x.accept(this));
        function.getBlockStmt().accept(this);
        var returnType = function.getReturnType();
        for (var stmt : function.getBlockStmt().getStmts()) {
            if (stmt instanceof ReturnNode returnStmt && returnStmt.getReturnValue().isPresent()) {
                expectEquals(returnType, returnStmt.getReturnValue().get().getType());
                return;
            }
        }
        expect(returnType, TypeVal.VOID);
    }

    @Override
    public void visit(AST node) {
        node.getGlobals().forEach(x -> x.accept(this));
        node.getFunctions().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(BlockNode node) {
        node.getLocals().forEach(x -> x.accept(this));
        node.getStmts().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ReturnNode node) {
        node.getReturnValue().ifPresent(x -> x.accept(this));
    }

    @Override
    public void visit(AssignNode node) {
        expectEquals(node.getLhs().getType(), node.getRhs().getType());
        node.setType(new Type(TypeVal.VOID));
    }

    @Override
    public void visit(BinaryOpNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        var lhs = node.getLeft().getType();
        var rhs = node.getRight().getType();
        expectOperandType(lhs);
        expectOperandType(rhs);
        expectEquals(lhs, rhs);

        node.setType(switch (node.getOp()) {
            case PLUS, MINUS, MUL, DIV -> lhs;
            default -> new Type(TypeVal.BOOL);
        });
    }

    @Override
    public void visit(CastNode node) {
        node.getExpr().accept(this);
        var lhs = node.getExpr().getType();
        expectNot(lhs, TypeVal.STRING);
        var rhs = node.getCastType();
        expectNot(rhs, TypeVal.VOID);
        node.setType(rhs);
    }

    @Override
    public void visit(FunctionCallNode node) {
        node.getArgs().forEach(x -> x.accept(this));
        var params = node.getFunction().getParams();
        var args = node.getArgs();

        if (params.size() != args.size()) {
            throw new RuntimeException("argument number error");
        }
        for (int i = 0; i < params.size(); i++) {
            expectEquals(params.get(i).getType(), args.get(i).getType());
        }
        node.setType(node.getFunction().getReturnType());
    }

    @Override
    public void visit(STLFunctionCallNode node) {
        // TODO ARGUMENT CHECK
        switch (node.getFunction().getName()) {
            case "getint", "getchar" -> {
                node.setType(new Type(TypeVal.UINT));
            }
            case "getdouble" -> {
                node.setType(new Type(TypeVal.DOUBLE));
            }
            default -> {
                node.setType(new Type(TypeVal.VOID));
            }
        }
    }

    @Override
    public void visit(LiteralNode node) {
    }

    @Override
    public void visit(UnaryOpNode node) {
        node.getExpr().accept(this);
        var type = node.getExpr().getType();
        expectNot(type, TypeVal.STRING);
        node.setType(type);
    }

    @Override
    public void visit(VariableNode node) {
        node.setType(node.getVariable().getType());
    }

    @Override
    public void visit(StringNode node) {
        node.getVariable().accept(this);
        node.setType(new Type(TypeVal.STRING));
    }
}
