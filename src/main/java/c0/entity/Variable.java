package c0.entity;

import c0.analyzer.Visitor;
import c0.ast.expr.ExprNode;
import c0.type.Type;
import lombok.Getter;

@Getter
public class Variable extends Entity {
    Type type;
    ExprNode expr;
    int offset;
    boolean isConst;

    public Variable(String name, Type type, ExprNode expr, boolean isConst) {
        super(name);
        this.type = type;
        this.expr = expr;
        this.isConst = isConst;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
