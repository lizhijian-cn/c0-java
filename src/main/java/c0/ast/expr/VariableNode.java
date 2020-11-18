package c0.ast.expr;

import c0.entity.Variable;

public class VariableNode extends ExprNode {
    String name;
    Variable variable;

    public VariableNode(String name) {
        this.name = name;
    }
}
