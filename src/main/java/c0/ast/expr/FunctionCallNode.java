package c0.ast.expr;

import c0.analyzer.Visitor;
import c0.entity.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FunctionCallNode extends ExprNode {
    String name;
    List<ExprNode> args;
    Function function;

    @Override
    public <T, E> T accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
