package c0.entity;

import c0.ast.expr.ExprNode;
import c0.type.Type;

public class Variable extends Entity {
    Type type;
    ExprNode expr;
    boolean isConst;
    boolean isInitialized;

    public Variable(String name, Type type, ExprNode expr, boolean isConst) {
        super(name);
        this.type = type;
        this.expr = expr;
        this.isConst = isConst;
    }
}
