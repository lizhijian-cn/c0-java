package c0.ast.expr;

import c0.type.Type;
import c0.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CastNode extends ExprNode {
    ExprNode expr;
    Type type;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
