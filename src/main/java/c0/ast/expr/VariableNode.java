package c0.ast.expr;

import c0.entity.Variable;

// TODO not distinction between lhs and rhs
public class VariableNode extends ExprNode {
    String name;
    Variable variable;

    public VariableNode(String name) {
        this.name = name;
    }
}
