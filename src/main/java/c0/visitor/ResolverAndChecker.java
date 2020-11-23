package c0.visitor;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.Variable;
import c0.scope.Scope;

import java.util.Deque;
import java.util.LinkedList;

public class ResolverAndChecker implements Visitor {
    Deque<Scope> scopeStack;

    public ResolverAndChecker() {
        this.scopeStack = new LinkedList<>();
    }
    Scope top() {
        if (scopeStack.isEmpty()) {
            throw new RuntimeException("scope stack is empty");
        }
        return scopeStack.peekLast();
    }
    Scope current() {
        if (scopeStack.isEmpty()) {
            throw new RuntimeException("scope stack is empty");
        }
        return scopeStack.peek();
    }

    Function getFunction(String name) {
        var entity = top().get(name);
        if (entity.isPresent() && entity.get() instanceof Function function) {
            return function;
        } else {
            throw new RuntimeException(String.format("function %s is not defined in the scope", name));
        }
    }

    Variable getVariable(String name) {
        for (var scope : scopeStack) {
            var entity = scope.get(name);
            if (entity.isPresent() && entity.get() instanceof Variable variable) {
                return variable;
            }
        }
        throw new RuntimeException(String.format("variable %s is not defined in the scope", name));
    }
    void push() {
        scopeStack.push(new Scope());
    }

    void pop() {
        scopeStack.pop();
    }

    @Override
    public void visit(Variable variable) {
        current().add(variable);
    }

    @Override
    public void visit(Function function) {
        current().add(function);
        function.getBlockStmt().accept(this);
    }

    @Override
    public void visit(AST node) {
        push();
        // add standard library function
        for (var entity : node.getEntities()) {
            entity.accept(this);
        }
        pop();
    }

    @Override
    public void visit(AssignNode node) {
        node.getLhs().accept(this);
        node.getRhs().accept(this);
    }

    @Override
    public void visit(BinaryOpNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
    }

    @Override
    public void visit(CastNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(FunctionCallNode node) {
        for (var expr : node.getArgs()) {
            expr.accept(this);
        }
        node.setFunction(getFunction(node.getName()));
    }

    @Override
    public void visit(LiteralNode node) {
    }

    @Override
    public void visit(UnaryOpNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(VariableNode node) {
        node.setVariable(getVariable(node.getName()));
    }

    @Override
    public void visit(BlockNode node) {
        push();
        for (var stmt : node.getStmts()) {
            stmt.accept(this);
        }
        pop();
    }

    @Override
    public void visit(DeclStmtNode node) {
        node.getVariable().getExpr().accept(this);
        current().add(node.getVariable());
    }

    @Override
    public void visit(EmptyNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
    }

    @Override
    public void visit(ReturnNode node) {
        node.getReturnValue().accept(this);
    }
}
