package c0.ast.expr;

import c0.entity.Variable;
import c0.visitor.Visitor;
import lombok.Getter;
import lombok.Setter;

// TODO not distinction between lhs and rhs
@Getter
public class VariableNode extends ExprNode {
    String name;
    @Setter
    Variable variable;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
