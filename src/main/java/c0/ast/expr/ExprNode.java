package c0.ast.expr;

import c0.ast.AbstractNode;
import c0.type.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class ExprNode extends AbstractNode {
    @Setter
    Type type;
}
