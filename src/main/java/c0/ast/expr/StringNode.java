package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.entity.StringVariable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StringNode extends ExprNode {
    StringVariable variable;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
