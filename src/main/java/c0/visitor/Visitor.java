package c0.visitor;

import c0.ast.AST;
import c0.ast.AbstractNode;
import c0.ast.expr.*;
import c0.ast.stmt.*;
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
    void visit(DeclStmtNode node);
    void visit(EmptyNode node);
    void visit(ExprStmtNode node);
    void visit(ReturnNode node);
}
