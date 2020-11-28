package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.entity.Variable;
import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO not distinction between lhs and rhs
@Getter
@AllArgsConstructor
public class VariableNode extends ExprNode {
    String name;
    Variable variable;

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
