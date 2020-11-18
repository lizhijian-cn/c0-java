package c0.ast.expr;

import c0.type.Type;

public class AsNode extends ExprNode {
    ExprNode expr;
    Type type;

    public AsNode(ExprNode expr, Type type) {
        this.expr = expr;
        this.type = type;
    }
}
