package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.entity.Variable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeclStmtNode extends StmtNode {
    String name;
    Variable variable;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
