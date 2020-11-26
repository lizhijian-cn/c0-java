package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CastNode extends ExprNode {
    ExprNode expr;
    Type castType;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
