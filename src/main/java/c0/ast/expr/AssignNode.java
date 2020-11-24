package c0.ast.expr;

import c0.analyzer.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AssignNode extends ExprNode {
    VariableNode lhs;
    ExprNode rhs;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
