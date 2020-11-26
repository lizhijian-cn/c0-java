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

import java.util.Deque;
import java.util.LinkedList;

/**
 * void return
 * static type checker
 * variable type void
 */
public class TypeChecker implements Visitor {

    @Override
    public void visit(Variable variable) {

    }

    @Override
    public void visit(Function function) {

    }

    @Override
    public void visit(AST node) {

    }

    @Override
    public void visit(AssignNode node) {

    }

    @Override
    public void visit(BinaryOpNode node) {

    }

    @Override
    public void visit(CastNode node) {

    }

    @Override
    public void visit(FunctionCallNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override
    public void visit(UnaryOpNode node) {

    }

    @Override
    public void visit(VariableNode node) {

    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(EmptyNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {

    }

    @Override
    public void visit(ReturnNode node) {

    }
}
