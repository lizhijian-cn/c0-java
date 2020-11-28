package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.entity.Variable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BlockNode extends StmtNode {
    List<Variable> locals;
    List<StmtNode> stmts;

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}

