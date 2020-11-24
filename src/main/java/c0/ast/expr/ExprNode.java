package c0.ast.expr;

import c0.ast.AbstractNode;

public abstract class ExprNode extends AbstractNode {
    public enum OpVal {
        PLUS, MINUS, MUL, DIV, LT, GT, LE, GE, EQ, NEQ,
        NEG
    }
}
