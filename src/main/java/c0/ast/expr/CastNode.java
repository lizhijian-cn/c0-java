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
    public <T, E> T accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
