package c0.ast;

import c0.analyzer.Visitor;
import c0.entity.Function;
import c0.entity.Variable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class AST extends AbstractNode {
    List<Function> functions;
    List<Variable> globals;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
