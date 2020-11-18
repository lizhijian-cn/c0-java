package c0.ast.expr;

import c0.ast.AbstractNode;

public class AssignNode extends ExprNode {
    VariableNode lhs;
    ExprNode rhs;

    public AssignNode(VariableNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
